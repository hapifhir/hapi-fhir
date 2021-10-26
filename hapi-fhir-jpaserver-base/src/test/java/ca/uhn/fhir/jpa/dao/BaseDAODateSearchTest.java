package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.conformance.DateSearchTestCase;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionCallback;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Run the tests defined in {@link DateSearchTestCase} in a DAO test as a @Nested suite.
 */
public abstract class BaseDAODateSearchTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDAODateSearchTest.class);

	/** Id of test Observation */
	IIdType myObservationId;

	/**
	 * Test for our date search operators.
	 * <p>
	 * Be careful - date searching is defined by set relations over intervals, not a simple number comparison.
	 * See http://hl7.org/fhir/search.html#prefix for details.
	 * <p>
	 *
	 * @param theResourceDate  the date to use as Observation effective date
	 * @param theQuery         the query parameter value including prefix (e.g. eq2020-01-01)
	 * @param theExpectedMatch true if tdheQuery should match theResourceDate.
	 */
	@ParameterizedTest
	// use @CsvSource to debug individual cases.
	//@CsvSource("2021-01-01,eq2020-01-02,false")
	@MethodSource("dateSearchCases")
	public void testDateSearchMatching(String theResourceDate, String theQuery, Boolean theExpectedMatch, String theFileName, int theLineNumber) {
		// setup
		createObservationWithEffectiveDate(theResourceDate);

		// run the query
		boolean matched = isSearchMatch(theQuery);

		String message =
			"Expected " + theQuery + " to " +
				(theExpectedMatch ? "" : "not ") + "match " + theResourceDate +
			" (" + theFileName + ":" + theLineNumber + ")"; // wrap this in () so tools recognize the line reference.
		assertEquals(theExpectedMatch, matched, message);
	}

	// we need these from the test container
	abstract protected FhirContext getMyFhirCtx();
	abstract protected <T> T doInTransaction(TransactionCallback<T> daoMethodOutcomeTransactionCallback);
	abstract protected <T extends IBaseResource> IFhirResourceDao<T> getObservationDao();

	protected void createObservationWithEffectiveDate(String theResourceDate) {
		IBaseResource obs = getMyFhirCtx().getResourceDefinition("Observation").newInstance();
		FhirTerser fhirTerser = getMyFhirCtx().newTerser();
		fhirTerser.addElement(obs, "effectiveDateTime", theResourceDate);
		ourLog.info("obs {}", getMyFhirCtx().newJsonParser().encodeResourceToString(obs));

		DaoMethodOutcome createOutcome = doInTransaction(s -> getObservationDao().create(obs));
		myObservationId = createOutcome.getId();
	}

	/**
	 * Does the query string match the observation created during setup?
	 */
	protected boolean isSearchMatch(String theQuery) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Observation.SP_DATE, new DateParam(theQuery));
		ourLog.info("Searching for observation {}", map);

		IBundleProvider results = getObservationDao().search(map);

		boolean matched = results.getAllResourceIds().contains(myObservationId.getIdPart());
		return matched;
	}

	static List<Arguments> dateSearchCases() {
		return DateSearchTestCase.ourCases.stream()
			.map(DateSearchTestCase::toArguments)
			.collect(Collectors.toList());
	}

}
