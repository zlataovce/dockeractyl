package me.kcra.dockeractyl.rest;

import me.kcra.dockeractyl.docker.Image;
import me.kcra.dockeractyl.docker.spec.ImageSpec;
import me.kcra.dockeractyl.docker.store.ImageStore;
import me.kcra.dockeractyl.serial.BidirectionalSerializer;
import me.kcra.dockeractyl.serial.ImageSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/image")
public class ImageController {
    private final ImageStore imageStor;
    private final BidirectionalSerializer<ImageSpec, Image> imageSer;

    @Autowired
    public ImageController(ImageStore imageStor, ImageSerializer imageSer) {
        this.imageStor = imageStor;
        this.imageSer = imageSer;
    }

    @GetMapping(path = "/find/{repo}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findImage(@PathVariable String repo) {
        final Optional<Image> image = imageStor.getImage(repo);
        return (image.isPresent()) ? new ResponseEntity<>(image.orElseThrow(), HttpStatus.OK) : new ResponseEntity<>(Collections.singletonMap("error", "Image not found."), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/find/{repo}/raw", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findImageRaw(@PathVariable String repo) {
        final Optional<Image> image = imageStor.getImage(repo);
        return (image.isPresent()) ? new ResponseEntity<>(imageSer.toSpec(image.orElseThrow()), HttpStatus.OK) : new ResponseEntity<>(Collections.singletonMap("error", "Image not found."), HttpStatus.NOT_FOUND);
    }
}
