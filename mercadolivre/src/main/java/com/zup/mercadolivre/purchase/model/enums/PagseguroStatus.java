package com.zup.mercadolivre.purchase.model.enums;

public enum PagseguroStatus {

    SUCESSO(TransactionStatus.SUCCESS),
    ERRO(TransactionStatus.ERROR);

    private final TransactionStatus transactionStatus;

    PagseguroStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}
