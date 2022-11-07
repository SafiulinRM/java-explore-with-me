package ewm.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс передачи данных статистики о просмотре события
 *
 * @author safiulinrm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    /**
     * Название микросервиса
     */
    private String app;
    /**
     * путь события
     */
    private String uri;
    /**
     * количество просмотров события
     */
    private Long hits;
}