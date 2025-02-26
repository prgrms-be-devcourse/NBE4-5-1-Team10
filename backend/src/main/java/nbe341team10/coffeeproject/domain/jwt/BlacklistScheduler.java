package nbe341team10.coffeeproject.domain.jwt;

import nbe341team10.coffeeproject.domain.user.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class BlacklistScheduler {

    private final BlacklistRepository blacklistRepository;

    @Autowired
    public BlacklistScheduler(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Scheduled(fixedRate = 1 * 60 * 60 * 1000L) // 1시간마다 실행
    @Transactional
    public void removeExpiredBlacklistEntries() {
        LocalDateTime now = LocalDateTime.now();
        blacklistRepository.deleteAllByExpiryDateBefore(now); // 만료된 항목 삭제
    }
}
