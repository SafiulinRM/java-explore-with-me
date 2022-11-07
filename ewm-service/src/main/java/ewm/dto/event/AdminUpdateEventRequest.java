package ewm.dto.event;

import ewm.dto.LocationShort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto события при обновлении администратором {@link ewm.model.Event}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
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
     * место события.
     */
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
     * заглавие события.
     */
    private String title;
}
