package me.kcra.dockeractyl.docker.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.docker.exceptions.ComposeException;
import me.kcra.dockeractyl.docker.model.spec.ContainerSpec;
import me.kcra.dockeractyl.serial.ContainerSerializer;
import me.kcra.dockeractyl.serial.DockerSerializer;
import me.kcra.dockeractyl.utils.SerialUtils;
import me.kcra.dockeractyl.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContainerStore {
    @Getter
    private final List<Container> containers = Collections.synchronizedList(new ArrayList<>());
    private final DockerSerializer<ContainerSpec, Container> containerSer;
    private final ObjectMapper mapper;
    private final TaskExecutor taskExecutor;
    private final Path tempFolder;

    @Autowired
    public ContainerStore(ContainerSerializer containerSer, ObjectMapper mapper, ThreadPoolTaskExecutor taskExecutor, Path tempFolder) {
        this.containerSer = containerSer;
        this.mapper = mapper;
        this.taskExecutor = taskExecutor;
        this.tempFolder = tempFolder;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateContainers() {
        containers.clear();
        log.info("Refreshing container info...");
        try {
            final Process proc = SystemUtils.process("docker", "ps", "--format", "'{{json .}}'", "--no-trunc", "--all");
            proc.waitFor();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                reader.lines().forEach(e -> {
                    log.info("Retrieved container details: " + e);
                    try {
                        containers.add(containerSer.fromSpec(mapper.readValue(SerialUtils.stripEnds(e, "'"), ContainerSpec.class)));
                    } catch (JsonProcessingException ex) {
                        log.error("Could not retrieve container!", ex);
                    }
                });
            }
        } catch (IOException e) {
            log.error("Could not retrieve containers!", e);
        } catch (InterruptedException ignored) {
            // ignored
        }
        log.info("Refreshed container info.");
    }

    public Optional<Container> getContainer(String id) {
        return containers.stream().filter(e -> e.getId().equals(id) || e.getNames().equals(id)).findFirst();
    }

    public void compose(String[] composeFileContent) throws ComposeException {
        final File composeFolder = Paths.get(tempFolder.toAbsolutePath().toString(), UUID.randomUUID().toString()).toFile();
        //noinspection ResultOfMethodCallIgnored
        composeFolder.mkdirs();
        final File composeFile = Paths.get(composeFolder.getAbsolutePath(), "docker-compose.yml").toFile();
        try {
            //noinspection ResultOfMethodCallIgnored
            composeFile.createNewFile();
        } catch (IOException e) {
            throw new ComposeException("File creation error (does dockeractyl have enough permissions?)", e);
        }
        try (final FileOutputStream writer = new FileOutputStream(composeFile, false)) {
            try {
                writer.write(String.join("\n", composeFileContent).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new ComposeException("File write error (does dockeractyl have enough permissions?)", e);
            }
        } catch (IOException e) {
            throw new ComposeException("File write error (does dockeractyl have enough permissions?)", e);
        }
        try {
            final Process proc = new ProcessBuilder("docker-compose", "up", "-d").directory(composeFolder).start();
            proc.waitFor();

            try (final BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                if (errorReader.lines().findAny().isPresent()) {
                    throw new ComposeException(errorReader.lines().collect(Collectors.joining(" ")));
                }
            }
            taskExecutor.execute(this::updateContainers);
        } catch (IOException | InterruptedException e) {
            throw new ComposeException("Process initialization error (does dockeractyl have enough permissions?)", e);
        }
        taskExecutor.execute(() -> FileSystemUtils.deleteRecursively(composeFolder));
    }

    public void composeAsynchronously(String[] composeFileContent) {
        taskExecutor.execute(() -> {
            try {
                compose(composeFileContent);
            } catch (ComposeException e) {
                log.error("Could not compose container: " + e.getMessage());
            }
        });
    }
}
