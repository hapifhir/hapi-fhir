package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BulkExportUseCaseTestAnyMode extends BulkExportUseCaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportUseCaseTestAnyMode.class);


	@BeforeEach
	public void setup() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
	}

	@AfterEach
	public void tearDown() {
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
	}

}
