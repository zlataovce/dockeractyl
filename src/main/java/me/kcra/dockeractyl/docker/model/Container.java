package me.kcra.dockeractyl.docker.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import me.kcra.dockeractyl.serial.ContainerDeserializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@JsonDeserialize(using = ContainerDeserializer.class)
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

    public enum State {
        RUNNING, EXITED
    }
}
