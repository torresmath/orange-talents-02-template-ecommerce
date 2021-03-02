package com.zup.mercadolivre.purchase.model;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    private Product validProduct;

    @BeforeEach
    public void init() {
        validProduct = new Product("P", BigDecimal.ONE, 3, "D", new Category(), new User());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("Deveria criar compra")
    void deveriaCriarCompra(int amount) {
        User user = new User();
        ReflectionTestUtils.setField(user, "login", "customer@email.com");
        assertDoesNotThrow(() -> new Purchase(PaymentGateway.PAGSEGURO, validProduct, amount, user));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -9999})
    @DisplayName("Deveria retornar Illegal Argument Exception para Amount")
    void deveriaRetornarExceptionAmount(int amount) {
        User user = new User();
        ReflectionTestUtils.setField(user, "login", "customer@email.com");
        assertThrows(IllegalArgumentException.class, () -> new Purchase(PaymentGateway.PAGSEGURO, validProduct, amount, user));
    }

    @Test
    @DisplayName("Deveria retornar Illegal Argument Exception para Amount")
    void deveriaRetornarExceptionProduct() {
        User user = new User();
        ReflectionTestUtils.setField(user, "login", "customer@email.com");
        assertThrows(IllegalArgumentException.class, () -> new Purchase(PaymentGateway.PAGSEGURO, null, 1, user));
    }

    @Test
    @DisplayName("Deveria retornar Illegal State Exception para Buyer")
    void deveriaRetornarExceptionBuyer() {
        User user = new User();
        assertThrows(IllegalStateException.class, () -> new Purchase(PaymentGateway.PAGSEGURO, validProduct, 1, user));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 9999})
    @DisplayName("Deveria retornar Illegal State Exception para Produto com estoque insuficiente")
    void deveriaRetornarExceptionProductAmount(int amount) {
        User user = new User();
        ReflectionTestUtils.setField(user, "login", "customer@email.com");
        assertThrows(IllegalStateException.class, () -> new Purchase(PaymentGateway.PAGSEGURO, validProduct, amount, user));
    }

}