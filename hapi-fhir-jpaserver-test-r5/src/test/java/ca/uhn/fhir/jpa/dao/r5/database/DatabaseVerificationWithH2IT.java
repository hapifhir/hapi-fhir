package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.H2EmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DatabaseVerificationWithH2IT.TestConfig.class
})
public class DatabaseVerificationWithH2IT extends BaseDatabaseVerificationIT {

	@Override
	protected DriverTypeEnum getDriverType() {
		return DriverTypeEnum.H2_EMBEDDED;
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new H2EmbeddedDatabase(),
				H2Dialect.class.getName()
			);
		}
	}


}