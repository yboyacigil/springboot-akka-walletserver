package com.yboyacigil.experiments.springboot.akka.walletserver.endpoints

import com.yboyacigil.experiments.springboot.akka.walletserver.services.AccountingService
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfiguration)
class WalletServerEndpointSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    AccountingService accountingService

    def ".../currency should return ok for an existing player info"() {
        given:
            def pid = 1L
            def url = "/walletserver/players/${pid}/account/currency"
            def isoCurrencyCode = "SEK"
            def playerInfo = AccountingService.PlayerInfo.builder()
            .pid(pid)
            .isoCurrencyCode(isoCurrencyCode)
            .build()
            1 * accountingService.getPlayerInfo(pid) >> Optional.of(playerInfo)

        when:
            def entity =
                    restTemplate.getForEntity(url, Currency.class)

        then:
            entity.statusCode == HttpStatus.OK
            entity.body.pid == pid
            entity.body.isoCurrencyCode == isoCurrencyCode
    }

    def ".../currency should return not found if no player info exists"() {
        given:
            def pid = 1L
            def url = "/walletserver/players/${pid}/account/currency"
            1 * accountingService.getPlayerInfo(pid) >> Optional.empty()

        when:
            def entity =
                    restTemplate.getForEntity(url, EndpointExceptionMapper.ErrorInfo)

        then:
            entity.statusCode == HttpStatus.NOT_FOUND
    }

    def ".../currency should return server error if exception thrown"() {
        given:
            def pid = 1L
            def url = "/walletserver/players/${pid}/account/currency"
            1 * accountingService.getPlayerInfo(pid) >> { throw new RuntimeException() }

        when:
            def entity =
                    restTemplate.getForEntity(url, EndpointExceptionMapper.ErrorInfo)

        then:
            entity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    }

}
