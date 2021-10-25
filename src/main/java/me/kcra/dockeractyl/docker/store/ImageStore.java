package me.kcra.dockeractyl.docker.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.model.Image;
import me.kcra.dockeractyl.docker.model.spec.ImageSpec;
import me.kcra.dockeractyl.serial.DockerSerializer;
import me.kcra.dockeractyl.serial.ImageSerializer;
import me.kcra.dockeractyl.utils.SerialUtils;
import me.kcra.dockeractyl.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImageStore {
    @Getter
    private final List<Image> images = Collections.synchronizedList(new ArrayList<>());
    private final DockerSerializer<ImageSpec, Image> imageSer;
    private final ObjectMapper mapper;

    @Autowired
    public ImageStore(ImageSerializer imageSer, ObjectMapper mapper) {
        this.imageSer = imageSer;
        this.mapper = mapper;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateImages() {
        images.clear();
        log.info("Refreshing image info...");
        try {
            final Process proc = SystemUtils.process("docker", "images", "--format", "'{{json .}}'", "--no-trunc", "--all");
            proc.waitFor();
            new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().forEach(e -> {
                log.info("Retrieved image info: " + e);
                try {
                    images.add(imageSer.fromSpec(mapper.readValue(SerialUtils.stripEnds(e, "'"), ImageSpec.class)));
                } catch (JsonProcessingException ex) {
                    log.error("Could not retrieve image!", ex);
                }
            });
        } catch (IOException e) {
            log.error("Could not retrieve images!", e);
        } catch (InterruptedException ignored) {
            // ignored
        }
        log.info("Image info refreshed.");
    }

    public Optional<Image> getImage(String img) {
        return images.stream().filter(image -> image.getRepository().equals(img) || (image.getRepository() + ":" + image.getTag()).equals(img) || image.getId().equals(img)).findFirst();
    }
}
