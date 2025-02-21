package nbe341team10.coffeeproject.domain.user.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.user.dto.CustomUserDetails;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;

    // user 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user=userRepository.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("");
        }
        // UserDetails에 담아서 리턴 -> AuthenticatiomnManager가 검증
        return new CustomUserDetails(user);

    }
}
