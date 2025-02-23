package nbe341team10.coffeeproject.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.dto.OrderDetailResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderListResponse;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Orders 등록
    //TODO 로그인 및 회원 정보 등록 로직 필요
    @PostMapping("/order")
    public RsData<Void> createOrder(@Valid @RequestBody OrderCreateRequest orderDto) {
        orderService.createOrder(orderDto);
        return new RsData<>(
                "200",
                "주문이 완료되었습니다."
        );
    }

    public record OrdersResponseBody(List<OrderListResponse> items) {}

    //Orders 목록 조회
    //TODO 본인의 주문만 볼 수 있도록 하는 로직 추가 필요
    @GetMapping("/order")
    public RsData<OrdersResponseBody> getOrders() {
        List<OrderListResponse> orders = orderService.getOrders();

        return new RsData<>(
                "200",
                "주문 내역 조회가 완료되었습니다.",
                new OrdersResponseBody(orders)
        );
    }

    //Orders 상세 정보 조회
    //TODO 본인의 상세 주문만 볼 수 있도록 하는 로직 추가 필요
    @GetMapping("/order/{id}")
    public RsData<OrderDetailResponse> getOrderDetail(@PathVariable Long id) {
        OrderDetailResponse order = orderService.getOrderDetail(id);
        return new RsData<>(
                "200",
                "주문 상세 정보 조회가 완료되었습니다.",
                order
        );
    }
}
