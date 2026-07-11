package dev.shoaibsuad.library_management;

import dev.shoaibsuad.library_management.common.service.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

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


