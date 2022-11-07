package ewm.model;

import lombok.*;

import javax.persistence.*;

/**
 * Класс хранения пользователей.
 *
 * @author safiulinrm
 * @see ewm.dto.user.NewUserRequest
 * @see ewm.dto.user.UserShortDto
 * @see ewm.dto.user.UserDto
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * почта пользователся.
     */
    @Column(length = 512, nullable = false, unique = true)
    private String email;
    /**
     * id пользователся.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * имя пользователся.
     */
    private long id;
    @Column(nullable = false, length = 255)
    private String name;
}
