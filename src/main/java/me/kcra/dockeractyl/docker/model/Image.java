package me.kcra.dockeractyl.docker.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
public class Image {
    private Collection<Container> containers;
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
