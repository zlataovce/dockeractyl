package me.kcra.dockeractyl.serial;

import me.kcra.dockeractyl.docker.Network;
import me.kcra.dockeractyl.docker.spec.NetworkSpec;
import org.springframework.stereotype.Service;

@Service
public class NetworkSerializer implements DockerSerializer<NetworkSpec, Network> {
    @Override
    public NetworkSpec toSpec(Network exact) {
        return null;
    }

    @Override
    public Network fromSpec(NetworkSpec spec) {
        return null;
    }
}
