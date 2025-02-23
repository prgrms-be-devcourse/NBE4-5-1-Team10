package nbe341team10.coffeeproject.domain.order.entity;

public enum OrderStatus {

    ORDERED, //주문 완료
    READY_DELIVERY_SAME_DAY, // 당일 배송 준비 완료
    READY_DELIVERY_NEXT_DAY, // 다음날 배송 준비 완료
    SHIPPED, //배송 시작
    InDelivery,//배송중
    DELIVERED //배송 완료

}