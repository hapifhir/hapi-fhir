package ca.uhn.fhir.empi.jpalink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ca.uhn.fhir.empi.jpalink.dao")
public class EmpiConfig {
}
