package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.kcra.dockeractyl.docker.model.Network;

import java.io.IOException;
import java.util.Locale;

public class PortSerializer extends StdSerializer<Network.Port> {
    protected PortSerializer(Class<Network.Port> t) {
        super(t);
    }

    @Override
    public void serialize(Network.Port value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value.getOuter() != null) {
            gen.writeString(value.getInner() + "->" + value.getOuter().getKey() + "/" + value.getOuter().getValue().name().toLowerCase(Locale.ROOT));
        } else {
            gen.writeString(value.getInner());
        }
    }
}
