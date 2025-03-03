package nbe341team10.coffeeproject.domain.orderitem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.product.entity.Product;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    private int quantity;

    //한 종류의 Product의 개수에 따른 총 가격
    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

}
