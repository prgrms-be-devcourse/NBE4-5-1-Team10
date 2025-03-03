package nbe341team10.coffeeproject.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemCreateRequest;
import nbe341team10.coffeeproject.domain.orderitem.repository.OrderItemRepository;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
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

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    private Users loginUser;
    private String accessToken;

    @BeforeEach
    @Transactional  // 트랜잭션 범위 내에서 DB 초기화
    public void setUp() throws Exception {
        // 기존 데이터를 삭제
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();


        // 회원가입 요청
        String userJoinRequestBody = "{ \"username\": \"testUser\", \"email\": \"testUser@example.com\", \"password\": \"1234\", \"address\": \"Test Address\" }";
        mockMvc.perform(post("/api/v1/user/join")  // 회원가입 API 경로
                        .contentType("application/json")
                        .content(userJoinRequestBody))
                .andExpect(status().isOk());  // 회원가입 성공 여부 확인

        loginUser = userService.findByEmail("testUser@example.com").get();
        String loginResponse = mockMvc.perform(post("/api/v1/user/login")
                        .content("""
                                {
                                    "email": "%s",
                                    "password": "%s"
                                }
                                """.formatted("testUser@example.com", "1234")
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

    @Test
    @DisplayName("장바구니에서 주문하기")
    public void testCreateOrder_withCart() throws Exception {
        // 장바구니에 상품 담기
        long productId = 1L;
        int quantity = 2;
        Cart cart = cartService.addProduct(loginUser, productId, quantity);
        // 주문 요청
        OrderItemCreateRequest orderItem = OrderItemCreateRequest.builder()
                .productId(1L)
                .price(1000)
                .quantity(2)
                .build();

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .address("서울시 강남구")
                .postalCode("12345")
                .orderItems(Collections.singletonList(orderItem))
                .build();

        mockMvc.perform(post("/api/v1/order")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.msg").value("주문이 완료되었습니다."));

        // 장바구니 검증
        Cart afterCart = cartService.getCart(loginUser.getId()).get();
        assertEquals(0, afterCart.getCartItems().size());
    }

    @Test
    @DisplayName("주문 등록")
    public void testCreateOrder_ValidRequest() throws Exception {
        // 주문 요청할 데이터 준비
        OrderItemCreateRequest orderItem = OrderItemCreateRequest.builder()
                .productId(1L)
                .price(1000)
                .quantity(2)
                .build();

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .address("서울시 강남구")
                .postalCode("12345")
                .orderItems(Collections.singletonList(orderItem))
                .build();

        // 인증된 사용자로 주문 등록
        mockMvc.perform(post("/api/v1/order")
                        .header("Authorization", "Bearer " + accessToken)  // 액세스 토큰 추가
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isOk())  // HTTP 200 OK 응답 확인
                .andExpect(jsonPath("$.code").value("200"))  // 응답 코드 확인
                .andExpect(jsonPath("$.msg").value("주문이 완료되었습니다."));  // 응답 메시지 확인
    }


    @Test
    @DisplayName("주문 등록 시 주소 값 누락")
    public void testCreateOrder_MissingRequiredField() throws Exception {

        // 요청을 보낼 데이터 준비 (주소가 빠짐)
        OrderItemCreateRequest orderItem = OrderItemCreateRequest.builder()
                .productId(1L)
                .price(1000)
                .quantity(2)
                .build();

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .postalCode("12345")  // 주소가 없음
                .orderItems(Collections.singletonList(orderItem))
                .build();

        // HTTP POST 요청 보내기 (필수 값 누락 시 400 Bad Request)
        mockMvc.perform(post("/api/v1/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest))
                        .header("Authorization", "Bearer " + accessToken))  // Authorization 헤더에 액세스 토큰 추가
                .andExpect(status().isBadRequest())  // HTTP 400 응답 확인
                .andExpect(jsonPath("$.code").value("400-1"))  // 응답 body에서 status 확인
                .andExpect(jsonPath("$.msg").value("address : NotBlank : 주소를 입력해주세요."));  // 응답 body에서 필수 값 누락 메시지 확인
    }

    @Test
    @DisplayName("주문 목록 조회")
    @Transactional
    public void testGetOrders() throws Exception {
        // 2. 첫 번째 주문 추가
        OrderItemCreateRequest orderItem1 = OrderItemCreateRequest.builder()
                .productId(1L)
                .price(1000)
                .quantity(2)
                .build();

        OrderCreateRequest orderCreateRequest1 = OrderCreateRequest.builder()
                .address("서울시 강남구")
                .postalCode("12345")
                .orderItems(Collections.singletonList(orderItem1))
                .build();

        // 3. 주문 요청 (로그인한 사용자의 토큰을 Authorization 헤더에 추가)
        mockMvc.perform(post("/api/v1/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest1))
                        .header("Authorization", "Bearer " + accessToken))  // accessToken 사용
                .andExpect(status().isOk());

        // 4. 두 번째 주문 추가
        OrderItemCreateRequest orderItem2 = OrderItemCreateRequest.builder()
                .productId(2L)
                .price(2000)
                .quantity(1)
                .build();

        OrderCreateRequest orderCreateRequest2 = OrderCreateRequest.builder()
                .address("서울시 서초구")
                .postalCode("67890")
                .orderItems(Collections.singletonList(orderItem2))
                .build();

        mockMvc.perform(post("/api/v1/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest2))
                        .header("Authorization", "Bearer " + accessToken))  // accessToken 사용
                .andExpect(status().isOk());

        // 5. 주문 목록 조회 요청 (로그인한 사용자의 토큰을 Authorization 헤더에 추가)
        mockMvc.perform(get("/api/v1/orders")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + accessToken))  // accessToken 사용
                .andExpect(status().isOk())  // HTTP 200 OK 응답 확인
                .andExpect(jsonPath("$.code").value("200"))  // 응답 코드 확인
                .andExpect(jsonPath("$.msg").value("주문 내역 조회가 완료되었습니다."))  // 응답 메시지 확인
                .andExpect(jsonPath("$.data.items").isArray())  // data 필드가 배열인지 확인
                .andExpect(jsonPath("$.data.items.length()").value(2))  // 주문이 2개 존재하는지 확인
                .andExpect(jsonPath("$.data.items[-1].firstProductName").value("에티오피아 예가체프"))  // 첫 번째 주문 상품명 확인
                .andExpect(jsonPath("$.data.items[-2].firstProductName").value("콜롬비아 수프리모"))  // 두 번째 주문 상품명 확인
                .andExpect(jsonPath("$.data.items[-1].totalPrice").value(2000))  // 첫 번째 주문 가격 확인
                .andExpect(jsonPath("$.data.items[-2].totalPrice").value(2000));  // 두 번째 주문 가격 확인
    }

    @Test
    @DisplayName("주문 상세 정보 조회")
    @Transactional
    public void testGetOrderDetail() throws Exception {
        // 1. 주문 생성
        OrderItemCreateRequest orderItem1 = OrderItemCreateRequest.builder()
                .productId(1L)
                .price(30000)
                .quantity(2)
                .build();

        OrderItemCreateRequest orderItem2 = OrderItemCreateRequest.builder()
                .productId(2L)
                .price(13000)
                .quantity(1)
                .build();

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .address("서울시 강남구")
                .postalCode("12345")
                .orderItems(List.of(orderItem1, orderItem2))
                .build();

        // 2. 주문 생성 요청 (Authorization 헤더에 액세스 토큰 추가)
        mockMvc.perform(post("/api/v1/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest))
                        .header("Authorization", "Bearer " + accessToken))  // 액세스 토큰 추가
                .andExpect(status().isOk());

        // 3. 주문 목록 조회 요청 후 JSON 응답 파싱
        String orderListResponse = mockMvc.perform(get("/api/v1/orders")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + accessToken))  // Authorization 헤더 추가
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode ordersNode = objectMapper.readTree(orderListResponse).get("data").get("items");

        // ✅ 생성한 주문의 ID 찾기 (여기선 첫 번째 주문 사용)
        Long orderId = ordersNode.get(0).get("orderId").asLong();

        // 4. 주문 상세 조회 요청 (Authorization 헤더에 액세스 토큰 추가)
        mockMvc.perform(get("/api/v1/order/{orderId}", orderId)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + accessToken))  // Authorization 헤더 추가
                .andExpect(status().isOk())  // HTTP 200 응답 확인
                .andExpect(jsonPath("$.code").value("200"))  // 응답 코드 확인
                .andExpect(jsonPath("$.msg").value("주문 상세 정보 조회가 완료되었습니다."))  // 응답 메시지 확인
                .andExpect(jsonPath("$.data.address").value("서울시 강남구"))  // 주소 확인
                .andExpect(jsonPath("$.data.orderStatus").value("ORDERED"))  // 주문 상태 확인
                .andExpect(jsonPath("$.data.orderItems").isArray())  // 주문 상품 리스트 확인
                .andExpect(jsonPath("$.data.orderItems.length()").value(2))  // 주문 상품 개수 확인
                .andExpect(jsonPath("$.data.orderItems[0].productName").value("에티오피아 예가체프"))  // 첫 번째 상품명 확인
                .andExpect(jsonPath("$.data.orderItems[0].quantity").value(2))  // 첫 번째 상품 수량 확인
                .andExpect(jsonPath("$.data.orderItems[0].price").value(30000))  // 첫 번째 상품 가격 확인
                .andExpect(jsonPath("$.data.orderItems[1].productName").value("콜롬비아 수프리모"))  // 두 번째 상품명 확인
                .andExpect(jsonPath("$.data.orderItems[1].quantity").value(1))  // 두 번째 상품 수량 확인
                .andExpect(jsonPath("$.data.orderItems[1].price").value(13000));  // 두 번째 상품 가격 확인
    }
}
