package nbe341team10.coffeeproject.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nbe341team10.coffeeproject.global.entity.BaseTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SuperBuilder
public class Orders extends BaseTime {

    //@Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //모든 Product 가격의 합
    @Column(nullable = false, name = "total_price")
    private int totalPrice;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member orderMember;
}
