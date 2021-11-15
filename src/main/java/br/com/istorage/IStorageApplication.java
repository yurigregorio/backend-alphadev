package br.com.istorage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IStorageApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(IStorageApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
	}

}
