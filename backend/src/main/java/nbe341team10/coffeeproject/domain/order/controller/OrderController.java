package nbe341team10.coffeeproject.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.dto.OrderDetailResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderListResponse;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.Rq;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    private final Rq rq;

    //Orders 등록
    @Operation(summary = "주문하기")
    @PostMapping("/order")
    public RsData<Void> createOrder(@Valid @RequestBody OrderCreateRequest orderDto) {
        Users actor = rq.getCurrentActor();
        orderService.createOrder(orderDto, actor);
        return new RsData<>(
                "200",
                "주문이 완료되었습니다."
        );
    }

    public record OrdersResponseBody(List<OrderListResponse> items) {}

    //Orders 목록 조회
    @Operation(summary = "내 주문 내역 조회하기")
    @GetMapping("/orders")
    public RsData<OrdersResponseBody> getOrders() {
        Users actor = rq.getCurrentActor();
        List<OrderListResponse> orders = orderService.getOrders(actor);

        return new RsData<>(
                "200",
                "주문 내역 조회가 완료되었습니다.",
                new OrdersResponseBody(orders)
        );
    }

    //Orders 상세 정보 조회
    @Operation(summary = "내 주문 상세 정보 조회하기")
    @GetMapping("/order/{id}")
    public RsData<OrderDetailResponse> getOrderDetail(@PathVariable Long id) {
        Users actor = rq.getCurrentActor();
        OrderDetailResponse order = orderService.getOrderDetail(id, actor);
        return new RsData<>(
                "200",
                "주문 상세 정보 조회가 완료되었습니다.",
                order
        );
    }
}
