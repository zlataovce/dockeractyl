package me.kcra.dockeractyl.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.store.NetworkStore;
import me.kcra.dockeractyl.utils.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/network")
public class NetworkController {
    private final NetworkStore networkStor;
    private final ObjectMapper mapper;

    @Autowired
    public NetworkController(NetworkStore networkStor, ObjectMapper mapper) {
        this.networkStor = networkStor;
        this.mapper = mapper;
    }

    @GetMapping(path = "/find/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findNetwork(@PathVariable String id) {
        final Optional<Network> result = networkStor.getNetwork(id);
        return (result.isPresent()) ? ResponseEntity.ok(result.orElseThrow()) : Responses.notFound(Collections.singletonMap("error", "Network not found."));
    }

    @GetMapping(path = "/find/{id}/raw", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> findNetworkRaw(@PathVariable String id) {
        final Optional<Network> result = networkStor.getNetwork(id);
        return (result.isPresent()) ? ResponseEntity.ok(networkSer.toSpec(result.orElseThrow())) : Responses.notFound(Collections.singletonMap("error", "Network not found."));
    }
}
