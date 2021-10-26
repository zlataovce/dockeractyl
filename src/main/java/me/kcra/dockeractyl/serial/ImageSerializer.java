package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.docker.model.Image;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.stream.Collectors;

public class ImageSerializer extends StdSerializer<Image> {
    protected ImageSerializer(Class<Image> t) {
        super(t);
    }

    @Override
    public void serialize(Image value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value.getContainers().size() == 0) {
            gen.writeStringField("Containers", "N/A");
        } else {
            gen.writeStringField("Containers", value.getContainers().stream().map(Container::getId).collect(Collectors.joining(", ")));
        }
        gen.writeStringField("Digest", value.getDigest());
        gen.writeStringField("ID", value.getId());
        gen.writeStringField("Repository", value.getRepository());
        gen.writeStringField("SharedSize", SerialUtils.humanReadableSize(value.getSharedSize()));
        gen.writeStringField("Size", SerialUtils.humanReadableSize(value.getSize()));
        gen.writeStringField("Tag", value.getTag());
        gen.writeStringField("UniqueSize", SerialUtils.humanReadableSize(value.getUniqueSize()));
        gen.writeStringField("VirtualSize", SerialUtils.humanReadableSize(value.getVirtualSize()));
        gen.writeEndObject();
    }
}
