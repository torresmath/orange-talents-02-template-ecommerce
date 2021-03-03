package com.zup.mercadolivre.mailing;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Service
@Primary
public class DevMailing implements Mailing {
    @Override
    public void send(@NotNull @NotBlank String title, @NotNull @NotBlank String from, @NotNull @NotBlank String to, @NotNull @NotBlank String content) {

        System.out.println("-----DEV MAILING IMPL-----");
        System.out.println("title = " + title);
        System.out.println("from = " + from);
        System.out.println("to = " + to);
        System.out.println("content = " + content);
        System.out.println("-----DEV MAILING IMPL-----");
    }
}
