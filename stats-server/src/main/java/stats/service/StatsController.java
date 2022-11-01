package stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import stats.model.EndpointHit;
import stats.model.ViewStats;

import java.util.List;

import static stats.service.HitMapper.toHit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit postHit(@RequestBody EndpointHit endpointHit) {
        statsService.postHit(toHit(endpointHit));
        log.info("Информация сохранена");
        return endpointHit;
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique,
                                    @RequestParam List<String> uris) {
        var stats = statsService.getStats(start, end, uris, unique);
        log.info("Статистика собрана");
        return stats;
    }
}
