package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.Network;
import me.kcra.dockeractyl.utils.SerialUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PortSerializer implements BidirectionalSerializer<String, Network.Port> {
    @Override
    public String toSpec(Network.Port exact) {
        if (exact.getOuter() != null) {
            return exact.getInner() + "->" + exact.getOuter().getKey() + "/" + exact.getOuter().getValue().name().toLowerCase(Locale.ROOT);
        }
        return exact.getInner();
    }

    @Override
    public Network.Port fromSpec(String spec) {
        if (spec.contains("->")) {
            final String[] parts = spec.split("->");
            return new Network.Port(parts[0], SerialUtils.fromDockerPort(parts[1]));
        }
        return new Network.Port(spec, null);
    }
}
