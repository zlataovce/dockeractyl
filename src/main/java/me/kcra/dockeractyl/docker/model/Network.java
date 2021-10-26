package me.kcra.dockeractyl.docker.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import me.kcra.dockeractyl.serial.PortDeserializer;
import me.kcra.dockeractyl.utils.ImmutablePair;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class Network {
    private Date createdAt;
    private Driver driver;
    private String id;
    private boolean ipv6;
    private boolean internal;
    private Map<String, String> labels;
    private String name;
    private Scope scope;

    public enum Driver {
        BRIDGE, HOST, OVERLAY, MACVLAN, NONE
    }

    public enum Protocol {
        UDP, TCP
    }

    public enum Scope {
        SWARM, GLOBAL, LOCAL
    }

    @Data
    @JsonDeserialize(using = PortDeserializer.class)
    public static class Port {
        private final String inner;
        private final @Nullable ImmutablePair<Integer, Protocol> outer;
    }
}
