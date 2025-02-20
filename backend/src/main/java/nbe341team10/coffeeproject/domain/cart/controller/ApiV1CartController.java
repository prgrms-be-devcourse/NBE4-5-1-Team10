package nbe341team10.coffeeproject.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.service.CartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ApiV1CartController {
    private final CartService cartService;
}
