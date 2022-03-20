package ca.uhn.fhir.storage.test;

/*-
 * #%L
 * hapi-fhir-storage-test-utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.conformance.DateSearchTestCase;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
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

	Fixture myFixture;

	//time zone set to EST
	@BeforeEach
	public void setTimeZoneEST() {
		TimeZone.setDefault(TimeZone.getTimeZone("EST"));
	}

	//reset time zone back to match the system
	@AfterEach
	public void resetTimeZone() {
		TimeZone.setDefault(null);
	}

	@BeforeEach
	public void setupFixture() {
		myFixture = constructFixture();
	}
	@AfterEach
	public void cleanup() {
		myFixture.cleanup();
	}

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
		if (isShouldSkip(theResourceDate, theQuery)) {
			return;
		}
		// setup
		myObservationId = myFixture.createObservationWithEffectiveDate(theResourceDate);

		// run the query
		boolean matched = myFixture.isObservationSearchMatch(theQuery, myObservationId);

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
	protected abstract Fixture constructFixture();

	public interface Fixture {
		/**
		 * Create an observation and save it
		 */
		IIdType createObservationWithEffectiveDate(String theResourceDate);

		/**
		 * Does date=theQuery match theObservationId created
		 */
		boolean isObservationSearchMatch(String theQuery, IIdType theObservationId);

		void cleanup();
	}

	public static class TestDataBuilderFixture<O extends IBaseResource> implements Fixture {
		final ITestDataBuilder myTestDataBuilder;
		final IFhirResourceDao<O> myObservationDao;
		final Set<IIdType> myCreatedIds = new HashSet<>();

		public TestDataBuilderFixture(ITestDataBuilder theTestDataBuilder, IFhirResourceDao<O> theObservationDao) {
			myTestDataBuilder = theTestDataBuilder;
			myObservationDao = theObservationDao;
		}

		@Override
		public IIdType createObservationWithEffectiveDate(String theResourceDate) {
			IIdType id = myTestDataBuilder.createObservation(myTestDataBuilder.withEffectiveDate(theResourceDate));
			myCreatedIds.add(id);
			return id;
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

		@Override
		public void cleanup() {
			myCreatedIds.forEach(myObservationDao::delete);
			myCreatedIds.clear();
		}
	}
}
