package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.kcra.dockeractyl.DockeractylApplication;
import me.kcra.dockeractyl.docker.model.Image;
import me.kcra.dockeractyl.docker.store.ContainerStore;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class ImageDeserializer extends StdDeserializer<Image> {
    private final ContainerStore containers = DockeractylApplication.applicationContext.getBean(ContainerStore.class);

    protected ImageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Image deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);
        return Image.builder()
                .containers(
                        Arrays.stream(node.get("Containers").asText().split(", "))
                                .map(e -> containers.getContainer(e).orElseThrow(() -> new RuntimeException("Could not find container '" + e + "' for image: " + node.get("ID").asText())))
                                .collect(Collectors.toUnmodifiableList())
                )
                .createdAt(SerialUtils.fromTimestamp(node.get("CreatedAt").asText()))
                .digest(node.get("Digest").asText())
                .id(node.get("ID").asText())
                .repository(node.get("Repository").asText())
                .sharedSize(SerialUtils.parseFileSize(node.get("SharedSize").asText()))
                .size(SerialUtils.parseFileSize(node.get("Size").asText()))
                .tag(node.get("Tag").asText())
                .uniqueSize(SerialUtils.parseFileSize(node.get("UniqueSize").asText()))
                .virtualSize(SerialUtils.parseFileSize(node.get("VirtualSize").asText()))
                .build();
    }
}
