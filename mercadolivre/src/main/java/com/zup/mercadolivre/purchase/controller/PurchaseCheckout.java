package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.mailing.Mailing;
import com.zup.mercadolivre.purchase.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCheckout {

    @Autowired
    private Mailing mailing;

    public void commit(Purchase purchase) {
        if (purchase.isDone()) {
            mailing.send("mock title", "mock buyer", "mock owner", "<html>PAGAMENTOU BEM SUCEDIDO</html>");
            System.out.println("-----INVOCA API DE NOTAS-----");
            System.out.println("-----INVOCA API DE RANKING-----");
        } else {
            mailing.send("mock title", "mock buyer", "mock owner", "<html>PAGAMENTOU FALOU</html>");
        }
    }
}
