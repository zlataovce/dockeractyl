package me.kcra.dockeractyl.docker.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.kcra.dockeractyl.serial.ImageDeserializer;
import me.kcra.dockeractyl.serial.ImageSerializer;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@JsonSerialize(using = ImageSerializer.class)
@JsonDeserialize(using = ImageDeserializer.class)
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
