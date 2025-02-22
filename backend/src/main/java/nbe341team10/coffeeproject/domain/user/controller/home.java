package nbe341team10.coffeeproject.domain.user.controller;

import jakarta.persistence.ManyToOne;
import nbe341team10.coffeeproject.domain.user.dto.CustomUserDetails;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import nbe341team10.coffeeproject.global.dto.RsData;
import nbe341team10.coffeeproject.global.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@ResponseBody
public class home {
    private final UserRepository userRepository;

    public home(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("homepage");
    }


    @GetMapping("/user")
    public ResponseEntity<RsData<String>> user(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Optional<Users> user=userRepository.findByEmail(userDetails.getEmail());
        if(user.isPresent()) {
            System.out.println(user.get().getEmail());
            RsData<String> response=new RsData<>("200","welcome",user.get().getEmail());
            return ResponseEntity.ok(response);
        }
        RsData<String> response=new RsData<>("400","user not found",null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("admin");
    }
}
