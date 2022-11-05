package ewm.model;

import ewm.util.status.EventState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс хранения событий.
 *
 * @author safiulinrm
 * @see ewm.dto.event.AdminUpdateEventRequest
 * @see ewm.dto.event.EventFullDto
 * @see ewm.dto.event.EventShortDto
 * @see ewm.dto.event.NewEventDto
 * @see ewm.dto.event.UpdateEventRequest
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    /**
     * аннотация события.
     */
    @Column(length = 1024, nullable = false)
    private String annotation;
    /**
     * категория события.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    /**
     * кол-во подтвержденных запросов на событие.
     */
    @Column(name = "confirmed_requests")
    private long confirmedRequests;
    /**
     * время создания события.
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();
    /**
     * описание события.
     */
    @Column(length = 1024, nullable = false)
    private String description;
    /**
     * дата события.
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    /**
     * id события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * инициатор события.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    /**
     * место события.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    /**
     * платное/бесплатное событие.
     */
    @Column(nullable = false)
    private Boolean paid;
    /**
     * лимит на участников события.
     */
    @Column(name = "participant_limit")
    private long participantLimit;
    /**
     * время публикации события.
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    /**
     * подтверждать/не подтверждать запросы.
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    /**
     * статус события.
     */
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;
    /**
     * заглавие события.
     */
    @Column(length = 128, nullable = false)
    private String title;
    /**
     * кол-во просмотров события.
     */
    @Column
    private long views;

    /**
     * конструктор события.
     */
    public Event(String annotation,
                 Category category,
                 String description,
                 LocalDateTime eventDate,
                 User initiator,
                 Location location,
                 Boolean paid,
                 long participantLimit,
                 Boolean requestModeration,
                 String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
