package me.kcra.dockeractyl.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kcra.dockeractyl.docker.model.Image;
import me.kcra.dockeractyl.docker.store.ImageStore;
import me.kcra.dockeractyl.utils.Responses;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ObjectMapper mapper;

    @Autowired
    public ImageController(ImageStore imageStor, ObjectMapper mapper) {
        this.imageStor = imageStor;
        this.mapper = mapper;
    }

    @GetMapping(path = "/find/{img}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findImage(@PathVariable String img) {
        final Optional<Image> image = imageStor.getImage(img);
        return (image.isPresent()) ? ResponseEntity.ok(image.orElseThrow()) : Responses.notFound(Collections.singletonMap("error", "Image not found."));
    }

    @GetMapping(path = "/find/{img}/raw", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findImageRaw(@PathVariable String img) {
        final Optional<Image> image = imageStor.getImage(img);
        return (image.isPresent()) ? ResponseEntity.ok(imageSer.toSpec(image.orElseThrow())) : Responses.notFound(Collections.singletonMap("error", "Image not found."));
    }

    @GetMapping(path = "/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> allImages() {
        return ResponseEntity.ok(imageStor.getImages());
    }
}
