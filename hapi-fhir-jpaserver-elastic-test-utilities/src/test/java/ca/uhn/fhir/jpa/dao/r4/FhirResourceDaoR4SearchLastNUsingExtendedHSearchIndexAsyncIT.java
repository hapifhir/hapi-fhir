package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Run entire @see {@link FhirResourceDaoR4SearchLastNAsyncIT} test suite this time
 * using Extended HSearch index as search target
 */
@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNUsingExtendedHSearchIndexAsyncIT extends FhirResourceDaoR4SearchLastNAsyncIT {

	@BeforeEach
	public void enableAdvancedHSearchIndexing() {
		myStorageSettings.setLastNEnabled(true);
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@AfterEach
	public void disableAdvancedHSearchIndex() {
		myStorageSettings.setAdvancedHSearchIndexing(new JpaStorageSettings().isAdvancedHSearchIndexing());
	}

}
