package exploreWithMe.controller.adminAPI;

import exploreWithMe.dto.category.CategoryDto;
import exploreWithMe.dto.category.NewCategoryDto;
import exploreWithMe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PatchMapping
    public CategoryDto patchCategory(@RequestBody @Validated CategoryDto categoryDto) {
        CategoryDto patchCategoryDto = categoryService.patchCategory(categoryDto);
        log.info("Данные категории изменены");
        return patchCategoryDto;
    }

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        log.info("Категория добавлена");
        return categoryDto;
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        log.info("Категория удалена");
    }
}
