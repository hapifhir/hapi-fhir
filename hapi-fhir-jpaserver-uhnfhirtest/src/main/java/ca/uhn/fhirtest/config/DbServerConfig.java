package ca.uhn.fhirtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbServerConfig {

	@Bean
	public String dbServer() {
		return "";
		// For mysql
		// return new ca.uhn.fhirtest.MySqlServer();
	}
	
	
}
