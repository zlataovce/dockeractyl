package me.kcra.dockeractyl.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kcra.dockeractyl.docker.model.Container;
import me.kcra.dockeractyl.docker.store.ContainerStore;
import me.kcra.dockeractyl.utils.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/container")
public class ContainerController {
    private final ContainerStore containerStor;
    private final ObjectMapper mapper;

    @Autowired
    public ContainerController(ContainerStore containerStor, ObjectMapper mapper) {
        this.containerStor = containerStor;
        this.mapper = mapper;
    }

    @GetMapping(path = "/find/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findContainer(@PathVariable String id) {
        final Optional<Container> container = containerStor.getContainer(id);
        return (container.isPresent()) ? ResponseEntity.ok(container.orElseThrow()) : Responses.notFound(Collections.singletonMap("error", "Container not found."));
    }

    @GetMapping(path = "/find/{id}/raw", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findContainerRaw(@PathVariable String id) {
        final Optional<Container> container = containerStor.getContainer(id);
        return (container.isPresent()) ? ResponseEntity.ok(containerSer.toSpec(container.orElseThrow())) : Responses.notFound(Collections.singletonMap("error", "Container not found."));
    }

    @GetMapping(path = "/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> allContainers() {
        return ResponseEntity.ok(containerStor.getContainers());
    }

    @PostMapping(path = "/compose")
    public ResponseEntity<?> composeContainer(@RequestBody String body) {
        try {
            new Yaml().load(body);
        } catch (YAMLException e) {
            return Responses.badRequest(Collections.singletonMap("error", "Invalid YAML structure"));
        }
        containerStor.composeAsynchronously(body.split("\n"));
        return Responses.processing(null);
    }
}
