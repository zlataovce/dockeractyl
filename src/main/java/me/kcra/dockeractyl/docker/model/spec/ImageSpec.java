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
public final class ImageSpec {
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
