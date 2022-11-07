package ewm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Dto обновленного комментария события {@link ewm.model.Comment}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentDto {
    /**
     * текст комментария.
     */
    @NotNull
    private String text;
}
