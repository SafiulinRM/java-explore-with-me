package ewm.dto.event;

import ewm.dto.LocationShort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private LocationShort location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotBlank
    private String title;
}
