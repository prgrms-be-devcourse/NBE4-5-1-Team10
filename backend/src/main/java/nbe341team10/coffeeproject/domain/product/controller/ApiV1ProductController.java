package nbe341team10.coffeeproject.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.dto.ProductDto;
import nbe341team10.coffeeproject.global.dto.ResponseData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ApiV1ProductController {

    public record ItemsResBody(List<ProductDto> items) {}

    @GetMapping()
    @Transactional(readOnly = true)
    public ResponseData<ItemsResBody> getItems() {

        List<ProductDto> products = new ArrayList<>();
        products.add(new ProductDto(1L, "name", "description", 0, "image", 0));
        products.add(new ProductDto(2L, "name", "description", 0, "image", 0));
        products.add(new ProductDto(3L, "name", "description", 0, "image", 0));
        products.add(new ProductDto(4L, "name", "description", 0, "image", 0));

        return new ResponseData<>(
                "200-1",
                "Products list retrieved successfully.",
                new ItemsResBody(products)
        );
    }
}
