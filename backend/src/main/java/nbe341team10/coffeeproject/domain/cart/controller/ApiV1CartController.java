package nbe341team10.coffeeproject.domain.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.dto.CartDetailResponse;
import nbe341team10.coffeeproject.domain.cart.entity.Cart;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.Rq;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ApiV1CartController {
    private final CartService cartService;
    private final Rq rq;

    public record CartAddProductRequest(@NotNull long productId, @NotNull int quantity) {}

    @PostMapping()
    @Transactional()
    public RsData<CartDetailResponse> addProduct(@Valid @RequestBody CartAddProductRequest request) {
        Users actor = rq.getCurrentActor();
        Cart cart = cartService.addProduct(actor, request.productId, request.quantity);

        return new RsData<>(
                "200",
                "The quantity of product %d is a total of %d".formatted(request.productId, cart.getCartItemByProductId(request.productId).getQuantity()),
                new CartDetailResponse(cart)
        );
    }

    public record CartUpdateProductRequest(@NotNull long productId, @NotNull int quantity) {}

    @PostMapping()
    @Transactional()
    public RsData<CartDetailResponse> addProduct(@Valid @RequestBody CartAddProductRequest request) {
        Users actor = rq.getCurrentActor();
        Cart cart = cartService.addProduct(actor, request.productId, request.quantity);

        return new RsData<>(
                "200",
                "The quantity of product %d is a total of %d".formatted(request.productId, cart.getCartItemByProductId(request.productId).getQuantity()),
                new CartDetailResponse(cart)
        );
    }

    public record CartUpdateProductRequest(@NotNull long productId, @NotNull int quantity) {}

    @PatchMapping()
    @Transactional()
    public RsData<CartDetailResponse> updateProduct(@Valid @RequestBody CartUpdateProductRequest request) {
        Users actor = rq.getCurrentActor();
        Cart cart = cartService.updateProduct(actor, request.productId, request.quantity);

        return new RsData<>(
                "200",
                "The quantity of product %d is a total of %d".formatted(request.productId, request.quantity),
                new CartDetailResponse(cart)
        );
    }

    @GetMapping()
    @Transactional(readOnly = true)
    public RsData<CartDetailResponse> getCart() {
        Users actor = rq.getCurrentActor();

        Optional<Cart> optionalCart = cartService.getCart(actor.getId());

        return optionalCart.map(cart ->
                new RsData<>(
                        "200-1",
                        "Cart retrieved successfully.",
                        new CartDetailResponse(cart)
                )
        )
                .orElseGet(() ->
                        new RsData<>(
                                "200-2",
                                "Cart is empty.",
                                null
                        )
                );

    }
}
