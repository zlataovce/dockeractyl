package me.kcra.dockeractyl.serial;

public interface Deserializer<S, E> {
    S toSpec(E exact);
}
