package nbe341team10.coffeeproject.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.dto.ProductDto;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ApiV1ProductController {
    private final ProductService productService;

    public record ItemsResBody(List<ProductDto> items) {}

    @GetMapping()
    @Transactional(readOnly = true)
    public RsData<ItemsResBody> getItems() {

        List<Product> products = productService.getItems();

        return new RsData<>(
                "200-1",
                "Products list retrieved successfully.",
                new ItemsResBody(products.stream()
                        .map(ProductDto::new)
                        .toList())
        );
    }
}
