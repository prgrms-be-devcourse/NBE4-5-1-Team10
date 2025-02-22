package nbe341team10.coffeeproject.domain.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    //@Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    //@Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member orderMember;
}
