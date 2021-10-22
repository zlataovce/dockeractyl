package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;

import java.text.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class SerialUtils {
    private final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z z");
    private final Pattern SIZE_PATTERN = Pattern.compile("([0-9]+(?:\\.[0-9]+)?) ?([TGMKk]i?B)");
    private final Pattern DOCKER_SIZE_PATTERN = Pattern.compile("(([0-9]+(?:\\.[0-9]+)?) ?([TGMKk]i?B)) \\(virtual (([0-9]+(?:\\.[0-9]+)?) ?([TGMKk]i?B))\\)");
    private final long KB_FACTOR = 1000;
    private final long KIB_FACTOR = 1024;
    private final long MB_FACTOR = 1000 * KB_FACTOR;
    private final long MIB_FACTOR = 1024 * KIB_FACTOR;
    private final long GB_FACTOR = 1000 * MB_FACTOR;
    private final long GIB_FACTOR = 1024 * MIB_FACTOR;
    private final long TB_FACTOR = 1000 * GB_FACTOR;
    private final long TIB_FACTOR = 1024 * GIB_FACTOR;

    public ImmutablePair<Long, Long> parseDockerSizes(String s) {
        final Matcher matcher = DOCKER_SIZE_PATTERN.matcher(s);
        return ImmutablePair.of(parseFileSize(matcher.group(1)), parseFileSize(matcher.group(2)));
    }

    public long parseFileSize(String s) {
        final Matcher matcher = SIZE_PATTERN.matcher(s);
        final double ret = (s.contains(".")) ? Double.parseDouble(matcher.group(1)) : Integer.parseInt(matcher.group(1));
        switch (matcher.group(2)) {
            case "TB":
                return Math.round(ret * TB_FACTOR);
            case "TiB":
                return Math.round(ret * TIB_FACTOR);
            case "GB":
                return Math.round(ret * GB_FACTOR);
            case "GiB":
                return Math.round(ret * GIB_FACTOR);
            case "MB":
                return Math.round(ret * MB_FACTOR);
            case "MiB":
                return Math.round(ret * MIB_FACTOR);
            case "kB":
            case "KB":
                return Math.round(ret * KB_FACTOR);
            case "kiB":
            case "KiB":
                return Math.round(ret * KIB_FACTOR);
            default:
                return 0;
        }
    }

    public String humanReadableSize(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGT");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public Date fromTimestamp(String timestamp) {
        try {
            return DATE_FORMATTER.parse(timestamp);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String toTimestamp(Date date) {
        return DATE_FORMATTER.format(date);
    }
}
