package nbe341team10.coffeeproject.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.dto.CartDto;
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

    @Operation(summary = "장바구니에 상품 담기")
    @PostMapping()
    @Transactional()
    public RsData<CartDto> addProduct(@Valid @RequestBody CartAddProductRequest request) {
        Users actor = rq.getCurrentActor();
        Cart cart = cartService.addProduct(actor, request.productId, request.quantity);

        return new RsData<>(
                "200",
                "The quantity of product %d is a total of %d".formatted(request.productId, cart.getCartItemByProductId(request.productId).getQuantity()),
                new CartDto(cart)
        );
    }

    public record CartUpdateProductRequest(@NotNull long productId, @NotNull int quantity) {}

    @Operation(summary = "장바구니에 담은 상품 수정하기")
    @PatchMapping()
    @Transactional()
    public RsData<CartDto> updateProduct(@Valid @RequestBody CartUpdateProductRequest request) {
        Users actor = rq.getCurrentActor();
        Cart cart = cartService.updateProduct(actor, request.productId, request.quantity);

        return new RsData<>(
                "200",
                "The quantity of product %d is a total of %d".formatted(request.productId, request.quantity),
                new CartDto(cart)
        );
    }

    @Operation(summary = "장바구니 조회하기")
    @GetMapping()
    @Transactional(readOnly = true)
    public RsData<CartDto> getCart() {
        Users actor = rq.getCurrentActor();

        Optional<Cart> optionalCart = cartService.getCart(actor.getId());

        return optionalCart.map(cart ->
                new RsData<>(
                        "200-1",
                        "Cart retrieved successfully.",
                        new CartDto(cart)
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
