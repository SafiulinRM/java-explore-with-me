package exploreWithMe.mapper;

import exploreWithMe.dto.category.CategoryDto;
import exploreWithMe.dto.category.NewCategoryDto;
import exploreWithMe.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category){
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto){
        Category category = new Category();
        category.setName(newCategoryDto.getName());
     return category;
    }

    public static List<CategoryDto> toCategoriesDto(Page<Category> categories){
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(toList());
    }
}
