package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.Container;
import me.kcra.dockeractyl.docker.spec.ContainerSpec;
import me.kcra.dockeractyl.docker.store.ImageStore;
import me.kcra.dockeractyl.utils.ImmutablePair;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContainerSerializer implements BidirectionalSerializer<ContainerSpec, Container> {
    private final ImageStore imageStor;

    @Autowired
    public ContainerSerializer(ImageStore imageStor) {
        this.imageStor = imageStor;
    }

    @Override
    public Container fromSpec(ContainerSpec spec) {
        final ImmutablePair<Long, Long> sizes = SerialUtils.parseDockerSizes(spec.getSize());
        return Container.builder()
                .command(SerialUtils.stripEnds(spec.getCommand(), "\""))
                .createdAt(SerialUtils.fromTimestamp(spec.getCreatedAt()))
                .id(spec.getId())
                .image(imageStor.getImageByRepository(spec.getImage()).orElseThrow(() -> new RuntimeException("Could not find image for container " + spec.getId() + "!")))
                .labels(preprocessLabels(spec.getLabels()))
                .localVolumes(Integer.parseInt(spec.getLocalVolumes()))
                .mounts(spec.getMounts())
                .names(spec.getNames())
                .networks(spec.getNetworks())
                .ports(spec.getPorts())
                .size(sizes.getKey())
                .virtualSize(sizes.getValue())
                .state(spec.getState())
                .status(spec.getStatus())
                .build();
    }

    @Override
    public ContainerSpec toSpec(Container exact) {
        return ContainerSpec.builder()
                .command("\"" + exact.getCommand() + "\"")
                .createdAt(SerialUtils.toTimestamp(exact.getCreatedAt()))
                .id(exact.getId())
                .image(exact.getImage().getRepository())
                .labels(exact.getLabels().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(",")))
                .localVolumes(Integer.toString(exact.getLocalVolumes()))
                .mounts(exact.getMounts())
                .names(exact.getNames())
                .networks(exact.getNetworks())
                .ports(exact.getPorts())
                .size(SerialUtils.sizeString(exact.getSize(), exact.getVirtualSize()))
                .state(exact.getState())
                .status(exact.getStatus())
                .build();
    }

    private Map<String, String> preprocessLabels(String s) {
        final Map<String, String> procLabels = new HashMap<>();
        Arrays.stream(s.split(",")).forEach(e -> {
            final String[] parts = e.split("=");
            procLabels.put(parts[0], String.join("=", Arrays.copyOfRange(parts, 1, parts.length)));
        });
        return procLabels;
    }
}
