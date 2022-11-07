package ewm.service;

import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;
import ewm.exception.NotFoundException;
import ewm.model.Category;
import ewm.repo.CategoryRepository;
import ewm.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ewm.mapper.CategoryMapper.*;

/**
 * Реализация интерфейса по работе с категориями {@link CategoryService}
 *
 * @author safiulinrm
 * @see Category
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    /**
     * Сортировка по умолчанию по полю id класса категории
     */
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    /**
     * Сообщение для исключения об отсутствии категории в базе данных
     */
    private static final String CATEGORY_NOT_FOUND = "Category not found ";
    /**
     * Репозиторий для работы с категориями событий {@link CategoryRepository}
     */
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return toCategoriesDto(categoryRepository.findAll(pageable));
    }

    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + catId));
        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + categoryDto.getId()));
        category.setName(categoryDto.getName());
        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        checkExistCategory(catId);
        categoryRepository.deleteById(catId);
    }

    /**
     * Проверка на наличие категории в базе данных
     *
     * @param catId id категории события
     */
    private void checkExistCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException(CATEGORY_NOT_FOUND + catId);
        }
    }
}
