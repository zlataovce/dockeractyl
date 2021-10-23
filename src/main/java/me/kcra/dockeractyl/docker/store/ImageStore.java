package me.kcra.dockeractyl.docker.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.Image;
import me.kcra.dockeractyl.docker.spec.ImageSpec;
import me.kcra.dockeractyl.serial.BidirectionalSerializer;
import me.kcra.dockeractyl.serial.ImageSerializer;
import me.kcra.dockeractyl.utils.JacksonUtils;
import me.kcra.dockeractyl.utils.MiscUtils;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ImageStore {
    @Getter
    private final List<Image> images = Collections.synchronizedList(new ArrayList<>());
    private final BidirectionalSerializer<ImageSpec, Image> imageSer;

    @Autowired
    public ImageStore(ImageSerializer imageSer) {
        this.imageSer = imageSer;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateImages() {
        images.clear();
        log.info("Refreshing image info...");
        try {
            final Process proc = MiscUtils.process("docker", "images", "--format", "'{{json .}}'", "--no-trunc", "--all");
            proc.waitFor();
            new BufferedReader(new InputStreamReader(proc.getInputStream())).lines().forEach(e -> {
                log.info("Retrieved image info: " + e);
                try {
                    images.add(imageSer.fromSpec(JacksonUtils.MAPPER.readValue(SerialUtils.stripEnds(e, "'"), ImageSpec.class)));
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

    public Optional<Image> getImageByRepository(String repo) {
        return images.stream().filter(image -> image.getRepository().equals(repo)).findFirst();
    }
}
