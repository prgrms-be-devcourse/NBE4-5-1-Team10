package nbe341team10.coffeeproject.domain.admin.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.delivery.dto.DeliveryDTO;
import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import nbe341team10.coffeeproject.domain.delivery.service.DeliveryService;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ApiV1AdminController {

    private final ProductService productService;
    private final DeliveryService deliveryService;

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

    @PutMapping("/product/{id}")
    public RsData<ProductGetItemDto> modifyProduct(@PathVariable Long id, @RequestBody ProductGetItemDto productGetItemDto) {
        Product modifiedProduct = productService.modify(
                id,
                productGetItemDto.getName(),
                productGetItemDto.getDescription(),
                productGetItemDto.getPrice(),
                productGetItemDto.getImageUrl(),
                productGetItemDto.getStockQuantity()
        );
        ProductGetItemDto modifiedProductGetItemDto = new ProductGetItemDto(modifiedProduct);
        return new RsData<>("200", "상품 수정 성공", modifiedProductGetItemDto);
    }


    @DeleteMapping("/product/{id}")
    public RsData<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new RsData<>("200", "상품 삭제 성공");
    }


//    @GetMapping("order/orderlist")
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

    // 모든 배송 조회
    @GetMapping("/deliveries")
    public RsData<List<DeliveryDTO>> getAllDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return new RsData<>("200-00", "Deliveries retrieved successfully.", deliveries);
    }

    // 특정 배송 조회
    @GetMapping("/delivery/{id}")
    public RsData<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        DeliveryDTO deliveryDTO = deliveryService.getDeliveryById(id);
        if (deliveryDTO != null) {
            return new RsData<>("200-00", "Delivery retrieved successfully.", deliveryDTO);
        } else {
            return new RsData<>("404-00", "Delivery not found.");
        }
    }


    // 배송 삭제
    @DeleteMapping("/delivery/{id}")
    public RsData<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return new RsData<>("204-00", "배송이 삭제되었습니다.");
    }
}

