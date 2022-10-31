package ewm.controller.adminController;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.service.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Validated NewCompilationDto compilationDto) {
        var compilation = compilationService.addCompilation(compilationDto);
        log.info("Подборка добавлена");
        return compilation;
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("Подборка удалена");
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventOfCompilation(@PathVariable Long compId,
                                         @PathVariable Long eventId) {
        compilationService.deleteEventOfCompilation(compId, eventId);
        log.info("Событие удалено из подборки");
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventOfCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        compilationService.addEventOfCompilation(compId, eventId);
        log.info("Событие добавлено");
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
        log.info("Подборка откреплена");
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
        log.info("Подборка закреплена");
    }
}
