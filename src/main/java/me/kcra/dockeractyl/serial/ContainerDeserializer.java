package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.utils.ImmutablePair;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;

public class ContainerDeserializer extends StdDeserializer<Container> {
    protected ContainerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Container deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = p.getCodec().readTree(p);
        final ImmutablePair<Long, Long> sizes = SerialUtils.parseDockerSizes(node.get("Size").asText());
        return Container.builder()
                .command(SerialUtils.stripEnds(node.get("Command").asText(), "\""))
                .createdAt(SerialUtils.fromTimestamp(node.get("CreatedAt").asText()))
                .id(node.get("ID").asText())
                .build();
    }
}
