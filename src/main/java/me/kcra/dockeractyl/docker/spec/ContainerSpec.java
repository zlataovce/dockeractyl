package me.kcra.dockeractyl.docker.spec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder")
public final class ContainerSpec implements Specification {
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
