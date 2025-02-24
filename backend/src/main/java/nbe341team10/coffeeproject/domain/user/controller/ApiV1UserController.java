package nbe341team10.coffeeproject.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.dto.UserDto;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.Rq;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class ApiV1UserController {
    private final Rq rq;

    @GetMapping()
    public RsData<UserDto> getUser() {
        Users actor = rq.getCurrentActor();

        return new RsData<>(
                "200",
                "Your profile information has been successfully retrieved.",
                new UserDto(actor)
        );
    }
}
