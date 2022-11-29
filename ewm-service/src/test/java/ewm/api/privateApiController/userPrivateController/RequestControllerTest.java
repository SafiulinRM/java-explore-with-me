package ewm.api.privateApiController.userPrivateController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.ParticipationRequestDto;
import ewm.model.*;
import ewm.service.interfaces.RequestService;
import ewm.util.status.EventState;
import ewm.util.status.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private RequestService requestService;
    @Autowired
    private MockMvc mvc;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Category testCategory = new Category(1L, "category");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final Comment testComment = new Comment("comment", testUser);
    private final Location testLocation =
            new Location(1L, (float) 64.6, (float) 64.67);
    private final Event testEvent = new Event(
            List.of(testComment),
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
    private final User testRequester = new User("requester1@mail.com", 2L, "requester");
    private final Request testRequest = new Request(
            testEvent,
            1L,
            testRequester,
            RequestStatus.CONFIRMED);
    private final ParticipationRequestDto testRequestDto = new ParticipationRequestDto(
            testRequest.getCreated().format(FORMATTER),
            1L,
            1L,
            2L,
            RequestStatus.CONFIRMED);

    @Test
    void getRequestsOfUser() throws Exception {
        when(requestService.getRequestsOfUser(anyLong())).thenReturn(List.of(testRequestDto));
        mvc.perform(get("/users/1/requests"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testRequestDto))));
    }

    @Test
    void addRequest() throws Exception {
        when(requestService.addRequest(anyLong(), anyLong())).thenReturn(testRequestDto);
        mvc.perform(post("/users/2/requests?eventId=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testRequestDto)));
    }

    @Test
    void cancelRequest() throws Exception {
        ParticipationRequestDto requestDto = testRequestDto;
        requestDto.setStatus(RequestStatus.CANCELED);
        when(requestService.cancelRequest(anyLong(), anyLong())).thenReturn(requestDto);
        mvc.perform(patch("/users/2/requests/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }
}