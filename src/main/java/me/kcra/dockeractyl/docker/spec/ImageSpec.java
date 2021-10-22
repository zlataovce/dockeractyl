package me.kcra.dockeractyl.docker.spec;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder")
public final class ImageSpec implements Specification {
    @JsonProperty("Containers")
    private String containers;
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("CreatedSince")
    private String createdSince;
    @JsonProperty("Digest")
    private String digest;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Repository")
    private String repository;
    @JsonProperty("SharedSize")
    private String sharedSize;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("Tag")
    private String tag;
    @JsonProperty("UniqueSize")
    private String uniqueSize;
    @JsonProperty("VirtualSize")
    private String virtualSize;
}
