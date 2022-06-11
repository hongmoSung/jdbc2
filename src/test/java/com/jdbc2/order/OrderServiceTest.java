package com.jdbc2.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService service;

    @Autowired
    OrderRepository repository;

    @Test
    void order() throws NotEnoughMoneyException {

        Order order = new Order();
        order.setUsername("정상");

        service.order(order);

        Order findOrder = repository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() {

        Order order = new Order();
        order.setUsername("예외");

        assertThatThrownBy(() -> service.order(order))
                .isInstanceOf(RuntimeException.class);

        Optional<Order> orderOptional = repository.findById(order.getId());
        assertThat(orderOptional.isEmpty()).isTrue();
    }

    @Test
    void bizException() {

        Order order = new Order();
        order.setUsername("잔고부족");

        try {
            service.order(order);
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        Order findOrder = repository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }

}