package nbe341team10.coffeeproject.domain.delivery.service;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.delivery.dto.DeliveryDTO;
import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final OrderRepository orderRepository; // 주문 리포지토리 추가

    // 매일 오후 2시에 배송 준비 상태로 변경
    @Scheduled(cron = "0 0 14 * * *") // 매일 오후 2시
    public void prepareDeliveries() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = now.withHour(14).withMinute(0).withSecond(0);


        List<Orders> ordersToPrepare = orderRepository.findAllByCreatedAtBetween(startTime, endTime);

        for (Orders order : ordersToPrepare) {
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.getOrder().setAddress(order.getAddress());
            delivery.getOrder().setPostalCode(order.getPostalCode());

            delivery.setDeliveryStartDate(now);

            // 배송 상태를 준비 상태로 변경
            delivery.setStatus(OrderStatus.READY_DELIVERY_SAME_DAY);

            // 배송 저장
            deliveryRepository.save(delivery);


            // 현재 시간이 오후 2시 이전인지 확인
            if (now.getHour() < 14) {
                // 오후 2시 이전: 당일 배송

                delivery.setStatus(OrderStatus.READY_DELIVERY_SAME_DAY);
            } else {
                // 오후 2시 이후: 다음날 배송
                delivery.setStatus(OrderStatus.READY_DELIVERY_NEXT_DAY);

            }

            // 주문 상태를 SHIPPED로 업데이트
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order); // 주문 상태 업데이트 저장

            deliveryRepository.save(delivery); // 배송 정보 저장
        }
    }

    // 매일 오후 2시에 배송 시작 및 이메일 전송
    @Scheduled(cron = "0 0 14 * * *") // 매일 오후 2시
    public void startDelivery() {

        List<Delivery> deliveries = deliveryRepository.findAllByStatus(OrderStatus.READY_DELIVERY_SAME_DAY);

        for (Delivery delivery : deliveries) {
            // 배송 상태 업데이트
            delivery.setStatus(OrderStatus.InDelivery);
            // 이메일 전송 로직
            sendEmail(delivery);

            deliveryRepository.save(delivery);
        }
    }


    // 이메일 전송 메서드

    private void sendEmail(Delivery delivery) {
        // 이메일 전송 로직 구현
        System.out.println("이메일 전송: 주문 " + delivery.getOrder().getId() + "의 배송 진행 상태가 업데이트되었습니다.");
    }

    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }


    public DeliveryDTO getDeliveryById(Long id) {
        return null;
    }


    public void updateDelivery(DeliveryDTO deliveryDTO) {
    }


    public List<DeliveryDTO> getAllDeliveries() {
        return List.of();
    }

    public DeliveryDTO saveDelivery(DeliveryDTO deliveryDTO) {
        return deliveryDTO;
    }
}

