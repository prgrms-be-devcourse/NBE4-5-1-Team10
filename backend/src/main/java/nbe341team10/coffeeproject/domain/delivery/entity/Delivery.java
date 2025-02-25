package nbe341team10.coffeeproject.domain.delivery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;
}

