package ewm.service.interfaces;

import ewm.dto.comment.CommentDto;
import ewm.dto.comment.NewCommentDto;
import ewm.dto.comment.UpdateCommentDto;

/**
 * Интерфейс сервиса комментариев события
 */
public interface CommentService {
    /**
     * Сохранение комментария событий
     *
     * @param newCommentDto данные нового комментария событий{@link NewCommentDto}
     * @param eventId       id события которого комментируют
     * @return {@link CommentDto}
     */
    CommentDto postComment(NewCommentDto newCommentDto, Long eventId);

    /**
     * Обновление уже существующего комментария событий
     *
     * @param updateCommentDto данные обновленного комментария событий
     * @param commentId        id комментария, который собираются обновить
     * @return {@link CommentDto}
     */
    CommentDto patchComment(UpdateCommentDto updateCommentDto, Long commentId);
}
