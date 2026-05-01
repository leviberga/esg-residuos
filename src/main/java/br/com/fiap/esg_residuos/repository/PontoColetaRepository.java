package br.com.fiap.esg_residuos.repository;


import br.com.fiap.esg_residuos.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    List<PontoColeta> findByCidade(String cidade);

    List<PontoColeta> findByTipo(String tipo);

    List<PontoColeta> findByCidadeAndTipo(String cidade, String tipo);

    // Mova para cá, dentro da interface:
    @Query("SELECT p FROM PontoColeta p LEFT JOIN FETCH p.registros")
    List<PontoColeta> findAllWithRegistros();
}