package ewm.service;

import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;
import ewm.exception.NotFoundException;
import ewm.model.Category;
import ewm.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");
    private final NewCategoryDto testNewCategory = new NewCategoryDto("category");

    @Test
    void getCategories() {
        when(categoryRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl(List.of(testCategory)));
        List<CategoryDto> categoriesDto = categoryService.getCategories(0, 10);
        assertEquals(categoriesDto.get(0), testCategoryDto, "Категории не получены");
    }

    @Test
    void getCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        val categoryDto = categoryService.getCategory(1);
        assertEquals(categoryDto, testCategoryDto, "Категорию не получили");
    }

    @Test
    void patchCategory() {
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(testCategory));
        val categoryDto = categoryService.patchCategory(testCategoryDto);
        assertEquals(categoryDto, testCategoryDto, "Категория не изменилась");
    }

    @Test
    void addCategory() {
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(testCategory);
        val categoryDto = categoryService.addCategory(testNewCategory);
        assertEquals(categoryDto, testCategoryDto, "Категория не создалась");
    }

    @Test
    void deleteCategory() {
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1))
                .deleteById(1L);
        verify(categoryRepository, times(1))
                .existsById(1L);
        when(categoryRepository.existsById(anyLong())).thenReturn(false);
        val exception = assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategory(1L));
        assertEquals("Category not found 1", exception.getMessage());
    }
}