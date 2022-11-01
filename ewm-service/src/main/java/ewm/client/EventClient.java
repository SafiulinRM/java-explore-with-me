package ewm.client;

import ewm.dto.stats.EndpointHit;
import ewm.dto.stats.ViewStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("stats-server.url")
    private String baseUri;

    public void postHit(EndpointHit endpointHit) {
        var entity = new HttpEntity<>(endpointHit);
        var restTemplate = new RestTemplate();
        restTemplate.exchange((baseUri + "/hit"),
                HttpMethod.POST,
                entity,
                Object.class);
    }

    public ResponseEntity<ViewStats[]> getStats(List<String> uris) {
        var entity = new HttpEntity<>(null);
        String start = LocalDateTime.now().minusDays(7).format(FORMATTER);
        start = URLEncoder.encode(start, StandardCharsets.UTF_8);
        String end = LocalDateTime.now().format(FORMATTER);
        end = URLEncoder.encode(end, StandardCharsets.UTF_8);
        var urisBuilder = new StringBuilder("uris=");
        int count = 0;
        for (String uri : uris) {
            count++;
            if (count == uris.size())
                urisBuilder.append(uri);
            else
                urisBuilder.append(uri).append("uris=");
        }
        var restTemplate = new RestTemplate();
        return restTemplate.exchange((baseUri + "/stats?start=" + start +
                        "&end=" + end + "&unique=true&" + urisBuilder),
                HttpMethod.GET,
                entity,
                ViewStats[].class);
    }
}

