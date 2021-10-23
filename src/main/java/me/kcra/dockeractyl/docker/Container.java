package me.kcra.dockeractyl.docker;

import lombok.*;

import java.util.Date;
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
    private String networks;
    private String ports;
    private long size;
    private long virtualSize;
    private String state;
    private String status;
}
