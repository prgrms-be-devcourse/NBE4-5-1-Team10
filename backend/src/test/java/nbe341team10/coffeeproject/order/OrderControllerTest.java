package nbe341team10.coffeeproject.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
}
