package com.zup.mercadolivre.purchase.model;

public enum PaymentGateway {
    PAGSEGURO(new PagSeguroGateway()),
    PAYPAL(new PaypalGateway());

    private final Gateway gateway;

    PaymentGateway(Gateway gateway) { this.gateway = gateway; }

    public Gateway getGateway() { return gateway; }
}
