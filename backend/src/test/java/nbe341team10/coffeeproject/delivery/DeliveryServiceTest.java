package nbe341team10.coffeeproject.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import nbe341team10.coffeeproject.domain.delivery.service.DeliveryService;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;




@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    public void testHandleDeliveries() {
        // Given
        LocalDateTime now = LocalDateTime.of(2025, 2, 24, 14, 0);
        LocalDateTime startTime = now.minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = now.withHour(14).withMinute(0).withSecond(0);

        Orders order1 = Orders.builder()
                .email("user1@example.com")
                .address("123 Main St")
                .postalCode("12345")
                .status(OrderStatus.ORDERED)
                .totalPrice(100)
                .createdAt(LocalDateTime.of(2025, 2, 23, 14, 0, 0, 0))
                .build();

        Orders order2 = Orders.builder()
                .email("user2@example.com")
                .address("456 Elm St")
                .postalCode("67890")
                .status(OrderStatus.ORDERED)
                .totalPrice(200)
                .createdAt(LocalDateTime.of(2025, 2, 24, 10, 0, 0, 0))
                .build();

        List<Orders> expectedOrders = Arrays.asList(order1, order2);

        // Mocking the repository to return expected orders
        when(orderRepository.findAllByCreatedAtBetween(startTime, endTime)).thenReturn(expectedOrders);

        // Mocking LocalDateTime.now() to return fixedNow
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(now);

            // When
            deliveryService.handleDeliveries();

            // Then
            verify(orderRepository).findAllByCreatedAtBetween(
                    argThat(arg -> arg.truncatedTo(ChronoUnit.MINUTES).equals(startTime)),
                    argThat(arg -> arg.truncatedTo(ChronoUnit.MINUTES).equals(endTime))
            );

            // Verify that the order status was updated to SHIPPED
            assertEquals(OrderStatus.SHIPPED, order1.getStatus());
            assertEquals(OrderStatus.SHIPPED, order2.getStatus());

            // Verify that deliveries were created and saved
           }
    }

    @Test
    public void testDeleteDelivery() {
        // Given
        Long deliveryId = 1L;

        // When
        deliveryService.deleteDelivery(deliveryId);

        // Then
        verify(deliveryRepository, times(1)).deleteById(deliveryId);
    }
}
