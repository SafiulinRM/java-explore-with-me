package ewm.api.privateApiController.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.category.CategoryDto;
import ewm.dto.category.NewCategoryDto;
import ewm.service.interfaces.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCategoriesController.class)
class AdminCategoriesControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mvc;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final NewCategoryDto testNewCategory = new NewCategoryDto("category");

    @Test
    void patchCategory() throws Exception {
        when(categoryService.patchCategory(any(CategoryDto.class))).thenReturn(testCategoryDto);
        mvc.perform(patch("/admin/categories")
                        .content(mapper.writeValueAsString(testCategoryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCategoryDto)));
    }

    @Test
    void addCategory() throws Exception {
        when(categoryService.addCategory(any(NewCategoryDto.class))).thenReturn(testCategoryDto);
        mvc.perform(post("/admin/categories")
                        .content(mapper.writeValueAsString(testNewCategory))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCategoryDto)));
    }

    @Test
    void deleteCategory() throws Exception {
        mvc.perform(delete("/admin/categories/1"));
        Mockito.verify(categoryService, Mockito.times(1))
                .deleteCategory(1L);
    }
}