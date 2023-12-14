package ca.uhn.fhir.jpa;

import ca.uhn.fhir.jpa.dao.IDaoTest;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DaoWithPostgresIT.TestConfig.class
})
public class DaoWithPostgresIT extends BaseDaoIT implements IDaoTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoWithPostgresIT.class);

	@Override
	public Logger getLogger() {
		return ourLog;
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return new JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}


}
