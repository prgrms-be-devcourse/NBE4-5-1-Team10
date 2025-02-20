package nbe341team10.coffeeproject.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.domain.Orders;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.orderitem.repository.OrderItemRepository;
import nbe341team10.coffeeproject.domain.product.entity.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public void createOrder(OrderCreateRequest orderDto) {

        //주문 Item DB에서 모두 가져오기
//        List<Long> productIds = orderDto.getOrderItems().stream()
//                .map(OrderItemCreateRequest::getItemId)
//                .toList();
//        List<Product> products = productRepository.findAllById(productIds);

        Orders order = orderDto.toOrder();

        orderRepository.save(order);


    }
}
