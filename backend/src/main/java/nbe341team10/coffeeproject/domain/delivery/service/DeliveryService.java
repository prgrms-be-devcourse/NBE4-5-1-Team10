package nbe341team10.coffeeproject.domain.delivery.service;

import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class DeliveryService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    public DeliveryService(OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }
    // 매일 오후 2시에 배송 준비 및 시작
    @Scheduled(cron = "0 0 14 * * *") // 매일 오후 2시
    public void handleDeliveries() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = now.withHour(14).withMinute(0).withSecond(0);
        List<Orders> ordersToPrepare = orderRepository.findAllByCreatedAtBetween(startTime, endTime);
        for (Orders order : ordersToPrepare) {
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setDeliveryStartDate(now);

            System.out.println("배송 준비 시작");

            // 주문 상태를 SHIPPED로 업데이트
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order); // 주문 상태 업데이트 저장
            // 배송 저장
            deliveryRepository.save(delivery);
            // 이메일 전송
            sendEmail(delivery);
        }
    }
    // 매일 오후 3시에 배송 완료 상태로 업데이트
    @Scheduled(cron = "0 0 15 * * *") // 매일 오후 3시
    public void completeDeliveries() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = now.withHour(14).withMinute(0).withSecond(0);
        List<Delivery> deliveriesToComplete = deliveryRepository.findAllByDeliveryStartDateBetween(startTime, endTime);
        for (Delivery delivery : deliveriesToComplete) {
            // 배송 상태를 DELIVERED로 업데이트
            delivery.getOrder().setStatus(OrderStatus.DELIVERED);
            orderRepository.save(delivery.getOrder());
            deliveryRepository.save(delivery);
            System.out.println("배송 완료");
            // 이메일 전송
            sendEmail(delivery);
        }
    }
    private void sendEmail(Delivery delivery) {
        // 이메일 전송 로직 구현
        System.out.println("이메일 전송: 주문 " + delivery.getOrder().getEmail() + "의 배송 진행 상태가 업데이트되었습니다.");
    }

    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }
}