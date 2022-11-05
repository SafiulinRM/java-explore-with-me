package ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Dto категории {@link ewm.model.Category}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    /**
     * id категории.
     */
    @NotNull
    private Long id;
    /**
     * имя категории.
     */
    @NotBlank
    private String name;
}
