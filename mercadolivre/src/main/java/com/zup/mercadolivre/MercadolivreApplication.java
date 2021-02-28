package com.zup.mercadolivre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication /*(exclude = {SecurityAutoConfiguration.class})*/
public class MercadolivreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MercadolivreApplication.class, args);
    }

}
