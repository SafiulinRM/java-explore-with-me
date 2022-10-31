package ewm.mapper;

import ewm.dto.ParticipationRequestDto;
import ewm.model.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getCreated().format(FORMATTER),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public static List<ParticipationRequestDto> toParticipationRequestsDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(toList());
    }
}
