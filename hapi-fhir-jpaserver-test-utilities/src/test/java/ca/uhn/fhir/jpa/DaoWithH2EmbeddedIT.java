package ca.uhn.fhir.jpa;

import ca.uhn.fhir.jpa.dao.IDaoTest;
import ca.uhn.fhir.jpa.embedded.H2EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
	DaoWithH2EmbeddedIT.TestConfig.class
})
public class DaoWithH2EmbeddedIT extends BaseDaoIT implements IDaoTest {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoWithH2EmbeddedIT.class);

	@Override
	public Logger getLogger() {
		return ourLog;
	}

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return new JpaDatabaseContextConfigParamObject(
				new H2EmbeddedDatabase(),
				HapiFhirH2Dialect.class.getName()
			);
		}
	}


}
