package ewm.api.publicApi.publicController;

import ewm.dto.category.CategoryDto;
import ewm.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Открытый контроллер категорий ({@link ewm.model.Category})
 * для пользователей
 *
 * @author safiulinrm
 * @see ewm.api.privateApiController.adminController.AdminCategoriesController
 */
@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryController {
    /**
     * Сервис для работы с категориями событий {@link CategoryService}
     */
    private final CategoryService categoryService;

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество категорий в наборе
     *             Default value : 10
     * @return возвращает список категорий событий {@link List {@link CategoryDto}}
     */
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        List<CategoryDto> categoriesDto = categoryService.getCategories(from, size);
        log.info("Категории найдены");
        return categoriesDto;

    }

    /**
     * Получение информации о категории по ее идентификатору
     *
     * @param catId id категории
     * @return возвращает категорию событий {@link CategoryDto}
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        CategoryDto categoryDto = categoryService.getCategory(catId);
        log.info("Категория найдена");
        return categoryDto;
    }
}
