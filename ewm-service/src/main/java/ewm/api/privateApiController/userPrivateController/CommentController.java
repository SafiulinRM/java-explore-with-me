package ewm.api.privateApiController.userPrivateController;

import ewm.dto.comment.CommentDto;
import ewm.dto.comment.NewCommentDto;
import ewm.dto.comment.UpdateCommentDto;
import ewm.service.interfaces.CommentService;
import ewm.service.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Закрытый контроллер комментирования событий ({@link ewm.model.Comment})
 * для пользователей
 *
 * @author safiulinrm
 */
@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class CommentController {
    /**
     * Сервис для работы с комментариями в событии {@link RequestService}
     */
    private final CommentService commentService;

    /**
     * Сохранение комментария событий
     *
     * @param newCommentDto данные нового комментария событий{@link NewCommentDto}
     * @param eventId       id события которого комментируют
     * @return {@link CommentDto}
     */
    @PostMapping("/{eventId}/comments")
    public CommentDto postComment(@RequestBody @Validated NewCommentDto newCommentDto,
                                  @PathVariable Long eventId) {
        var comment = commentService.postComment(newCommentDto, eventId);
        log.info("Комментарий сохранен");
        return comment;
    }

    /**
     * Обновление уже существующего комментария событий
     *
     * @param updateCommentDto данные обновленного комментария событий
     * @param commentId        id комментария, который собираются обновить
     * @return {@link CommentDto}
     */
    @PatchMapping("/comments/{commentId}")
    public CommentDto patchComment(@RequestBody @Validated UpdateCommentDto updateCommentDto,
                                   @PathVariable Long commentId) {
        var comment = commentService.patchComment(updateCommentDto, commentId);
        log.info("Комментарий обновлен");
        return comment;
    }
}
