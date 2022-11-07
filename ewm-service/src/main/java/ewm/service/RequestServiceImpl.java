package ewm.service;

import ewm.dto.ParticipationRequestDto;
import ewm.exception.NotFoundException;
import ewm.exception.RequestException;
import ewm.exception.StateEventException;
import ewm.exception.UserEventException;
import ewm.model.Event;
import ewm.model.Request;
import ewm.model.User;
import ewm.repo.EventRepository;
import ewm.repo.RequestRepository;
import ewm.repo.UserRepository;
import ewm.service.interfaces.RequestService;
import ewm.util.status.EventState;
import ewm.util.status.RequestStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static ewm.mapper.RequestMapper.toParticipationRequestDto;
import static ewm.mapper.RequestMapper.toParticipationRequestsDto;

/**
 * Реализация интерфейса по работе с запросами на участие в событии {@link RequestService}
 *
 * @author safiulinrm
 * @see Request
 */
@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    /**
     * Сообщение для исключения об отсутствии события в базе данных
     */
    private static final String EVENT_NOT_FOUND = "Event not found ";
    /**
     * Сообщение для исключения об отсутствии пользователя в базе данных
     */
    private static final String USER_NOT_FOUND = "User not found ";
    /**
     * Сообщение для исключения о достижении лимита участников события
     */
    private static final String REQUEST_MESSAGE = "У события достигнут лимит запросов на участие";
    /**
     * Сообщение для исключения об отсутствии запроса на участие в базе данных
     */
    private static final String REQUEST_NOT_FOUND = "Request not found ";
    /**
     * Репозиторий для работы с запросами на участие в событие {@link RequestRepository}
     */
    private final RequestRepository requestRepository;
    /**
     * Репозиторий для работы с пользователями событий {@link UserRepository}
     */
    private final UserRepository userRepository;
    /**
     * Репозиторий для работы с событиями {@link EventRepository}
     */
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequestsOfEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        List<Request> requests = requestRepository.findByEventId(eventId);
        return toParticipationRequestsDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        validationConfirmRequest(event);
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + reqId));
        request.setStatus(RequestStatus.CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            setStatusOfRequests(eventId);
        }
        return toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + reqId));
        request.setStatus(RequestStatus.REJECTED);
        return toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        return toParticipationRequestsDto(requestRepository.findByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        validationRequest(requester, event);
        Request saveRequest = requestRepository.save(new Request(event, requester));
        Request request = requestRepository.findById(saveRequest.getId())
                .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + saveRequest.getId()));
        if (event.getRequestModeration().equals(false)) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND + requestId));
        if (userId != request.getRequester().getId()) {
            throw new RequestException("User " + userId + " not initiator of request " + requestId);
        }
        request.setStatus(RequestStatus.CANCELED);
        return toParticipationRequestDto(request);
    }

    /**
     * Проверка инициатора события
     *
     * @param userId      id возможного инициатора
     * @param initiatorId id события
     */
    private void checkInitiatorOfEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new UserEventException("User " + userId + " not initiator of event " + initiatorId);
        }
    }

    /**
     * Валидация запроса
     *
     * @param requester пользователь, который собирается участвовать {@link User}
     * @param event     событие {@link Event}
     */
    private void validationRequest(@NonNull User requester, @NonNull Event event) {
        Request request = requestRepository.findByRequesterIdAndEventId(requester.getId(), event.getId());
        if (request != null) {
            throw new RequestException("Такой запрос уже существует");
        }
        if (requester.getId() == event.getInitiator().getId()) {
            throw new RequestException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new RequestException(REQUEST_MESSAGE);
        }
    }

    /**
     * Проверка свободных мест для участия
     *
     * @param event событие {@link Event}
     */
    private void validationConfirmRequest(@NonNull Event event) {
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            throw new StateEventException("Лимит заявок равен 0 или отключена пре-модерация заявок");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new RequestException(REQUEST_MESSAGE);
        }
    }

    /**
     * Автоматическое изменение статусов запросов на отклоненные,
     * если свободные места на участие в событии закончились
     *
     * @param eventId id события
     */
    private void setStatusOfRequests(Long eventId) {
        List<Request> requests = requestRepository.findByEventId(eventId);
        for (Request req : requests) {
            req.setStatus(RequestStatus.REJECTED);
        }
    }
}
