package ewm.api.publicApi.publicController;

import ewm.dto.compilation.CompilationDto;
import ewm.service.interfaces.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Открытый контроллер подборок событий ({@link ewm.model.Compilation})
 * для пользователя
 *
 * @author safiulinrm
 * @see ewm.api.privateApiController.adminController.AdminCompilationsController
 */
@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationController {
    /**
     * Сервис для работы с подборками событий {@link CompilationService}
     */
    private final CompilationService compilationService;

    /**
     * Получение подборок событий
     *
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     *               Default value : 0
     * @param size   количество элементов в наборе
     *               Default value : 10
     * @return возвращает список подборок событий {@link List {@link CompilationDto}}
     */
    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        var compilations = compilationService.getCompilationsByPinned(pinned, from, size);
        log.info("Найдены подборки событий");
        return compilations;
    }

    /**
     * Получение подборок событий по его id
     *
     * @param compId id подборки
     * @return возвращает подборку событий {@link CompilationDto}
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        var compilation = compilationService.getCompilation(compId);
        log.info("Подборка событий найдена");
        return compilation;
    }
}
