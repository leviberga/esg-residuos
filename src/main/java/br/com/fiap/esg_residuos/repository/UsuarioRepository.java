package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método necessário para o Spring Security encontrar o usuário pelo email/username
    Optional<Usuario> findByEmail(String email);
}