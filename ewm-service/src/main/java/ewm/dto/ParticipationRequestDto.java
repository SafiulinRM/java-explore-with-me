package ewm.dto;

import ewm.util.status.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto запроса {@link ewm.model.Request}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    /**
     * дата запроса.
     */
    private String created;
    /**
     * id события на которое идет запрос.
     */
    private Long event;
    /**
     * id запроса.
     */
    private Long id;
    /**
     * id Пользователя делающего запрос.
     */
    private Long requester;
    /**
     * статус запроса.
     */
    private RequestStatus status;
}
