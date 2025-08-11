package br.com.api.gestao_cursos.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    private static final String[] PERMIT_ALL= {"/h2-console/**","/auth/**","/users/**"};

    //Método do Spring Security para personalizarmos algumas políticas de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        //csrf é uma das políticas de segurança. Aqui eu desabilito o método de autenticação geral das páginas, pois vou personalizar as que quero
        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // exibe H2 Console via iframe
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(PERMIT_ALL).permitAll() // libera acesso ao H2 Console
                            //.anyRequest().permitAll() // todas as requisições são liberadas temporariamente
                            .anyRequest().authenticated();//Aqui já exige o token para autenticação
                })
                .addFilterBefore(securityFilter, BasicAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
