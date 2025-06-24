package org.bian.exc.carcatalog;

import org.bian.exc.carcatalog.service.CarService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarCatalogServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(CarService carService) {
		return args -> carService.populateInitialData();
	}
}
