package me.kcra.dockeractyl.docker.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ComposeException extends RuntimeException {
    public ComposeException() {
        super();
    }

    public ComposeException(String message) {
        super(message);
    }

    public ComposeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComposeException(Throwable cause) {
        super(cause);
    }
}
