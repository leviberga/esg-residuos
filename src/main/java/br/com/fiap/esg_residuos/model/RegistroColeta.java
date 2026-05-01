package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ESG_REGISTROS_COLETA")
@Data
@NoArgsConstructor
public class RegistroColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esg_seq_registro_coleta")
    @SequenceGenerator(name = "esg_seq_registro_coleta", sequenceName = "esg_seq_registro_coleta", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PONTO_COLETA_ID", nullable = false)
    private PontoColeta pontoColeta;

    @Column(name = "volume_coletado", nullable = false)
    private BigDecimal volumeColetado;

    @Column(name = "data_coleta", nullable = false)
    private LocalDateTime dataColeta;

    @PrePersist
    protected void onCreate() {
        dataColeta = LocalDateTime.now();
    }
}