package com.danialechoes.depositSystem.service;

import com.danialechoes.depositSystem.integration.CurrencyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final CurrencyClient currencyClient;

    @Value("${currency.api.key}")
    private String apiKey;

    @Autowired
    public CurrencyService(CurrencyClient currencyClient) {
        this.currencyClient = currencyClient;
    }

    public Double getExchangeRate(String baseCurrency, String targetCurrency) {
        logger.info("[CurrencyService] Fetching exchange rate from {} to {}", baseCurrency, targetCurrency);
        return currencyClient.getLatestRates(apiKey, baseCurrency)
                .getData()
                .get(targetCurrency);
    }

}
