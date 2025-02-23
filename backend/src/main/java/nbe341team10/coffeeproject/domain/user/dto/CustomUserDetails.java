package nbe341team10.coffeeproject.domain.user.dto;

import nbe341team10.coffeeproject.domain.user.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Users user;
    public CustomUserDetails(Users user) {
        this.user = user;
    }

    // role 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().name();
            }
        });
        return collection;
    }

    // 임시
    public String getRole(){

        return user.getRole().name();
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getAddress(){
        return user.getAddress();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }


}
