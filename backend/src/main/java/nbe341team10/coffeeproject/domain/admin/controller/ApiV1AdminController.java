package nbe341team10.coffeeproject.domain.admin.controller;

import lombok.RequiredArgsConstructor;
//import nbe341team10.coffeeproject.domain.admin.service.AdminService;

import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
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

    private final ProductService productService;

    @PostMapping("/products")
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

//    @PutMapping("products/{id}")
//추후 메소드 생기면



//    @DeleteMapping("products/{id}")


    //delete 메소드 생기면
}