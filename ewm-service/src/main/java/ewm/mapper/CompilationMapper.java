package ewm.mapper;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.model.Compilation;
import ewm.model.Event;
import org.springframework.data.domain.Page;

import java.util.List;

import static ewm.mapper.EventMapper.toEventsShortDto;
import static java.util.stream.Collectors.toList;

/**
 * Класс, с помощью которого можно преобразовать подборку в dto и наоборот
 *
 * @author safiulinrm
 * @see Compilation
 * @see NewCompilationDto
 * @see CompilationDto
 */
public final class CompilationMapper {
    /**
     * Преобразует новую подборку в класс хранения
     *
     * @param dto    данные новой подборки {@link NewCompilationDto}
     * @param events Список событий в подборке {@link List {@link Event}}
     * @return {@link Compilation}
     */
    public static Compilation toCompilation(NewCompilationDto dto, List<Event> events) {
        return new Compilation((events),
                null,
                dto.getPinned(),
                dto.getTitle());
    }

    /**
     * Преобразует подборку в дто
     *
     * @param compilation данные подборки событий {@link Compilation}
     * @return {@link CompilationDto}
     */
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(toEventsShortDto(compilation.getEvents()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }

    /**
     * Преобразует подборки событий в список dto
     *
     * @param compilations подборки событий {@link List {@link Compilation}}
     * @return {@link List {@link CompilationDto}}
     */
    public static List<CompilationDto> toCompilationsDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(toList());
    }
}
