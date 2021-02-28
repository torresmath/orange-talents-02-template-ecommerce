package com.zup.mercadolivre.product.listener;

import com.zup.mercadolivre.product.event.SaveProductImageEvent;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductImage;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Profile(value = {"dev", "test"})
public class DevSaveProductImageListener {

    @PersistenceContext
    private EntityManager manager;

    @Async
    @EventListener
    @Transactional
    public void handleEvent(SaveProductImageEvent event) {
        System.out.println("DEV LISTENING EVENT");

        Product product = event.getProduct();

        event.getImages()
                .forEach(img -> {
                    ProductImage productImage = new ProductImage("dev-" + img, product);
                    manager.persist(productImage);
                });
    }
}
