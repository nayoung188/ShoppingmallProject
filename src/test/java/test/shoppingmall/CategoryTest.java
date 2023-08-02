package test.shoppingmall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.controller.AdminController;
import com.shoppingmall.dto.CategoryCreateRequest;
import com.shoppingmall.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {
    @InjectMocks
    AdminController adminController;

    @Mock
    CategoryService categoryService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }
    @Test
    @DisplayName("카테고리 생성")
    void createTest() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("category1", 1, 1);

        // when, then
        mockMvc.perform(
                        post("/category/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        //verify(categoryService).addCategory(req);
    }
}
