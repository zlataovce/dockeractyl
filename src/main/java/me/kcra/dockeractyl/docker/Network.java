package me.kcra.dockeractyl.docker;

import lombok.*;
import me.kcra.dockeractyl.utils.ImmutablePair;
import org.springframework.lang.Nullable;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class Network {
    private @Nullable String name;
    private Type type;

    public enum Type {
        BRIDGE, HOST, OVERLAY, MACVLAN, NONE
    }

    public enum Protocol {
        UDP, TDP
    }

    @Data
    public static class Port {
        private final String inner;
        private final @Nullable ImmutablePair<Integer, Protocol> outer;
    }
}
