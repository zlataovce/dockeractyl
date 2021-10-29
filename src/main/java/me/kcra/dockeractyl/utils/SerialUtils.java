package me.kcra.dockeractyl.utils;

import lombok.experimental.UtilityClass;
import me.kcra.dockeractyl.docker.model.Network;

import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class SerialUtils {
    private final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z z");
    private final Pattern SIZE_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?) ?((?:[TGMKk]i?)?B)");
    private final Pattern DOCKER_SIZE_PATTERN = Pattern.compile("((\\d+(?:\\.\\d+)?) ?((?:[TGMKk]i?)?B))(?: \\(virtual ((\\d+(?:\\.\\d+)?) ?((?:[TGMKk]i?)?B))\\))?");
    private final Pattern PROPERTIES_PATTERN = Pattern.compile("^([^=]+)=(.*)$");
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
        if (matcher.matches()) {
            final long virtualSize = (matcher.results().count() == 2) ? parseFileSize(matcher.group(2)) : 0;
            return ImmutablePair.of(parseFileSize(matcher.group(1)), virtualSize);
        }
        return ImmutablePair.of(0L, 0L);
    }

    public long parseFileSize(String s) {
        final Matcher matcher = SIZE_PATTERN.matcher(s);
        if (matcher.matches()) {
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
                case "B":
                    return Math.round(ret);
            }
        }
        return 0;
    }

    public String humanReadableSize(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        final CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public String sizeString(long size, long virtual) {
        return (virtual == 0) ? humanReadableSize(size) : humanReadableSize(size) + " (virtual " + humanReadableSize(virtual) + ")";
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

    public String stripEnds(String s, String end) {
        if (s.startsWith(end)) {
            s = s.replaceFirst(end, "");
        }
        if (s.endsWith(end)) {
            s = s.substring(0, s.lastIndexOf(end) - 1);
        }
        return s;
    }

    public Map<String, String> parseLabels(String s) {
        return Arrays.stream(s.split(",")).map(e -> {
            final Matcher matcher = PROPERTIES_PATTERN.matcher(e);
            if (matcher.matches()) {
                return Map.entry(matcher.group(1), matcher.group(2));
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Network.Port deserializePort(String node) {
        if (node.contains("->")) {
            final String[] parts = node.split("->");
            final String[] portParts = parts[1].split("/");
            return new Network.Port(parts[0], ImmutablePair.of(Integer.parseInt(portParts[0]), Network.Protocol.valueOf(portParts[1].toUpperCase(Locale.ROOT))));
        }
        return new Network.Port(node, null);
    }

    public String serializePort(Network.Port value) {
        if (value.getOuter() != null) {
            return value.getInner() + "->" + value.getOuter().getKey() + "/" + value.getOuter().getValue().name().toLowerCase(Locale.ROOT);
        }
        return value.getInner();
    }
}
