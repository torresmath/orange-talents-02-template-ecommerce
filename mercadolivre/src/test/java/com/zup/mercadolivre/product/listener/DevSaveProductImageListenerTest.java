package com.zup.mercadolivre.product.listener;

import com.zup.mercadolivre.category.model.Category;
import com.zup.mercadolivre.product.controller.request.ProductImageRequest;
import com.zup.mercadolivre.product.model.Product;
import com.zup.mercadolivre.product.model.ProductImage;
import com.zup.mercadolivre.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DevSaveProductImageListenerTest {

    @Mock
    EntityManager manager;

    @InjectMocks
    @Autowired
    private DevSaveProductImageListener listener;

    private Product product;

    @BeforeEach
    public void initialize() {

        product = new Product("product", BigDecimal.ONE, 1, "Desc", new Category(), new User());
    }

    @Test
    @DisplayName("Listener deveria salvar imagem")
    void deveriaSalvarImagem() {

        ProductImageRequest request = new ProductImageRequest(Collections.singletonList("url-test"));

        listener.handleEvent(request.toEvent(product));

        ArgumentCaptor<ProductImage> args = ArgumentCaptor.forClass(ProductImage.class);

        assertAll(
                () -> verify(manager, times(1)).persist(args.capture()),
                () -> assertEquals("dev-url-test", args.getValue().getLink()),
                () -> assertEquals(product, args.getValue().getProduct())
        );
    }

    @Test
    @DisplayName("Listener deveria salvar multiplas imagens")
    void deveriaSalvarImagens() {

        ProductImageRequest request = new ProductImageRequest(Arrays.asList("url-test", "url-test", "url-test"));

        listener.handleEvent(request.toEvent(product));

        ArgumentCaptor<ProductImage> args = ArgumentCaptor.forClass(ProductImage.class);


        args.getAllValues()
                .forEach(arg -> {
                    assertEquals("dev-url-test", arg.getLink());
                    assertEquals(product, arg.getProduct());
                });

        assertAll(
                () -> verify(manager, times(3)).persist(args.capture())
        );
    }
}