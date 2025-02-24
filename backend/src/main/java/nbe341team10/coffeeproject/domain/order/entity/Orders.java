package nbe341team10.coffeeproject.domain.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.domain.user.entity.Users;
import nbe341team10.coffeeproject.global.entity.BaseTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
public class Orders extends BaseTime {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //모든 Product 가격의 합
    @Column(nullable = false)
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Users user;
}
