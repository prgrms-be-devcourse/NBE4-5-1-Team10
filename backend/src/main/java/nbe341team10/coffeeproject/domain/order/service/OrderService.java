package nbe341team10.coffeeproject.domain.order.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import nbe341team10.coffeeproject.domain.order.dto.*;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final CartService cartService;

    //Orders 등록
    public void createOrder(OrderCreateRequest orderDto, Users user) {

        // DB에서 주문된 Product 모두 가져오기
        List<Long> productIds = orderDto.getOrderItems().stream()
                .map(OrderItemCreateRequest::getProductId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));

        // Orders 생성
        Orders order = orderDto.toOrder(user);

        // OrderItem 엔티티 생성 및 Order와 Product와 연결
        List<OrderItem> orderItemList = orderDto.getOrderItems().stream()
                .map(item -> {
                    // orderItems의 productId에 해당하는 Product를 products 리스트에서 찾음
                    Product product = productMap.get(item.getProductId());
                    if (product == null) {
                        throw new ServiceException("404", "%d번 상품은 존재하지 않는 상품입니다.".formatted(item.getProductId()));
                    }
                    // 주문 수량이 재고보다 많을 경우 예외 처리
                    if (product.getStockQuantity() < item.getQuantity()) {
                        throw new ServiceException("400", "%d번 상품의 재고가 부족합니다.".formatted(item.getProductId()));
                    }

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

        // 각 상품의 재고 수량 차감
        orderDto.getOrderItems().forEach(item -> {
            Product product = productMap.get(item.getProductId());
            // 이미 재고 체크를 진행했으므로 안전하게 차감
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
        });
        productRepository.saveAll(productMap.values());

        // 주문 완료된 장바구니 비우기
        cartService.getCart(user.getId())
            .ifPresent(cart ->
                    productIds.forEach(cart::removeCartItem)
            );
    }

    //Orders 목록 조회
    public List<OrderListResponse> getOrdersOrderByCreatedAtDesc(Users actor) {
        List<Orders> allOrders = orderRepository.findByUserOrderByCreatedAtDesc(actor);  // 모든 주문 가져오기
        return allOrders.stream()
                .map(order -> {
                    // 해당 주문에 대한 OrderItem 리스트 가져오기
                    List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

                    // 상품 개수 구하기
                    int orderItemCount = orderItems.size();

                    // 첫 번째 상품명 구하기 (첫 번째 상품이 있을 경우)
                    String firstProductName = orderItems.get(0).getProduct().getName();
                    String firstProductImage = orderItems.get(0).getProduct().getImageUrl();

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
                            .firstProductImageUrl(firstProductImage)
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

        //DTO 생성
        return OrderDetailResponse.builder()
                .orderItems(orderItems)
                .orderStatus(order.getStatus())
                .username(actor.getUsername())
                .email(order.getEmail())
                .address(order.getAddress())
                .postalCode(order.getPostalCode())
                .totalPrice(order.getTotalPrice())
                .build();//Orders 상세 정보 조회

    }

    public Orders getOrderEntity(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        "404",
                        id + "번 주문을 찾을 수 없습니다.")
                );
    }

    public OrderDetailResponse getOrder(Long id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        "404",
                        id + "번 주문을 찾을 수 없습니다.")
                );

        List<OrderItemDetailResponse> orderItems = orderItemRepository.findByOrder(order).stream()
                .map(orderItem -> OrderItemDetailResponse.builder()
                        .productName(orderItem.getProduct().getName())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDetailResponse.builder()
                .orderItems(orderItems)
                .orderStatus(order.getStatus())
                .username(order.getUser().getUsername())
                .email(order.getEmail())
                .address(order.getAddress())
                .postalCode(order.getPostalCode())
                .totalPrice(order.getTotalPrice())
                .build();//Orders 상세 정보 조회
    }

    public List<OrderResponse> getLatestTop3OrderList() {
        List<Orders> allOrders = orderRepository.findTop3ByOrderByCreatedAtDesc();
        return allOrders.stream().map(OrderResponse::new).collect(Collectors.toList());
    }

    public List<OrderDetailListResponse> getAllOrders() {
        List<Orders> allOrders = orderRepository.findAllByOrderByCreatedAtDesc();
        return allOrders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

                    int orderItemCount = orderItems.size();

                    String firstProductName = orderItems.get(0).getProduct().getName();
                    String firstProductImage = orderItems.get(0).getProduct().getImageUrl();

                    int totalPrice = order.getTotalPrice();

                    return OrderDetailListResponse.builder()
                            .orderId(order.getId())
                            .orderDate(order.getCreatedAt())
                            .orderStatus(order.getStatus())
                            .firstProductName(firstProductName)
                            .productCategoryCount(orderItemCount)
                            .totalPrice(totalPrice)
                            .firstProductImageUrl(firstProductImage)
                            .username(order.getUser().getUsername())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public int countOrdered() {
        return orderRepository.countByStatus(OrderStatus.ORDERED);
    }
}
