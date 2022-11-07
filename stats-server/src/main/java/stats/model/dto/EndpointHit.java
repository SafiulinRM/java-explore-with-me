package stats.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, с помощью которого передаются сведения о просмотре события {@link stats.model.Hit}
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
