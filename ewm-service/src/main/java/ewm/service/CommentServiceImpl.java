package ewm.service;

import ewm.dto.comment.CommentDto;
import ewm.dto.comment.NewCommentDto;
import ewm.dto.comment.UpdateCommentDto;
import ewm.exception.NotFoundException;
import ewm.exception.StateEventException;
import ewm.model.Comment;
import ewm.model.Event;
import ewm.model.User;
import ewm.repo.CommentRepository;
import ewm.repo.EventRepository;
import ewm.repo.UserRepository;
import ewm.service.interfaces.CommentService;
import ewm.util.status.EventState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static ewm.mapper.CommentMapper.toCommentDto;

/**
 * Реализация интерфейса по работе комментариями событий {@link CommentService}
 *
 * @author safiulinrm
 * @see ewm.model.Comment
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    /**
     * Репозиторий для работы с событиями {@link EventRepository}
     */
    private final EventRepository eventRepository;
    /**
     * Репозиторий для работы с пользователями событий {@link UserRepository}
     */
    private final UserRepository userRepository;
    /**
     * Репозиторий для работы с комментариями событий {@link ewm.repo.CommentRepository}
     */
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto postComment(NewCommentDto newCommentDto, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found" + eventId));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new StateEventException("Событие не опубликовано");
        }
        User commentator = userRepository.findById(newCommentDto.getCommentator()).orElseThrow();
        Comment comment = commentRepository.save(new Comment(newCommentDto.getText(), commentator));
        event.getComments().add(comment);
        return toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto patchComment(UpdateCommentDto updateCommentDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getCreatedOn().plusHours(1).isAfter(LocalDateTime.now()))
            comment.setText(updateCommentDto.getText());
        else
            throw new RuntimeException("Изменить комментарий можно только в течении часа");
        return toCommentDto(comment);
    }
}
