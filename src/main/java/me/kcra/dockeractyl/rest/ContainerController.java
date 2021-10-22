package me.kcra.dockeractyl.rest;

import me.kcra.dockeractyl.docker.Container;
import me.kcra.dockeractyl.docker.spec.ContainerSpec;
import me.kcra.dockeractyl.docker.store.ContainerStore;
import me.kcra.dockeractyl.serial.BidirectionalSerializer;
import me.kcra.dockeractyl.serial.ContainerSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/container")
public class ContainerController {
    private final ContainerStore containerStor;
    private final BidirectionalSerializer<ContainerSpec, Container> containerSer;

    @Autowired
    public ContainerController(ContainerStore containerStor, ContainerSerializer containerSer) {
        this.containerStor = containerStor;
        this.containerSer = containerSer;
    }

    @GetMapping(path = "/find/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findContainer(@PathVariable String id) {
        final Optional<Container> container = containerStor.getContainerByID(id);
        return (container.isPresent()) ? new ResponseEntity<>(container.orElseThrow(), HttpStatus.OK) : new ResponseEntity<>(Collections.singletonMap("error", "Container not found."), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/find/{id}/raw", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findContainerRaw(@PathVariable String id) {
        final Optional<Container> container = containerStor.getContainerByID(id);
        return (container.isPresent()) ? new ResponseEntity<>(containerSer.toSpec(container.orElseThrow()), HttpStatus.OK) : new ResponseEntity<>(Collections.singletonMap("error", "Container not found."), HttpStatus.NOT_FOUND);
    }
}
