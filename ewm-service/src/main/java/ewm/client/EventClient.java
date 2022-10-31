package ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class EventClient extends BaseClient {

    @Autowired
    public EventClient(@Value("${ewm-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

   /*  public void postHit(EndpointHit endpointHit) {
        post("/hit", endpointHit);
    }

    public List<ViewStats> getStats(String start,
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
