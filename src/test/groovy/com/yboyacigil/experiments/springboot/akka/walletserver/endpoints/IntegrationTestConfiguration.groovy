package com.yboyacigil.experiments.springboot.akka.walletserver.endpoints

import com.yboyacigil.experiments.springboot.akka.walletserver.services.AccountingService
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

class IntegrationTestConfiguration {

    private final detachedMockFactory = new DetachedMockFactory()

    @Bean
    AccountingService accountingService() {
        detachedMockFactory.Mock(AccountingService)
    }
}
