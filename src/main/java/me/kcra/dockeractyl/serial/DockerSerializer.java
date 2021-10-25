package me.kcra.dockeractyl.serial;

public interface DockerSerializer<S, E> {
    S toSpec(E exact);
    E fromSpec(S spec);
}
