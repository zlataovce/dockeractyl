package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.Container;
import me.kcra.dockeractyl.docker.Image;
import me.kcra.dockeractyl.docker.spec.ImageSpec;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageSerializer implements BidirectionalSerializer<ImageSpec, Image> {
    @Override
    public Image fromSpec(ImageSpec spec) {
        return Image.builder()
                .containers(toContainers(spec.getContainers()))
                .createdAt(SerialUtils.fromTimestamp(spec.getCreatedAt()))
                .digest(spec.getDigest())
                .id(spec.getId())
                .repository(spec.getRepository())
                .sharedSize(SerialUtils.parseFileSize(spec.getSharedSize()))
                .size(SerialUtils.parseFileSize(spec.getSize()))
                .tag(spec.getTag())
                .uniqueSize(SerialUtils.parseFileSize(spec.getUniqueSize()))
                .virtualSize(SerialUtils.parseFileSize(spec.getVirtualSize()))
                .build();
    }

    @Override
    public ImageSpec toSpec(Image exact) {
        return ImageSpec.builder()
                .containers(fromContainers(exact.getContainers()))
                .createdAt(SerialUtils.toTimestamp(exact.getCreatedAt()))
                .digest(exact.getDigest())
                .id(exact.getId())
                .repository(exact.getRepository())
                .sharedSize(SerialUtils.humanReadableSize(exact.getSharedSize()))
                .size(SerialUtils.humanReadableSize(exact.getSize()))
                .tag(exact.getTag())
                .uniqueSize(SerialUtils.humanReadableSize(exact.getUniqueSize()))
                .virtualSize(SerialUtils.humanReadableSize(exact.getVirtualSize()))
                .build();
    }

    private List<Container> toContainers(String s) {
        return new ArrayList<>();
    }

    private String fromContainers(List<Container> containers) {
        return "N/A";
    }
}
