package com.zup.mercadolivre.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.mailing.Mailing;
import com.zup.mercadolivre.purchase.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PurchaseCheckout {

    @Autowired
    private Mailing mailing;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${jwt.internal_token}")
    private String token;

    public boolean commit(Purchase purchase) {

        if (purchase.isDone()) {

            mailing.send("mock title", "mock buyer", "mock owner", "<html>PAGAMENTOU BEM SUCEDIDO</html>");

            Map<String, Long> invoiceMap = Map.of("id_purchase", purchase.getId(), "id_buyer", purchase.getBuyerId());
            objectToJson(invoiceMap, "http://localhost:8080/api/v1/invoice");

            Map<String, Long> salesMap = Map.of("id_purchase", purchase.getId(), "id_owner", purchase.getOwnerId());
            objectToJson(salesMap, "http://localhost:8080/api/v1/sales");
            return true;

        } else {
            mailing.send("mock title", "mock buyer", "mock owner", "<html>PAGAMENTOU FALOU</html>");
            return false;
        }
    }

    void objectToJson(Object request, String uri) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);
        restTemplate.postForObject(uri, requestEntity, String.class);
    }
}
