package me.kcra.dockeractyl.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.utils.SerialUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

public class NetworkSerializer extends StdSerializer<Network> {
    protected NetworkSerializer(Class<Network> t) {
        super(t);
    }

    @Override
    public void serialize(Network value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("CreatedAt", SerialUtils.toTimestamp(value.getCreatedAt()));
        gen.writeStringField("Driver", value.getDriver().name().toLowerCase(Locale.ROOT));
        gen.writeStringField("ID", value.getId());
        gen.writeStringField("IPv6", Boolean.toString(value.isIpv6()));
        gen.writeStringField("Internal", Boolean.toString(value.isInternal()));
        gen.writeStringField("Labels", value.getLabels().entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(",")));
        gen.writeStringField("Name", value.getName());
        gen.writeStringField("Scope", value.getScope().name().toLowerCase(Locale.ROOT));
        gen.writeEndObject();
    }
}
