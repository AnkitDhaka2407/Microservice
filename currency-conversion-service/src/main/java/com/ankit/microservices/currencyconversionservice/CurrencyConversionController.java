package com.ankit.microservices.currencyconversionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {

        Map<String, String> map = new HashMap<>();
        map.put("from",from);
        map.put("to",to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,map);
        CurrencyConversion currencyConversion = responseEntity.getBody();
        return new CurrencyConversion(1000L, currencyConversion.getFrom(), currencyConversion.getTo(), currencyConversion.getQuantity(),
                currencyConversion.getConversionMultiple(), currencyConversion.getConversionMultiple().multiply(quantity), currencyConversion.getEnvironment());
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionWithFeign(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeRate(from,to);
        return new CurrencyConversion(1000L, currencyConversion.getFrom(), currencyConversion.getTo(), currencyConversion.getQuantity(),
                currencyConversion.getConversionMultiple(), currencyConversion.getConversionMultiple().multiply(quantity), currencyConversion.getEnvironment());
    }

}
