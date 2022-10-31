package exploreWithMe.mapper;

import exploreWithMe.dto.compilation.CompilationDto;
import exploreWithMe.dto.compilation.NewCompilationDto;
import exploreWithMe.model.Compilation;
import exploreWithMe.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

import static exploreWithMe.mapper.EventMapper.toEventsShortDto;
import static java.util.stream.Collectors.toList;

public class CompilationMapper {
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

    public static List<CompilationDto>  toCompilationsDto(Page<Compilation> compilations){
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(toList());
    }
}
