package nbe341team10.coffeeproject.domain.user.repository;

import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}