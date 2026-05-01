package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ESG_PONTOS_COLETA")
@Data
@NoArgsConstructor
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esg_seq_ponto_coleta")
    @SequenceGenerator(name = "esg_seq_ponto_coleta", sequenceName = "esg_seq_ponto_coleta", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String endereco;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "volume_maximo")
    private BigDecimal volumeMaximo;

    @OneToMany(mappedBy = "pontoColeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroColeta> registros;
}