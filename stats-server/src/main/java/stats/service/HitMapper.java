package stats.service;

import stats.model.EndpointHit;
import stats.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(EndpointHit endpointHit) {
        return new Hit(null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                LocalDateTime.parse(endpointHit.getTimestamp(), FORMATTER));
    }
}
