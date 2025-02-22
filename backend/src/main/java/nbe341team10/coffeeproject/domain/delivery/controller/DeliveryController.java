package nbe341team10.coffeeproject.domain.delivery.controller;

import lombok.RequiredArgsConstructor;
import nbe341team10.coffeeproject.domain.delivery.dto.DeliveryDTO;
import nbe341team10.coffeeproject.domain.delivery.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        DeliveryDTO deliveryDTO = deliveryService.getDeliveryById(id);
        return deliveryDTO != null ? ResponseEntity.ok(deliveryDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        return ResponseEntity.ok(deliveryService.saveDelivery(deliveryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}

