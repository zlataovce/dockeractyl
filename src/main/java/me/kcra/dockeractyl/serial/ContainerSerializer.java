package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContainerSerializer extends StdSerializer<Container> {
    protected ContainerSerializer(Class<Container> t) {
        super(t);
    }

    @Override
    public void serialize(Container value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("Command", "\"" + value.getCommand() + "\"");
        gen.writeStringField("CreatedAt", SerialUtils.toTimestamp(value.getCreatedAt()));
        gen.writeStringField("ID", value.getId());
        gen.writeStringField("Image", value.getImage().getId());
        gen.writeStringField("Labels", value.getLabels().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(",")));
        gen.writeStringField("LocalVolumes", Integer.toString(value.getLocalVolumes()));
        gen.writeStringField("Mounts", value.getMounts());
        gen.writeStringField("Names", value.getNames());
        gen.writeStringField(
                "Networks",
                value.getNetworks().stream()
                        .map(net -> Objects.requireNonNullElse(net.getName(), net.getDriver().name().toLowerCase(Locale.ROOT)))
                        .collect(Collectors.joining(", "))
        );
        gen.writeStringField("Ports", value.getPorts().stream().map(SerialUtils::serializePort).collect(Collectors.joining(", ")));
        gen.writeStringField("Size", SerialUtils.sizeString(value.getSize(), value.getVirtualSize()));
        gen.writeStringField("State", value.getState().name().toLowerCase(Locale.ROOT));
        gen.writeStringField("Status", value.getStatus());
        gen.writeEndObject();
    }
}
