package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.kcra.dockeractyl.docker.model.Image;

import java.io.IOException;

public class ImageSerializer extends StdSerializer<Image> {
    protected ImageSerializer(Class<Image> t) {
        super(t);
    }

    @Override
    public void serialize(Image value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        // gen.writeStringField("Containers", value.getContainers().stream().map(e -> ));
    }
}
