package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.config.HapiMigrationConfig;
import ca.uhn.fhir.jpa.migrate.config.TestMigrationConfig;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestMigrationConfig.class, HapiMigrationConfig.class})
public class BaseMigrationTest {
	@Autowired
	protected
	HapiMigrationDao myHapiMigrationDao;

	@AfterEach
	void after() {
		myHapiMigrationDao.deleteAll();
	}

}
