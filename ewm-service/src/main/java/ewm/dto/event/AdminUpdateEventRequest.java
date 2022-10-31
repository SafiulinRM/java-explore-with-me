package ewm.dto.event;

import ewm.dto.LocationShort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationShort location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private String title;
}
