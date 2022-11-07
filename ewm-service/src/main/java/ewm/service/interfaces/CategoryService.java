package ewm.service.interfaces;

import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;

import java.util.List;

/**
 * Интерфейс сервиса обработки запросов по категориям
 */
public interface CategoryService {
    /**
     * Получение категорий
     *
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество событий в наборе
     *             Default value : 10
     * @return возвращает список {@link List} {@link CategoryDto}}
     */
    List<CategoryDto> getCategories(int from, int size);

    /**
     * Получение категории
     *
     * @param catId id категории событий
     * @return {@link CategoryDto}
     */
    CategoryDto getCategory(long catId);

    /**
     * Обновление категории
     *
     * @param categoryDto {@link CategoryDto}
     * @return {@link CategoryDto}
     */
    CategoryDto patchCategory(CategoryDto categoryDto);

    /**
     * Создание категории {@link ewm.model.Category}
     *
     * @param newCategoryDto {@link NewCategoryDto}
     * @return {@link CategoryDto}
     */
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     *
     * @param catId id категории
     */
    void deleteCategory(long catId);
}
