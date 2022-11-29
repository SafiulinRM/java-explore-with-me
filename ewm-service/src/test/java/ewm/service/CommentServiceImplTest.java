package ewm.service;

import ewm.dto.comment.CommentDto;
import ewm.dto.comment.NewCommentDto;
import ewm.dto.comment.UpdateCommentDto;
import ewm.exception.StateEventException;
import ewm.model.*;
import ewm.repo.CommentRepository;
import ewm.repo.EventRepository;
import ewm.repo.UserRepository;
import ewm.util.status.EventState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentServiceImplTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    CommentServiceImpl commentService;
    private final NewCommentDto testNewComment = new NewCommentDto("comment", 1L);
    private final Location testLocation = new Location(1L, (float) 64.6, (float) 64.67);
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final Category testCategory = new Category(1L, "category");
    private final Comment testComment = new Comment("comment", testUser);
    private final UpdateCommentDto testUpdateComment = new UpdateCommentDto("update text");
    private final Event testEvent = new Event(new ArrayList<>(),
            "annotation",
            testCategory,
            10L,
            LocalDateTime.now(),
            "description",
            LocalDateTime.now().plusHours(4),
            1L,
            testUser,
            testLocation,
            true,
            12L,
            LocalDateTime.now().plusHours(1),
            true,
            EventState.PUBLISHED,
            "title",
            1L);

    @Test
    void postComment() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        CommentDto commentDto = commentService.postComment(testNewComment, 1L);
        assertEquals(commentDto.getText(), testComment.getText(), "Коммент не сохранился");
        assertEquals(testEvent.getComments().get(0), testComment);
    }

    @Test
    void postCommentEventNotPublished() {
        Event event = testEvent;
        event.setState(EventState.CANCELED);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        val exception = assertThrows(StateEventException.class,
                () -> commentService.postComment(testNewComment, 1L), "Исключение не вышло");
        assertEquals(exception.getMessage(), "Событие не опубликовано");
    }

    @Test
    void patchComment() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        val commentDto = commentService.patchComment(testUpdateComment, 1L);
        assertEquals(commentDto.getText(), testUpdateComment.getText(), "Коммент не обновился");
    }

    @Test
    void patchCommentCreatedOnAfterTwoHours() {
        Comment comment = testComment;
        testComment.setCreatedOn(LocalDateTime.now().minusHours(2));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        val exception = assertThrows(RuntimeException.class,
                () -> commentService.patchComment(testUpdateComment, 1L), "Исключение не вышло");
        assertEquals(exception.getMessage(), "Изменить комментарий можно только в течении часа");
    }
}