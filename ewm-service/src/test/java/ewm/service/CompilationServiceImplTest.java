package ewm.service;

import ewm.dto.LocationShort;
import ewm.dto.category.CategoryDto;
import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.user.UserShortDto;
import ewm.model.*;
import ewm.repo.CompilationRepository;
import ewm.repo.EventRepository;
import ewm.util.status.EventState;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CompilationServiceImplTest {
    @Mock
    CompilationRepository compilationRepository;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    CompilationServiceImpl compilationService;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");
    private final UserShortDto testShortUserDto = new UserShortDto(1L, "user");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final Comment testComment = new Comment("comment", testUser);
    private final LocationShort testLocationDto = new LocationShort((float) 64.6, (float) 64.67);
    private final Location testLocation = new Location(1L, (float) 64.6, (float) 64.67);
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
    private final EventShortDto testEventShortDto = new EventShortDto("annotation",
            testCategoryDto,
            10L,
            testEvent.getEventDate(),
            1L,
            testShortUserDto,
            true,
            "title",
            1L);
    private final CompilationDto testCompilationDto = new CompilationDto(
            List.of(testEventShortDto),
            1L,
            true,
            "title");
    private final Compilation testCompilation = new Compilation(
            List.of(testEvent),
            1L,
            true,
            "title");
    private final NewCompilationDto testNewCompilation = new NewCompilationDto(
            List.of(1L),
            true,
            "title");

    @Test
    void getCompilationsWithOutPinned() {
        when(compilationRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl(List.of(testCompilation)));
        val compilationsDto = compilationService
                .getCompilationsByPinned(null, 0, 10);
        assertEquals(compilationsDto, List.of(testCompilationDto), "Получить подборки не удалось");
    }

    @Test
    void getCompilationsWithPinned() {
        when(compilationRepository.findByPinned(anyBoolean(), any(PageRequest.class))).thenReturn(new PageImpl(List.of(testCompilation)));
        val compilationsDto = compilationService
                .getCompilationsByPinned(true, 0, 10);
        assertEquals(compilationsDto, List.of(testCompilationDto),
                "Получить подборки по параметру pinned не удалось");
    }

    @Test
    void getCompilation() {
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(testCompilation));
        val compilationDto = compilationService.getCompilation(1L);
        assertEquals(compilationDto, testCompilationDto, "Получить подборку не удалось");
    }

    @Test
    void addCompilation() {
        when(compilationRepository.save(any(Compilation.class))).thenReturn(testCompilation);
        when(eventRepository.findAllById(any())).thenReturn(List.of(testEvent));
        val compilationDto = compilationService.addCompilation(testNewCompilation);
        assertEquals(compilationDto, testCompilationDto, "Сохранить подборку не удалось");
    }

    @Test
    void deleteCompilation() {
        compilationService.deleteCompilation(1L);
        verify(compilationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEventOfCompilation() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(testEvent);
        Compilation compilation = testCompilation;
        compilation.setEvents(events);
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(compilation));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        compilationService.deleteEventOfCompilation(1L, 1L);
        verify(compilationRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(1L);
        assertTrue(compilation.getEvents().isEmpty(), "Событие не удалилось из подборки");
    }

    @Test
    void addEventOfCompilation() {
        ArrayList<Event> events = new ArrayList<>();
        Compilation compilation = testCompilation;
        compilation.setEvents(events);
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(compilation));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        compilationService.addEventOfCompilation(1L, 1L);
        verify(compilationRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(1L);
        assertFalse(compilation.getEvents().isEmpty(), "Событие не добавилось в подборку");
    }

    @Test
    void unpinCompilation() {
        Compilation compilation = testCompilation;
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(compilation));
        compilationService.unpinCompilation(1L);
        assertFalse(compilation.getPinned(), "Подборка не открепилась");
    }

    @Test
    void pinCompilation() {
        Compilation compilation = testCompilation;
        compilation.setPinned(false);
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(compilation));
        compilationService.pinCompilation(1L);
        assertTrue(compilation.getPinned(), "Подборка не закрепилась");
    }
}