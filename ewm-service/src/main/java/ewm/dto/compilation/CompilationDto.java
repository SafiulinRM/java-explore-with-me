package ewm.dto.compilation;

import ewm.dto.event.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Dto категории {@link ewm.dto.compilation.CompilationDto}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    /**
     * список событий.
     */
    private List<EventShortDto> events;
    /**
     * id категории.
     */
    @NotNull
    private Long id;
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
