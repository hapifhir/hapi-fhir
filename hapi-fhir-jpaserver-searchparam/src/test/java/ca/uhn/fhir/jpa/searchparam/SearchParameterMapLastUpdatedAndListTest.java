package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for <code>_lastUpdated</code> AND/OR expressions in {@link SearchParameterMap} (hapi-fhir#8857)
 */
class SearchParameterMapLastUpdatedAndListTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	/**
	 * Parses raw query values the same way the REST layer binds a <code>DateAndListParam</code>:
	 * each element of the outer array is one repetition of the parameter, and commas separate OR values.
	 */
	private static DateAndListParam bindQualifiedAndList(String theQualifier, String... theRepetitions) {
		// Same as MatchUrlService: an unescaped '+' in an offset arrives as ' '
		String[] repetitions = Arrays.stream(theRepetitions).map(t -> t.replace(' ', '+')).toArray(String[]::new);
		List<QualifiedParamList> params = toQualifiedParamLists(theQualifier, repetitions);
		DateAndListParam retVal = new DateAndListParam();
		retVal.setValuesAsQueryTokens(ourCtx, Constants.PARAM_LASTUPDATED, params);
		return retVal;
	}

	private static DateAndListParam bindAndList(String... theRepetitions) {
		return bindQualifiedAndList(null, theRepetitions);
	}

	/**
	 * Parses raw query values the same way <code>_lastUpdated</code> was bound before (as a single range)
	 */
	private static DateRangeParam bindRange(String theQualifier, String... theRepetitions) {
		DateRangeParam retVal = new DateRangeParam();
		retVal.setValuesAsQueryTokens(ourCtx, Constants.PARAM_LASTUPDATED, toQualifiedParamLists(theQualifier, theRepetitions));
		return retVal;
	}

	private static List<QualifiedParamList> toQualifiedParamLists(String theQualifier, String[] theRepetitions) {
		List<QualifiedParamList> params = new ArrayList<>();
		for (String next : theRepetitions) {
			QualifiedParamList list = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(theQualifier, next);
			params.add(list);
		}
		return params;
	}

	private static SearchParameterMap mapWithAndList(String... theRepetitions) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdatedAndList(bindAndList(theRepetitions));
		return map;
	}

	/**
	 * Every value that a plain range accepts today (single values, one lower and/or one upper bound),
	 * with every prefix that a range accepts and several precisions and offsets.
	 */
	static Stream<Arguments> representableExpressionArgs() {
		return representableExpressions().map(t -> Arguments.of((Object) t));
	}

	static Stream<String[]> representableExpressions() {
		return Stream.of(
			new String[]{"2020"},
			new String[]{"2020-05"},
			new String[]{"2020-05-05"},
			new String[]{"2020-05-05T10:11:12Z"},
			new String[]{"2020-05-05T10:11:12.123Z"},
			new String[]{"eq2020-05-05T10:11:12.123Z"},
			new String[]{"ne2020"},
			new String[]{"ne2020-05-05T10:11:12.123Z"},
			new String[]{"gt2020-05-05"},
			new String[]{"ge2020-05-05T10:11:12+03:00"},
			new String[]{"lt2020-05-05T10:11:12-04:00"},
			new String[]{"le2020-05-05T10:11:12.999Z"},
			new String[]{"sa2020-05"},
			new String[]{"eb2020-05-05T10:11:12Z"},
			new String[]{">2020-05-05"},
			new String[]{"<=2020-05-05"},
			new String[]{"ge2020", "le2021"},
			new String[]{"le2021", "ge2020"},
			new String[]{"gt2020-01-01T00:00:00Z", "lt2020-01-02T00:00:00Z"},
			new String[]{"sa2020-01-01", "eb2020-02-01"},
			new String[]{"ge2020-01-01T00:00:00 03:00", "lt2021-01-01T00:00:00Z"},
			// Inverted bounds: a range that can't match anything, accepted today
			new String[]{"gt2021", "lt2020"},
			new String[]{"lt2020-01-01T00:00:00.000Z", "gt2020-01-01T00:00:00.000Z"}
		);
	}

	@ParameterizedTest
	@MethodSource("representableExpressionArgs")
	void testSetLastUpdatedAndList_RepresentableAsRange_StoredAsTheSameRangeAsBefore(String[] theRepetitions) {
		DateRangeParam expected = bindRange(null, theRepetitions);

		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdatedAndList(bindAndList(theRepetitions));

		SearchParameterMap oldMap = new SearchParameterMap();
		oldMap.setLastUpdated(expected);

		DateRangeParam actual = map.getLastUpdated();
		if (expected.isEmpty()) {
			assertThat(actual).isNull();
			assertThat(map.hasLastUpdated()).isFalse();
			return;
		}
		assertThat(actual).isNotNull();
		assertThat(actual.getClass()).isEqualTo(DateRangeParam.class);
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.toString()).isEqualTo(expected.toString());
		assertThat(actual.getLowerBoundAsInstant()).isEqualTo(expected.getLowerBoundAsInstant());
		assertThat(actual.getUpperBoundAsInstant()).isEqualTo(expected.getUpperBoundAsInstant());
		assertThat(prefixOf(actual.getLowerBound())).isEqualTo(prefixOf(expected.getLowerBound()));
		assertThat(prefixOf(actual.getUpperBound())).isEqualTo(prefixOf(expected.getUpperBound()));
		assertThat(precisionOf(actual.getLowerBound())).isEqualTo(precisionOf(expected.getLowerBound()));
		assertThat(precisionOf(actual.getUpperBound())).isEqualTo(precisionOf(expected.getUpperBound()));
		assertThat(map.getLastUpdated()).isEqualTo(expected);
		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map).isEqualTo(oldMap);
		assertThat(map.hashCode()).isEqualTo(oldMap.hashCode());
		assertThat(map.toNormalizedQueryString(ourCtx)).isEqualTo(oldMap.toNormalizedQueryString(ourCtx));
	}

	private static ParamPrefixEnum prefixOf(DateParam theDateParam) {
		return theDateParam != null ? theDateParam.getPrefix() : null;
	}

	private static Object precisionOf(DateParam theDateParam) {
		return theDateParam != null && !theDateParam.isEmpty() ? theDateParam.getPrecision() : null;
	}

	static Stream<Arguments> notRepresentableExpressionArgs() {
		return notRepresentableExpressions().map(t -> Arguments.of((Object) t));
	}

	static Stream<String[]> notRepresentableExpressions() {
		return Stream.of(
			new String[]{"lt2020,gt2021"},
			new String[]{"gt2020", "gt2021"},
			new String[]{"lt2020", "lt2021"},
			new String[]{"ge2020", "le2022", "ne2021"},
			new String[]{"ne2020", "ne2021"},
			new String[]{"2020", "2021"},
			new String[]{"eq2020,eq2021"},
			new String[]{"ne2020-01-01T10:00:00Z,eq2020-01-02"},
			new String[]{"ge2020", "ge2021", "le2023", "le2024"},
			new String[]{"sa2020", "gt2020-06"},
			new String[]{"2020-01-01T10:00:00 03:00,2020-01-01T10:00:00-04:00"}
		);
	}

	@ParameterizedTest
	@MethodSource("notRepresentableExpressionArgs")
	void testSetLastUpdatedAndList_NotRepresentableAsRange(String[] theRepetitions) {
		// The old binding rejected all of these
		assertThatThrownBy(() -> bindRange(null, theRepetitions)).isInstanceOf(InvalidRequestException.class);

		SearchParameterMap map = new SearchParameterMap();
		DateAndListParam andList = bindAndList(theRepetitions);
		map.setLastUpdatedAndList(andList);

		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThatThrownBy(map::getLastUpdated)
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-3075: ")
			.hasMessageContaining("getLastUpdatedAndList()");

		DateAndListParam actual = map.getLastUpdatedAndList();
		assertThat(toTokens(actual)).isEqualTo(toTokens(andList));
	}

	@Test
	void testSetLastUpdatedAndList_ApproximatePrefix_RejectedAsBefore() {
		assertThatThrownBy(() -> bindRange(null, "ap2020")).hasMessageStartingWith("HAPI-1925: ");

		assertThatThrownBy(() -> mapWithAndList("ap2020"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-1925: ");
		assertThatThrownBy(() -> mapWithAndList("ap2020,gt2021"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-1925: ");
		assertThatThrownBy(() -> mapWithAndList("gt2021", "ap2020"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-1925: ");
		assertThatThrownBy(() -> mapWithAndList("gt2021", "gt2022", "ap2020"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageStartingWith("HAPI-1925: ");
	}

	@Test
	void testSetLastUpdatedAndList_ApproximatePrefix_LeavesPreviousValueUntouched() {
		SearchParameterMap map = mapWithAndList("gt2020");
		assertThatThrownBy(() -> map.setLastUpdatedAndList(bindAndList("ap2020,gt2021")))
			.isInstanceOf(InvalidRequestException.class);
		// Either the previous value, or nothing - but never a partially applied filter
		DateRangeParam lastUpdated = map.isLastUpdatedRepresentableAsRange() ? map.getLastUpdated() : null;
		if (lastUpdated != null) {
			assertThat(lastUpdated).isEqualTo(bindRange(null, "gt2020"));
		}
	}

	@Test
	void testSetLastUpdatedAndList_Null_ClearsBothForms() {
		SearchParameterMap map = mapWithAndList("lt2020,gt2021");
		map.setLastUpdatedAndList(null);
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.getLastUpdated()).isNull();
		assertThat(map.getLastUpdatedAndList()).isNull();

		map = mapWithAndList("gt2021");
		map.setLastUpdatedAndList(new DateAndListParam());
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.getLastUpdated()).isNull();
	}

	@Test
	void testSetLastUpdatedAndList_OnlyEmptyValues_NoFilter() {
		for (String[] next : List.of(
			new String[]{""},
			new String[]{","},
			new String[]{",", ","},
			new String[]{"", ""},
			new String[]{",", ""})) {
			SearchParameterMap map = mapWithAndList(next);
			assertThat(map.hasLastUpdated()).as(Arrays.toString(next)).isFalse();
			assertThat(map.getLastUpdated()).as(Arrays.toString(next)).isNull();
			assertThat(map.getLastUpdatedAndList()).as(Arrays.toString(next)).isNull();
			SearchParameterMap emptyMap = new SearchParameterMap();
			// toNormalizedQueryString() also initializes lazy fields, so call it on both before comparing
			assertThat(map.toNormalizedQueryString(ourCtx)).as(Arrays.toString(next)).isEqualTo(emptyMap.toNormalizedQueryString(ourCtx));
			assertThat(map).as(Arrays.toString(next)).isEqualTo(emptyMap);
		}
	}

	@Test
	void testSetLastUpdatedAndList_EmptyValuesAreIgnored() {
		// Empty alternatives and empty repetitions don't change the expression
		assertThat(mapWithAndList("lt2020,,gt2021", ",")).isEqualTo(mapWithAndList("lt2020,gt2021"));
		assertThat(mapWithAndList("lt2020,,gt2021", ",").toNormalizedQueryString(ourCtx))
			.isEqualTo(mapWithAndList("lt2020,gt2021").toNormalizedQueryString(ourCtx));
		assertThat(mapWithAndList("ge2020", "").getLastUpdated()).isEqualTo(bindRange(null, "ge2020"));
		assertThat(mapWithAndList("ge2020,", "le2021").getLastUpdated()).isEqualTo(bindRange(null, "ge2020", "le2021"));
		assertThat(mapWithAndList(",", "").hasLastUpdated()).isFalse();
	}

	@Test
	void testSetters_AreMutuallyExclusive() {
		SearchParameterMap map = mapWithAndList("lt2020,gt2021");

		DateRangeParam range = bindRange(null, "ge2019", "le2022");
		map.setLastUpdated(range);
		assertThat(map.getLastUpdated()).isEqualTo(range).isNotSameAs(range);
		assertThat(toTokens(map.getLastUpdatedAndList())).containsExactly(List.of("ge2019"), List.of("le2022"));

		map.setLastUpdatedAndList(bindAndList("lt2020,gt2021"));
		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThatThrownBy(map::getLastUpdated).hasMessageStartingWith("HAPI-3075: ");

		map.setLastUpdated(null);
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.getLastUpdated()).isNull();
		assertThat(map.getLastUpdatedAndList()).isNull();
	}

	/**
	 * A caller that reads the full expression and sets it back (e.g. to add a value) must not turn a
	 * plain range into the AND/OR form, which {@link SearchParameterMap#getLastUpdated()} rejects.
	 */
	@Test
	void testGetLastUpdatedAndList_FromRange_RoundTrips() {
		SoftAssertions softly = new SoftAssertions();
		for (String[] next : representableExpressions().toList()) {
			DateRangeParam range = bindRange(null, next);
			if (range.isEmpty()) {
				continue;
			}
			SearchParameterMap map = new SearchParameterMap();
			map.setLastUpdated(range);

			DateAndListParam andList = map.getLastUpdatedAndList();
			SearchParameterMap roundTripped = new SearchParameterMap();
			roundTripped.setLastUpdatedAndList(andList);
			softly.assertThat(roundTripped.getLastUpdated()).as(Arrays.toString(next)).isEqualTo(range);

			// The returned list is a copy
			andList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).setValue(new Date(0));
			softly.assertThat(map.getLastUpdated()).as(Arrays.toString(next)).isEqualTo(bindRange(null, next));
		}
		softly.assertAll();
	}

	@Test
	void testSetLastUpdatedAndList_MissingQualifier_SameAsBefore() {
		for (String value : List.of("true", "false")) {
			DateRangeParam expected = bindRange(Constants.PARAMQUALIFIER_MISSING, value);
			SearchParameterMap oldMap = new SearchParameterMap();
			oldMap.setLastUpdated(expected);

			SearchParameterMap map = new SearchParameterMap();
			map.setLastUpdatedAndList(bindQualifiedAndList(Constants.PARAMQUALIFIER_MISSING, value));

			assertThat(map.hasLastUpdated()).isEqualTo(oldMap.getLastUpdated() != null);
			assertThat(map.getLastUpdated()).isEqualTo(oldMap.getLastUpdated());
			assertThat(map.toNormalizedQueryString(ourCtx)).isEqualTo(oldMap.toNormalizedQueryString(ourCtx));
		}
	}

	@Test
	void testSetLastUpdatedAndList_DoesNotModifyTheInput() {
		DateAndListParam input = bindAndList("2020", "gt2021,lt2019", "ne2020-05-05T10:00:00+03:00");
		List<List<String>> before = toTokens(input);
		List<List<ParamPrefixEnum>> prefixesBefore = toPrefixes(input);

		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdatedAndList(input);
		map.toNormalizedQueryString(ourCtx);
		map.getLastUpdatedAndList();

		assertThat(toTokens(input)).isEqualTo(before);
		assertThat(toPrefixes(input)).isEqualTo(prefixesBefore);
		assertThat(prefixesBefore.get(0).get(0)).isNull();

		DateAndListParam representable = bindAndList("2020");
		map.setLastUpdatedAndList(representable);
		assertThat(representable.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getPrefix()).isNull();
	}

	@Test
	void testJpaParamUtil_RangeAlternatives_NewInstancesEveryCall() {
		DateAndListParam param = bindAndList("ne2020,eq2021", "gt2019-06");

		List<List<DateRangeParam>> first = JpaParamUtil.toLastUpdatedRangeAlternatives(param);
		List<List<DateRangeParam>> second = JpaParamUtil.toLastUpdatedRangeAlternatives(param);
		assertThat(first).hasSize(2);
		assertThat(first.get(0)).hasSize(2);
		assertThat(first.get(1)).hasSize(1);
		for (int i = 0; i < first.size(); i++) {
			for (int j = 0; j < first.get(i).size(); j++) {
				assertThat(first.get(i).get(j)).isNotSameAs(second.get(i).get(j));
				assertThat(first.get(i).get(j)).isEqualTo(second.get(i).get(j));
				assertThat(first.get(i).get(j).getLowerBound()).isNotSameAs(second.get(i).get(j).getLowerBound());
			}
		}

		// "ne" keeps both bounds as "ne" (it is not turned into "eq")
		DateRangeParam ne = first.get(0).get(0);
		assertThat(ne.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(ne.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(ne).isEqualTo(bindRange(null, "ne2020"));
		assertThat(first.get(0).get(1)).isEqualTo(bindRange(null, "eq2021"));
		assertThat(first.get(1).get(0)).isEqualTo(bindRange(null, "gt2019-06"));

		// Mutating a returned range doesn't change the next result
		first.get(1).get(0).setLowerBound("gt1900");
		assertThat(JpaParamUtil.toLastUpdatedRangeAlternatives(param).get(1).get(0)).isEqualTo(bindRange(null, "gt2019-06"));
	}

	/**
	 * An empty range, a plain range and an AND/OR expression are never equal to each other.
	 */
	@Test
	void testSearchParameterMapEquals_SymmetricBetweenRangeAndAndOrForms() {
		SearchParameterMap emptyRange = new SearchParameterMap();
		emptyRange.setLastUpdated(new DateRangeParam());
		SearchParameterMap range = new SearchParameterMap();
		range.setLastUpdated(bindRange(null, "lt2020"));
		SearchParameterMap andOr = new SearchParameterMap();
		andOr.setLastUpdatedAndList(bindAndList("lt2020,gt2021"));

		List<SearchParameterMap> maps = List.of(emptyRange, range, andOr);
		for (int i = 0; i < maps.size(); i++) {
			for (int j = 0; j < maps.size(); j++) {
				boolean expected = i == j;
				assertThat(maps.get(i).equals(maps.get(j))).as(i + " vs " + j).isEqualTo(expected);
				assertThat(maps.get(j).equals(maps.get(i))).as(j + " vs " + i).isEqualTo(expected);
			}
		}

		// Equal maps of each kind have equal hash codes
		SearchParameterMap range2 = new SearchParameterMap();
		range2.setLastUpdated(bindRange(null, "lt2020"));
		SearchParameterMap andOr2 = new SearchParameterMap();
		andOr2.setLastUpdatedAndList(bindAndList("gt2021,lt2020"));
		SearchParameterMap emptyRange2 = new SearchParameterMap();
		emptyRange2.setLastUpdated(new DateRangeParam());
		assertThat(range2).isEqualTo(range).hasSameHashCodeAs(range);
		assertThat(andOr2).isEqualTo(andOr).hasSameHashCodeAs(andOr);
		assertThat(emptyRange2).isEqualTo(emptyRange).hasSameHashCodeAs(emptyRange);
	}

	/**
	 * Documents the behaviour of hapi-fhir-base, which is not changed: {@link DateRangeParam#equals(Object)}
	 * accepts any subclass and only compares the bounds.
	 */
	@Test
	void testHistorySearchDateRangeParam_EqualsPlainRange_AsUpstream() {
		DateRangeParam range = bindRange(null, "ge2020", "le2021");
		HistorySearchDateRangeParam history = new HistorySearchDateRangeParam(Map.of("_since", new String[]{"2020"}), range, 5);
		assertThat(range.equals(history)).isTrue();
		assertThat(history.equals(range)).isTrue();
	}

	/**
	 * The normalized query string (the search cache key) is lexical: two expressions for the same instants,
	 * written with different offsets, get different keys. They return the same results, they just don't share
	 * a cache entry.
	 */
	@Test
	void testNormalizedQueryString_IsLexical() {
		SearchParameterMap utc = mapWithAndList("eq2020-01-01T00:00:00Z,eq2021");
		SearchParameterMap offset = mapWithAndList("eq2019-12-31T19:00:00-05:00,eq2021");
		assertThat(utc.toNormalizedQueryString(ourCtx)).isNotEqualTo(offset.toNormalizedQueryString(ourCtx));
		assertThat(utc).isNotEqualTo(offset);
		// ...but the alternatives are the same ranges in time
		List<List<DateRangeParam>> utcRanges = JpaParamUtil.toLastUpdatedRangeAlternatives(utc.getLastUpdatedAndList());
		List<List<DateRangeParam>> offsetRanges = JpaParamUtil.toLastUpdatedRangeAlternatives(offset.getLastUpdatedAndList());
		for (int i = 0; i < 2; i++) {
			assertThat(utcRanges.get(0).get(i).getLowerBoundAsInstant()).isEqualTo(offsetRanges.get(0).get(i).getLowerBoundAsInstant());
			assertThat(utcRanges.get(0).get(i).getUpperBoundAsInstant()).isEqualTo(offsetRanges.get(0).get(i).getUpperBoundAsInstant());
		}
	}

	@Test
	void testEqualsHashCodeAndNormalizedQueryString_IndependentOfOrder() {
		SearchParameterMap a = mapWithAndList("lt2020,gt2021", "ne2020-06");
		SearchParameterMap b = mapWithAndList("ne2020-06", "gt2021,lt2020");
		SearchParameterMap c = mapWithAndList("gt2021,lt2020", "ne2020-06");

		assertThat(a).isEqualTo(b).isEqualTo(c);
		assertThat(a.hashCode()).isEqualTo(b.hashCode()).isEqualTo(c.hashCode());
		assertThat(a.toNormalizedQueryString(ourCtx))
			.isEqualTo(b.toNormalizedQueryString(ourCtx))
			.isEqualTo(c.toNormalizedQueryString(ourCtx));
	}

	@Test
	void testEqualsAndNormalizedQueryString_DifferentExpressionsAreDifferent() {
		List<SearchParameterMap> maps = new ArrayList<>();
		maps.add(new SearchParameterMap());
		SearchParameterMap emptyRange = new SearchParameterMap();
		emptyRange.setLastUpdated(new DateRangeParam());
		maps.add(mapWithAndList("lt2020,gt2021"));
		maps.add(mapWithAndList("le2020,gt2021"));
		maps.add(mapWithAndList("lt2020,ge2021"));
		maps.add(mapWithAndList("lt2020,sa2021"));
		maps.add(mapWithAndList("eb2020,gt2021"));
		maps.add(mapWithAndList("lt2020", "gt2021", "ne2020-06"));
		maps.add(mapWithAndList("gt2020", "gt2021"));
		maps.add(mapWithAndList("ge2020", "gt2021"));
		maps.add(mapWithAndList("gt2020", "ge2021"));
		maps.add(mapWithAndList("gt2021"));
		maps.add(mapWithAndList("lt2020,gt2021", "lt2020,gt2021"));
		maps.add(mapWithAndList("lt2020-01-01,gt2021"));
		maps.add(mapWithAndList("ne2020", "ne2021"));
		maps.add(mapWithAndList("ne2020,ne2021"));

		for (int i = 0; i < maps.size(); i++) {
			for (int j = 0; j < maps.size(); j++) {
				if (i == j) {
					continue;
				}
				SearchParameterMap first = maps.get(i);
				SearchParameterMap second = maps.get(j);
				assertThat(first).as(i + " vs " + j).isNotEqualTo(second);
				assertThat(first.toNormalizedQueryString(ourCtx))
					.as(i + " vs " + j)
					.isNotEqualTo(second.toNormalizedQueryString(ourCtx));
			}
			assertThat(maps.get(i)).isNotEqualTo(emptyRange);
		}
	}

	@Test
	void testNormalizedQueryString_KeepsOriginalPrefixes() {
		assertThat(mapWithAndList("gt2021,lt2020", "ne2020-06").toNormalizedQueryString(ourCtx))
			.isEqualTo("?_lastUpdated=gt2021,lt2020&_lastUpdated=ne2020-06");
	}

	/**
	 * The normalized query string of a plain range turns "gt" into "ge" (and "lt" into "le"), so
	 * two ranges that select different resources get the same normalized query string, which is
	 * used as the key of the search cache. This is not caused by the AND/OR support, but this test
	 * documents it.
	 */
	@Test
	@Disabled("Existing behaviour, not changed by hapi-fhir#8857: to be reported separately")
	void testNormalizedQueryString_PlainRange_ExclusiveAndInclusiveBoundsAreDifferent() {
		SearchParameterMap gt = mapWithAndList("gt2020-01-01T10:00:00.000Z");
		SearchParameterMap ge = mapWithAndList("ge2020-01-01T10:00:00.000Z");
		assertThat(gt).isNotEqualTo(ge);
		assertThat(gt.toNormalizedQueryString(ourCtx)).isNotEqualTo(ge.toNormalizedQueryString(ourCtx));
	}

	@Test
	void testClone() {
		SearchParameterMap map = mapWithAndList("lt2020,gt2021", "ne2020-06");
		map.add("name", new StringParam("smith"));

		SearchParameterMap clone = map.clone();
		assertThat(clone).isEqualTo(map);
		assertThat(clone.hashCode()).isEqualTo(map.hashCode());
		assertThat(clone.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThat(clone.hasLastUpdated()).isTrue();
		assertThat(toTokens(clone.getLastUpdatedAndList())).isEqualTo(toTokens(map.getLastUpdatedAndList()));
		assertThat(clone.toNormalizedQueryString(ourCtx)).isEqualTo(map.toNormalizedQueryString(ourCtx));

		// Clearing the clone doesn't change the original
		clone.setLastUpdated(null);
		assertThat(map.hasLastUpdated()).isTrue();

		SearchParameterMap rangeMap = mapWithAndList("ge2020", "le2021");
		assertThat(rangeMap.clone().getLastUpdated()).isEqualTo(rangeMap.getLastUpdated());
	}

	@Test
	void testSerialization_RoundTrip() throws Exception {
		SearchParameterMap map = mapWithAndList("lt2020,gt2021", "ne2020-06-01T10:00:00+03:00");
		map.add("name", new StringParam("smith"));
		map.toNormalizedQueryString(ourCtx);

		SearchParameterMap read = (SearchParameterMap) deserialize(serialize(map), getClass().getClassLoader());
		read.toNormalizedQueryString(ourCtx);
		assertThat(read).isEqualTo(map);
		assertThat(read.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThat(toTokens(read.getLastUpdatedAndList())).isEqualTo(toTokens(map.getLastUpdatedAndList()));
		assertThat(read.toNormalizedQueryString(ourCtx)).isEqualTo(map.toNormalizedQueryString(ourCtx));
	}

	/**
	 * A plain range is stored exactly as before (a {@link DateRangeParam} in the <code>myLastUpdated</code> field), so a
	 * server that doesn't know about AND/OR can read it.
	 */
	@Test
	void testSerialization_PlainRange_ReadableAsBefore() throws Exception {
		for (String[] next : representableExpressions().toList()) {
			SearchParameterMap map = mapWithAndList(next);
			map.add("name", new StringParam("smith"));
			String expectedQueryString = map.toNormalizedQueryString(ourCtx);

			SearchParameterMap read = (SearchParameterMap) deserialize(serialize(map), getClass().getClassLoader());
			if (read.hasLastUpdated()) {
				assertThat(read.getLastUpdated()).as(Arrays.toString(next)).isEqualTo(bindRange(null, next));
			}
			assertThat(read.toNormalizedQueryString(ourCtx)).isEqualTo(expectedQueryString);
			assertThat(read).isEqualTo(map);
		}
	}

	private static byte[] serialize(Object theObject) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
			out.writeObject(theObject);
		}
		return bytes.toByteArray();
	}

	private static Object deserialize(byte[] theBytes, ClassLoader theClassLoader) throws IOException, ClassNotFoundException {
		try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(theBytes)) {
			@Override
			protected Class<?> resolveClass(ObjectStreamClass theDesc) throws ClassNotFoundException {
				return Class.forName(theDesc.getName(), false, theClassLoader);
			}
		}) {
			return in.readObject();
		}
	}

	private static List<List<String>> toTokens(DateAndListParam theAndList) {
		List<List<String>> retVal = new ArrayList<>();
		for (DateOrListParam nextOr : theAndList.getValuesAsQueryTokens()) {
			List<String> group = new ArrayList<>();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				group.add(next.getValueAsQueryToken());
			}
			retVal.add(group);
		}
		return retVal;
	}

	private static List<List<ParamPrefixEnum>> toPrefixes(DateAndListParam theAndList) {
		List<List<ParamPrefixEnum>> retVal = new ArrayList<>();
		for (DateOrListParam nextOr : theAndList.getValuesAsQueryTokens()) {
			List<ParamPrefixEnum> group = new ArrayList<>();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				group.add(next.getPrefix());
			}
			retVal.add(group);
		}
		return retVal;
	}
}
