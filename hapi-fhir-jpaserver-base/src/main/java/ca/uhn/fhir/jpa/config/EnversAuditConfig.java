package ca.uhn.fhir.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class EnversAuditConfig {
	private final EntityManagerFactory myEntityManagerFactory;

	public EnversAuditConfig(EntityManagerFactory entityManagerFactory) {
		this.myEntityManagerFactory = entityManagerFactory;
	}

	// TODO:  uncomment this after merging
//	@Bean
//	AuditReader auditReader() {
//		return AuditReaderFactory.get(myEntityManagerFactory.createEntityManager());
//	}

}
