package nbe341team10.coffeeproject.domain.user.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
