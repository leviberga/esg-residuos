package br.com.fiap.esg_residuos.repository;


import br.com.fiap.esg_residuos.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    // Métodos de filtro que serão usados nos endpoints (Dia 2)
    List<PontoColeta> findByCidade(String cidade);

    List<PontoColeta> findByTipo(String tipo);

    List<PontoColeta> findByCidadeAndTipo(String cidade, String tipo);
}