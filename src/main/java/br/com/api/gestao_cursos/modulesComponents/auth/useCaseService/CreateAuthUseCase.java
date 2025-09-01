package br.com.api.gestao_cursos.modulesComponents.auth.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.auth.dto.CreateAuthRequestDto;
import br.com.api.gestao_cursos.modulesComponents.auth.dto.CreateAuthResponseDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CreateAuthUseCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateAuthResponseDto create(CreateAuthRequestDto createAuthRequestDto, RoleUser roleUser) throws UserPrincipalNotFoundException {

        //Verificar se o usuário existe no banco de dados
        Optional<UserEntity> userEntity = userRepository.findByEmail(createAuthRequestDto.getEmail());
        if (userEntity.isEmpty()){
            throw new UsernameNotFoundException("Usuário não existe no sistema!");
        }

        if (!userEntity.get().getRole().equals(roleUser)){
            throw new UsernameNotFoundException("Privilégio do usuário não liberado!");
        }

        //Verificar se o email e a senha conferem:

        //Credenciais do user que existe
        String userExistsPassword = userEntity.get().getPassword();//Como é um Optional, tenho que dar um get() no objeto user
        String userExistsEmail = userEntity.get().getPassword();//Este password está criptografado

        //Credenciais do usuário da autenticação
        String userAuthRequestEmail = createAuthRequestDto.getEmail();
        String userAuthRequestPassword = createAuthRequestDto.getPassword();

        //Verificar se as senhas conferem
        boolean passwordEquals = passwordEncoder.matches(userAuthRequestPassword, userExistsPassword);
        if (!passwordEquals){
            throw new UsernameNotFoundException("Email ou senha não conferem.");
        }

        //Caso sucesso acima, gero o token com o tempo de expiração
        var issueAt = Instant.now();//Início da criação do token
        var expiresIn = issueAt.plus(30, ChronoUnit.MINUTES);
        SecretKey key = Keys.hmacShaKeyFor("secretKeyAapiCursos@*Forte$secretKeyAapiCursos@*Forte".getBytes());

        Map<String, String> claims = new HashMap<>();
        claims.put("roles", roleUser.toString());

        String token = Jwts.builder()
                .subject(userEntity.get().getId().toString())
                .issuer("api_gestao_cursos")
                .issuedAt(Date.from(issueAt))
                .expiration(Date.from(expiresIn))
                .claims(claims)
                .signWith(key)
                .compact();

        return CreateAuthResponseDto.builder()
                        .access_token(token)
                        .created_at(Date.from(issueAt))
                        .expires_in(Date.from(expiresIn))
                        .build();
    }



}
