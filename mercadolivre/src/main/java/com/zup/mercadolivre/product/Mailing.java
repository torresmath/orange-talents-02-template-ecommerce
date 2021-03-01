package com.zup.mercadolivre.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface Mailing {

    void send(@NotNull @NotBlank String title, @NotNull @NotBlank String from, @NotNull @NotBlank String to, @NotNull @NotBlank String content);
}
