package nbe341team10.coffeeproject.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemCreateRequest;
import nbe341team10.coffeeproject.domain.orderitem.entity.OrderItem;
import nbe341team10.coffeeproject.domain.orderitem.repository.OrderItemRepository;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;


    //Orders 등록
    public void createOrder(OrderCreateRequest orderDto) {

        // DB에서 주문된 Product 모두 가져오기
        List<Long> productIds = orderDto.getOrderItems().stream()
                .map(OrderItemCreateRequest::getProductId)
                .toList();
        List<Product> products = productRepository.findAllById(productIds);

        //Orders 생성
        Orders order = orderDto.toOrder(products);

        // OrderItem 엔티티 생성 및 Order와 Product와 연결
        List<OrderItem> orderItemList = orderDto.getOrderItems().stream()
                .map(item -> {
                    // orderItems의 productId에 해당하는 Product를 products 리스트에서 찾음
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new ServiceException(
                                    "404",
                                    item.getProductId() + "번 상품은 존재하지 않는 상품입니다.")
                            );

                    // OrderItem 생성 후 Order와 Product와 연결
                    return OrderItem.builder()
                            .product(product)
                            .order(order)
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);
    }
}
