package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class Responses {
    public <T> ResponseEntity<T> notFound(T body) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
