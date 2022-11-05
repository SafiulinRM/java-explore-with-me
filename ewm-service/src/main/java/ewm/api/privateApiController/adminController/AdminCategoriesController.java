package ewm.api.privateApiController.adminController;

import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;
import ewm.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Закрытый контроллер категорий ({@link ewm.model.Category})
 * для администратора
 *
 * @author safiulinrm
 * @see ewm.api.publicApi.publicController.CategoryController
 */
@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    /**
     * Сервис для работы с категориями событий {@link CategoryService}
     */
    private final CategoryService categoryService;

    /**
     * Обновление новой категории.
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param categoryDto Данные категории для изменения
     * @return возвращает обновленную категорию {@link CategoryDto}
     */
    @PatchMapping
    public CategoryDto patchCategory(@RequestBody @Validated CategoryDto categoryDto) {
        CategoryDto patchCategoryDto = categoryService.patchCategory(categoryDto);
        log.info("Данные категории изменены");
        return patchCategoryDto;
    }

    /**
     * Добавление новой категории.
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param newCategoryDto экземпляр класса данных для создания категорий {@link  NewCategoryDto}
     * @return возвращает созданную категорию {@link CategoryDto}
     */
    @PostMapping
    public CategoryDto addCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryService.addCategory(newCategoryDto);
        log.info("Категория добавлена");
        return categoryDto;
    }

    /**
     * удаление категории.
     *
     * @param catId id категории {@link ewm.model.Category}
     */
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        log.info("Категория удалена");
    }
}
