package com.zup.mercadolivre.purchase.controller;

import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.purchase.controller.request.PurchaseRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class PurcharseProductAndAmountValidator implements Validator {

    private static final String ID_PRODUCT = "idProduct";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(PurchaseRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        PurchaseRequest request = (PurchaseRequest) o;

        Product product = manager.find(Product.class, request.getIdProduct());

        if (product == null) {
            errors.rejectValue(ID_PRODUCT, "product.not_found");
            return;
        }

        if (product.outOfStock()) {
            errors.rejectValue(ID_PRODUCT, "product.out_of_stock");
            return;
        }

        if (!product.hasAmount(request.getAmount())) {
            errors.rejectValue(ID_PRODUCT, "product.insufficient_amount");
        }
    }
}
