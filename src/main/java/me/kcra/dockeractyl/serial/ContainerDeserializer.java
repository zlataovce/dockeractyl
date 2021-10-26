package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import me.kcra.dockeractyl.DockeractylApplication;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.store.ImageStore;
import me.kcra.dockeractyl.docker.store.NetworkStore;
import me.kcra.dockeractyl.utils.ImmutablePair;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class ContainerDeserializer extends StdDeserializer<Container> {
    private final ImageStore images = DockeractylApplication.applicationContext.getBean(ImageStore.class);
    private final NetworkStore networks = DockeractylApplication.applicationContext.getBean(NetworkStore.class);

    protected ContainerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Container deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);
        final ImmutablePair<Long, Long> sizes = SerialUtils.parseDockerSizes(node.get("Size").asText());
        return Container.builder()
                .command(SerialUtils.stripEnds(node.get("Command").asText(), "\""))
                .createdAt(SerialUtils.fromTimestamp(node.get("CreatedAt").asText()))
                .id(node.get("ID").asText())
                .image(images.getImage(node.get("Image").asText()).orElseThrow(() -> new RuntimeException("Could not find image for container: " + node.get("ID").asText())))
                .labels(SerialUtils.parseLabels(node.get("Labels").asText()))
                .localVolumes(Integer.parseInt(node.get("LocalVolumes").asText()))
                .mounts(node.get("Mounts").asText())
                .names(node.get("Names").asText())
                .networks(
                        Arrays.stream(node.get("Networks").asText().split(", "))
                                .map(net -> networks.getNetwork(net).orElseGet(() -> Network.builder()
                                        .driver(Network.Driver.valueOf(net.toUpperCase(Locale.ROOT)))
                                        .build()
                                )).collect(Collectors.toUnmodifiableList())
                )
                // ports
                .size(sizes.getKey())
                .virtualSize(sizes.getValue())
                .state(Container.State.valueOf(node.get("State").asText().toUpperCase(Locale.ROOT)))
                .status(node.get("Status").asText())
                .build();
    }
}
