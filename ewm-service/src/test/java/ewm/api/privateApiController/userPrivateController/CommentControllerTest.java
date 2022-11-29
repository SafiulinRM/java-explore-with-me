package ewm.api.privateApiController.userPrivateController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.comment.CommentDto;
import ewm.dto.comment.NewCommentDto;
import ewm.dto.comment.UpdateCommentDto;
import ewm.model.Comment;
import ewm.model.User;
import ewm.service.interfaces.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final Comment testComment = new Comment("comment", testUser);
    private final CommentDto testCommentDto = new CommentDto(
            "comment",
            testUser.getName(),
            testComment.getCreatedOn());
    private final NewCommentDto testNewComment = new NewCommentDto("comment", 1L);
    private final UpdateCommentDto testUpdateComment = new UpdateCommentDto("update text");

    @Test
    void postComment() throws Exception {
        when(commentService.postComment(any(NewCommentDto.class), anyLong()))
                .thenReturn(testCommentDto);
        mvc.perform(post("/events/1/comments")
                        .content(mapper.writeValueAsString(testNewComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCommentDto)));
    }

    @Test
    void patchComment() throws Exception {
        when(commentService.patchComment(any(UpdateCommentDto.class), anyLong()))
                .thenReturn(testCommentDto);
        mvc.perform(patch("/events/comments/1")
                        .content(mapper.writeValueAsString(testUpdateComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCommentDto)));
    }
}