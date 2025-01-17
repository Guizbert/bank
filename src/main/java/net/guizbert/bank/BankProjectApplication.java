package net.guizbert.bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//http://localhost:8080/swagger-ui/index.html

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank project by 'Guizbert'",
				description = "Backend Rest APIs Spring boot project",
				version = "v1.0",
				contact = @Contact(
						name = "Albérich Ravier",
						email = "albe.ravier@hotmail.fr",
						url = "https://github.com/guizbert"
				),
				license = @License(
						name = "Guizbert",
						url = "https://github.com/guizbert"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Learning project - Bank 'app' using Spring boot",
				url = "https://github.com/guizbert"
		)
)
public class BankProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankProjectApplication.class, args);
	}

}
