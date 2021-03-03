package com.zup.mercadolivre.purchase.model.enums;

import com.zup.mercadolivre.purchase.model.Gateway;
import com.zup.mercadolivre.purchase.model.PagSeguroGateway;
import com.zup.mercadolivre.purchase.model.PaypalGateway;

public enum PaymentGateway {
    PAGSEGURO(new PagSeguroGateway()),
    PAYPAL(new PaypalGateway());

    private final Gateway gateway;

    PaymentGateway(Gateway gateway) { this.gateway = gateway; }

    public Gateway getGateway() { return gateway; }
}
