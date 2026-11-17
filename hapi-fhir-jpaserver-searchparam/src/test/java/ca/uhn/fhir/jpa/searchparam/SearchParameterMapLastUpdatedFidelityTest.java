package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.searchparam.LastUpdatedTestData.RangeCase;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Fidelity of {@link SearchParameterMap#setLastUpdated(DateRangeParam)} / {@link SearchParameterMap#getLastUpdated()}
 * against what the same range gave before AND/OR support (see also {@link SearchParameterMapLastUpdatedCacheKeyTest},
 * which records the cache keys of the base commit).
 */
class SearchParameterMapLastUpdatedFidelityTest {

	static Stream<Arguments> rangeCases() {
		return LastUpdatedTestData.allRangeCases().stream().map(c -> Arguments.of(c));
	}

	@Test
	void matrixIsLarge() {
		List<RangeCase> cases = LastUpdatedTestData.allRangeCases();
		System.out.println("FIDELITY cases=" + cases.size() + " skipped(by DateRangeParam)=" + LastUpdatedTestData.SKIPPED.size());
		assertThat(cases.size()).isGreaterThan(1000);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("rangeCases")
	void roundTrip(RangeCase theCase) {
		DateRangeParam orig = theCase.supplier().get();
		SearchParameterMap map = new SearchParameterMap();

		if (LastUpdatedTestData.isWrongSide(orig)) {
			// A map could hold it, but any later read blew up (the range's isEmpty() throws IllegalStateException)
			assertThatThrownBy(orig::isEmpty).isInstanceOf(IllegalStateException.class);
			assertThatThrownBy(() -> map.setLastUpdated(orig)).isInstanceOf(IllegalArgumentException.class);
			assertThat(map.hasLastUpdated()).isFalse();
			return;
		}
		if (LastUpdatedTestData.isNeNeDifferent(orig)) {
			assertThatThrownBy(() -> map.setLastUpdated(orig))
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining("HAPI-3077");
			assertThat(map.hasLastUpdated()).isFalse();
			return;
		}

		map.setLastUpdated(orig);
		DateRangeParam got = map.getLastUpdated();

		if (!LastUpdatedTestData.hasAnyValue(orig)) {
			assertThat(got).isNull();
			assertThat(map.hasLastUpdated()).isFalse();
			assertThat(map.isLastUpdatedRepresentableAsRange()).isTrue();
			assertThat(map.getLastUpdatedAndList()).isNull();
			assertThat(map.toNormalizedQueryString()).isEqualTo(new SearchParameterMap().toNormalizedQueryString());
			return;
		}

		assertThat(got).isNotNull();
		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isTrue();
		assertThat(map.getLastUpdated()).isNotSameAs(got);

		SoftAssertions soft = new SoftAssertions();
		LastUpdatedTestData.assertBound(soft, "lower", orig, got, true);
		LastUpdatedTestData.assertBound(soft, "upper", orig, got, false);
		LastUpdatedTestData.assertInstants(soft, orig, got);
		// the three inputs of SearchQueryBuilder.addPredicateLastUpdated, read from the original range itself
		soft.assertThat(got.getLowerBoundAsInstant()).as("SQL lower").isEqualTo(orig.getLowerBoundAsInstant());
		soft.assertThat(got.getUpperBoundAsInstant()).as("SQL upper").isEqualTo(orig.getUpperBoundAsInstant());
		soft.assertThat(LastUpdatedTestData.bothNotEqual(got)).as("SQL both ne").isEqualTo(LastUpdatedTestData.bothNotEqual(orig));
		soft.assertThat(got.isEmpty()).as("isEmpty").isEqualTo(orig.isEmpty());

		// Cache key: the one of the expected range (normalized, valueless bounds dropped) stored on its own
		SearchParameterMap expectedMap = new SearchParameterMap();
		expectedMap.setLastUpdated(LastUpdatedTestData.expectedRange(orig));
		soft.assertThat(map.toNormalizedQueryString()).as("toNormalizedQueryString").isEqualTo(expectedMap.toNormalizedQueryString());

		// Idempotent: storing what we got gives the same thing
		SearchParameterMap second = new SearchParameterMap();
		second.setLastUpdated(got);
		DateRangeParam got2 = second.getLastUpdated();
		soft.assertThat(got2.getLowerBound() == null ? null : got2.getLowerBound().getValueAsQueryToken())
				.as("idempotent lower")
				.isEqualTo(got.getLowerBound() == null ? null : got.getLowerBound().getValueAsQueryToken());
		soft.assertThat(got2.getUpperBound() == null ? null : got2.getUpperBound().getValueAsQueryToken())
				.as("idempotent upper")
				.isEqualTo(got.getUpperBound() == null ? null : got.getUpperBound().getValueAsQueryToken());
		soft.assertThat(second.toNormalizedQueryString()).as("idempotent key").isEqualTo(map.toNormalizedQueryString());
		soft.assertThat(second).as("equals after re-set").isEqualTo(map);
		soft.assertThat(second.hashCode()).as("hashCode after re-set").isEqualTo(map.hashCode());

		// clone
		SearchParameterMap clone = map.clone();
		soft.assertThat(clone).as("clone equals").isEqualTo(map);
		soft.assertThat(clone.hashCode()).as("clone hashCode").isEqualTo(map.hashCode());
		soft.assertThat(clone.toNormalizedQueryString()).as("clone key").isEqualTo(map.toNormalizedQueryString());

		// the AND list is consistent with the range
		soft.assertThat(map.getLastUpdatedAndList()).as("andList").isNotNull();
		soft.assertAll();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("rangeCases")
	void mutationDoesNotLeak(RangeCase theCase) {
		DateRangeParam orig = theCase.supplier().get();
		if (LastUpdatedTestData.isWrongSide(orig)
				|| LastUpdatedTestData.isNeNeDifferent(orig)
				|| !LastUpdatedTestData.hasAnyValue(orig)) {
			return;
		}
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(orig);
		String key = map.toNormalizedQueryString();
		SearchParameterMap before = map.clone();

		// mutate the range we passed in
		orig.setLowerBound(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, "1900-01-01"));
		orig.setUpperBound(new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, "1950-01-01"));
		assertThat(map.toNormalizedQueryString()).isEqualTo(key);
		assertThat(map).isEqualTo(before);

		// mutate the range we got back
		DateRangeParam got = map.getLastUpdated();
		got.setLowerBound(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, "1900-01-01"));
		got.setUpperBound(new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, "1950-01-01"));
		assertThat(map.toNormalizedQueryString()).isEqualTo(key);
		assertThat(map).isEqualTo(before);

		// mutate the list we got back
		map.getLastUpdatedAndList().addAnd(new ca.uhn.fhir.rest.param.DateOrListParam()
				.addOr(new DateParam(ParamPrefixEnum.GREATERTHAN, "1999")));
		assertThat(map.toNormalizedQueryString()).isEqualTo(key);

		// mutate the clone
		SearchParameterMap clone = before.clone();
		clone.setLastUpdated(new DateRangeParam("1900-01-01", "1901-01-01"));
		assertThat(before.toNormalizedQueryString()).isEqualTo(key);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("rangeCases")
	void javaSerializationNewToNew(RangeCase theCase) throws Exception {
		DateRangeParam orig = theCase.supplier().get();
		if (LastUpdatedTestData.isWrongSide(orig) || LastUpdatedTestData.isNeNeDifferent(orig)) {
			return;
		}
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(orig);
		byte[] bytes = LastUpdatedTestData.serialize(map);
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			SearchParameterMap read = (SearchParameterMap) ois.readObject();
			assertThat(read).isEqualTo(map);
			assertThat(read.toNormalizedQueryString()).isEqualTo(map.toNormalizedQueryString());
			assertThat(read.hasLastUpdated()).isEqualTo(map.hasLastUpdated());
		}
		// serializing must not leave the carrier behind or change the map
		assertThat(map.toNormalizedQueryString()).isEqualTo(read(bytes).toNormalizedQueryString());
	}

	private static SearchParameterMap read(byte[] theBytes) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(theBytes))) {
			return (SearchParameterMap) ois.readObject();
		}
	}

	@Test
	void setLastUpdatedNullClears() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam("2020", "2021"));
		assertThat(map.hasLastUpdated()).isTrue();
		map.setLastUpdated(null);
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.getLastUpdated()).isNull();
	}

	@Test
	void invalidRequestExceptionTypeForNonRepresentable() {
		SearchParameterMap map = new SearchParameterMap();
		ca.uhn.fhir.rest.param.DateAndListParam list = new ca.uhn.fhir.rest.param.DateAndListParam();
		list.addAnd(new ca.uhn.fhir.rest.param.DateOrListParam().addOr(LastUpdatedTestData.token("gt2020")));
		list.addAnd(new ca.uhn.fhir.rest.param.DateOrListParam().addOr(LastUpdatedTestData.token("gt2021")));
		map.setLastUpdatedAndList(list);
		assertThatThrownBy(map::getLastUpdated)
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-3075");
	}
}
