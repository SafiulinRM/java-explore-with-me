package ewm.mapper;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.model.Compilation;
import ewm.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;

import static ewm.mapper.EventMapper.toEventsShortDto;
import static java.util.stream.Collectors.toList;

public final class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto dto, List<Event> events) {
        return new Compilation((events),
                null,
                dto.getPinned(),
                dto.getTitle());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(toEventsShortDto(compilation.getEvents()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public static List<CompilationDto> toCompilationsDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(toList());
    }
}
