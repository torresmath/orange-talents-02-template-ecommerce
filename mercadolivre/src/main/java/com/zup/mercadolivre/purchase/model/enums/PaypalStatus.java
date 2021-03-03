package com.zup.mercadolivre.purchase.model.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PaypalStatus {
    SUCCESS(1, TransactionStatus.SUCCESS),
    ERROR(0, TransactionStatus.ERROR);

    private final int code;
    private final TransactionStatus transactionStatus;

    PaypalStatus(int code, TransactionStatus transactionStatus) {
        this.code = code;
        this.transactionStatus = transactionStatus;
    }

    public int getCode() {
        return code;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public static PaypalStatus getStatus(int code) {

        Optional<PaypalStatus> possibleStatus = Arrays.stream(PaypalStatus.values())
                .filter(c -> c.getCode() == code)
                .findFirst();

        if (possibleStatus.isEmpty()) {
            throw new IllegalArgumentException("Codigo do Paypal nao mapeado: "+ code);
        }

        return possibleStatus.get();
    }
}
