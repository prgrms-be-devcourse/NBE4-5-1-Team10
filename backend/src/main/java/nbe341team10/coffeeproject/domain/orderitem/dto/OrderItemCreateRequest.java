package nbe341team10.coffeeproject.domain.orderitem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderItemCreateRequest {

    @NotBlank(message = "상품 ID를 입력해주세요.")
    private Long itemId;

    private int price;

    @Min(value = 1, message = "1개 이상 선택 후 주문해주세요.")
    private int quantity;
}
