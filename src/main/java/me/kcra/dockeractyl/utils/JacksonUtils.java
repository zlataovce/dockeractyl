package me.kcra.dockeractyl.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonUtils {
    public final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
}
