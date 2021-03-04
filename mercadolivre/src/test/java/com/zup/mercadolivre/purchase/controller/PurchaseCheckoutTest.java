package com.zup.mercadolivre.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.mercadolivre.mailing.Mailing;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.enums.PurchaseStatus;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseCheckoutTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Mailing mailing;

    @Autowired
    @InjectMocks
    private PurchaseCheckout purchaseCheckout;

    private Purchase purchase;

    @BeforeEach
    public void init() {

        User owner = new User();
        ReflectionTestUtils.setField(owner, "id", 1L);

        User buyer = new User();
        ReflectionTestUtils.setField(buyer, "id", 1L);

        Product product = new Product();
        ReflectionTestUtils.setField(product, "owner", owner);

        purchase = new Purchase();
        ReflectionTestUtils.setField(purchase, "id", 1L);
        ReflectionTestUtils.setField(purchase, "product", product);
        ReflectionTestUtils.setField(purchase, "buyer", buyer);

    }

    @Test
    void deveriaRetornarTrue() {

        ReflectionTestUtils.setField(purchase, "status", PurchaseStatus.DONE);

        boolean commit = purchaseCheckout.commit(purchase);

        assertTrue(commit);
        verify(restTemplate, times(2)).postForObject(anyString(), any(), any());
    }

    @Test
    void deveriaRetornarFalse() {

        ReflectionTestUtils.setField(purchase, "status", PurchaseStatus.IN_PROGRESS);

        boolean commit = purchaseCheckout.commit(purchase);

        assertFalse(commit);
        verifyNoInteractions(restTemplate);
    }
}