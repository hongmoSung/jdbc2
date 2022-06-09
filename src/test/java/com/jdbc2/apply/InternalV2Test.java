package com.jdbc2.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalV2Test {

    @Autowired
    CallService callService;

    @Test
    void printProxy() {
        log.info("service {}", callService.getClass());
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class CallServiceConfig {

        @Bean
        public CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        public InternalService internalService() {
            return new InternalService();
        }
    }

    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("external call");
            print();
            internalService.internal();
        }

        private void print() {
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", transactionActive);
        }

    }

    static class InternalService {

        @Transactional
        public void internal() {
            log.info("internal call");
            print();
        }

        private void print() {
            boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", transactionActive);
        }

    }

}
