package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.model.spec.NetworkSpec;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NetworkSerializer implements DockerSerializer<NetworkSpec, Network> {
    @Override
    public NetworkSpec toSpec(Network exact) {
        return NetworkSpec.builder()
                .createdAt(SerialUtils.toTimestamp(exact.getCreatedAt()))
                .driver(exact.getDriver().name().toLowerCase(Locale.ROOT))
                .id(exact.getId())
                .ipv6(Boolean.toString(exact.isIpv6()))
                .internal(Boolean.toString(exact.isInternal()))
                .labels(exact.getLabels().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(",")))
                .name(exact.getName())
                .scope(exact.getScope().name().toLowerCase(Locale.ROOT))
                .build();
    }

    @Override
    public Network fromSpec(NetworkSpec spec) {
        return Network.builder()
                .createdAt(SerialUtils.fromTimestamp(spec.getCreatedAt()))
                .driver(Network.Driver.valueOf(spec.getDriver().toUpperCase(Locale.ROOT)))
                .id(spec.getId())
                .ipv6(Boolean.parseBoolean(spec.getIpv6()))
                .internal(Boolean.parseBoolean(spec.getInternal()))
                .labels(SerialUtils.parseLabels(spec.getLabels()))
                .name(spec.getName())
                .scope(Network.Scope.valueOf(spec.getScope().toUpperCase(Locale.ROOT)))
                .build();
    }
}
