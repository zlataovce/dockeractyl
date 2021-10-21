package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.Image;
import me.kcra.dockeractyl.docker.spec.ImageSpec;
import org.springframework.stereotype.Service;

@Service
public class ImageSerializer implements BidirectionalSerializer<ImageSpec, Image> {
    @Override
    public Image fromSpec(ImageSpec spec) {
        return null;
    }

    @Override
    public ImageSpec toSpec(Image exact) {
        return null;
    }
}
