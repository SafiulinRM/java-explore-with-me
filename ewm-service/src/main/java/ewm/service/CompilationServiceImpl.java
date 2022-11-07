package ewm.service;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.exception.NotFoundException;
import ewm.model.Compilation;
import ewm.model.Event;
import ewm.repo.CompilationRepository;
import ewm.repo.EventRepository;
import ewm.service.interfaces.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ewm.mapper.CompilationMapper.*;

/**
 * Реализация интерфейса по работе с подборками событий {@link CompilationService}
 *
 * @author safiulinrm
 * @see Compilation
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    /**
     * Сортировка по умолчанию по полю id класса категории
     */
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    /**
     * Сообщение для исключения об отсутствии подборки в базе данных
     */
    private static final String COMPILATION_NOT_FOUND = "Compilation not found ";
    /**
     * Сообщение для исключения об отсутствии событии в базе данных
     */
    private static final String EVENT_NOT_FOUND = "Event not found ";
    /**
     * Репозиторий для работы с подборками событий {@link CompilationRepository}
     */
    private final CompilationRepository compilationRepository;
    /**
     * Репозиторий для работы с событиями {@link EventRepository}
     */
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilationsByPinned(Boolean pinned, int from, int size) {
        Page<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(makePageable(from, size));
        } else {
            compilations = compilationRepository.findByPinned(pinned, makePageable(from, size));
        }
        return toCompilationsDto(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND + compId));
        return toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilation = compilationRepository.save(toCompilation(compilationDto, events));
        return toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventOfCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        compilation.getEvents().remove(event);
    }

    @Override
    @Transactional
    public void addEventOfCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        compilation.getEvents().add(event);
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND + compId));
        compilation.setPinned(false);
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND + compId));
        compilation.setPinned(true);
    }

    /**
     * Задает настройки пагинации
     *
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество элементов в наборе
     *             Default value : 10
     * @return готовые настройки пагинации {@link Pageable}
     */
    private Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, DEFAULT_SORT);
    }
}
