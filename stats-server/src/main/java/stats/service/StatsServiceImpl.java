package stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stats.model.Hit;
import stats.model.ViewStats;
import stats.repo.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Реализация интерфейса по работе со статистикой просмотров событий {@link ViewStats}
 *
 * @author safiulinrm
 * @see stats.model.dto.EndpointHit
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    /**
     * Общий формат времени во всей программе
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Репозиторий для работы с просмотрами событий {@link HitRepository}
     */
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void postHit(Hit hit) {
        hitRepository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(String start,
                                    String end,
                                    List<String> uris,
                                    Boolean unique) {
        List<ViewStats> stats;
        LocalDateTime formattedStart = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime formattedEnd = LocalDateTime.parse(end, FORMATTER);
        if (unique.equals(true)) {
            stats = hitRepository.getStatsUnique(formattedStart, formattedEnd, uris);
        } else {
            stats = hitRepository.getStats(formattedStart, formattedEnd, uris);
        }
        return stats;
    }
}