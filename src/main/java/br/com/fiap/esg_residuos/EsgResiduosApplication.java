package br.com.fiap.esg_residuos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.fiap.esg_residuos.repository")
@EntityScan("br.com.fiap.esg_residuos.model")
public class EsgResiduosApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsgResiduosApplication.class, args);
	}

}