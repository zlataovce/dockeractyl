package me.kcra.dockeractyl.docker.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.Container;
import me.kcra.dockeractyl.docker.spec.ContainerSpec;
import me.kcra.dockeractyl.serial.BidirectionalSerializer;
import me.kcra.dockeractyl.serial.ContainerSerializer;
import me.kcra.dockeractyl.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ContainerStore {
    @Getter
    private final List<Container> containers = Collections.synchronizedList(new ArrayList<>());
    private final BidirectionalSerializer<ContainerSpec, Container> containerSer;

    @Autowired
    public ContainerStore(ContainerSerializer containerSer) {
        this.containerSer = containerSer;
    }

    @Async
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateContainers() {
        containers.clear();
        try {
            final Process proc = Runtime.getRuntime().exec("docker ps --format '{{json .}}' --no-trunc --all");
            proc.waitFor();
            new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().forEach(e -> {
                try {
                    containers.add(containerSer.fromSpec(JacksonUtils.MAPPER.readValue(e, ContainerSpec.class)));
                } catch (JsonProcessingException ex) {
                    log.error("Could not retrieve container!", ex);
                }
            });
        } catch (IOException e) {
            log.error("Could not retrieve containers!", e);
        } catch (InterruptedException ignored) {
            // ignored
        }
    }

    public Optional<Container> getContainerByID(String id) {
        return containers.stream().filter(e -> e.getId().equals(id)).findFirst();
    }
}
