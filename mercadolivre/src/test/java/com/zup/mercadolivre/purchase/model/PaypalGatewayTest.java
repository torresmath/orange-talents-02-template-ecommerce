package com.zup.mercadolivre.purchase.model;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PaypalGatewayTest {

    @Test
    void test() {
        PaypalGateway paypalGateway = new PaypalGateway();
        Purchase purchase = new Purchase();
        ReflectionTestUtils.setField(purchase, "identifier", "1234-4567-89ab-cdef");
        String s = paypalGateway.submitPurchase(purchase);
        System.out.println("s = " + s);
    }

}