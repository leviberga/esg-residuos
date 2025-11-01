package br.com.fiap.esg_residuos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Define um "codificador" de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Cria usuários em memória (para teste)
    @Bean
    public UserDetailsService userDetailsService() {
        // Cria um usuário "admin" com a senha "admin" (codificada)
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin")) // Senha é "admin"
                .roles("ADMIN")
                .build();

        // Cria um usuário "user" com a senha "user" (codificada)
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user")) // Senha é "user"
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // 3. Define as regras de segurança da API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita o CSRF (necessário para APIs stateless)
                .csrf(csrf -> csrf.disable())

                // Define a política de sessão como STATELESS (API REST não usa sessão)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define as regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        // Regras de ADMIN (só ADMIN pode modificar dados)
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        // Regras de USER (ADMIN e USER podem consultar)
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER")

                        // Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated()
                )

                // Ativa a autenticação HTTP Basic
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}