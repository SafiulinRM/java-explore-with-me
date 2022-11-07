package ewm.dto.event;

import ewm.dto.LocationShort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Dto события {@link ewm.model.Event}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    /**
     * аннотация события.
     */
    @NotBlank
    private String annotation;
    /**
     * категория события.
     */
    @NotNull
    private Long category;
    /**
     * описание события.
     */
    @NotBlank
    private String description;
    /**
     * дата события.
     */
    @NotNull
    private String eventDate;
    /**
     * место события.
     */
    @NotNull
    private LocationShort location;
    /**
     * платное/бесплатное событие.
     */
    private Boolean paid;
    /**
     * лимит на участников события.
     */
    private Long participantLimit;
    /**
     * подтверждать/не подтверждать запросы.
     */
    private Boolean requestModeration;
    /**
     * кол-во просмотров события.
     */
    @NotBlank
    private String title;
}
