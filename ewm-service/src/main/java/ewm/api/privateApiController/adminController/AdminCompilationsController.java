package ewm.api.privateApiController.adminController;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.service.interfaces.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Закрытый контроллер подборок событий ({@link ewm.model.Compilation})
 * для администратора
 *
 * @author safiulinrm
 * @see ewm.api.publicApi.publicController.CompilationController
 */
@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    /**
     * Сервис для работы с подборками событий {@link CompilationService}
     */
    private final CompilationService compilationService;

    /**
     * Добавление новой подборки
     *
     * @param compilationDto данные новой подборки
     * @return Возвращает добавленную подборку {@link CompilationDto}
     */
    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Validated NewCompilationDto compilationDto) {
        var compilation = compilationService.addCompilation(compilationDto);
        log.info("Подборка добавлена");
        return compilation;
    }

    /**
     * Удаление подборки
     *
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("Подборка удалена");
    }

    /**
     * Удаление события из подборки
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventOfCompilation(@PathVariable Long compId,
                                         @PathVariable Long eventId) {
        compilationService.deleteEventOfCompilation(compId, eventId);
        log.info("Событие удалено из подборки");
    }

    /**
     * Добавление события в подборку
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventOfCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        compilationService.addEventOfCompilation(compId, eventId);
        log.info("Событие добавлено");
    }

    /**
     * Открепить подборку на главной странице
     *
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
        log.info("Подборка откреплена");
    }

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId id подборки
     */
    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
        log.info("Подборка закреплена");
    }
}
