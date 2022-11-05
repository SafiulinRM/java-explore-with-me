package ewm.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, с помощью которого передаются сведения о просмотре события {@link ewm.model.Event}
 *
 * @author safiulinrm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    /**
     * Название микросервиса
     */
    private String app;
    /**
     * Путь события
     */
    private String uri;
    /**
     * ip пользователя
     */
    private String ip;
    /**
     * Дата и время просмотра события
     */
    private String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    /**
     * Конструктор просмотра события
     *
     * @param app название микросервиса
     * @param uri Путь события
     * @param ip  ip пользователя
     */
    public EndpointHit(String app, String uri, String ip) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
    }
}