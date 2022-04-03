package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Run entire @see {@link FhirResourceDaoR4SearchLastNIT} test suite this time
 * using Extended Lucene index as search target.
 *
 * The other implementation is obsolete, and we can merge these someday.
 */
@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNUsingExtendedLuceneIndexIT extends FhirResourceDaoR4SearchLastNIT {
	// awkward override so we can spy
	@SpyBean
	@Autowired(required = false)
	IFulltextSearchSvc myFulltestSearchSvc;

	@BeforeEach
	public void enableAdvancedLuceneIndexing() {
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@AfterEach
	public void disableAdvancedLuceneIndex() {
		myDaoConfig.setAdvancedLuceneIndexing(new DaoConfig().isAdvancedLuceneIndexing());
	}

	/**
	 * We pull the resources from Hibernate Search when LastN uses Hibernate Search.
	 * Override the test verification
	 */
	@Override
	void verifyResourcesLoadedFromElastic(List<IIdType> theObservationIds, List<String> theResults) {
		List<Long> expectedArgumentPids =
			theObservationIds.stream().map(IIdType::getIdPartAsLong).collect(Collectors.toList());

		ArgumentCaptor<List<Long>> actualPids = ArgumentCaptor.forClass(List.class);

		verify(myFulltestSearchSvc, times(1)).getResources(actualPids.capture());
		assertThat(actualPids.getValue(), is(expectedArgumentPids));

		// we don't include the type in the id returned from Hibernate Search for now.
		List<String> expectedObservationList = theObservationIds.stream()
			.map(id -> id.toUnqualifiedVersionless().getIdPart()).collect(Collectors.toList());
		assertEquals(expectedObservationList, theResults);

	}

}
