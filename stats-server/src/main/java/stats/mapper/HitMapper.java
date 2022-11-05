package stats.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import stats.model.dto.EndpointHit;
import stats.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, с помощью которого можно преобразовать просмотр события в dto и наоборот
 *
 * @author safiulinrm
 * @see Hit
 * @see EndpointHit
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    /**
     * Общий формат времени во всей программе
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Преобразовывает данные просмотра события в класс хранения
     *
     * @param endpointHit данные просмотра события {@link EndpointHit}
     * @return {@link Hit}
     */
    public static Hit toHit(EndpointHit endpointHit) {
        return new Hit(null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                LocalDateTime.parse(endpointHit.getTimestamp(), FORMATTER));
    }
}
