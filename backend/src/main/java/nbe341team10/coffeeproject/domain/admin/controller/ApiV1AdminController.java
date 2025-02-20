package nbe341team10.coffeeproject.domain.admin.controller;

import lombok.RequiredArgsConstructor;
//import nbe341team10.coffeeproject.domain.admin.service.AdminService;
import nbe341team10.coffeeproject.domain.product.dto.ProductDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class ApiV1AdminController {
//    private final AdminService adminService;
    private final ProductService productService;
//    private final Orderservice orderService;
//    private final UserService userService;

//    @GetMapping("/products")
//    @Transactional(readOnly = true)
//    public List<ProductDto> getAllproducts() {
//
//        List<Product> products = productService.getItems();
//
//        if (products == null || products.isEmpty()) {
//            throw new ServiceException("404-2", "등록된 상품이 없습니다."); // 상품이 없을 때 예외 발생
//        }
//
//        return products.stream()
//                .map(product -> new ProductDto(
//                        product.getId(),
//                        product.getName(),
//                        product.getDescription(), // Add description
//                        product.getPrice(),
//                        product.getImageUrl(),
//                        product.getStockQuantity() // Add stockQuantity
//                ))
//                .collect(Collectors.toList());
//
//    }

    @PostMapping("/products")
    public RsData<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        Product addedProduct = productService.register(
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getImageUrl(),
                productDto.getStockQuantity()
        );

        ProductDto addedProductDto = new ProductDto(addedProduct);

        return new RsData<>("200", "상품 등록 성공", addedProductDto);
    }

//    @PutMapping("products/{id}")
//추후 메소드 생기면



//    @DeleteMapping("products/{id}")
//    public ResponseEntity<Void> deleteproduct(@PathVariable Long id) {
//        try {
//            productService.deleteproduct(id);
//            return ResponseEntity.noContent().build(); // 204 No Content 응답
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."); // 404 Not Found 예외 발생
//        }
//    }

    //delete 메소드 생기면
}