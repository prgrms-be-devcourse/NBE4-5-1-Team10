package nbe341team10.coffeeproject.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.order.dto.OrderCreateRequest;
import nbe341team10.coffeeproject.domain.order.dto.OrderDetailResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderListResponse;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemCreateRequest;
import nbe341team10.coffeeproject.domain.orderitem.dto.OrderItemDetailResponse;
import nbe341team10.coffeeproject.domain.orderitem.entity.OrderItem;
import nbe341team10.coffeeproject.domain.orderitem.repository.OrderItemRepository;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.repository.ProductRepository;
import nbe341team10.coffeeproject.domain.user.entity.Users;
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
    public void createOrder(OrderCreateRequest orderDto, Users actor) {

        // DB에서 주문된 Product 모두 가져오기
        List<Long> productIds = orderDto.getOrderItems().stream()
                .map(OrderItemCreateRequest::getProductId)
                .toList();
        List<Product> products = productRepository.findAllById(productIds);

        //Orders 생성
        Orders order = orderDto.toOrder(products, actor);

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

    //Orders 목록 조회
    public List<OrderListResponse> getOrders(Users actor) {
        List<Orders> allOrders = orderRepository.findByUser(actor);  // 모든 주문 가져오기
        return allOrders.stream()
                .map(order -> {
                    // 해당 주문에 대한 OrderItem 리스트 가져오기
                    List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

                    // 상품 개수 구하기
                    int orderItemCount = orderItems.size();

                    // 첫 번째 상품명 구하기 (첫 번째 상품이 있을 경우)
                    String firstProductName = orderItems.get(0).getProduct().getName();

                    // 해당 주문의 총 가격 구하기
                    int totalPrice = order.getTotalPrice();

                    // OrderListResponse 객체 생성
                    return OrderListResponse.builder()
                            .orderId(order.getId())
                            .orderDate(order.getCreatedAt())
                            .orderStatus(order.getStatus())
                            .firstProductName(firstProductName)
                            .productCategoryCount(orderItemCount)
                            .totalPrice(totalPrice)
                            .build();
                })
                .collect(Collectors.toList());  // 결과를 List로 변환
    }

    public OrderDetailResponse getOrderDetail(Long id, Users actor) {

        //Order 조회
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        "404",
                        id + "번 주문을 찾을 수 없습니다.")
                );

        System.out.println("오더: " + order.getAddress());

        if(!order.getUser().equals(actor)){
            throw new ServiceException(
                    "401",
                    "권한이 없는 주문입니다."
            );
        }


        //Order에 맞는 OrderItem 조회
        List<OrderItemDetailResponse> orderItems = orderItemRepository.findByOrder(order).stream()
                .map(orderItem -> OrderItemDetailResponse.builder()
                        .productName(orderItem.getProduct().getName())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        for(OrderItemDetailResponse orderItemDetailResponse : orderItems){
            System.out.println("오더 아이템: " + orderItemDetailResponse.getProductName());
        }

        //DTO 생성
        return OrderDetailResponse.builder()
                .orderItems(orderItems)
                .orderStatus(order.getStatus())
                .email(order.getEmail())
                .address(order.getAddress())
                .build();//Orders 상세 정보 조회

    }
}
