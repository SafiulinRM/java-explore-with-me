package stats.model;

import lombok.*;

/**
 * Класс передачи данных статистики о просмотре события
 *
 * @author safiulinrm
 */
@Getter
@Setter
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
