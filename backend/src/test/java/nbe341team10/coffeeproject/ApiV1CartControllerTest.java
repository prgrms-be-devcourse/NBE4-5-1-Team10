package nbe341team10.coffeeproject;

import nbe341team10.coffeeproject.domain.cart.controller.ApiV1CartController;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import nbe341team10.coffeeproject.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1CartControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;

    private Users loginedUser;
    private String accessToken;

    @BeforeEach
    void login() {
        loginedUser = userService.findByUsername("user1").get();
        accessToken = loginService.getAccessToken(loginedUser);
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 장바구니가 없다면 새로 생성")
    void add() throws Exception {
        long productId = 1L;
        int quantity = 1;
        ResultActions resultActions = mvc
                .perform(post("/api/v1/cart")
                        .header("Authorization", "Bearer " + accessToken)
                        .content("""
                            {
                                "productId": "%d"
                                "quantity": "%d"
                            }
                            """.formatted(productId, quantity)
                            .stripIndent())
                    .contentType(
                            new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                    )
                )
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Cart cart = cartService.getLatestCart(loginedUser.getId()).get();
        CartItem cartItem = cartService.getCartItem(cart.getId(), productId).get();

        resultActions
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(jsonPath("$.code").value("201-1"))
                .andExpect(jsonPath("$.msg").value("The quantity of item %d is a total of %d".formatted(productId, cartItem.getQuantity())));
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 장바구니가 있을 때 새로운 상품 추가")
    void add2() throws Exception {
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 이미 존재하는 상품 수량 추가")
    void add3() throws Exception {

    }

    @Test
    @DisplayName("장바구니 상품 담기 - 존재하지 않는 상품 추가")
    void add4() throws Exception {}


}
