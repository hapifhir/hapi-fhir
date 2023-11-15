package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IHSearchEventListener;
import ca.uhn.fhir.jpa.test.util.TestHSearchEventDispatcher;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * Run entire @see {@link FhirResourceDaoR4SearchLastNIT} test suite this time
 * using Extended HSearch index as search target.
 *
 * The other implementation is obsolete, and we can merge these someday.
 */
@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNUsingExtendedHSearchIndexIT extends FhirResourceDaoR4SearchLastNIT {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchLastNUsingExtendedHSearchIndexIT.class);

	@Autowired
	private TestHSearchEventDispatcher myHSearchEventDispatcher;

	@Mock
	private IHSearchEventListener mySearchEventListener;


	@BeforeEach
	public void enableAdvancedHSearchIndexing() {
		myStorageSettings.setLastNEnabled(true);
		myStorageSettings.setAdvancedHSearchIndexing(true);
		myHSearchEventDispatcher.register(mySearchEventListener);
		ourLog.info("enableAdvancedHSearchIndexing finished.  lastn {} advancedHSearchIndexing {}", myStorageSettings.isLastNEnabled(), myStorageSettings.isAdvancedHSearchIndexing());

	}

	@AfterEach
	public void disableAdvancedHSearchIndex() {
		myStorageSettings.setAdvancedHSearchIndexing(new JpaStorageSettings().isAdvancedHSearchIndexing());
	}

	/**
	 * We pull the resources from Hibernate Search when LastN uses Hibernate Search
	 * Override the test verification to validate only one search was performed
	 */
	@Override
	void verifyResourcesLoadedFromElastic(List<IIdType> theObservationIds, List<String> theResults) {
		Mockito.verify(mySearchEventListener, Mockito.times(1))
			.hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
	}

}
