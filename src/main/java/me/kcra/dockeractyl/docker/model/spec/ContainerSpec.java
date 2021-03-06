package me.kcra.dockeractyl.docker.model.spec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(builderClassName = "Builder")
public final class ContainerSpec {
    @JsonProperty("Command")
    private String command;
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Labels")
    private String labels;
    @JsonProperty("LocalVolumes")
    private String localVolumes;
    @JsonProperty("Mounts")
    private String mounts;
    @JsonProperty("Names")
    private String names;
    @JsonProperty("Networks")
    private String networks;
    @JsonProperty("Ports")
    private String ports;
    @JsonProperty("RunningFor")
    private String runningFor;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Status")
    private String status;
}
