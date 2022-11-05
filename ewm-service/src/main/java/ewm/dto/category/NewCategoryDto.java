package ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Dto категории {@link ewm.model.Category}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    /**
     * имя категории.
     */
    @NotBlank
    private String name;
}
