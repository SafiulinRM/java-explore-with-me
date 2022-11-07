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
public class NewUserRequest {
    /**
     * почта пользователся.
     */
    @Email
    private String email;
    /**
     * имя пользователся.
     */
    @NotBlank
    private String name;
}
