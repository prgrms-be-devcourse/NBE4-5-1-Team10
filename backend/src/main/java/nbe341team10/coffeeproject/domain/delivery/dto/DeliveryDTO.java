package nbe341team10.coffeeproject.domain.delivery.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private String deliveryAddress;
    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryArriveDate;

}

