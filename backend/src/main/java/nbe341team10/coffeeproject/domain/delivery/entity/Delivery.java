package nbe341team10.coffeeproject.domain.delivery.entity;

import jakarta.persistence.*;
import lombok.*;
import nbe341team10.coffeeproject.global.entity.BaseTime;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery extends BaseTime {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Orders order;

    private String deliveryAddress;

    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;

    public void setStatus(String s) {
    }
}

