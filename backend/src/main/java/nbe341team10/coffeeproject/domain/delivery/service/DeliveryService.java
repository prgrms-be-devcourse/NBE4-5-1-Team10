package nbe341team10.coffeeproject.domain.delivery.service;


import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.delivery.dto.DeliveryDTO;
import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

//    public List<DeliveryDTO> getAllDeliveries() {
//        return deliveryRepository.findAll().stream()
//                .map(delivery -> new DeliveryDTO(
//                        delivery.getId(),
//                        delivery.getOrder().getId(),
//                        delivery.getDeliveryAddress(),
//                        delivery.getDeliveryStartDate(),
//                        delivery.getDeliveryArriveDate()))
//                .collect(Collectors.toList());
//    }
//
//    public DeliveryDTO getDeliveryById(Long id) {
//        Optional<Delivery> delivery = deliveryRepository.findById(id);
//        return delivery.map(d -> new DeliveryDTO(
//                d.getId(),
//                d.getOrder().getId(),
//                d.getDeliveryAddress(),
//                d.getDeliveryStartDate(),
//                d.getDeliveryArriveDate()
//        )).orElse(null);
//    }
//
//    public DeliveryDTO saveDelivery(DeliveryDTO deliveryDTO) {
//        Delivery delivery = Delivery.builder()
//                .order(new Orders(deliveryDTO.getOrderId()))  // 가짜 Orders 객체
//                .deliveryAddress(deliveryDTO.getDeliveryAddress())
//                .deliveryStartDate(deliveryDTO.getDeliveryStartDate())
//                .deliveryArriveDate(deliveryDTO.getDeliveryArriveDate())
//                .build();
//
//        Delivery saved = deliveryRepository.save(delivery);
//        return new DeliveryDTO(
//                saved.getId(),
//                saved.getOrder().getId(),
//                saved.getDeliveryAddress(),
//                saved.getDeliveryStartDate(),
//                saved.getDeliveryArriveDate());
//    }

    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }
}
