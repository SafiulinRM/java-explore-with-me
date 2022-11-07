package stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс хранения просмотра события {@link stats.model.dto.EndpointHit}
 *
 * @author safiulinrm
 */
@Entity
@Table(name = "hits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    /**
     * id просмотра
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Название микросервиса
     */
    @Column(length = 1024, nullable = false)
    private String app;
    /**
     * Путь события
     */
    @Column(length = 1024, nullable = false)
    private String uri;
    /**
     * ip пользователя
     */
    @Column(length = 1024, nullable = false)
    private String ip;
    /**
     * Дата и время просмотра события
     */
    @Column(name = " times_tamp", nullable = false)
    private LocalDateTime timestamp;
}
