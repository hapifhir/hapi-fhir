package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BulkExportUseCaseTestAnyModeIT extends BulkExportUseCaseIT {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportUseCaseTestAnyModeIT.class);


	@BeforeEach
	public void setup() {
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);
	}

	@AfterEach
	public void tearDown() {
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
	}

}
