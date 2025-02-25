package nbe341team10.coffeeproject.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemDto;
import nbe341team10.coffeeproject.domain.product.dto.ProductGetItemsDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ApiV1ProductController {
    private final ProductService productService;

    public record ItemsResBody(List<ProductGetItemsDto> items) {}

    @Operation(summary = "상품 목록 조회하기")
    @GetMapping()
    @Transactional(readOnly = true)
    public RsData<ItemsResBody> getItems() {

        List<Product> products = productService.getItems();

        return new RsData<>(
                "200-1",
                "Products list retrieved successfully.",
                new ItemsResBody(products.stream()
                        .map(ProductGetItemsDto::new)
                        .toList())
        );
    }

    @Operation(summary = "특정 상품 정보 조회하기")
    @GetMapping("{productId}")
    public RsData<ProductGetItemDto> getItem(@PathVariable long productId) {
        Product product = productService.getItem(productId).orElseThrow(
                () -> new ServiceException("404-1", "Product not found.")
        );

        return new RsData<>(
                "200-1",
                "Product retrieved successfully.",
                new ProductGetItemDto(product)
        );
    }
}
