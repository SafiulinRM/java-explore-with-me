package exploreWithMe.client;
import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;
import exploreWithMe.dto.stats.EndpointHit;
import exploreWithMe.dto.stats.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class EventClient extends BaseClient {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public EventClient(@Value("${ewm-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void postHit(EndpointHit endpointHit) {
        post("/hit", endpointHit);
    }

   /*  public List<ViewStats> getStats(String start,
                                    String end,
                                    List<String> uris,
                                    Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> responseEntity = get("/stats", parameters);
        Gson gson = new Gson();

         List<ViewStats> objects = gson.fromJson(gson.toJson(responseEntity.getBody()), List<ViewStats>.class);
        return get("/stats", parameters); */
    }

