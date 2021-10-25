package me.kcra.dockeractyl.docker.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class Image {
    private List<Container> containers;
    private Date createdAt;
    private String digest;
    private String id;
    private String repository;
    private long sharedSize;
    private long size;
    private String tag;
    private long uniqueSize;
    private long virtualSize;
}
