
package nbe341team10.coffeeproject.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import nbe341team10.coffeeproject.domain.admin.controller.ApiV1AdminController; // Import your controller
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class ProductAdminTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("상품 추가 성공 확인")
    @WithMockUser(roles = "ADMIN")
    void addProductSuccess() throws Exception {
        // 1. Create ProductGetItemDto for request body
        ProductGetItemDto ProductGetItemDto = new ProductGetItemDto(null, "Test Product", "Test Description", 1000, "test.jpg", 50);

        // 2. Convert ProductGetItemDto to JSON
        String json = objectMapper.writeValueAsString(ProductGetItemDto);

        // 3. Perform POST request and verify response
        mvc.perform(post("/api/v1/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("상품 등록 성공"))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.description").value("Test Description"))
                .andExpect(jsonPath("$.data.price").value(1000))
                .andExpect(jsonPath("$.data.imageUrl").value("test.jpg"))
                .andExpect(jsonPath("$.data.stockQuantity").value(50))
                .andExpect(handler().handlerType(ApiV1AdminController.class));

        Product product = productService.getItems().stream()
                .filter(p -> p.getName().equals("Test Product"))
                .findFirst()
                .orElse(null);

        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(1000, product.getPrice());
        assertEquals("test.jpg", product.getImageUrl());
        assertEquals(50, product.getStockQuantity());
    }
}