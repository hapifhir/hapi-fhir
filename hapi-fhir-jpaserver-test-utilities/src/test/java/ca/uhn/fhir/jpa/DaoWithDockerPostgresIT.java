package ca.uhn.fhir.jpa;

import ca.uhn.fhir.jpa.dao.IDaoTest;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static ca.uhn.fhir.jpa.BaseDaoIT.DaoTestSupport;
import static ca.uhn.fhir.jpa.BaseDaoIT.JpaDatabaseContextConfigParamObject;
import static ca.uhn.fhir.jpa.BaseDaoIT.JpaDatabaseContextConfigParamObject.of;
@ContextConfiguration(classes = {
	DaoWithDockerPostgresIT.PostgresTailoredConfigForDaoTest.class, BaseDaoIT.BaseConfigForDaoTest.class
})
public class DaoWithDockerPostgresIT implements IDaoTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoWithDockerPostgresIT.class);

	@Autowired
	private DaoTestSupport myDaoTestSupport;
	@Override
	public DaoTestSupport getSupport() {
		return myDaoTestSupport;
	}

	@Configuration
	public static class PostgresTailoredConfigForDaoTest {

		@Bean
		public DaoTestSupport daoTestSupport(){
			return new DaoTestSupport(ourLog);
		}

		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return of(new PostgresEmbeddedDatabase(), HapiFhirPostgresDialect.class.getName());
		}
	}


}
