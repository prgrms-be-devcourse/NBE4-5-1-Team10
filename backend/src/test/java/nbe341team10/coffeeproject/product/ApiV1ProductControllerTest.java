package nbe341team10.coffeeproject.product;

import nbe341team10.coffeeproject.domain.product.controller.ApiV1ProductController;
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

    }

}
