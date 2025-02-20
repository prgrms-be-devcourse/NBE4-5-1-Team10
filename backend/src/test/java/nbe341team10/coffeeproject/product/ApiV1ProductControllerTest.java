package nbe341team10.coffeeproject.product;

import nbe341team10.coffeeproject.domain.product.controller.ApiV1ProductController;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import org.aspectj.weaver.ast.And;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1ProductControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProductService productService;

    private void checkProducts(ResultActions resultActions, List<Product> products) throws Exception {
        for(int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            resultActions
                .andExpect(jsonPath("$.data.items[%d]".formatted(i)).exists())
                .andExpect(jsonPath("$.data.items[%d].id".formatted(i)).value(product.getId()))
                .andExpect(jsonPath("$.data.items[%d].name".formatted(i)).value(product.getName()))
                .andExpect(jsonPath("$.data.items[%d].description".formatted(i)).value(product.getDescription()))
                .andExpect(jsonPath("$.data.items[%d].price".formatted(i)).value(product.getPrice()))
                .andExpect(jsonPath("$.data.items[%d].stockQuantity".formatted(i)).value(product.getStockQuantity()));
        }
    }

    @Test
    @DisplayName("상품 목록 조회")
    void items() throws Exception {
//        Given 사용자가 상품 목록을 조회하면
//        When 시스템이 상품 목록을 가져오면
//        Then 사용자가 상품 목록을 볼 수 있다.

        ResultActions resultActions = mvc
                .perform(get("/api/v1/products"))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1ProductController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(jsonPath("$.code").value("200-1"))
                .andExpect(jsonPath("$.msg").value("Products list retrieved successfully."))
                .andExpect(jsonPath("$.data.items.length()").value(4));

        List<Product> products = productService.getItems();
        checkProducts(resultActions, products);
    }

    private void checkProduct(ResultActions resultActions, Product product) throws Exception {
        resultActions
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(product.getId()))
            .andExpect(jsonPath("$.data.name").value(product.getName()))
            .andExpect(jsonPath("$.data.description").value(product.getDescription()))
            .andExpect(jsonPath("$.data.imageUrl").value(product.getImageUrl()))
            .andExpect(jsonPath("$.data.price").value(product.getPrice()))
            .andExpect(jsonPath("$.data.stockQuantity").value(product.getStockQuantity()));
    }

    @Test
    @DisplayName("상품 상세정보 조회")
    void item() throws Exception {
//        Given 사용자가 특정 상품을 선택하면
//        When 시스템이 해당 상품의 상세 정보를 조회하여 제공하면
//        Then 사용자는 상품명, 설명, 가격, 리뷰 등을 확인할 수 있다.
        long productId = 1;

        ResultActions resultActions = mvc
                .perform(get("/api/v1/products/%d".formatted(productId)))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1ProductController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(jsonPath("$.code").value("200-1"))
                .andExpect(jsonPath("$.msg").value("Product retrieved successfully."));

        Product product = productService.getItem(productId).get();
        checkProduct(resultActions, product);
    }

}
