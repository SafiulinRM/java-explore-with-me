package ewm.dto.event;

import ewm.dto.category.CategoryDto;
import ewm.dto.comment.CommentDto;
import ewm.dto.user.UserShortDto;
import ewm.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dto события {@link ewm.model.Event}.
 *
 * @author safiulinrm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    /**
     * список комментариев события.
     */
    private List<CommentDto> comments;
    /**
     * аннотация события.
     */
    @NotBlank
    private String annotation;
    /**
     * категория события.
     */
    @NotNull
    private CategoryDto category;
    /**
     * кол-во подтвержденных запросов на событие.
     */
    private Long confirmedRequests;
    /**
     * время создания события.
     */
    @NotNull
    private LocalDateTime createdOn;
    /**
     * описание события.
     */
    private String description;
    /**
     * дата события.
     */
    @NotNull
    private String eventDate;
    /**
     * id события.
     */
    private Long id;
    /**
     * инициатор события.
     */
    @NotNull
    private UserShortDto initiator;
    /**
     * место события.
     */
    @NotNull
    private Location location;
    /**
     * платное/бесплатное событие.
     */
    @NotNull
    private Boolean paid;
    /**
     * лимит на участников события.
     */
    private Long participantLimit;
    /**
     * время публикации события.
     */
    private LocalDateTime publishedOn;
    /**
     * подтверждать/не подтверждать запросы.
     */
    private Boolean requestModeration;
    /**
     * статус события.
     */
    private String state;
    /**
     * заглавие события.
     */
    @NotBlank
    private String title;
    /**
     * кол-во просмотров события.
     */
    private Long views;
}
