package nbe341team10.coffeeproject.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ApiV1AdminController {

    private final ProductService productService;
    //    private final OrderService orderService;

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




//    @GetMapping("products/orderlist")
//    public RsData<List<OrderDto>> getOrderList() {
//        try {
//            List<Order> orderList = orderService.getAllOrders();
//
//            List<OrderDto> orderDtoList = orderList.stream()
//                    .map(order -> new OrderDto(
//                            order.getId(),
//                            order.getEmail(),
//                            order.getAddress(),
//                            order.getPostalCode(),
//                            order.getStatus(),
//                            order.getTotalPrice(),
//                            order.getCreatedAt(),
//                            order.getUpdatedAt(),
//                            order.getMemberId()
//                    ))
//                    .collect(Collectors.toList());
//
//            return new RsData<>("200", "주문 목록 조회 성공", orderDtoList);
//
//        } catch (Exception e) {
//            return new RsData<>("500", "주문 목록 조회 실패: " + e.getMessage(), null);
//        }
//    }


}

