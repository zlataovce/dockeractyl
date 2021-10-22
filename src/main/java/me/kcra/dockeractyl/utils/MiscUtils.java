package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Stream;

@UtilityClass
public class MiscUtils {
    public boolean hasExecutable(String s) {
        return Stream.of(
                Paths.get(System.getProperty("user.home"), ".local", "bin", s).toFile(),
                Paths.get(System.getProperty("user.home"), "bin", s).toFile(),
                new File("/usr/local/sbin/" + s),
                new File("/usr/local/bin/" + s),
                new File("/usr/sbin/" + s),
                new File("/usr/bin/" + s),
                new File("/sbin/" + s),
                new File("/bin/" + s)
        ).anyMatch(e -> e.exists() && e.isFile());
    }
}
