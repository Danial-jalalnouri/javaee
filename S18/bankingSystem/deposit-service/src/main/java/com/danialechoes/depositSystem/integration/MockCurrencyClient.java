package com.danialechoes.depositSystem.integration;

import com.danialechoes.depositSystem.dto.CurrencyResponse;
import com.danialechoes.depositSystem.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(
        prefix = "api.currency",
        name = "mock",
        havingValue = "true",
        matchIfMissing = true
)
public class MockCurrencyClient implements CurrencyClient {
    private static final Logger logger = LoggerFactory.getLogger(MockCurrencyClient.class);

    @Override
    public CurrencyResponse getLatestRates(String apiKey, String baseCurrency) {
        logger.info("[MockCurrencyClient] Returning hardcoded exchange rates for base currency: {}", baseCurrency);
        // Mock implementation returning hardcoded values
        CurrencyResponse response = new CurrencyResponse();
        response.setData(Map.of(
                "USD", 1.0,
                "EUR", 1.0,
                "GBP", 1.0
        ));
        return response;
    }
}
