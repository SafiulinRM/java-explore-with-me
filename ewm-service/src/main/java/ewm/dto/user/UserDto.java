package ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Dto пользователя {@link ewm.model.User}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    /**
     * почта пользователся.
     */
    @NotBlank
    @Email
    private String email;
    /**
     * id пользователся.
     */
    private Long id;
    /**
     * имя пользователся.
     */
    @NotBlank
    private String name;
}
