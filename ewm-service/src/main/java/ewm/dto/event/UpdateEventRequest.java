package ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Dto события при обновлении пользователем {@link ewm.model.Event}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    /**
     * аннотация события.
     */
    private String annotation;
    /**
     * категория события.
     */
    private Long category;
    /**
     * описание события.
     */
    private String description;
    /**
     * дата события.
     */
    private String eventDate;
    /**
     * id события.
     */
    @NotNull
    private Long eventId;
    /**
     * платное/бесплатное событие.
     */
    private Boolean paid;
    /**
     * лимит на участников события.
     */
    private Long participantLimit;
    /**
     * заглавие события.
     */
    private String title;
}
