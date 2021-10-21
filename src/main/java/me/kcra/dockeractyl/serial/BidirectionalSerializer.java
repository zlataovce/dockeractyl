package me.kcra.dockeractyl.serial;

public interface BidirectionalSerializer<S, E> extends Serializer<S, E>, Deserializer<S, E> {
}
