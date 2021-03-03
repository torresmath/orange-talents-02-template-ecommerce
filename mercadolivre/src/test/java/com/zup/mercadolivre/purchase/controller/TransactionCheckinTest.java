package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.controller.request.PagseguroTransactionRequest;
import com.zup.mercadolivre.purchase.model.Purchase;
import com.zup.mercadolivre.purchase.model.enums.PaymentGateway;
import com.zup.mercadolivre.purchase.model.enums.PurchaseStatus;
import com.zup.mercadolivre.purchase.repository.PurchaseRepository;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionCheckinTest {

    @Mock
    private PurchaseRepository repository;

    @Mock
    private PurchaseCheckout purchaseCheckout;

    private Purchase validPurchase;

    @BeforeEach
    public void init() {
        User buyer = new User();
        ReflectionTestUtils.setField(buyer, "login", "login@teste.com", String.class);
        Product product = new Product();
        ReflectionTestUtils.setField(product, "amount", 1, int.class);
        validPurchase = new Purchase(PaymentGateway.PAGSEGURO, product, 1, buyer);
        ReflectionTestUtils.setField(validPurchase, "status", PurchaseStatus.IN_PROGRESS, PurchaseStatus.class);
    }

    @Test
    @DisplayName("Deveria retornar 200")
    void deveriaRetornar200() {

        when(repository.findByIdentifier("identifier"))
                .thenReturn(Optional.of(validPurchase));

        TransactionCheckin<PagseguroTransactionRequest> checkin = new TransactionCheckin<>(repository, purchaseCheckout);

        PagseguroTransactionRequest request = new PagseguroTransactionRequest("1234", "identifier", "SUCESSO");

        ResponseEntity<Map<String, String>> response = checkin.checkIn(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Deveria retornar 404")
    void deveriaRetornar404() {

        when(repository.findByIdentifier("identifier"))
                .thenReturn(Optional.empty());

        TransactionCheckin<PagseguroTransactionRequest> checkin = new TransactionCheckin<>(repository, purchaseCheckout);

        PagseguroTransactionRequest request = new PagseguroTransactionRequest("1234", "identifier", "SUCESSO");

        assertThrows(ResponseStatusException.class, () -> checkin.checkIn(request));
    }

    @Test
    @DisplayName("Deveria retornar 409")
    void deveriaRetornar409() {

        ReflectionTestUtils.setField(validPurchase, "status", PurchaseStatus.DONE, PurchaseStatus.class);
        when(repository.findByIdentifier("identifier"))
                .thenReturn(Optional.of(validPurchase));

        TransactionCheckin<PagseguroTransactionRequest> checkin = new TransactionCheckin<>(repository, purchaseCheckout);

        PagseguroTransactionRequest request = new PagseguroTransactionRequest("1234", "identifier", "SUCESSO");

        assertThrows(ResponseStatusException.class, () -> checkin.checkIn(request));
    }
}