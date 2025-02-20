package nbe341team10.coffeeproject.domain.cart.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
}
