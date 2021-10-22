package me.kcra.dockeractyl.docker;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Container {
    private String command;
    private Date createdAt;
    private String id;
    private Image image;
    private Map<String, String> labels;
    private String localVolumes;
    private String mounts;
    private String names;
    private String networks;
    private String ports;
    private long size;
    private String state;
    private String status;
}
