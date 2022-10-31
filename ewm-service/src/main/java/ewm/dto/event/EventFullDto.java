package ewm.dto.event;

import ewm.dto.category.CategoryDto;
import ewm.dto.user.UserShortDto;
import ewm.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    @NotNull
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    private String eventDate;
    private Long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    @NotBlank
    private String title;
    private Long views;
}
