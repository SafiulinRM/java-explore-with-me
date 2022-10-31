package ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    @NotNull
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
