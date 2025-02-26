package nbe341team10.coffeeproject.domain.user.repository;

import nbe341team10.coffeeproject.domain.user.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {
    Boolean existsByRefresh(String refresh);

    // 삭제
    @Transactional
    void deleteByRefresh(String refresh);

    Refresh findByEmail(String refresh);


}
