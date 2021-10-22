package me.kcra.dockeractyl.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.docker.spec.Specification;

@Slf4j
@UtilityClass
public class JacksonUtils {
    public final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());

    public String rawImage(Specification spec) {
        try {
            return MAPPER.writeValueAsString(spec);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while serializing to a raw image.", e);
            return "{}";
        }
    }
}
