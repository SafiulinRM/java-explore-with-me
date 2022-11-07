package ewm.util.client;

import ewm.dto.stats.EndpointHit;
import ewm.dto.stats.ViewStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Клиент, который связывается с сервером статистики для обработки информации
 *
 * @author safiulinrm
 */
@Service
public class EventClient {
    /**
     * Общий формат времени во всей программе
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Путь к серверу
     */
    @Value("${stats-server.url}")
    private String baseUri;

    /**
     * Сохранение просмотра события
     *
     * @param endpointHit данные о просмотре {@link EndpointHit}
     */
    public void postHit(EndpointHit endpointHit) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        var entity = new HttpEntity<>(endpointHit, headers);
        var restTemplate = new RestTemplate();
        restTemplate.exchange(baseUri + "/hit",
                HttpMethod.POST,
                entity,
                Object.class);
    }

    /**
     * Получение статистики просмотров событий
     *
     * @param uris список путей на события
     * @return массив статистик просмотров {@link ViewStats}
     */
    public ResponseEntity<ViewStats[]> getStats(List<String> uris) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        var entity = new HttpEntity<>(null, headers);
        String start = LocalDateTime.now().minusDays(7).format(FORMATTER);
        String end = LocalDateTime.now().plusMinutes(1).format(FORMATTER);
        String uri = buildUri(uris);
        var restTemplate = new RestTemplate();
        return restTemplate.exchange(baseUri + "/stats?start=" + start + "&end=" + end + "&" + uri + "&unique=true",
                HttpMethod.GET,
                entity,
                ViewStats[].class);
    }

    /**
     * Построение параметров пути
     *
     * @param uris список путей событий
     * @return строка готовых парам
     * <p>
     * етров
     */
    private String buildUri(List<String> uris) {
        var urisBuilder = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                urisBuilder.append("uris").append("=").append(uris.get(i)).append("&");
            } else {
                urisBuilder.append("uris").append("=").append(uris.get(i));
            }
        }
        return urisBuilder.toString();
    }
}

