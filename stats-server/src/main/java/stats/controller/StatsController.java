package stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import stats.model.dto.EndpointHit;
import stats.model.ViewStats;
import stats.service.StatsService;

import java.util.List;

import static stats.mapper.HitMapper.toHit;

/**
 * Контроллер статистики {@link EndpointHit}
 *
 * @author safiulinrm
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    /**
     * Сервис для работы со статистикой {@link stats.service.StatsService}
     */
    private final StatsService statsService;

    /**
     * Сохранение просмотра события
     *
     * @param endpointHit данные о просмотре события {@link EndpointHit}
     * @return возвращает сохраненный просмотр
     */
    @PostMapping("/hit")
    public EndpointHit postHit(@RequestBody EndpointHit endpointHit) {
        statsService.postHit(toHit(endpointHit));
        log.info("Информация сохранена");
        return endpointHit;
    }

    /**
     * Получение статистики просмотров
     *
     * @param start  дата и время не раньше которых должно просмотрено событие
     * @param end    текущее время и дата
     * @param uris   список путей событий
     * @param unique уникальное/полное количество просмотров
     * @return список статистик {@link List {@link ViewStats}}
     */
    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        var stats = statsService.getStats(start, end, uris, unique);
        log.info("Статистика собрана");
        return stats;
    }
}
