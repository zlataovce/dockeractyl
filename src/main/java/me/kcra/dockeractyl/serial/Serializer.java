package me.kcra.dockeractyl.serial;

public interface Serializer<S, E> {
    E fromSpec(S spec);
}
