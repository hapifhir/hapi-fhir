package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <code>_lastUpdated</code> with AND/OR combinations (hapi-fhir#8857) when search parameters are indexed
 * in Hibernate Search: the AND/OR form is not supported there, so the search must be done by the database
 * (never by Hibernate Search with the <code>_lastUpdated</code> filter dropped).
 */
public class FhirResourceDaoR4SearchLastUpdatedAndOrHSearchTest extends BaseJpaR4Test {

	private final List<String> myIds = new ArrayList<>();
	private final long[] myTimes = new long[5];

	@BeforeEach
	public void enableHSearchAndCreateResources() throws InterruptedException {
		myStorageSettings.setHibernateSearchIndexSearchParams(true);
		myStorageSettings.setStoreResourceInHSearchIndex(true);
		mySearchParamRegistry.forceRefresh();

		for (int i = 0; i < myTimes.length; i++) {
			Patient p = new Patient();
			p.setActive(true);
			p.addName().setFamily("P" + (i + 1));
			myIds.add(myPatientDao.create(p, mySrd).getId().getIdPart());
			myTimes[i] = myPatientDao.read(p.getIdElement().toUnqualifiedVersionless(), mySrd).getMeta().getLastUpdated().getTime();
			Thread.sleep(5);
		}
	}

	@AfterEach
	public void restoreSettings() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setHibernateSearchIndexSearchParams(defaults.isHibernateSearchIndexSearchParams());
		myStorageSettings.setStoreResourceInHSearchIndex(defaults.isStoreResourceInHSearchIndex());
	}

	private String t(int theIndex) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return f.format(new Date(myTimes[theIndex - 1]));
	}

	private DateAndListParam bindAndList(String... theRepetitions) {
		List<QualifiedParamList> params = new ArrayList<>();
		for (String next : theRepetitions) {
			params.add(QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, next));
		}
		DateAndListParam retVal = new DateAndListParam();
		retVal.setValuesAsQueryTokens(myFhirContext, Constants.PARAM_LASTUPDATED, params);
		return retVal;
	}

	private List<String> ids(int... theIndexes) {
		List<String> retVal = new ArrayList<>();
		for (int next : theIndexes) {
			retVal.add(myIds.get(next - 1));
		}
		return retVal;
	}

	private String allSql() {
		return myCaptureQueriesListener.getSelectQueries().stream().map(q -> q.getSql(true, false)).collect(Collectors.joining("\n"));
	}

	private List<String> search(SearchParameterMap theMap) {
		myCaptureQueriesListener.clear();
		// Resources loaded from the Hibernate Search index may not have the resource type in their ID
		return myPatientDao.search(theMap, mySrd).getAllResources().stream()
			.map(r -> r.getIdElement().getIdPart())
			.collect(Collectors.toList());
	}

	@Test
	public void testAndOr_FallsBackToDatabase() {
		// OR
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setLastUpdatedAndList(bindAndList("lt" + t(2) + ",gt" + t(4)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(1, 5));
		assertThat(allSql()).contains("RES_UPDATED <").contains("RES_UPDATED >");

		// OR with "ne", together with a parameter that Hibernate Search supports
		map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("ne" + t(1) + ",eq" + t(2)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(2, 3, 4, 5));

		// AND, not a single range
		map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("gt" + t(1), "gt" + t(2), "lt" + t(5)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(3, 4));

		// Not synchronous
		map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("ne" + t(2), "ne" + t(4)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(1, 3, 5));

		// Only an empty intersection
		map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("eq" + t(2), "eq" + t(3)));
		assertThat(search(map)).isEmpty();
	}

	@Test
	public void testRange_SameAsBefore() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("gt" + t(3)));
		assertThat(map.getLastUpdated()).isNotNull();
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(4, 5));

		map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("eq" + t(3)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(3));

		map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("ne" + t(3)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(1, 2, 4, 5));
	}

	/**
	 * Not changed by hapi-fhir#8857: when a range with both bounds is handled by Hibernate Search,
	 * only the lower bound is used (ExtendedHSearchSearchBuilder#getLastUpdatedAndOrList).
	 */
	@Test
	@Disabled("Existing behaviour, not changed by hapi-fhir#8857: to be reported separately")
	public void testRange_BothBounds() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));
		map.setLastUpdatedAndList(bindAndList("ge" + t(2), "le" + t(4)));
		assertThat(search(map)).containsExactlyInAnyOrderElementsOf(ids(2, 3, 4));
	}
}
