package nbe341team10.coffeeproject.domain.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.dto.CartAddProductResponse;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.entity.CartItem;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import nbe341team10.coffeeproject.domain.product.entity.Product;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.Rq;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ApiV1CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final Rq rq;

    public record CartAddProductRequest(@NotNull long productId, @NotNull int quantity) {}

    @PostMapping()
    public RsData<CartAddProductResponse> addProduct(@Valid @RequestBody CartAddProductRequest addProductRequest) {
        Users actor = rq.getCurrentActor();
        Product product = productService.getItem(addProductRequest.productId).orElseThrow(
                () -> new ServiceException(
                        "404-1",
                        "Product with ID %d not found".formatted(addProductRequest.productId)
                )
        );
        CartItem cartItem = cartService.addProduct(actor, product, addProductRequest.quantity);
        
        return new RsData<>(
                "201-1",
                "The quantity of product %d is a total of %d".formatted(product.getId(), cartItem.getQuantity()),
                new CartAddProductResponse(cartItem.getCart())
        );
    }
}
