package me.kcra.dockeractyl.docker.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.model.Network;
import me.kcra.dockeractyl.docker.model.spec.NetworkSpec;
import me.kcra.dockeractyl.serial.DockerSerializer;
import me.kcra.dockeractyl.serial.NetworkSerializer;
import me.kcra.dockeractyl.utils.SerialUtils;
import me.kcra.dockeractyl.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NetworkStore {
    @Getter
    private final List<Network> networks = Collections.synchronizedList(new ArrayList<>());
    private final DockerSerializer<NetworkSpec, Network> networkSer;
    private final ObjectMapper mapper;

    @Autowired
    public NetworkStore(NetworkSerializer networkSer, ObjectMapper mapper) {
        this.networkSer = networkSer;
        this.mapper = mapper;
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateNetworks() {
        networks.clear();
        log.info("Refreshing network info...");
        try {
            final Process proc = SystemUtils.process("docker", "network", "ls", "--format", "'{{json .}}'", "--no-trunc");
            proc.waitFor();
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                reader.lines().forEach(e -> {
                    log.info("Retrieved network details: " + e);
                    try {
                        networks.add(networkSer.fromSpec(mapper.readValue(SerialUtils.stripEnds(e, "'"), NetworkSpec.class)));
                    } catch (JsonProcessingException ex) {
                        log.error("Could not retrieve network!", ex);
                    }
                });
            }
        } catch (IOException e) {
            log.error("Could not retrieve networks!", e);
        } catch (InterruptedException ignored) {
            // ignored
        }
        log.info("Refreshed network info.");
    }

    public Optional<Network> getNetwork(String net) {
        return networks.stream().filter(e -> e.getName().equals(net) || e.getId().equals(net)).findFirst();
    }
}
