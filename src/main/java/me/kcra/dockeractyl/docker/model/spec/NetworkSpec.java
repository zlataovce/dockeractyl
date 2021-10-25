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
public final class NetworkSpec {
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("Driver")
    private String driver;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("IPv6")
    private String ipv6;
    @JsonProperty("Internal")
    private String internal;
    @JsonProperty("Labels")
    private String labels;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Scope")
    private String scope;
}
