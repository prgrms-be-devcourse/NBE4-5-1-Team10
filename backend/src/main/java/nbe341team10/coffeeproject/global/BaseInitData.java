package nbe341team10.coffeeproject.global;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.product.service.ProductService;
import nbe341team10.coffeeproject.domain.user.dto.UserJoinRequest;
import nbe341team10.coffeeproject.domain.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final ProductService productService;
    private final LoginService loginService;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            self.userInit();
            self.productInit();
        };
    }

    @Transactional
    public void userInit() {
        if(loginService.count() > 0) {
            return;
        }
        UserJoinRequest user = new UserJoinRequest("tester1",
                "tester1@example.com",
                "password123",
                "Test Address");

        loginService.join(user);
    }

    @Transactional
    public void productInit() {

        if(productService.count() > 0) {
            return;
        }

        productService.register("에티오피아 예가체프", "화사한 꽃 향과 감귤류의 산미가 특징인 싱글 오리진 원두", 15000, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTopZAEGDsB83n1do9tMCTST3sLk5zDkYjWqg&s", 50);
        productService.register("콜롬비아 수프리모", "고소한 너트 향과 부드러운 단맛이 어우러진 밸런스 좋은 원두", 13000, "https://www.econgreen.co.kr/data/item/1519924818/thumb-cb_450_280x280.jpg", 30);
        productService.register("케냐 AA", "깊은 과일 향과 와인 같은 풍미를 가진 강렬한 개성의 원두", 17000, "https://groasting.com/web/product/big/202011/f03b4f32fb0d5714f9ef87cdd8d53b68.jpg", 40);
        productService.register("브라질 세라도", "초콜릿과 견과류의 고소함이 돋보이는 대중적인 원두", 12000, "https://m.coffeegdero.com/web/product/big/202309/007268479d5435a23cdfef9f67e25ec0.jpg", 60);

    }

}
