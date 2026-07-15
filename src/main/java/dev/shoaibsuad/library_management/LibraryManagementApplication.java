package dev.shoaibsuad.library_management;

import dev.shoaibsuad.library_management.common.service.TestService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;


@OpenAPIDefinition(
		info = @Info(
				title = "Library Management API",
				version = "v1.0.0",
				description = "REST API for Library Management System",
				contact = @Contact(
						name = "Saiful Islam",
						email = "support.shoaibsuad@gmail.com"
				)
		)
)

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
public class LibraryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}
    	@Bean
        CommandLineRunner runner(TestService testService) {
		return args -> testService.printInfo();
	}

}


