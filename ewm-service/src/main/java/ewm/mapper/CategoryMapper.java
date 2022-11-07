package ewm.mapper;

import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;
import ewm.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Класс, с помощью которого можно преобразовать категорию в dto и наоборот
 *
 * @author safiulinrm
 * @see Category
 * @see CategoryDto
 * @see NewCategoryDto
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    /**
     * Преобразует категорию в dto
     *
     * @param category данные категории {@link Category}
     * @return {@link CategoryDto}
     */
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    /**
     * Преобразует новую категорию в класс хранения
     *
     * @param newCategoryDto данные новой категории {@link NewCategoryDto}
     * @return {@link Category}
     */
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    /**
     * Преобразует категории в dto
     *
     * @param categories категории {@link Category}
     * @return {@link List {@link CategoryDto}}
     */
    public static List<CategoryDto> toCategoriesDto(Page<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(toList());
    }
}
