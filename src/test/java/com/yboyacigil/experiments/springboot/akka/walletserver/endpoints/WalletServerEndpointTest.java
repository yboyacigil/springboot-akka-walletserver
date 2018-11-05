package com.yboyacigil.experiments.springboot.akka.walletserver.endpoints;

import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Currency;
import com.yboyacigil.experiments.springboot.akka.walletserver.services.AccountingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletServerEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private AccountingService accountingService;

    @Test
    public void should_ReturnOkWithCurrency_When_PlayerInfoExists() {
        final Long pid = 1L;
        final String isoCurrencyCode = "SEK";

        AccountingService.PlayerInfo playerInfo = AccountingService.PlayerInfo.builder()
            .pid(pid)
            .isoCurrencyCode(isoCurrencyCode)
            .build();
        when(accountingService.getPlayerInfo(pid)).thenReturn(Optional.of(playerInfo));

        ResponseEntity<Currency> entity =
            restTemplate.getForEntity("/walletserver/players/1/account/currency", Currency.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(Currency.builder().pid(1L).isoCurrencyCode("SEK").build());

        verify(accountingService).getPlayerInfo(pid);
    }

    @Test
    public void should_ReturnServerError_When_ExceptionThrown() {
        final Long pid = 1L;

        when(accountingService.getPlayerInfo(pid)).thenThrow(new RuntimeException());

        ResponseEntity<EndpointExceptionMapper.ErrorInfo> entity =
            restTemplate.getForEntity("/walletserver/players/1/account/currency", EndpointExceptionMapper.ErrorInfo.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        verify(accountingService).getPlayerInfo(pid);
    }

    @Test
    public void should_ReturnNotFound_When_NoPlayerOrCurrencyFound() {
        final Long pid = 1L;

        when(accountingService.getPlayerInfo(pid)).thenReturn(Optional.empty());

        ResponseEntity<EndpointExceptionMapper.ErrorInfo> entity =
            restTemplate.getForEntity("/walletserver/players/1/account/currency", EndpointExceptionMapper.ErrorInfo.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(accountingService).getPlayerInfo(pid);
    }
}
