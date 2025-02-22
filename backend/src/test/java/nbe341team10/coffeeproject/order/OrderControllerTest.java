package nbe341team10.coffeeproject.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemCreateRequest;
import nbe341team10.coffeeproject.domain.orderitem.repository.OrderItemRepository;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * TODO 회원 관련 로직 추가 하기(전부)
 */
@SpringBootTest
@AutoConfigureMockMvc
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

    @BeforeEach
    @Transactional  // 트랜잭션 범위 내에서 DB 초기화
    public void setUp() {
        // 기존 데이터를 삭제
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 등록")
    public void testCreateOrder_ValidRequest() throws Exception {

        // 요청을 보낼 데이터 준비
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

        // HTTP POST 요청과 예상 반환 값 비교
        mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isOk())  // HTTP 200 OK 응답 확인
                .andExpect(jsonPath("$.code").value("200"))  // 응답 body에서 status 확인
                .andExpect(jsonPath("$.msg").value("주문이 완료되었습니다."))  // 응답 body에서 message 확인
                .andExpect(jsonPath("$.data").doesNotExist());  // "data" 필드는 null이므로 존재하지 않음을 확인
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
        mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isBadRequest())  // HTTP 400 응답 확인
                .andExpect(jsonPath("$.code").value("400-1"))  // 응답 body에서 status 확인
                .andExpect(jsonPath("$.msg").value("address : NotBlank : 주소를 입력해주세요."));  // 응답 body에서 필수 값 누락 메시지 확인
    }

    @Test
    @DisplayName("주문 목록 조회")
    @Transactional
    public void testGetOrders() throws Exception {
        // 1. 첫 번째 주문 추가
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

        mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest1)))
                .andExpect(status().isOk());

        // 2. 두 번째 주문 추가
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

        mockMvc.perform(post("/order")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderCreateRequest2)))
                .andExpect(status().isOk());

        // 3. 주문 목록 조회 요청
        mockMvc.perform(get("/orders")
                        .contentType("application/json"))
                .andExpect(status().isOk())  // HTTP 200 OK 응답 확인
                .andExpect(jsonPath("$.code").value("200"))  // 응답 코드 확인
                .andExpect(jsonPath("$.msg").value("주문 내역 조회가 완료되었습니다."))  // 응답 메시지 확인
                .andExpect(jsonPath("$.data.items").isArray())  // data 필드가 배열인지 확인
                .andExpect(jsonPath("$.data.items.length()").value(2))  // 주문이 2개 존재하는지 확인
                .andExpect(jsonPath("$.data.items[0].firstProductName").value("에티오피아 예가체프"))  // 첫 번째 주문 상품명 확인
                .andExpect(jsonPath("$.data.items[1].firstProductName").value("콜롬비아 수프리모"))  // 두 번째 주문 상품명 확인
                .andExpect(jsonPath("$.data.items[0].totalPrice").value(2000))  // 첫 번째 주문 가격 확인
                .andExpect(jsonPath("$.data.items[1].totalPrice").value(2000));  // 두 번째 주문 가격 확인
    }
}
