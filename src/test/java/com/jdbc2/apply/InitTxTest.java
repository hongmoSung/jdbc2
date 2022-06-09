package com.jdbc2.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@SpringBootTest
public class InitTxTest {

    @Autowired
    Init init;

    @Test
    void initTest() {

    }

    @TestConfiguration
    static class InitConfig {

        @Bean
        public Init init() {
            return new Init();
        }
    }

    @Slf4j
    static class Init {

        @PostConstruct
        @Transactional
        public void initV1() {
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active {}", transactionActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active {}", transactionActive);
        }
    }
}
