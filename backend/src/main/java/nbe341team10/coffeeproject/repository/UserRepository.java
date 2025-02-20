package nbe341team10.coffeeproject.repository;

import nbe341team10.coffeeproject.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}