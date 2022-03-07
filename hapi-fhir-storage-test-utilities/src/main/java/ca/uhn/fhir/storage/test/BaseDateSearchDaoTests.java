package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.conformance.DateSearchTestCase;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Run the tests defined in {@link DateSearchTestCase} in a DAO test as a @Nested suite.
 */
public abstract class BaseDateSearchDaoTests {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDateSearchDaoTests.class);

	/**
	 * Id of test Observation
	 */
	IIdType myObservationId;

	/**
	 * Test for our date search operators.
	 * <p>
	 * Be careful - date searching is defined by set relations over intervals, not a simple number comparison.
	 * See http://hl7.org/fhir/search.html#prefix for details.
	 * <p>
	 * To debug, uncomment the @CsvSource line and comment @MethodSource to run a single case
	 *
	 *
	 * @param theResourceDate  the date to use as Observation effective date
	 * @param theQuery         the query parameter value including prefix (e.g. eq2020-01-01)
	 * @param theExpectedMatch true if theQuery should match theResourceDate.
	 * @param theFileName      source file for test case
	 * @param theLineNumber    source file line number for test case (-1 for inline tests)
	 */
	@ParameterizedTest
	// use @CsvSource to debug individual cases.
	//@CsvSource("2019-12-31T08:00:00,eq2020,false,inline,1")
	@MethodSource("dateSearchCases")
	public void testDateSearchMatching(String theResourceDate, String theQuery, boolean theExpectedMatch, String theFileName, int theLineNumber) {
		Fixture fixture = getFixture();
		if (isShouldSkip(theResourceDate, theQuery)) {
			return;
		}
		// setup
		myObservationId = fixture.createObservationWithEffectiveDate(theResourceDate);

		// run the query
		boolean matched = fixture.isObservationSearchMatch(theQuery, myObservationId);

		assertExpectedMatch(theResourceDate, theQuery, theExpectedMatch, matched, theFileName, theLineNumber);
	}


	protected boolean isShouldSkip(String theResourceDate, String theQuery) {
		return false;
	}

	protected static void assertExpectedMatch(String theResourceDate, String theQuery, boolean theExpectedMatch, boolean matched, String theFileName, int theLineNumber) {
		String message =
			"Expected " + theQuery + " to " +
				(theExpectedMatch ? "" : "not ") + "match " + theResourceDate +
				" (" + theFileName + ":" + theLineNumber + ")"; // wrap this in () so tools recognize the line reference.
		assertEquals(theExpectedMatch, matched, message);
	}


	/**
	 * Turn the cases into expanded arguments for better reporting output and debugging
	 */
	public static List<Arguments> dateSearchCases() {
		return DateSearchTestCase.ourCases.stream()
			.map(DateSearchTestCase::toArguments)
			.collect(Collectors.toList());
	}

	/**
	 * Helper to provide local setup and query services.
	 *
	 * Use an abstract method instead of a constructor because JUnit has a such a funky lifecycle.
	 */
	protected abstract Fixture getFixture();

	public interface Fixture {
		/**
		 * Create an observation and save it
		 */
		IIdType createObservationWithEffectiveDate(String theResourceDate);

		/**
		 * Does date=theQuery match theObservationId created
		 */
		boolean isObservationSearchMatch(String theQuery, IIdType theObservationId);

	}

	public static class TestDataBuilderFixture<O extends IBaseResource> implements Fixture {
		final ITestDataBuilder myTestDataBuilder;
		final IFhirResourceDao<O> myObservationDao;

		public TestDataBuilderFixture(ITestDataBuilder theTestDataBuilder, IFhirResourceDao<O> theObservationDao) {
			myTestDataBuilder = theTestDataBuilder;
			myObservationDao = theObservationDao;
		}

		@Override
		public IIdType createObservationWithEffectiveDate(String theResourceDate) {
			return myTestDataBuilder.createObservation(myTestDataBuilder.withEffectiveDate(theResourceDate));
		}

		@Override
		public boolean isObservationSearchMatch(String theQuery, IIdType theObservationId) {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add("date", new DateParam(theQuery));
			ourLog.info("Searching for observation {}", map);

			IBundleProvider results = myObservationDao.search(map);

			boolean matched = results.getAllResourceIds().contains(theObservationId.getIdPart());
			return matched;
		}
	}
}
