package nbe341team10.coffeeproject.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.delivery.service.DeliveryService;
import nbe341team10.coffeeproject.domain.order.controller.OrderController;
import nbe341team10.coffeeproject.domain.order.dto.OrderDetailListResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderDetailResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderListResponse;
import nbe341team10.coffeeproject.domain.order.dto.OrderResponse;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.aspectj.weaver.ast.Or;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ApiV1AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

    public record ProductAddRequest(@NonNull String name, String description, @NonNull int price, String imageUrl, @NonNull int stockQuantity) {}

    @Operation(summary = "상품 등록하기(관리자)")
    @PostMapping("/product")
    public RsData<ProductGetItemDto> addProduct(@RequestBody ProductAddRequest request) {
        Product addedProduct = productService.register(
                request.name(),
                request.description(),
                request.price(),
                request.imageUrl(),
                request.stockQuantity()
        );

        ProductGetItemDto addedProductGetItemDto = new ProductGetItemDto(addedProduct);

        return new RsData<>("200", "상품 등록 성공", addedProductGetItemDto);
    }

    public record ProductModifyProduct(@NonNull String name, String description, @NonNull int price, String imageUrl, @NonNull int stockQuantity) {}

    @Operation(summary = "상품 정보 수정하기(관리자)")
    @PutMapping("/product/{id}")
    public RsData<ProductGetItemDto> modifyProduct(@PathVariable Long id, @RequestBody ProductModifyProduct request) {
        Product modifiedProduct = productService.modify(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.imageUrl(),
                request.stockQuantity()
        );
        ProductGetItemDto modifiedProductGetItemDto = new ProductGetItemDto(modifiedProduct);
        return new RsData<>("200", "상품 수정 성공", modifiedProductGetItemDto);
    }


    @Operation(summary = "등록된 상품 삭제하기(관리자)")
    @DeleteMapping("/product/{id}")
    public RsData<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new RsData<>("200", "상품 삭제 성공");
    }

    @GetMapping("orders/ordered/count")
    public RsData<Integer> countOrdered() {
            int countOrdered = orderService.countOrdered();

        return new RsData<>(
                "200",
                "대기 중인 주문 개수 조회를 완료했습니다.",
                countOrdered
        );
    }

    public record OrdersListResponseBody(List<OrderDetailListResponse> items) {}

    @GetMapping("orders")
    public RsData<OrdersListResponseBody> getOrderList() {
            List<OrderDetailListResponse> orderList = orderService.getAllOrders();

        return new RsData<>(
                "200",
                "주문 내역 조회가 완료되었습니다.",
                new OrdersListResponseBody(orderList)
        );
    }


    public record OrdersLatestResponseBody(List<OrderResponse> items) {}

    @GetMapping("orders/latest")
    public RsData<OrdersLatestResponseBody> getLatestOrderList() {
        List<OrderResponse> orderList = orderService.getLatestTop3OrderList();

        return new RsData<>(
                "200",
                "최근 주문 내역 조회가 완료되었습니다.",
                new OrdersLatestResponseBody(orderList)
        );
    }

    @GetMapping("/orders/{id}")
    public RsData<OrderDetailResponse> getOrderDetail(@PathVariable Long id) {
        OrderDetailResponse order = orderService.getOrder(id);
        return new RsData<>(
                "200",
                "주문 상세 정보 조회가 완료되었습니다.",
                order
        );
    }

    @PostMapping("orders/{id}/delivery")
    public RsData<Void> orderDelivery(@PathVariable Long id) {
        Orders order = orderService.getOrderEntity(id);
        deliveryService.handleDelivery(order);
        return new RsData<>(
                "200",
                "배송 처리가 완료되었습니다."
        );
    }

}

