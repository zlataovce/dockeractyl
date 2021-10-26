package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.utils.ImmutablePair;

import java.io.IOException;
import java.util.Locale;

public class PortDeserializer extends StdDeserializer<Network.Port> {
    protected PortDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Network.Port deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String node = p.getValueAsString();
        if (node.contains("->")) {
            final String[] parts = node.split("->");
            final String[] portParts = parts[1].split("/");
            return new Network.Port(parts[0], ImmutablePair.of(Integer.parseInt(portParts[0]), Network.Protocol.valueOf(portParts[1].toUpperCase(Locale.ROOT))));
        }
        return new Network.Port(node, null);
    }
}
