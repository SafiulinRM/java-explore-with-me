package ewm.service;

import ewm.dto.ParticipationRequestDto;
import ewm.exception.NotFoundException;
import ewm.exception.RequestException;
import ewm.exception.StateException;
import ewm.exception.UserEventException;
import ewm.model.Event;
import ewm.model.Request;
import ewm.model.User;
import ewm.repo.EventRepository;
import ewm.repo.RequestRepository;
import ewm.repo.UserRepository;
import ewm.status.EventState;
import ewm.status.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ewm.mapper.RequestMapper.toParticipationRequestDto;
import static ewm.mapper.RequestMapper.toParticipationRequestsDto;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public List<ParticipationRequestDto> getRequestsOfEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        List<Request> requests = requestRepository.findByEventId(eventId);
        return toParticipationRequestsDto(requests);
    }

    @Transactional
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        validationConfirmRequest(event);
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("Request not found " + reqId));
        request.setStatus(RequestStatus.CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            setStatusOfRequests(eventId);
        }
        return toParticipationRequestDto(request);
    }

    @Transactional
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("Request not found " + reqId));
        request.setStatus(RequestStatus.REJECTED);
        return toParticipationRequestDto(request);
    }

    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        return toParticipationRequestsDto(requestRepository.findByRequesterId(userId));
    }

    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        validationRequest(requester, event);
        Request saveRequest = requestRepository.save(new Request(event, requester));
        Request request = requestRepository.findById(saveRequest.getId())
                .orElseThrow(() -> new NotFoundException("Request not found " + saveRequest.getId()));
        if (event.getRequestModeration().equals(false)) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return toParticipationRequestDto(request);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found " + requestId));
        if (userId != request.getRequester().getId()) {
            throw new RequestException("Юзер " + userId + " не является владельцем запроса " + requestId);
        }
        request.setStatus(RequestStatus.CANCELED);
        return toParticipationRequestDto(request);
    }

    private void checkInitiatorOfEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new UserEventException("User " + userId + " not initiator of event " + initiatorId);
        }
    }

    private void validationRequest(User requester, Event event) {
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
            throw new RequestException("У события достигнут лимит запросов на участие");
        }
    }

    private void validationConfirmRequest(Event event) {
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            throw new StateException("Лимит заявок равен 0 или отключена пре-модерация заявок");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new RequestException("У события достигнут лимит запросов на участие");
        }
    }

    private void setStatusOfRequests(Long eventId) {
        List<Request> requests = requestRepository.findByEventId(eventId);
        for (Request req : requests) {
            req.setStatus(RequestStatus.REJECTED);
        }
    }
}
