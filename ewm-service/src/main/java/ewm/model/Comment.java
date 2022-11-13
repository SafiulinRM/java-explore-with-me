package ewm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс хранения комментария события.
 *
 * @author safiulinrm
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    /**
     * id комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * текст комментария.
     */
    @Column(nullable = false)
    private String text;
    /**
     * Пользователь, который прокомментировал
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "commentator_id", nullable = false)
    private User commentator;
    /**
     * Дата и время создания комментария события
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    public Comment(String text, User commentator) {
        this.text = text;
        this.commentator = commentator;
    }
}
