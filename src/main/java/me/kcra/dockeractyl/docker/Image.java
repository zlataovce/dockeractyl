package me.kcra.dockeractyl.docker;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(builderClassName = "Builder")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {
    private List<Container> containers;
    private Date createdAt;
    private String digest;
    private String id;
    private String repository;
    private String sharedSize;
    private String size;
    private String tag;
    private String uniqueSize;
    private String virtualSize;
}
