package ca.uhn.fhir.jpa;

import ca.uhn.fhir.jpa.dao.IDaoTest;
import ca.uhn.fhir.jpa.embedded.H2EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ca.uhn.fhir.jpa.BaseDaoIT.DaoTestSupport;
import static ca.uhn.fhir.jpa.BaseDaoIT.JpaDatabaseContextConfigParamObject;
import static ca.uhn.fhir.jpa.BaseDaoIT.JpaDatabaseContextConfigParamObject.of;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	DaoWithH2EmbeddedIT.H2EmbeddedTailoredConfigForDaoTest.class, BaseDaoIT.BaseConfigForDaoTest.class})
public class DaoWithH2EmbeddedIT implements IDaoTest {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoWithH2EmbeddedIT.class);

	@Autowired
	public DaoTestSupport myDaoTestSupport;

	@Override
	public DaoTestSupport getSupport() {
		return myDaoTestSupport;
	}

	@Configuration
	public static class H2EmbeddedTailoredConfigForDaoTest {

		@Bean
		public DaoTestSupport daoTestSupport(){
			return new DaoTestSupport(ourLog);
		}

		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return of(new H2EmbeddedDatabase(),	HapiFhirH2Dialect.class.getName());
		}
	}


}
