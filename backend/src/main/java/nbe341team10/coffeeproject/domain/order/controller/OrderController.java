package nbe341team10.coffeeproject.domain.order.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Orders 등록
    @PostMapping("/order")
    public void createOrder(@RequestBody OrderCreateRequest orderDto) {
        orderService.createOrder(orderDto);
    }
}
