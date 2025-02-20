package nbe341team10.coffeeproject.service.login;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.DTO.login.JoinDTO;
import nbe341team10.coffeeproject.domain.user.Role;
import nbe341team10.coffeeproject.domain.user.UserEntity;
import nbe341team10.coffeeproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 일반 회원가입
    public UserEntity join(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();
        String address = joinDTO.getAddress();

        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .address(address)
                .role(Role.ROLE_USER)
                .build();
        
        userRepository.save(user);
        return user;
    }

}
