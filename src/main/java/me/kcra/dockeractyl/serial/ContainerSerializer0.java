package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.model.spec.ContainerSpec;
import me.kcra.dockeractyl.docker.store.ImageStore;
import me.kcra.dockeractyl.docker.store.NetworkStore;
import me.kcra.dockeractyl.utils.ImmutablePair;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContainerSerializer0 implements DockerSerializer<ContainerSpec, Container> {
    private final ImageStore imageStor;
    private final NetworkStore networkStor;
    private final DockerSerializer<String, Network.Port> portSer;

    @Autowired
    public ContainerSerializer0(ImageStore imageStor, NetworkStore networkStor, PortSerializer0 portSer) {
        this.imageStor = imageStor;
        this.portSer = portSer;
        this.networkStor = networkStor;
    }

    @Override
    public Container fromSpec(ContainerSpec spec) {
        final ImmutablePair<Long, Long> sizes = SerialUtils.parseDockerSizes(spec.getSize());
        return Container.builder()
                .command(SerialUtils.stripEnds(spec.getCommand(), "\""))
                .createdAt(SerialUtils.fromTimestamp(spec.getCreatedAt()))
                .id(spec.getId())
                .image(imageStor.getImage(spec.getImage()).orElseThrow(() -> new RuntimeException("Could not find image for container " + spec.getId() + "!")))
                .labels(SerialUtils.parseLabels(spec.getLabels()))
                .localVolumes(Integer.parseInt(spec.getLocalVolumes()))
                .mounts(spec.getMounts())
                .names(spec.getNames())
                .networks(
                        Arrays.stream(spec.getNetworks().split(", "))
                                .map(net -> networkStor.getNetwork(net).orElseGet(() -> Network.builder()
                                        .driver(Network.Driver.valueOf(net.toUpperCase(Locale.ROOT)))
                                        .build()
                                )).collect(Collectors.toUnmodifiableList())
                )
                .ports(Arrays.stream(spec.getPorts().split(", ")).map(portSer::fromSpec).collect(Collectors.toUnmodifiableList()))
                .size(sizes.getKey())
                .virtualSize(sizes.getValue())
                .state(Container.State.valueOf(spec.getState().toUpperCase(Locale.ROOT)))
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
                .networks(
                        exact.getNetworks().stream()
                                .map(net -> Objects.requireNonNullElse(net.getName(), net.getDriver().name().toLowerCase(Locale.ROOT)))
                                .collect(Collectors.joining(", "))
                )
                .ports(exact.getPorts().stream().map(portSer::toSpec).collect(Collectors.joining(", ")))
                .size(SerialUtils.sizeString(exact.getSize(), exact.getVirtualSize()))
                .state(exact.getState().name().toLowerCase(Locale.ROOT))
                .status(exact.getStatus())
                .build();
    }
}
