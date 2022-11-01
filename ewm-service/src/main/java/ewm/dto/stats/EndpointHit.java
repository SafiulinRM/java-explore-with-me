package ewm.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
    private String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public EndpointHit(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}