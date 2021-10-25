package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class Responses {
    public <T> ResponseEntity<T> notFound(T body) {
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    public <T> ResponseEntity<T> badRequest(T body) {
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<T> processing(T body) {
        return new ResponseEntity<>(body, HttpStatus.PROCESSING);
    }
}
