package nbe341team10.coffeeproject.domain.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.global.entity.BaseTime;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseTime {
    @ManyToOne
    @JoinColumn(nullable = false)
    private Orders order;

    private String deliveryAddress;

    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

