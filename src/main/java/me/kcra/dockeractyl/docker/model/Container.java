package me.kcra.dockeractyl.docker.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
public class Container {
    private String command;
    private Date createdAt;
    private String id;
    private Image image;
    private Map<String, String> labels;
    private int localVolumes;
    private String mounts;
    private String names;
    private Collection<Network> networks;
    private Collection<Network.Port> ports;
    private long size;
    private long virtualSize;
    private State state;
    private String status;

    public enum State {
        RUNNING, EXITED
    }
}
