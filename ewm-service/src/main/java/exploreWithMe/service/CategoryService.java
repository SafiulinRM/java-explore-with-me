package exploreWithMe.service;

import exploreWithMe.dto.category.CategoryDto;
import exploreWithMe.dto.category.NewCategoryDto;
import exploreWithMe.exception.NotFoundException;
import exploreWithMe.model.Category;
import exploreWithMe.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static exploreWithMe.mapper.CategoryMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return toCategoriesDto(categoryRepository.findAll(pageable));
    }

    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found " + catId));
        return toCategoryDto(category);
    }

    @Transactional
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Category not found " + categoryDto.getId()));
        category.setName(categoryDto.getName());
        return toCategoryDto(category);
    }

    @Transactional
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Transactional
    public void deleteCategory(long catId) {
        checkExistCategory(catId);
        categoryRepository.deleteById(catId);
    }

    private void checkExistCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category not found " + catId);
        }
    }
}
