package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

@UtilityClass
public class SystemUtils {
    public boolean hasExecutable(String s) {
        if (getPlatform() == Platform.WINDOWS) {
            return hasExecutableWindows(s);
        } else {
            return hasExecutableUnix(s);
        }
    }

    private boolean hasExecutableWindows(String s) {
        return System.getenv("PATH").toLowerCase(Locale.ROOT).contains("docker");
    }

    private boolean hasExecutableUnix(String s) {
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

    public Platform getPlatform() {
        return (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) ? Platform.WINDOWS : Platform.UNIX;
    }

    public Process process(String... command) throws IOException {
        return new ProcessBuilder(command).start();
    }

    // no dumb macOS
    public enum Platform {
        WINDOWS,
        UNIX
    }
}
