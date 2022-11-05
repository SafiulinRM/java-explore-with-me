package ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Dto категории {@link ewm.dto.compilation.NewCompilationDto}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    /**
     * список событий.
     */
    private List<Long> events;
    /**
     * закрепленная/не закрепленная подборка.
     */
    private Boolean pinned;
    /**
     * заглавие подборки.
     */
    @NotBlank
    private String title;
}
