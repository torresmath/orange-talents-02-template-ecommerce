package com.zup.mercadolivre.common.configurations;

import com.zup.mercadolivre.user.model.User;
import com.zup.mercadolivre.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UserRepository repository;

    public JwtAuthenticationFilter(TokenService tokenService, UserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    // Aqui será feita a validação de token para as URLs
    // que necessitam de autenticação para serem acessadas
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1 - Verifica se o cabeçalho Authorization foi enviado
        // com um token devidamente formatado

        String token = retrieveToken(request);

        // 2 - Verifica se o token enviado é válido
        // Aqui o Jwts vai verificar se a assinatura do token fornecido
        // é equivalente ao padrão definido na propriedade jwt.secret
        boolean valid = tokenService.isValid(token);
        if (valid) {
            authenticate(token);
        }

        // 4 - Continua a cadeia de filtros do Spring Security
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        // 3.1 - Tenta retirar de dentro do token o Id de usuário
        // ao qual ele foi vinculado em sua criação
        Long idUsuario = tokenService.getIdUser(token);

        // 3.2 - Verifica se o usuário existe no banco
        // (Caso não exista é sinal de um BUG preocupante)
        Optional<User> possibleUser = repository.findById(idUsuario);

        if (possibleUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado para ID: " + idUsuario);
        }

        User usuario = possibleUser.get();

        // 3.3 - Autentica o usuario da requisição
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7);
    }
}
