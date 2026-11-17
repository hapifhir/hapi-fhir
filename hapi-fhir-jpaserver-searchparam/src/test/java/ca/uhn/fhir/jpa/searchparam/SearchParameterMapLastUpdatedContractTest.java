package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Contract of the AND/OR API of {@link SearchParameterMap} for <code>_lastUpdated</code>: representability, 400s,
 * equals/hashCode/clone and defensive copies. The expectation of representability is the parser of today
 * ({@link DateRangeParam#setValuesAsQueryTokens}), called directly.
 */
class SearchParameterMapLastUpdatedContractTest {

	private static final List<String> PREFIXES = List.of("", "eq", "ne", "ge", "gt", "sa", "le", "lt", "eb");
	private static final List<String> VALUES = List.of("2020-01-01", "2020-06-15T10:00:00Z", "2021", "2019-03");

	/** AND list as tokens: outer = AND groups, inner = OR alternatives */
	record Expr(List<List<String>> groups) {
		@Override
		public String toString() {
			List<String> parts = new ArrayList<>();
			for (List<String> g : groups) {
				parts.add(String.join(",", g));
			}
			return String.join(" & ", parts);
		}

		DateAndListParam toAndList() {
			DateAndListParam retVal = new DateAndListParam();
			for (List<String> g : groups) {
				DateOrListParam or = new DateOrListParam();
				for (String t : g) {
					or.addOr(LastUpdatedTestData.token(t));
				}
				retVal.addAnd(or);
			}
			return retVal;
		}

		boolean hasAp() {
			return groups.stream().flatMap(List::stream).anyMatch(t -> t.startsWith("ap"));
		}

		/** The parser of today */
		boolean parserAccepts() {
			List<QualifiedParamList> lists = new ArrayList<>();
			for (List<String> g : groups) {
				QualifiedParamList l = new QualifiedParamList();
				l.addAll(g);
				lists.add(l);
			}
			try {
				new DateRangeParam().setValuesAsQueryTokens(null, Constants.PARAM_LASTUPDATED, lists);
				return true;
			} catch (InvalidRequestException e) {
				return false;
			}
		}
	}

	static List<Expr> generate() {
		List<Expr> retVal = new ArrayList<>();
		Random rnd = new Random(8857);
		// all single tokens
		for (String p : PREFIXES) {
			for (String v : VALUES) {
				retVal.add(new Expr(List.of(List.of(p + v))));
			}
		}
		// ap
		for (String v : VALUES) {
			retVal.add(new Expr(List.of(List.of("ap" + v))));
			retVal.add(new Expr(List.of(List.of("ge" + v), List.of("ap" + v))));
			retVal.add(new Expr(List.of(List.of("ge2019", "ap" + v))));
		}
		// pairs of single tokens
		for (String p1 : PREFIXES) {
			for (String p2 : PREFIXES) {
				retVal.add(new Expr(List.of(List.of(p1 + "2019-03"), List.of(p2 + "2021"))));
				retVal.add(new Expr(List.of(List.of(p1 + "2020-01-01"), List.of(p2 + "2020-01-01"))));
				retVal.add(new Expr(List.of(List.of(p1 + "2019-03", p2 + "2021"))));
			}
		}
		// random
		for (int i = 0; i < 700; i++) {
			int ands = 1 + rnd.nextInt(3);
			List<List<String>> groups = new ArrayList<>();
			for (int a = 0; a < ands; a++) {
				int ors = 1 + rnd.nextInt(3);
				List<String> g = new ArrayList<>();
				for (int o = 0; o < ors; o++) {
					g.add(PREFIXES.get(rnd.nextInt(PREFIXES.size())) + VALUES.get(rnd.nextInt(VALUES.size())));
				}
				groups.add(g);
			}
			retVal.add(new Expr(groups));
		}
		return retVal;
	}

	static Stream<Arguments> exprs() {
		return generate().stream().map(Arguments::of);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("exprs")
	void representabilityMatchesTheParser(Expr theExpr) {
		SearchParameterMap map = new SearchParameterMap();
		if (theExpr.hasAp()) {
			assertThatThrownBy(() -> map.setLastUpdatedAndList(theExpr.toAndList()))
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("HAPI-1925");
			assertThat(map.hasLastUpdated()).isFalse();
			return;
		}
		map.setLastUpdatedAndList(theExpr.toAndList());
		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.getLastUpdatedAndList()).isNotNull();
		boolean expected = theExpr.parserAccepts();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isEqualTo(expected);
		if (expected) {
			DateRangeParam range = map.getLastUpdated();
			assertThat(range).isNotNull();
			// same as the parser of today
			DateRangeParam direct = new DateRangeParam();
			List<QualifiedParamList> lists = new ArrayList<>();
			for (List<String> g : theExpr.groups()) {
				QualifiedParamList l = new QualifiedParamList();
				l.addAll(g);
				lists.add(l);
			}
			direct.setValuesAsQueryTokens(null, Constants.PARAM_LASTUPDATED, lists);
			assertThat(range.getLowerBoundAsInstant()).isEqualTo(direct.getLowerBoundAsInstant());
			assertThat(range.getUpperBoundAsInstant()).isEqualTo(direct.getUpperBoundAsInstant());
			assertThat(range.getLowerBound() == null ? null : range.getLowerBound().getValueAsQueryToken())
					.isEqualTo(direct.getLowerBound() == null ? null : direct.getLowerBound().getValueAsQueryToken());
			assertThat(range.getUpperBound() == null ? null : range.getUpperBound().getValueAsQueryToken())
					.isEqualTo(direct.getUpperBound() == null ? null : direct.getUpperBound().getValueAsQueryToken());
			// and the key is the one of that range stored on its own
			SearchParameterMap viaRange = new SearchParameterMap();
			viaRange.setLastUpdated(direct);
			assertThat(map.toNormalizedQueryString()).isEqualTo(viaRange.toNormalizedQueryString());
		} else {
			assertThatThrownBy(map::getLastUpdated)
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("HAPI-3075");
			// the key keeps one _lastUpdated parameter per AND group (never a partial range)
			String key = map.toNormalizedQueryString();
			assertThat(key.split("_lastUpdated=", -1).length - 1)
					.isEqualTo(map.getLastUpdatedAndList().getValuesAsQueryTokens().size());
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("exprs")
	void equalsHashCodeCloneAndCopies(Expr theExpr) throws Exception {
		if (theExpr.hasAp()) {
			return;
		}
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdatedAndList(theExpr.toAndList());
		prime(map);

		// clone
		SearchParameterMap clone = map.clone();
		assertThat(clone).isEqualTo(map);
		assertThat(clone.hashCode()).isEqualTo(map.hashCode());

		// permuted groups and alternatives are equal, with the same hash and key
		List<List<String>> permuted = new ArrayList<>();
		for (List<String> g : theExpr.groups()) {
			List<String> copy = new ArrayList<>(g);
			Collections.reverse(copy);
			permuted.add(copy);
		}
		Collections.reverse(permuted);
		SearchParameterMap other = new SearchParameterMap();
		other.setLastUpdatedAndList(new Expr(permuted).toAndList());
		prime(other);
		assertThat(other).isEqualTo(map);
		assertThat(other.hashCode()).isEqualTo(map.hashCode());

		// a different expression is not equal
		SearchParameterMap different = new SearchParameterMap();
		List<List<String>> more = new ArrayList<>(theExpr.groups());
		more.add(List.of("lt1999"));
		different.setLastUpdatedAndList(new Expr(more).toAndList());
		prime(different);
		assertThat(different).isNotEqualTo(map);
		assertThat(map).isNotEqualTo(prime(new SearchParameterMap()));
		assertThat(prime(new SearchParameterMap())).isNotEqualTo(map);

		// NOTE: toNormalizedQueryString() changes what equals() sees (also in master), so keys are compared after the
		// equality checks
		String key = map.toNormalizedQueryString();
		assertThat(clone.toNormalizedQueryString()).isEqualTo(key);
		assertThat(other.toNormalizedQueryString()).isEqualTo(key);

		// mutating the input after the set does not change the map
		DateAndListParam input = theExpr.toAndList();
		SearchParameterMap viaInput = new SearchParameterMap();
		viaInput.setLastUpdatedAndList(input);
		input.addAnd(new DateOrListParam().addOr(LastUpdatedTestData.token("lt1900")));
		input.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).setValueAsString("1800");
		assertThat(viaInput.toNormalizedQueryString()).isEqualTo(key);

		// mutating the output does not change the map
		DateAndListParam out = map.getLastUpdatedAndList();
		out.addAnd(new DateOrListParam().addOr(LastUpdatedTestData.token("lt1900")));
		out.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).setValueAsString("1800");
		assertThat(map.toNormalizedQueryString()).isEqualTo(key);
		assertThat(map.getLastUpdatedAndList()).isNotSameAs(map.getLastUpdatedAndList());

		// mutating a range we got
		if (map.isLastUpdatedRepresentableAsRange()) {
			DateRangeParam r = map.getLastUpdated();
			r.setLowerBound("1700");
			r.setUpperBound("1800");
			assertThat(map.toNormalizedQueryString()).isEqualTo(key);
		}

		// Java serialization new -> new (representable or not)
		SearchParameterMap fresh = new SearchParameterMap();
		fresh.setLastUpdatedAndList(theExpr.toAndList());
		prime(fresh);
		byte[] bytes = LastUpdatedTestData.serialize(fresh);
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			SearchParameterMap read = prime((SearchParameterMap) ois.readObject());
			assertThat(read).isEqualTo(fresh);
			assertThat(read.hashCode()).isEqualTo(fresh.hashCode());
			assertThat(read.isLastUpdatedRepresentableAsRange()).isEqualTo(map.isLastUpdatedRepresentableAsRange());
			assertThat(read.toNormalizedQueryString()).isEqualTo(key);
		}
		// the map is still usable after being serialized
		assertThat(map.toNormalizedQueryString()).isEqualTo(key);
	}

	/**
	 * <code>toNormalizedQueryString()</code> and <code>clone()</code> initialize lazily some unrelated fields
	 * (<code>myRevIncludes</code>, <code>myIncludes</code>, also in master) which <code>equals()</code> sees. Calling it on every map being compared makes equality depend
	 * only on <code>_lastUpdated</code>.
	 */
	private static SearchParameterMap prime(SearchParameterMap theMap) {
		theMap.clone();
		theMap.toNormalizedQueryString();
		return theMap;
	}

	@Test
	void blankAndMissingValues() {
		SearchParameterMap map = new SearchParameterMap();

		// null and empty
		map.setLastUpdatedAndList(null);
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.getLastUpdated()).isNull();
		assertThat(map.getLastUpdatedAndList()).isNull();
		assertThat(map.isLastUpdatedRepresentableAsRange()).isTrue();
		map.setLastUpdatedAndList(new DateAndListParam());
		assertThat(map.hasLastUpdated()).isFalse();

		// blank values and groups are ignored
		DateAndListParam blanks = new DateAndListParam();
		blanks.addAnd(new DateOrListParam().addOr(new DateParam()));
		blanks.addAnd(new DateOrListParam());
		blanks.addAnd(new DateOrListParam().addOr(LastUpdatedTestData.token("")));
		map.setLastUpdatedAndList(blanks);
		assertThat(map.hasLastUpdated()).isFalse();
		assertThat(map.toNormalizedQueryString()).isEqualTo(new SearchParameterMap().toNormalizedQueryString());

		// :missing alone
		DateParam missing = new DateParam();
		missing.setMissing(true);
		DateAndListParam onlyMissing = new DateAndListParam();
		onlyMissing.addAnd(new DateOrListParam().addOr(missing));
		map.setLastUpdatedAndList(onlyMissing);
		assertThat(map.hasLastUpdated()).isFalse();

		// :missing group next to a real group: the missing group is ignored
		DateAndListParam mixed = new DateAndListParam();
		mixed.addAnd(new DateOrListParam().addOr(missing));
		mixed.addAnd(new DateOrListParam().addOr(LastUpdatedTestData.token("ge2020")));
		map.setLastUpdatedAndList(mixed);
		assertThat(map.hasLastUpdated()).isTrue();
		assertThat(map.getLastUpdatedAndList().getValuesAsQueryTokens()).hasSize(1);
		assertThat(map.getLastUpdated().getLowerBound().getValueAsString()).isEqualTo("2020");

		// :missing inside an OR group: the whole group is ignored (documented)
		DateAndListParam missingInOr = new DateAndListParam();
		missingInOr.addAnd(new DateOrListParam().addOr(missing).addOr(LastUpdatedTestData.token("ge2020")));
		map.setLastUpdatedAndList(missingInOr);
		assertThat(map.hasLastUpdated()).isFalse();

		// setting a new list replaces the old one; null clears
		map.setLastUpdatedAndList(mixed);
		assertThat(map.hasLastUpdated()).isTrue();
		map.setLastUpdatedAndList(null);
		assertThat(map.hasLastUpdated()).isFalse();
	}

	@Test
	void rangeAndListViewsAgree() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam("2020-01-01", "2020-12-31"));
		assertThat(map.getLastUpdatedAndList().getValuesAsQueryTokens()).hasSize(2);
		SearchParameterMap viaList = new SearchParameterMap();
		viaList.setLastUpdatedAndList(map.getLastUpdatedAndList());
		prime(map);
		prime(viaList);
		assertThat(viaList).isEqualTo(map);
		assertThat(viaList.toNormalizedQueryString()).isEqualTo(map.toNormalizedQueryString());
	}
}
