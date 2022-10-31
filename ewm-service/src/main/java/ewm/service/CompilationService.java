package ewm.service;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.exception.NotFoundException;
import ewm.model.Compilation;
import ewm.model.Event;
import ewm.repo.CompilationRepository;
import ewm.repo.EventRepository;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationService {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public List<CompilationDto> getCompilationsByPinned(Boolean pinned, int from, int size) {
        Page<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(makePageable(from, size));
        } else {
            compilations = compilationRepository.findByPinned(pinned, makePageable(from, size));
        }
        return toCompilationsDto(compilations);
    }

    public List<CompilationDto> getCompilations(int from, int size) {
        Page<Compilation> compilations = compilationRepository.findAll(makePageable(from, size));
        return toCompilationsDto(compilations);
    }

    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found" + compId));
        return toCompilationDto(compilation);
    }

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilation = compilationRepository.save(toCompilation(compilationDto, events));
        return toCompilationDto(compilation);
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public void deleteEventOfCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found" + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found" + eventId));
        compilation.getEvents().remove(event);
    }

    @Transactional
    public void addEventOfCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found" + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found" + eventId));
        compilation.getEvents().add(event);
    }

    @Transactional
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found" + compId));
        compilation.setPinned(false);
    }

    @Transactional
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found" + compId));
        compilation.setPinned(true);
    }

    private Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, DEFAULT_SORT);
    }
}
