
package br.com.fiap.esg_residuos.repository;

import br.com.fiap.esg_residuos.model.RegistroColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroColetaRepository extends JpaRepository<RegistroColeta, Long> {
    // DPS Podemos adicionar consultas customizadas aqui no futuro
    // Ex: para os relatórios de alerta
}