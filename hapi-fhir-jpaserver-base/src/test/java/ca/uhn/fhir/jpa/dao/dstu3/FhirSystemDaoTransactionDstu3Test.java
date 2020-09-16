package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirSystemDaoTransactionDstu3Test extends BaseJpaDstu3SystemTest {
	public static final int TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE = 5;

	@AfterEach
	public void after() {
		myDaoConfig.setMaximumTransactionBundleSize(new DaoConfig().getMaximumTransactionBundleSize());
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setMaximumTransactionBundleSize(TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE);
	}

	private Bundle createInputTransactionWithSize(int theSize) {
		Bundle retval = new Bundle();
		retval.setType(BundleType.TRANSACTION);
		for (int i = 0; i < theSize; ++i) {
			Observation obs = new Observation();
			obs.setStatus(Observation.ObservationStatus.FINAL);
			retval
				.addEntry()
				.setFullUrl("urn:uuid:000" + i)
				.setResource(obs)
				.getRequest()
				.setMethod(HTTPVerb.POST);
		}

		return retval;
	}

	@Test
	public void testTransactionTooBig() {
		Bundle bundle = createInputTransactionWithSize(TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE + 1);

		try {
			mySystemDao.transaction(null, bundle);
			fail();
		} catch (PayloadTooLargeException e) {
			assertThat(e.getMessage(), containsString("Transaction Bundle Too large.  Transaction bundle contains " +
				(TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE + 1) +
				" which exceedes the maximum permitted transaction bundle size of " + TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE));
		}
	}

	@Test
	public void testTransactionSmallEnough() {
		testTransactionBundleSucceedsWithSize(TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE);
		testTransactionBundleSucceedsWithSize(TEST_MAXIMUM_TRANSACTION_BUNDLE_SIZE - 1);
		testTransactionBundleSucceedsWithSize(1);
	}

	private void testTransactionBundleSucceedsWithSize(int theSize) {
		Bundle bundle = createInputTransactionWithSize(theSize);
		Bundle response = mySystemDao.transaction(null, bundle);

		assertEquals(theSize, response.getEntry().size());
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", response.getEntry().get(theSize - 1).getResponse().getStatus());
	}

}
