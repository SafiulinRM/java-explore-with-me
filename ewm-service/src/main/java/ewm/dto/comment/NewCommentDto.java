package ewm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Dto нового комментария события {@link ewm.model.Comment}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    /**
     * текст комментария.
     */
    @NotBlank
    private String text;
    /**
     * Пользователь, который прокомментировал
     */
    @NotNull
    private Long commentator;
}
