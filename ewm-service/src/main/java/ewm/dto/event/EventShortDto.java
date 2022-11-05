package ewm.dto.event;

import ewm.dto.category.CategoryDto;
import ewm.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Dto события {@link ewm.model.Event}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    /**
     * аннотация события.
     */
    @NotBlank
    private String annotation;
    /**
     * категория события.
     */
    @NotNull
    private CategoryDto category;
    /**
     * кол-во подтвержденных запросов на событие.
     */
    private Long confirmedRequests;
    /**
     * дата события.
     */
    @NotNull
    private LocalDateTime eventDate;
    /**
     * id события.
     */
    private Long id;
    /**
     * инициатор события.
     */
    @NotNull
    private UserShortDto initiator;
    /**
     * платное/бесплатное событие.
     */
    @NotNull
    private Boolean paid;
    /**
     * заглавие события.
     */
    @NotBlank
    private String title;
    /**
     * кол-во просмотров события.
     */
    private Long views;
}
