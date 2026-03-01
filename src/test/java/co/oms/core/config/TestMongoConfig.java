package co.oms.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/** Embedded MongoDB는 트랜잭션 미지원 → 테스트용 no-op 트랜잭션 매니저 */
@TestConfiguration
public class TestMongoConfig {

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new AbstractPlatformTransactionManager() {

            @Override
            protected Object doGetTransaction() {
                return new Object();
            }

            @Override
            protected void doBegin(Object transaction, TransactionDefinition definition) {
                // no-op
            }

            @Override
            protected void doCommit(DefaultTransactionStatus status) {
                // no-op
            }

            @Override
            protected void doRollback(DefaultTransactionStatus status) {
                // no-op
            }
        };
    }
}
