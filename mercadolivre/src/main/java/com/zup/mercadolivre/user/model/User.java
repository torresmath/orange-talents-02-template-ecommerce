package com.zup.mercadolivre.user.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Column(unique = true)
    private String login;

    @NotBlank
    @NotNull
    private String password;

    @CreationTimestamp
    private LocalDateTime createDate;

    /**
     *
     * @param login string no formato de email
     * @param password string em texto limpo (sem criptografia)
     */
    public User(@Email String login, @Length(min = 6) String password) {

        Assert.hasText(login, "Login não pode estar vazio");
        Assert.state(password.length() >= 6, "Senha precisa ter no mínimo 6 caracteres");

        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        this.login = login;
        this.password = encodedPassword;
    }

    @Deprecated
    public User() {}

}
