package ewm.mapper;

import ewm.dto.ParticipationRequestDto;
import ewm.model.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Класс, с помощью которого можно преобразовать запрос на участие в dto и наоборот
 *
 * @author safiulinrm
 * @see Request
 * @see ParticipationRequestDto
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {
    /**
     * Общий формат времени во всей программе
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Преобразование запроса на участие в dto
     *
     * @param request данные о запросе на участие {@link Request}
     * @return {@link ParticipationRequestDto}
     */
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getCreated().format(FORMATTER),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    /**
     * Преобразование списка запросов на участие в dto
     *
     * @param requests request данные о запросе на участие {@link Request}
     * @return {@link List {@link ParticipationRequestDto}}
     */
    public static List<ParticipationRequestDto> toParticipationRequestsDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(toList());
    }
}
