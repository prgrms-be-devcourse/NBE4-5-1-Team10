package nbe341team10.coffeeproject.domain.user.repository;

import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);
}