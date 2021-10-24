package me.kcra.dockeractyl.docker;

import lombok.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class Container {
    private String command;
    private Date createdAt;
    private String id;
    private Image image;
    private Map<String, String> labels;
    private int localVolumes;
    private String mounts;
    private String names;
    private List<Network> networks;
    private List<Network.Port> ports;
    private long size;
    private long virtualSize;
    private State state;
    private String status;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum State {
        RUNNING("running"),
        EXITED("exited");

        private final String dockerState;

        public static State fromDocker(String dockerState) {
            return Arrays.stream(State.values()).filter(e -> e.dockerState.equals(dockerState)).findFirst().orElseThrow(() -> new IllegalArgumentException("No enum found"));
        }
    }
}
