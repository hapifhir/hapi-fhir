package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.test.DaoTestDataBuilder;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataBuilderConfig {

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	DaoRegistry myDaoRegistry;

	@Bean
	DaoTestDataBuilder testDataBuilder() {
		return new DaoTestDataBuilder(myFhirContext, myDaoRegistry, new SystemRequestDetails());
	}
}
