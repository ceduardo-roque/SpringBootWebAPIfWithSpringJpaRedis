package br.com.fiap.persistence.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicação para inicializar a aplicação e subir o servidor com o Spring Boot
 * @author Ayrton Henrique
 *
 */
@SpringBootApplication
@ComponentScan("br.com.fiap")
public class RunApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
	}

}
