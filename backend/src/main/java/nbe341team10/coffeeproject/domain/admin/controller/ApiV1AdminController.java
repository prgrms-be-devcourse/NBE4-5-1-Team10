package nbe341team10.coffeeproject.domain.admin.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ApiV1AdminController {

    private final ProductService productService;
    //    private final OrderService orderService;
    @PostMapping("/product")
    public RsData<ProductGetItemDto> addProduct(@RequestBody ProductGetItemDto ProductGetItemDto) {
        Product addedProduct = productService.register(
                ProductGetItemDto.getName(),
                ProductGetItemDto.getDescription(),
                ProductGetItemDto.getPrice(),
                ProductGetItemDto.getImageUrl(),
                ProductGetItemDto.getStockQuantity()
        );

        ProductGetItemDto addedProductGetItemDto = new ProductGetItemDto(addedProduct);

        return new RsData<>("200", "상품 등록 성공", addedProductGetItemDto);
    }

//    @PutMapping("product/{id}")


    @DeleteMapping("/product/{id}")
    public ResponseEntity<RsData<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new RsData<>("200", "상품 삭제 성공"));
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

