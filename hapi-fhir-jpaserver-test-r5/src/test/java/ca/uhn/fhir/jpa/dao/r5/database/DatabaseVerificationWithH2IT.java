package ca.uhn.fhir.jpa.dao.r5.database;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.H2.class
})
public class DatabaseVerificationWithH2IT extends BaseDatabaseVerificationIT {

}
