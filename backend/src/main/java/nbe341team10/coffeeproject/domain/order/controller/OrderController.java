package nbe341team10.coffeeproject.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Orders 등록
    @PostMapping("/order")
    public RsData<Void> createOrder(@Valid @RequestBody OrderCreateRequest orderDto) {
        orderService.createOrder(orderDto);
        return new RsData<>(
                "200",
                "주문이 완료되었습니다."
        );
    }
}
