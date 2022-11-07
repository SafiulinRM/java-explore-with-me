package ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Dto пользователя {@link ewm.model.User}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    /**
     * id пользователся.
     */
    @NotNull
    private Long id;
    /**
     * имя пользователся.
     */
    @NotBlank
    private String name;
}
