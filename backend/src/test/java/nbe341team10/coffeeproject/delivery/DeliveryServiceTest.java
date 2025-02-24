package nbe341team10.coffeeproject.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManagerFactory;
import nbe341team10.coffeeproject.domain.delivery.entity.Delivery;
import nbe341team10.coffeeproject.domain.delivery.repository.DeliveryRepository;
import nbe341team10.coffeeproject.domain.delivery.service.DeliveryService;
import nbe341team10.coffeeproject.domain.order.entity.OrderStatus;
import nbe341team10.coffeeproject.domain.order.entity.Orders;
import nbe341team10.coffeeproject.domain.order.repository.OrderRepository;
import nbe341team10.coffeeproject.domain.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class TestConfig {

        @Bean
        public TestEntityManager entityManager(EntityManagerFactory entityManagerFactory) {
            return new TestEntityManager(entityManagerFactory);
        }

        @Bean
        public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(dataSource);
            // 추가적인 설정 (예: 패키지 스캔, JPA 속성 등)을 여기에 추가
            return factory.getObject();
        }

        @Bean
        public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
        }
    }

    @Test
    public void testPrepareDeliveries0() {
        // Given
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 23, 14, 0).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 24, 14, 0).truncatedTo(ChronoUnit.MINUTES);

        Orders order1 = Orders.builder()
                .email("user1@example.com")
                .address("123 Main St")
                .postalCode("12345")
                .status(OrderStatus.ORDERED)
                .totalPrice(100)
                .createdAt(LocalDateTime.of(2025, 2, 23, 14, 0, 0, 0)) // 나노초를 0으로 설정
                .build();

        Orders order2 = Orders.builder()
                .email("user2@example.com")
                .address("456 Elm St")
                .postalCode("67890")
                .status(OrderStatus.ORDERED)
                .totalPrice(200)
                .createdAt(LocalDateTime.of(2025, 2, 24, 10, 0, 0, 0)) // 나노초를 0으로 설정
                .build();

        List<Orders> expectedOrders = Arrays.asList(order1, order2);

        // Mocking the repository to return expected orders
        when(orderRepository.findAllByCreatedAtBetween(startTime, endTime)).thenReturn(expectedOrders);

        // When
        deliveryService.prepareDeliveries(); // prepareDeliveries 메서드 호출

        // Then
        verify(orderRepository).findAllByCreatedAtBetween(
                argThat(arg -> arg.truncatedTo(ChronoUnit.MINUTES).equals(startTime)),
                argThat(arg -> arg.truncatedTo(ChronoUnit.MINUTES).equals(endTime))
        );
        // 추가적인 검증 로직을 여기에 추가할 수 있습니다.
    }
    @Test
    public void testPrepareDeliveries_BeforeAfternoon() {
        // Given
        LocalDateTime fixedNow = LocalDateTime.of(2025, 2, 24, 13, 0);
        LocalDateTime startTime = fixedNow.minusDays(1).withHour(14).withMinute(0).withSecond(0);
        LocalDateTime endTime = fixedNow.withHour(14).withMinute(0).withSecond(0);

        Orders order = new Orders();
        order.setAddress("123 Main St");
        order.setPostalCode("12345");
        order.setStatus(OrderStatus.ORDERED);

        when(orderRepository.findAllByCreatedAtBetween(startTime, endTime)).thenReturn(Arrays.asList(order));

        // Mocking LocalDateTime.now() to return fixedNow
        try (MockedStatic<LocalDateTime> mockedStatic = mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(fixedNow);

            // When
            deliveryService.prepareDeliveries();

            // Then
            assertEquals(OrderStatus.SHIPPED, order.getStatus());

        }
    }

    @Test
    public void testStartDelivery() {
        // Given
        Delivery delivery1 = new Delivery();
        delivery1.setOrder(new Orders());

        delivery1.setStatus(OrderStatus.READY_DELIVERY_SAME_DAY);

        Delivery delivery2 = new Delivery();
        delivery2.setOrder(new Orders());

        delivery2.setStatus(OrderStatus.READY_DELIVERY_SAME_DAY);

        when(deliveryRepository.findAllByStatus(OrderStatus.READY_DELIVERY_SAME_DAY)).thenReturn(Arrays.asList(delivery1, delivery2));

        // When
        deliveryService.startDelivery();

        // Then
        verify(deliveryRepository, times(2)).save(any(Delivery.class));
        assertEquals(OrderStatus.InDelivery, delivery1.getStatus());
        assertEquals(OrderStatus.InDelivery, delivery2.getStatus());
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