package ewm.api.publicApi.publicController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.category.CategoryDto;
import ewm.model.Category;
import ewm.service.interfaces.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mvc;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");

    @Test
    void getCategories() throws Exception {
        when(categoryService.getCategories(anyInt(), anyInt())).thenReturn(List.of(testCategoryDto));
        mvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testCategory))));
    }

    @Test
    void getCategory() throws Exception {
        when(categoryService.getCategory(anyLong())).thenReturn(testCategoryDto);
        mvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCategoryDto)));
    }
}