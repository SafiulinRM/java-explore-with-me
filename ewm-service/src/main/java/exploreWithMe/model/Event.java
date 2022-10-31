package exploreWithMe.model;

import exploreWithMe.status.EventState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Column(length = 1024, nullable = false)
    private String annotation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "confirmed_requests")
    private long confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(length = 1024, nullable = false)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit")
    private long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;
    @Column(length = 128, nullable = false)
    private String title;
    @Column
    private long views;

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
