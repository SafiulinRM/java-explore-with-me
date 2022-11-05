package ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Dto локации {@link ewm.model.Location}.
 *
 * @author safiulinrm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationShort {
    /**
     * широта.
     */
    @NotNull
    private Float lat;
    /**
     * долгота.
     */
    @NotNull
    private Float lon;
}
