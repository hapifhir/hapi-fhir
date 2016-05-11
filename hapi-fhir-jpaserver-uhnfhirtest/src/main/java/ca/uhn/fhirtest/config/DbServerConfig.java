package ca.uhn.fhirtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhirtest.DerbyNetworkServer;

@Configuration
public class DbServerConfig {

	@Bean
	public DerbyNetworkServer dbServer() {
		return new DerbyNetworkServer();
		// For mysql
		// return new ca.uhn.fhirtest.MySqlServer();
	}
	
	
}
