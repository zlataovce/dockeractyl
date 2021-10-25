package me.kcra.dockeractyl.rest;

import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.model.spec.NetworkSpec;
import me.kcra.dockeractyl.docker.store.NetworkStore;
import me.kcra.dockeractyl.serial.DockerSerializer;
import me.kcra.dockeractyl.serial.NetworkSerializer;
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
    private final DockerSerializer<NetworkSpec, Network> networkSer;

    @Autowired
    public NetworkController(NetworkStore networkStor, NetworkSerializer networkSer) {
        this.networkStor = networkStor;
        this.networkSer = networkSer;
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
