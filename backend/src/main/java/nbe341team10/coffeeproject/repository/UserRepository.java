package nbe341team10.coffeeproject.repository;

import nbe341team10.coffeeproject.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);
}