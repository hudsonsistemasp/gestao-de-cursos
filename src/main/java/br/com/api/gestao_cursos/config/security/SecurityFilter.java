package br.com.api.gestao_cursos.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SecretKey key = Keys.hmacShaKeyFor("secretKeyAapiCursos@*Forte$secretKeyAapiCursos@*Forte".getBytes());
        String header = request.getHeader("Authorization");

        //Verificar se veio o token na requisição
        if (header != null){
            //Retirar o prefixo que vem com o token
            var token = header.replace("Bearer ", "");
            try{
                //Verificar se o token está expirado
                var resultToken = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
                var claims = resultToken.getPayload().get("roles");
                System.out.println(claims);
                var userId = resultToken.getPayload().getSubject();

                /*Agora preciso pegar as informações do que estão no Spring SecurityContextHolder, que é onde
                 estão as informações dos detalhes de quem está autenticado, permissões do usuário, ou seja,
                 a segurança esteja aplicada à requisição daquele usuário e não misturada com outros milhares.
                 Esse contexto é essencial para que o Spring Security garanta que a segurança esteja aplicada
                 à todas requisições, individualmente. Havendo esta comunicação entre SecurityContext e o
                 SpringSecurity, as permissões poderão ser aplicadas também.*/
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.toString()));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        resultToken.getPayload().getSubject(),null,authorities);
                request.setAttribute("userId", userId);//Está na var acima do resultToken
                SecurityContextHolder.getContext().setAuthentication(auth);//Garantindo a individualidade de cada user àquela requisição

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }


        filterChain.doFilter(request, response);
    }
}
