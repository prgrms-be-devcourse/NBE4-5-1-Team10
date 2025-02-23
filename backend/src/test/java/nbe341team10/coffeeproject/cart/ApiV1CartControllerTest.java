package nbe341team10.coffeeproject.cart;

import com.jayway.jsonpath.JsonPath;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    private Users loginUser;
    private String accessToken;

    @BeforeEach
    void login() throws Exception {
        String email = "tester1@example.com";
        String password = "password123";
        loginUser = userService.findByEmail(email).get();
        String loginResponse = mvc.perform(post("/api/v1/user/login")
                        .content("""
                                {
                                    "email": "%s",
                                    "password": "%s"
                                }
                                """.formatted(email, password)
                                .stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)))
                .andReturn().getResponse().getContentAsString();
        accessToken = JsonPath.read(loginResponse, "$.data.access");
    }

    private void checkCartItems(ResultActions resultActions, List<CartItem> cartItems) throws Exception {
        for(int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            resultActions
                .andExpect(jsonPath("$.data.cartItems[%d]".formatted(i)).exists())
                .andExpect(jsonPath("$.data.cartItems[%d].id".formatted(i)).value(cartItem.getId()))
                .andExpect(jsonPath("$.data.cartItems[%d].productId".formatted(i)).value(cartItem.getProduct().getId()))
                .andExpect(jsonPath("$.data.cartItems[%d].quantity".formatted(i)).value(cartItem.getQuantity()));
        }
    }

    ResultActions addRequest(long productId, int quantity) throws Exception {
        return mvc
                .perform(patch("/api/v1/cart")
                        .header("Authorization", "Bearer " + accessToken)
                        .content("""
                            {
                                "productId": %d,
                                "quantity": %d
                            }
                            """.formatted(productId, quantity)
                            .stripIndent())
                    .contentType(
                            new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                    )
                )
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 상품 추가 - 장바구니가 없다면 새로 생성")
    void add() throws Exception {
        long productId = 1L;
        int quantity = 1;
        ResultActions resultActions = updateRequest(productId, quantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, quantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 추가 - 장바구니가 있을 때 새로운 상품 추가")
    void add2() throws Exception {
        long anotherProductId = 1L;
        int anotherQuantity = 1;

        // Add another product to the cart
        updateRequest(anotherProductId, anotherQuantity);

        long productId = 2L;
        int quantity = 2;

        ResultActions resultActions = updateRequest(productId, quantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, quantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 추가 - 이미 존재하는 상품 수량 증가")
    void add3() throws Exception {
        long productId = 1L;
        int initialQuantity = 2;
        int updatedQuantity = 3;

        // Add the product to the cart for the first time
        updateRequest(productId, initialQuantity);

        // Add the same product again to update quantity
        ResultActions resultActions = updateRequest(productId, updatedQuantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, totalQuantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 추가 - 존재하지 않는 상품 추가")
    void add4() throws Exception {
        long nonExistentProductId = 999L;
        int quantity = 1;

        ResultActions resultActions = addRequest(nonExistentProductId, quantity);

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("addProduct"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.msg").value("Product with ID %d not found".formatted(nonExistentProductId)));
    }

    ResultActions updateRequest(long productId, int quantity) throws Exception {
        return mvc
                .perform(patch("/api/v1/cart")
                        .header("Authorization", "Bearer " + accessToken)
                        .content("""
                            {
                                "productId": %d,
                                "quantity": %d
                            }
                            """.formatted(productId, quantity)
                            .stripIndent())
                    .contentType(
                            new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                    )
                )
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 장바구니가 없다면 새로 생성")
    void update() throws Exception {
        long productId = 1L;
        int quantity = 1;
        ResultActions resultActions = updateRequest(productId, quantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("updateProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, quantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 장바구니가 있을 때 새로운 상품 추가")
    void update2() throws Exception {
        long anotherProductId = 1L;
        int anotherQuantity = 1;

        // Add another product to the cart
        updateRequest(anotherProductId, anotherQuantity);

        long productId = 2L;
        int quantity = 2;

        ResultActions resultActions = updateRequest(productId, quantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("updateProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, quantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 이미 존재하는 상품 수량 변경")
    void update3() throws Exception {
        long productId = 1L;
        int initialQuantity = 2;
        int updatedQuantity = 3;

        // Add the product to the cart for the first time
        updateRequest(productId, initialQuantity);

        // Add the same product again to update quantity
        ResultActions resultActions = updateRequest(productId, updatedQuantity);

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("updateProduct"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("The quantity of product %d is a total of %d".formatted(productId, updatedQuantity)))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 상품 담기 - 존재하지 않는 상품 추가")
    void update4() throws Exception {
    long nonExistentProductId = 999L;
    int quantity = 1;

    ResultActions resultActions = updateRequest(nonExistentProductId, quantity);

    resultActions
            .andExpect(status().isNotFound())
            .andExpect(handler().handlerType(ApiV1CartController.class))
            .andExpect(handler().methodName("updateProduct"))
            .andExpect(jsonPath("$.code").value("404"))
            .andExpect(jsonPath("$.msg").value("Product with ID %d not found".formatted(nonExistentProductId)));
    }

    ResultActions cartRequest() throws Exception {
        return mvc
                .perform(get("/api/v1/cart")
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andDo(print());
    }


    @Test
    @DisplayName("장바구니 조회 - 장바구니가 존재할 경우")
    void cart() throws Exception {
        // Given
        long productId1 = 1L;
        long productId2 = 2L;
        int quantity1 = 2;
        int quantity2 = 3;

        updateRequest(productId1, quantity1);
        updateRequest(productId2, quantity2);

        ResultActions resultActions = cartRequest();

        Cart cart = cartService.getCart(loginUser.getId()).get();

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("getCart"))
                .andExpect(jsonPath("$.code").value("200-1"))
                .andExpect(jsonPath("$.msg").value("Cart retrieved successfully."))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.data.cartItems").isArray());

        checkCartItems(resultActions, cart.getCartItems());
    }

    @Test
    @DisplayName("장바구니 조회 - 장바구니가 존재하지 않을 경우 빈 응답 반환")
    void cart2() throws Exception {
        // When
        ResultActions resultActions = cartRequest();

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1CartController.class))
                .andExpect(handler().methodName("getCart"))
                .andExpect(jsonPath("$.code").value("200-2"))
                .andExpect(jsonPath("$.msg").value("Cart is empty."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
