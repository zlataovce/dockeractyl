package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.Locale;

public class NetworkDeserializer extends StdDeserializer<Network> {
    protected NetworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Network deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);
        return Network.builder()
                .createdAt(SerialUtils.fromTimestamp(node.get("CreatedAt").asText()))
                .driver(Network.Driver.valueOf(node.get("Driver").asText().toUpperCase(Locale.ROOT)))
                .id(node.get("ID").asText())
                .ipv6(Boolean.parseBoolean(node.get("ID").asText()))
                .internal(Boolean.parseBoolean(node.get("Internal").asText()))
                .labels(SerialUtils.parseLabels(node.get("Labels").asText()))
                .name(node.get("Name").asText())
                .scope(Network.Scope.valueOf(node.get("Scope").asText().toUpperCase(Locale.ROOT)))
                .build();
    }
}
