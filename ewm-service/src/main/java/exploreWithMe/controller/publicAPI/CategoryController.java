package exploreWithMe.controller.publicAPI;

import exploreWithMe.dto.category.CategoryDto;
import exploreWithMe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        List<CategoryDto> categoriesDto = categoryService.getCategories(from, size);
        log.info("Категории найдены");
        return categoriesDto;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        CategoryDto categoryDto = categoryService.getCategory(catId);
        log.info("Категория найдена");
        return categoryDto;
    }
}
