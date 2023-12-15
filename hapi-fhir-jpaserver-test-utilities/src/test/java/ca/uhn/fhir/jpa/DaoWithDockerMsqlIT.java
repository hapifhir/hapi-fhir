package ca.uhn.fhir.jpa;

import ca.uhn.fhir.jpa.dao.IDaoTest;
import ca.uhn.fhir.jpa.embedded.MsSqlEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ca.uhn.fhir.jpa.BaseDaoIT.JpaDatabaseContextConfigParamObject.of;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	DaoWithDockerMsqlIT.MsqlTailoredConfigForDaoTest.class, BaseDaoIT.BaseConfigForDaoTest.class})
public class DaoWithDockerMsqlIT
	implements IDaoTest {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(DaoWithDockerMsqlIT.class);

	@Autowired
	BaseDaoIT.DaoTestSupport mySupport;

	@Override
	public BaseDaoIT.DaoTestSupport getSupport() {
		return mySupport;
	}

	@Configuration
	public static class MsqlTailoredConfigForDaoTest {
		@Bean
		BaseDaoIT.DaoTestSupport support(){
			return new BaseDaoIT.DaoTestSupport(ourLog);
		}

		@Bean
		public BaseDaoIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return of( new MsSqlEmbeddedDatabase(), HapiFhirSQLServerDialect.class.getName()	);
		}
	}


}
