package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.api.server.SearchCacheStatus;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * <code>_lastUpdated</code> with AND/OR combinations (hapi-fhir#8857), at the DAO level
 */
public class FhirResourceDaoR4SearchLastUpdatedAndOrTest extends BaseJpaR4Test {

	private final List<String> myIds = new ArrayList<>();
	private final long[] myTimes = new long[5];

	@Autowired
	private IBatch2DaoSvc myBatch2DaoSvc;

	@BeforeEach
	public void createResources() throws InterruptedException {
		for (int i = 0; i < myTimes.length; i++) {
			Patient p = new Patient();
			p.setActive(true);
			p.addName().setFamily("P" + (i + 1));
			String id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless().getValue();
			myIds.add(id);
			myTimes[i] = myPatientDao.read(p.getIdElement().toUnqualifiedVersionless(), mySrd).getMeta().getLastUpdated().getTime();
			if (i > 0) {
				assertThat(myTimes[i]).isGreaterThan(myTimes[i - 1]);
			}
			Thread.sleep(5);
		}
	}

	/**
	 * Formats T1..T5 (1-based) with millisecond precision
	 */
	private String t(int theIndex) {
		return format(myTimes[theIndex - 1]);
	}

	private static String format(long theMillis) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return f.format(new Date(theMillis));
	}

	private static List<QualifiedParamList> toQualifiedParamLists(String... theRepetitions) {
		List<QualifiedParamList> retVal = new ArrayList<>();
		for (String next : theRepetitions) {
			retVal.add(QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, next));
		}
		return retVal;
	}

	private DateRangeParam bindRange(String... theRepetitions) {
		DateRangeParam retVal = new DateRangeParam();
		retVal.setValuesAsQueryTokens(myFhirContext, Constants.PARAM_LASTUPDATED, toQualifiedParamLists(theRepetitions));
		return retVal;
	}

	private DateAndListParam bindAndList(String... theRepetitions) {
		DateAndListParam retVal = new DateAndListParam();
		retVal.setValuesAsQueryTokens(myFhirContext, Constants.PARAM_LASTUPDATED, toQualifiedParamLists(theRepetitions));
		return retVal;
	}

	private List<String> searchIds(SearchParameterMap theMap) {
		IBundleProvider outcome = myPatientDao.search(theMap, mySrd);
		return toUnqualifiedVersionlessIdValues(outcome);
	}

	private List<String> ids(int... theIndexes) {
		List<String> retVal = new ArrayList<>();
		for (int next : theIndexes) {
			retVal.add(myIds.get(next - 1));
		}
		return retVal;
	}

	/**
	 * FHIR R4 prefix semantics for an instant (lastUpdated) against a search value with millisecond precision
	 */
	private static LongPredicate spec(String thePrefix, long theValue) {
		return switch (thePrefix) {
			case "eq" -> t -> t == theValue;
			case "ne" -> t -> t != theValue;
			case "gt", "sa" -> t -> t > theValue;
			case "lt", "eb" -> t -> t < theValue;
			case "ge" -> t -> t >= theValue;
			case "le" -> t -> t <= theValue;
			default -> throw new IllegalArgumentException(thePrefix);
		};
	}

	/**
	 * @param theAndOr AND groups of OR alternatives, each "[prefix][index of T]" (e.g. "ne2")
	 */
	private List<String> expected(String[]... theAndOr) {
		TreeSet<String> retVal = new TreeSet<>();
		for (int i = 0; i < myTimes.length; i++) {
			boolean all = true;
			for (String[] nextOr : theAndOr) {
				boolean any = false;
				for (String next : nextOr) {
					any |= spec(next.substring(0, 2), myTimes[Integer.parseInt(next.substring(2)) - 1]).test(myTimes[i]);
				}
				all &= any;
			}
			if (all) {
				retVal.add(myIds.get(i));
			}
		}
		return new ArrayList<>(retVal);
	}

	private String[] toQuery(String[]... theAndOr) {
		String[] retVal = new String[theAndOr.length];
		for (int i = 0; i < theAndOr.length; i++) {
			List<String> values = new ArrayList<>();
			for (String next : theAndOr[i]) {
				values.add(next.substring(0, 2) + t(Integer.parseInt(next.substring(2))));
			}
			retVal[i] = String.join(",", values);
		}
		return retVal;
	}

	private void assertAndOrSearch(String[]... theAndOr) {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setLastUpdatedAndList(bindAndList(toQuery(theAndOr)));
		List<String> expected = expected(theAndOr);
		assertThat(searchIds(map)).as(map.toNormalizedQueryString(myFhirContext)).containsExactlyInAnyOrderElementsOf(expected);
	}

	private static String[] or(String... theValues) {
		return theValues;
	}

	@Test
	public void testSearch_AndOrCombinations() {
		// OR
		assertAndOrSearch(or("lt2", "gt4"));
		assertAndOrSearch(or("gt4", "lt2"));
		assertAndOrSearch(or("le2", "ge4"));
		assertAndOrSearch(or("eq1", "eq3", "eq5"));
		assertAndOrSearch(or("ne1", "eq2"));
		assertAndOrSearch(or("ne2", "ne4"));
		assertAndOrSearch(or("eb2", "sa4"));
		// AND, not representable as a single range
		assertAndOrSearch(or("gt1"), or("gt2"));
		assertAndOrSearch(or("lt5"), or("lt4"));
		assertAndOrSearch(or("ge2"), or("le4"), or("ne3"));
		assertAndOrSearch(or("ne1"), or("ne3"), or("ne5"));
		assertAndOrSearch(or("eq2"), or("ge2"));
		// AND + OR
		assertAndOrSearch(or("lt2", "gt3"), or("lt4", "gt5"));
		assertAndOrSearch(or("ne2", "eq3"), or("ge2"));
		assertAndOrSearch(or("eq1", "eq2", "eq3"), or("eq3", "eq4", "eq5"));
		// Empty intersection
		assertAndOrSearch(or("lt2", "lt1"), or("gt4"));
		assertAndOrSearch(or("eq1"), or("eq2"));
	}

	/**
	 * Same as {@link #testSearch_AndOrCombinations()} but through the generic parameter map, as used by
	 * HFQL and by callers that add <code>_lastUpdated</code> with {@link SearchParameterMap#add(String, ca.uhn.fhir.model.api.IQueryParameterOr)}
	 */
	@Test
	public void testSearch_GenericParameterMap_OrKeepsEveryPrefix() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_LASTUPDATED, new DateOrListParam()
			.addOr(new DateParam("ne" + t(1)))
			.addOr(new DateParam("eq" + t(2))));
		assertThat(searchIds(map)).containsExactlyInAnyOrderElementsOf(expected(or("ne1", "eq2")));

		map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_LASTUPDATED, new DateOrListParam()
			.addOr(new DateParam("ne" + t(2)))
			.addOr(new DateParam("ne" + t(4))));
		assertThat(searchIds(map)).containsExactlyInAnyOrderElementsOf(expected(or("ne2", "ne4")));

		map = SearchParameterMap.newSynchronous();
		map.add(Constants.PARAM_LASTUPDATED, new DateOrListParam()
			.addOr(new DateParam("lt" + t(2)))
			.addOr(new DateParam("gt" + t(4))));
		map.add(Constants.PARAM_LASTUPDATED, new DateParam("ne" + t(5)));
		assertThat(searchIds(map)).containsExactlyInAnyOrderElementsOf(expected(or("lt2", "gt4"), or("ne5")));
	}

	/**
	 * Every <code>_lastUpdated</code> that worked before (a single range) gives the same range, the same
	 * SQL and the same results.
	 */
	@Test
	public void testSearch_RepresentableAsRange_SameRangeSqlAndResultsAsBefore() {
		String t3 = t(3);
		List<String[]> queries = List.of(
			new String[]{t3},
			new String[]{"eq" + t3},
			new String[]{"ne" + t3},
			new String[]{"gt" + t3},
			new String[]{"ge" + t3},
			new String[]{"lt" + t3},
			new String[]{"le" + t3},
			new String[]{"sa" + t3},
			new String[]{"eb" + t3},
			new String[]{"gt" + t(1), "lt" + t(5)},
			new String[]{"lt" + t(5), "gt" + t(1)},
			new String[]{"ge" + t(2), "le" + t(4)},
			new String[]{"sa" + t(1), "eb" + t(4)},
			// Inverted bounds: empty result
			new String[]{"lt" + t(2), "gt" + t(4)},
			// Precisions
			new String[]{t3.substring(0, 4)},
			new String[]{"ne" + t3.substring(0, 4)},
			new String[]{t3.substring(0, 7)},
			new String[]{"ge" + t3.substring(0, 10)},
			new String[]{"lt" + t3.substring(0, 10)},
			new String[]{t3.substring(0, 19) + "Z"},
			new String[]{"ge" + t3.substring(0, 19) + "Z"},
			new String[]{"gt" + t3.substring(0, 19) + "+00:00"},
			new String[]{"le" + t3.substring(0, 19) + "-00:00"}
		);

		for (String[] next : queries) {
			String description = String.join("&", next);

			// The SQL of SearchQueryBuilder.addPredicateLastUpdated depends only on the two bound instants and on
			// whether both bounds are "ne". The expected SQL is therefore the one of a range built from the
			// ORIGINAL range's instants (ge/le), which does not go through the code under test.
			DateRangeParam original = bindRange(next);
			if (original.getLowerBoundAsInstant() != null
					&& original.getUpperBoundAsInstant() != null
					&& original.getLowerBoundAsInstant().after(original.getUpperBoundAsInstant())) {
				// Inverted bounds (no range can hold them): same instants as the original, and no results
				SearchParameterMap inverted = SearchParameterMap.newSynchronous();
				inverted.setLastUpdatedAndList(bindAndList(next));
				assertThat(inverted.getLastUpdated().getLowerBoundAsInstant()).as(description).isEqualTo(original.getLowerBoundAsInstant());
				assertThat(inverted.getLastUpdated().getUpperBoundAsInstant()).as(description).isEqualTo(original.getUpperBoundAsInstant());
				assertThat(searchIds(inverted)).as(description).isEmpty();
				continue;
			}
			SearchParameterMap oldMap = SearchParameterMap.newSynchronous();
			oldMap.setLastUpdated(oracleRange(original));
			myCaptureQueriesListener.clear();
			List<String> oldIds = searchIds(oldMap);
			List<String> oldSql = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList();

			SearchParameterMap newMap = SearchParameterMap.newSynchronous();
			newMap.setLastUpdatedAndList(bindAndList(next));
			assertThat(newMap.getLastUpdated().getLowerBoundAsInstant()).as(description).isEqualTo(original.getLowerBoundAsInstant());
			assertThat(newMap.getLastUpdated().getUpperBoundAsInstant()).as(description).isEqualTo(original.getUpperBoundAsInstant());
			myCaptureQueriesListener.clear();
			List<String> newIds = searchIds(newMap);
			List<String> newSql = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList();

			assertThat(newIds).as(description).containsExactlyElementsOf(oldIds);
			assertThat(newSql).as(description).containsExactlyElementsOf(oldSql);
		}

		// And these match the FHIR rules
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setLastUpdatedAndList(bindAndList("ne" + t3));
		assertThat(searchIds(map)).containsExactlyInAnyOrderElementsOf(expected(or("ne3")));
		map = SearchParameterMap.newSynchronous();
		map.setLastUpdatedAndList(bindAndList("lt" + t(2), "gt" + t(4)));
		assertThat(searchIds(map)).isEmpty();
	}

	/**
	 * Range with the same SQL inputs as the given one, built without the code under test: ge/le with the instants
	 * of the original range, or the original itself when both bounds are "ne" (which has its own SQL shape).
	 */
	private static DateRangeParam oracleRange(DateRangeParam theOriginal) {
		DateParam lb = theOriginal.getLowerBound();
		DateParam ub = theOriginal.getUpperBound();
		if (lb != null && ub != null && ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL.equals(lb.getPrefix())
				&& ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL.equals(ub.getPrefix())) {
			return theOriginal;
		}
		return new DateRangeParam(theOriginal.getLowerBoundAsInstant(), theOriginal.getUpperBoundAsInstant());
	}

	/**
	 * Mixed-prefix ranges built directly (never produced by the parser) at every precision: the SQL and the
	 * results must be those of the original range, i.e. the same instants.
	 */
	@Test
	public void testSearch_MixedPrefixRanges_SameSqlAsOriginalRange() {
		String[] lows = {t(1).substring(0, 4), t(1).substring(0, 7), t(1).substring(0, 10), t(1).substring(0, 19) + "Z", t(1)};
		String[] highs = {t(5).substring(0, 4), t(5).substring(0, 7), t(5).substring(0, 10), t(5).substring(0, 19) + "Z", t(5)};
		String[][] prefixes = {
			{"eq", "le"}, {"eq", "lt"}, {"eq", "eb"}, {"ne", "le"}, {"ne", "lt"}, {"ge", "eq"}, {"gt", "eq"}, {"sa", "ne"},
			{"eq", "eq"}, {"eq", "ne"}, {"ne", "eq"}, {"ge", "le"}, {"gt", "lt"}, {"sa", "eb"}
		};
		int checked = 0;
		for (int i = 0; i < lows.length; i++) {
			for (int j = 0; j < highs.length; j++) {
				for (String[] p : prefixes) {
					DateRangeParam original;
					try {
						original = new DateRangeParam();
						original.setLowerBound(new DateParam(ca.uhn.fhir.rest.param.ParamPrefixEnum.forValue(p[0]), lows[i]));
						original.setUpperBound(new DateParam(ca.uhn.fhir.rest.param.ParamPrefixEnum.forValue(p[1]), highs[j]));
					} catch (RuntimeException e) {
						continue;
					}
					String description = p[0] + lows[i] + " " + p[1] + highs[j];
					if (p[0].equals("ne") && p[1].equals("ne")) {
						continue;
					}
					Date lowInstant = original.getLowerBoundAsInstant();
					Date highInstant = original.getUpperBoundAsInstant();
					List<String> expectedIds = new ArrayList<>();
					for (int k = 0; k < myTimes.length; k++) {
						boolean lowOk = lowInstant == null || myTimes[k] >= lowInstant.getTime();
						boolean highOk = highInstant == null || myTimes[k] <= highInstant.getTime();
						if (lowOk && highOk) {
							expectedIds.add(myIds.get(k));
						}
					}

					SearchParameterMap oracle = SearchParameterMap.newSynchronous();
					oracle.setLastUpdated(oracleRange(original));
					myCaptureQueriesListener.clear();
					List<String> oracleIds = searchIds(oracle);
					List<String> oracleSql = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList();

					SearchParameterMap actual = SearchParameterMap.newSynchronous();
					actual.setLastUpdated(original);
					myCaptureQueriesListener.clear();
					List<String> actualIds = searchIds(actual);
					List<String> actualSql = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).toList();

					assertThat(actualSql).as(description).containsExactlyElementsOf(oracleSql);
					assertThat(actualIds).as(description).containsExactlyElementsOf(oracleIds);
					assertThat(actualIds).as(description).containsExactlyInAnyOrderElementsOf(expectedIds);
					checked++;
				}
			}
		}
		assertThat(checked).isGreaterThan(100);
	}

	@Test
	public void testSearch_AndOr_UsesOneConditionPerAlternative() {
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.setLastUpdatedAndList(bindAndList("lt" + t(2) + ",gt" + t(4), "ne" + t(5)));
		myCaptureQueriesListener.clear();
		assertThat(searchIds(map)).containsExactlyInAnyOrderElementsOf(expected(or("lt2", "gt4"), or("ne5")));
		String sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertThat(sql).contains(" OR ");
		assertThat(sql).containsIgnoringCase("RES_UPDATED");
	}

	@Test
	public void testSearch_ApproximatePrefix_StillRejected() {
		assertThatThrownBy(() -> SearchParameterMap.newSynchronous().setLastUpdatedAndList(bindAndList("ap" + t(2) + ",gt" + t(4))))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-1925: ");
	}

	@Test
	public void testSearchCache_EquivalentAndOrReused_DifferentNotReused() {
		SearchParameterMap first = new SearchParameterMap();
		first.setLastUpdatedAndList(bindAndList("lt" + t(2) + ",gt" + t(4), "ne" + t(5)));
		IBundleProvider firstOutcome = myPatientDao.search(first, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(firstOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("lt2", "gt4"), or("ne5")));
		assertThat(firstOutcome.getCacheStatus().getStatus()).isEqualTo(SearchCacheStatus.SearchCacheStatusEnum.FWD_MISS);

		// Same expression, different order
		SearchParameterMap second = new SearchParameterMap();
		second.setLastUpdatedAndList(bindAndList("ne" + t(5), "gt" + t(4) + ",lt" + t(2)));
		IBundleProvider secondOutcome = myPatientDao.search(second, mySrd);
		assertThat(secondOutcome.getCacheStatus().getStatus()).isEqualTo(SearchCacheStatus.SearchCacheStatusEnum.HIT);
		assertThat(secondOutcome.getUuid()).isEqualTo(firstOutcome.getUuid());
		assertThat(toUnqualifiedVersionlessIdValues(secondOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("lt2", "gt4"), or("ne5")));

		// Different expression (le instead of lt)
		SearchParameterMap third = new SearchParameterMap();
		third.setLastUpdatedAndList(bindAndList("le" + t(2) + ",gt" + t(4), "ne" + t(5)));
		IBundleProvider thirdOutcome = myPatientDao.search(third, mySrd);
		assertThat(thirdOutcome.getCacheStatus().getStatus()).isEqualTo(SearchCacheStatus.SearchCacheStatusEnum.FWD_MISS);
		assertThat(toUnqualifiedVersionlessIdValues(thirdOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("le2", "gt4"), or("ne5")));

		// A range is never confused with an AND/OR expression
		SearchParameterMap fourth = new SearchParameterMap();
		fourth.setLastUpdatedAndList(bindAndList("ne" + t(5)));
		IBundleProvider fourthOutcome = myPatientDao.search(fourth, mySrd);
		assertThat(fourthOutcome.getCacheStatus().getStatus()).isEqualTo(SearchCacheStatus.SearchCacheStatusEnum.FWD_MISS);
		assertThat(toUnqualifiedVersionlessIdValues(fourthOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("ne5")));
	}

	/**
	 * A plain range with <code>gt</code> and one with <code>ge</code> have the same normalized query string
	 * (see SearchParameterMap#toNormalizedQueryString), so the second search reuses the results of the first one.
	 * This is not caused by the AND/OR support (it happens with a plain range).
	 */
	@Test
	@Disabled("Existing behaviour, not changed by hapi-fhir#8857: to be reported separately")
	public void testSearchCache_PlainRange_GtAndGeAreNotReused() {
		SearchParameterMap gt = new SearchParameterMap();
		gt.setLastUpdated(bindRange("gt" + t(3)));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(gt, mySrd))).containsExactlyInAnyOrderElementsOf(expected(or("gt3")));

		SearchParameterMap ge = new SearchParameterMap();
		ge.setLastUpdated(bindRange("ge" + t(3)));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(ge, mySrd))).containsExactlyInAnyOrderElementsOf(expected(or("ge3")));
	}

	@Test
	public void testConditionalCreate_IfNoneExistWithAndOr() {
		// Matches only T3
		Patient patient = new Patient();
		patient.setActive(false);
		DaoMethodOutcome outcome = myPatientDao.create(patient, "Patient?_lastUpdated=lt" + t(1) + ",eq" + t(3), mySrd);
		assertThat(outcome.getCreated()).isFalse();
		assertThat(outcome.getId().toUnqualifiedVersionless().getValue()).isEqualTo(myIds.get(2));

		// Three repetitions, matches only T4
		outcome = myPatientDao.create(patient, "Patient?_lastUpdated=gt" + t(2) + "&_lastUpdated=gt" + t(3) + "&_lastUpdated=lt" + t(5), mySrd);
		assertThat(outcome.getCreated()).isFalse();
		assertThat(outcome.getId().toUnqualifiedVersionless().getValue()).isEqualTo(myIds.get(3));

		// Matches two
		assertThatThrownBy(() -> myPatientDao.create(patient, "Patient?_lastUpdated=eq" + t(1) + ",eq" + t(5), mySrd))
			.isInstanceOf(PreconditionFailedException.class);

		// Matches none
		outcome = myPatientDao.create(patient, "Patient?_lastUpdated=lt" + t(1) + ",gt" + t(5) + ",eq" + format(myTimes[2] + 1), mySrd);
		assertThat(outcome.getCreated()).isTrue();
	}

	@Test
	public void testBatch2_UrlWithAndOr_RejectedWhereTheUrlIsParsed() {
		Date start = new Date(0);
		Date end = new Date(System.currentTimeMillis() + 3600_000L);

		// A range works as before
		List<String> rangeIds = myBatch2DaoSvc
			.fetchResourceIdStream(start, end, RequestPartitionId.allPartitions(), "Patient?_lastUpdated=ge" + t(2) + "&_lastUpdated=le" + t(4))
			.visitStream(s -> s.map(p -> p.resourceType).collect(Collectors.toList()));
		assertThat(rangeIds).hasSize(3);

		// AND/OR is not supported there: a 400, not a reindex of every resource
		assertThatThrownBy(() -> myBatch2DaoSvc
			.fetchResourceIdStream(start, end, RequestPartitionId.allPartitions(), "Patient?_lastUpdated=lt" + t(2) + ",gt" + t(4))
			.visitStream(s -> s.collect(Collectors.toList())))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-3075: ");
	}

	/**
	 * A <code>_lastUpdated</code> with only empty values is no filter at all: a conditional operation with
	 * only that is rejected exactly like one with no parameters (and never matches every resource).
	 */
	@Test
	public void testConditionalOperations_OnlyEmptyLastUpdated_RejectedLikeNoParameters() {
		for (String url : List.of("Patient?_lastUpdated=,&_lastUpdated=,", "Patient?_lastUpdated=", "Patient?_lastUpdated=,")) {
			Patient patient = new Patient();
			patient.setActive(false);

			Throwable expected = catchThrowable(() -> myPatientDao.create(patient, "Patient?", mySrd));
			Throwable actual = catchThrowable(() -> myPatientDao.create(patient, url, mySrd));
			assertSameRejection(url, "create", expected, actual);

			expected = catchThrowable(() -> myPatientDao.update(patient, "Patient?", mySrd));
			actual = catchThrowable(() -> myPatientDao.update(patient, url, mySrd));
			assertSameRejection(url, "update", expected, actual);

			expected = catchThrowable(() -> myPatientDao.deleteByUrl("Patient?", mySrd));
			actual = catchThrowable(() -> myPatientDao.deleteByUrl(url, mySrd));
			assertSameRejection(url, "delete", expected, actual);
		}
		// Nothing was created or deleted
		assertThat(searchIds(SearchParameterMap.newSynchronous())).containsExactlyInAnyOrderElementsOf(myIds);
	}

	private static void assertSameRejection(String theUrl, String theOperation, Throwable theExpected, Throwable theActual) {
		assertThat(theActual).as(theOperation + " " + theUrl).isNotNull();
		assertThat(theActual.getClass()).as(theOperation + " " + theUrl).isEqualTo(theExpected.getClass());
		assertThat(theActual.getMessage()).as(theOperation + " " + theUrl).startsWith(theExpected.getMessage().substring(0, 10));
	}

	/**
	 * The search cache key is lexical: the same instants written with another offset are a cache miss,
	 * but return the same results.
	 */
	@Test
	public void testSearchCache_SameInstantsWithAnotherOffset_SameResults() {
		SimpleDateFormat minus5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		minus5.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));
		String t2Minus5 = minus5.format(new Date(myTimes[1]));
		assertThat(t2Minus5).endsWith("-05:00");

		SearchParameterMap utc = new SearchParameterMap();
		utc.setLastUpdatedAndList(bindAndList("eq" + t(2) + ",eq" + t(4)));
		IBundleProvider utcOutcome = myPatientDao.search(utc, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(utcOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("eq2", "eq4")));

		SearchParameterMap offset = new SearchParameterMap();
		offset.setLastUpdatedAndList(bindAndList("eq" + t2Minus5 + ",eq" + t(4)));
		assertThat(offset.toNormalizedQueryString(myFhirContext)).isNotEqualTo(utc.toNormalizedQueryString(myFhirContext));
		IBundleProvider offsetOutcome = myPatientDao.search(offset, mySrd);
		assertThat(offsetOutcome.getCacheStatus().getStatus()).isEqualTo(SearchCacheStatus.SearchCacheStatusEnum.FWD_MISS);
		assertThat(toUnqualifiedVersionlessIdValues(offsetOutcome)).containsExactlyInAnyOrderElementsOf(expected(or("eq2", "eq4")));
	}
}
