package ewm.model;

import ewm.util.status.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс хранения событий.
 *
 * @author safiulinrm
 * @see ewm.dto.ParticipationRequestDto
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * дата запроса.
     */
    @Column
    private final LocalDateTime created = LocalDateTime.now();
    /**
     * событие на которое идет запрос.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    /**
     * id запроса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Пользователь делающий запрос.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    /**
     * статус запроса.
     */
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    /**
     * конструктор запроса.
     *
     * @param event     событие
     * @param requester пользователь
     */
    public Request(Event event, User requester) {
        this.event = event;
        this.requester = requester;
    }
}
