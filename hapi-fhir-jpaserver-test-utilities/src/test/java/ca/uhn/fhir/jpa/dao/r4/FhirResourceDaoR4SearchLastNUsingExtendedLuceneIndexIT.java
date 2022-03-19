package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Run entire @see {@link FhirResourceDaoR4SearchLastNIT} test suite this time
 * using Extended Lucene index as search target
 */
@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNUsingExtendedLuceneIndexIT extends FhirResourceDaoR4SearchLastNIT {

	@BeforeEach
	public void enableAdvancedLuceneIndexing() {
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@AfterEach
	public void disableAdvancedLuceneIndex() {
		myDaoConfig.setAdvancedLuceneIndexing(new DaoConfig().isAdvancedLuceneIndexing());
	}

}
