package ewm.mapper;

import ewm.dto.comment.CommentDto;
import ewm.model.Comment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, с помощью которого можно преобразовать комментарий события в dto и наоборот
 *
 * @author safiulinrm
 * @see ewm.model.Comment
 * @see ewm.dto.comment.CommentDto
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    /**
     * Преобразование комментария в dto
     *
     * @param comment данные комментария
     * @return {@link CommentDto}
     */
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getText(),
                comment.getCommentator().getName(),
                comment.getCreatedOn());
    }

    /**
     * Преобразование списка комментариев события в dto
     *
     * @param comments список комментов {@link List {@link Comment}}
     * @return {@link List {@link CommentDto}}
     */
    public static List<CommentDto> toCommentsDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
