package ewm.controller.publicController;

import ewm.dto.compilation.CompilationDto;
import ewm.service.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        var compilations = compilationService.getCompilationsByPinned(pinned, from, size);
        log.info("Найдены подборки событий");
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        var compilation = compilationService.getCompilation(compId);
        log.info("Подборка событий найдена");
        return compilation;
    }
}
