package ewm.controller.privateController;

import ewm.dto.ParticipationRequestDto;
import ewm.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable Long userId) {
        var requests = requestService.getRequestsOfUser(userId);
        log.info("Найдены запросы на участие");
        return requests;
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam Long eventId) {
        var request = requestService.addRequest(userId, eventId);
        log.info("Заявка создана");
        return request;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        var request = requestService.cancelRequest(userId, requestId);
        log.info("Заявка отменена");
        return request;
    }
}
