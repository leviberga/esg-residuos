package br.com.fiap.esg_residuos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // <-- IMPORTE ISSO
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <-- IMPORTE ISSO

@SpringBootApplication
@EnableJpaRepositories("br.com.fiap.esg_residuos.repository") // <-- ADICIONE ESTA LINHA
@EntityScan("br.com.fiap.esg_residuos.model") // <-- ADICIONE ESTA LINHA
public class EsgResiduosApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsgResiduosApplication.class, args);
	}

}