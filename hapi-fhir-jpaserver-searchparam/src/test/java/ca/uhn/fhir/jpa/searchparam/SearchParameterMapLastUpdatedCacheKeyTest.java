package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The cache key (<code>toNormalizedQueryString()</code>) of every <code>_lastUpdated</code> range that could be built
 * before AND/OR support, as recorded from the <code>SearchParameterMap</code> of the base commit of this change. The
 * key must not change, so that an existing cached search is still found, except for the ranges that are normalized
 * (<code>eq</code>/<code>ne</code> on one bound of a two-value range, which become <code>ge</code>/<code>le</code>);
 * for those, the expected key is the one of the normalized range.
 */
class SearchParameterMapLastUpdatedCacheKeyTest {

	private static final String P = "?_lastUpdated=";

	/** spec, expected key. Specs: see {@link #build(String)}. */
	static final String[][] GOLDEN = {
		{"lo:ge2019", "?_lastUpdated=ge2019"},
		{"lo:ge2019-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30+03:00"},
		{"up:le2021", "?_lastUpdated=le2021"},
		{"up:le2021-03-15T10:20:30+03:00", "?_lastUpdated=le2021-03-15T10:20:30+03:00"},
		{"lo:gt2019-03", "?_lastUpdated=ge2019-03"},
		{"lo:gt2019-03-15T10:20:30-05:00", "?_lastUpdated=ge2019-03-15T10:20:30-05:00"},
		{"up:lt2021-03", "?_lastUpdated=le2021-03"},
		{"up:lt2021-03-15T10:20:30-05:00", "?_lastUpdated=le2021-03-15T10:20:30-05:00"},
		{"lo:sa2019-03-15", "?_lastUpdated=ge2019-03-15"},
		{"lo:sa2019-03-15T10:20:30", "?_lastUpdated=ge2019-03-15T10:20:30"},
		{"up:eb2021-03-15", "?_lastUpdated=le2021-03-15"},
		{"up:eb2021-03-15T10:20:30", "?_lastUpdated=le2021-03-15T10:20:30"},
		{"lo:eq2019-03-15T10:20:30Z", "?_lastUpdated=ge2019-03-15T10:20:30Z"}, // normalized
		{"lo:eq2019-03-15T10:20:30.123Z", "?_lastUpdated=ge2019-03-15T10:20:30.123Z"}, // normalized
		{"up:eq2021-03-15T10:20:30Z", "?_lastUpdated=le2021-03-15T10:20:30Z"}, // normalized
		{"up:eq2021-03-15T10:20:30.123Z", "?_lastUpdated=le2021-03-15T10:20:30.123Z"}, // normalized
		{"lo:ne2019-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30+03:00"}, // normalized
		{"lo:ne2019-03-15T10:20:30.123+03:00", "?_lastUpdated=ge2019-03-15T10:20:30.123+03:00"}, // normalized
		{"up:ne2021-03-15T10:20:30+03:00", "?_lastUpdated=le2021-03-15T10:20:30+03:00"}, // normalized
		{"up:ne2021-03-15T10:20:30.123-05:00", "?_lastUpdated=le2021-03-15T10:20:30.123-05:00"}, // normalized
		{"both:ge2019|le2021-03", "?_lastUpdated=ge2019&_lastUpdated=le2021-03"},
		{"both:ge2019-03|lt2021-03-15T10:20:30-05:00", "?_lastUpdated=ge2019-03&_lastUpdated=le2021-03-15T10:20:30-05:00"},
		{"both:ge2019-03-15|eb2021", "?_lastUpdated=ge2019-03-15&_lastUpdated=le2021"},
		{"both:ge2019-03-15T10:20:30Z|eq2021-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30Z&_lastUpdated=le2021-03-15T10:20:30+03:00"}, // normalized
		{"both:ge2019-03-15T10:20:30+03:00|ne2021-03-15T10:20:30.123-05:00", "?_lastUpdated=ge2019-03-15T10:20:30+03:00&_lastUpdated=le2021-03-15T10:20:30.123-05:00"}, // normalized
		{"both:gt2019-03-15T10:20:30-05:00|le2021-03-15T10:20:30Z", "?_lastUpdated=ge2019-03-15T10:20:30-05:00&_lastUpdated=le2021-03-15T10:20:30Z"},
		{"both:gt2019-03-15T10:20:30|lt2021-03-15T10:20:30.123Z", "?_lastUpdated=ge2019-03-15T10:20:30&_lastUpdated=le2021-03-15T10:20:30.123Z"},
		{"both:gt2019-03-15T10:20:30.123Z|eb2021-03-15", "?_lastUpdated=ge2019-03-15T10:20:30.123Z&_lastUpdated=le2021-03-15"},
		{"both:gt2019-03-15T10:20:30.123+03:00|eq2021-03-15T10:20:30", "?_lastUpdated=ge2019-03-15T10:20:30.123+03:00&_lastUpdated=le2021-03-15T10:20:30"}, // normalized
		{"both:gt2019|ne2021-03", "?_lastUpdated=ge2019&_lastUpdated=le2021-03"}, // normalized
		{"both:sa2019-03|le2021-03-15T10:20:30-05:00", "?_lastUpdated=ge2019-03&_lastUpdated=le2021-03-15T10:20:30-05:00"},
		{"both:sa2019-03-15|lt2021", "?_lastUpdated=ge2019-03-15&_lastUpdated=le2021"},
		{"both:sa2019-03-15T10:20:30Z|eb2021-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30Z&_lastUpdated=le2021-03-15T10:20:30+03:00"},
		{"both:sa2019-03-15T10:20:30+03:00|eq2021-03-15T10:20:30.123-05:00", "?_lastUpdated=ge2019-03-15T10:20:30+03:00&_lastUpdated=le2021-03-15T10:20:30.123-05:00"}, // normalized
		{"both:sa2019-03-15T10:20:30-05:00|ne2021-03-15T10:20:30Z", "?_lastUpdated=ge2019-03-15T10:20:30-05:00&_lastUpdated=le2021-03-15T10:20:30Z"}, // normalized
		{"both:eq2019-03-15T10:20:30|le2021-03-15T10:20:30.123Z", "?_lastUpdated=ge2019-03-15T10:20:30&_lastUpdated=le2021-03-15T10:20:30.123Z"}, // normalized
		{"both:eq2019-03-15T10:20:30.123Z|lt2021-03-15", "?_lastUpdated=ge2019-03-15T10:20:30.123Z&_lastUpdated=le2021-03-15"}, // normalized
		{"both:eq2019-03-15T10:20:30.123+03:00|eb2021-03-15T10:20:30", "?_lastUpdated=ge2019-03-15T10:20:30.123+03:00&_lastUpdated=le2021-03-15T10:20:30"}, // normalized
		{"both:eq2019|eq2021-03", "?_lastUpdated=ge2019&_lastUpdated=le2021-03"}, // normalized
		{"both:eq2019-03|ne2021-03-15T10:20:30-05:00", "?_lastUpdated=ge2019-03&_lastUpdated=le2021-03-15T10:20:30-05:00"}, // normalized
		{"both:ne2019-03-15|le2021", "?_lastUpdated=ge2019-03-15&_lastUpdated=le2021"}, // normalized
		{"both:ne2019-03-15T10:20:30Z|lt2021-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30Z&_lastUpdated=le2021-03-15T10:20:30+03:00"}, // normalized
		{"both:ne2019-03-15T10:20:30+03:00|eb2021-03-15T10:20:30.123-05:00", "?_lastUpdated=ge2019-03-15T10:20:30+03:00&_lastUpdated=le2021-03-15T10:20:30.123-05:00"}, // normalized
		{"both:ne2019-03-15T10:20:30-05:00|eq2021-03-15T10:20:30Z", "?_lastUpdated=ge2019-03-15T10:20:30-05:00&_lastUpdated=le2021-03-15T10:20:30Z"}, // normalized
		{"parse:2019-03-15T10:20:30", "?_lastUpdated=ge2019-03-15T10:20:30&_lastUpdated=le2019-03-15T10:20:30"},
		{"parse:eq2019-03-15T10:20:30.123Z", "?_lastUpdated=ge2019-03-15T10:20:30.123Z&_lastUpdated=le2019-03-15T10:20:30.123Z"},
		{"parse:ne2019-03-15T10:20:30.123+03:00", "?_lastUpdated=ne2019-03-15T10:20:30.123+03:00"},
		{"parse:ge2019", "?_lastUpdated=ge2019"},
		{"parse:gt2019-03", "?_lastUpdated=ge2019-03"},
		{"parse:sa2019-03-15", "?_lastUpdated=ge2019-03-15"},
		{"parse:le2019-03-15T10:20:30Z", "?_lastUpdated=le2019-03-15T10:20:30Z"},
		{"parse:lt2019-03-15T10:20:30+03:00", "?_lastUpdated=le2019-03-15T10:20:30+03:00"},
		{"parse:eb2019-03-15T10:20:30-05:00", "?_lastUpdated=le2019-03-15T10:20:30-05:00"},
		{"parse:ge2019|le2021", "?_lastUpdated=ge2019&_lastUpdated=le2021"},
		{"parse:gt2019-03-15T10:20:30Z|lt2021-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30Z&_lastUpdated=le2021-03-15T10:20:30+03:00"},
		{"parse:sa2019-03|eb2021-03", "?_lastUpdated=ge2019-03&_lastUpdated=le2021-03"},
		{"both:eq2020|eq2020", "?_lastUpdated=ge2020&_lastUpdated=le2020"},
		{"both:ne2020-05-05|ne2020-05-05", "?_lastUpdated=ne2020-05-05"},
		{"both:ge2020-05-05T10:00:00Z|le2020-05-05T10:00:00Z", "?_lastUpdated=ge2020-05-05T10:00:00Z&_lastUpdated=le2020-05-05T10:00:00Z"},
		{"str:2019|2021", "?_lastUpdated=ge2019&_lastUpdated=le2021"},
		{"str:2019-03-15T10:20:30Z|2021-03-15T10:20:30+03:00", "?_lastUpdated=ge2019-03-15T10:20:30Z&_lastUpdated=le2021-03-15T10:20:30+03:00"},
		{"str:2020|2020", "?_lastUpdated=ge2020&_lastUpdated=le2020"},
		{"date:2019-03-15T10:20:30Z|2021-03-15T10:20:30Z", "?_lastUpdated=ge2019-03-15T07:20:30.000-03:00&_lastUpdated=le2021-03-15T07:20:30.000-03:00"},
		{"date:2019-03-15T10:20:30.123-05:00|2021-03-15T10:20:30.123+03:00", "?_lastUpdated=ge2019-03-15T12:20:30.123-03:00&_lastUpdated=le2021-03-15T04:20:30.123-03:00"},
		{"one:2020-05", "?_lastUpdated=ge2020-05&_lastUpdated=le2020-05"},
		{"one:ne2020-05-05", "?_lastUpdated=ge2020-05-05&_lastUpdated=le2020-05-05"},
		{"one:gt2020", "?_lastUpdated=ge2020-12-31"},
		{"one:le2020-05-05T10:20:30Z", "?_lastUpdated=le2020-05-05T10:20:30Z"},
		{"miss:lo", "?"}, // :missing bound ignored
		{"miss:up", "?"}, // :missing bound ignored
		{"empty:", "?"},
	};

	static Stream<Arguments> golden() {
		List<Arguments> retVal = new ArrayList<>();
		for (String[] next : GOLDEN) {
			retVal.add(Arguments.of(next[0], next[1]));
		}
		return retVal.stream();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("golden")
	void keyIsTheOneOfTheBaseCommit(String theSpec, String theExpectedKey) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(build(theSpec));
		assertThat(map.toNormalizedQueryString()).isEqualTo(theExpectedKey);
		// and stable when set again from what we read back
		SearchParameterMap again = new SearchParameterMap();
		again.setLastUpdated(map.getLastUpdated());
		assertThat(again.toNormalizedQueryString()).isEqualTo(theExpectedKey);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("nonRepresentable")
	void nonRepresentableListUsesTheCanonicalForm(String theName, String[][] theGroups, String theExpectedKey) {
		SearchParameterMap map = new SearchParameterMap();
		DateAndListParam list = new DateAndListParam();
		for (String[] group : theGroups) {
			DateOrListParam or = new DateOrListParam();
			for (String token : group) {
				or.addOr(LastUpdatedTestData.token(token));
			}
			list.addAnd(or);
		}
		map.setLastUpdatedAndList(list);
		assertThat(map.isLastUpdatedRepresentableAsRange()).isFalse();
		assertThat(map.toNormalizedQueryString()).isEqualTo(theExpectedKey);
	}

	static Stream<Arguments> nonRepresentable() {
		return Stream.of(
				Arguments.of("two gt", new String[][] {{"gt2020"}, {"gt2021"}}, P + "gt2020&_lastUpdated=gt2021"),
				Arguments.of("two gt reversed", new String[][] {{"gt2021"}, {"gt2020"}}, P + "gt2020&_lastUpdated=gt2021"),
				Arguments.of("or", new String[][] {{"lt2020", "gt2021"}}, P + "gt2021,lt2020"),
				Arguments.of("or reversed", new String[][] {{"gt2021", "lt2020"}}, P + "gt2021,lt2020"),
				Arguments.of(
						"or and ne", new String[][] {{"lt2020", "gt2021"}, {"ne2019"}}, P + "gt2021,lt2020&_lastUpdated=ne2019"),
				Arguments.of("two ne", new String[][] {{"ne2020"}, {"ne2021"}}, P + "ne2020&_lastUpdated=ne2021"),
				Arguments.of("two eq or", new String[][] {{"eq2020", "eq2021"}}, P + "eq2020,eq2021"));
	}

	private static ParamPrefixEnum prefix(String theToken) {
		return theToken.length() > 2 && Character.isLetter(theToken.charAt(0))
				? ParamPrefixEnum.forValue(theToken.substring(0, 2))
				: null;
	}

	private static String value(String theToken) {
		return prefix(theToken) == null ? theToken : theToken.substring(2);
	}

	private static DateParam dp(String theToken) {
		return new DateParam(prefix(theToken), value(theToken));
	}

	private static DateParam missing() {
		DateParam retVal = new DateParam();
		retVal.setMissing(true);
		return retVal;
	}

	/**
	 * Builds a range from a spec of the form <code>kind:args</code> (args separated by <code>|</code>):
	 * <ul>
	 * <li><code>lo:ge2019</code>, <code>up:le2021</code>, <code>both:ge2019|le2021</code>: setLowerBound/setUpperBound(DateParam)</li>
	 * <li><code>parse:ne2020</code>, <code>parse:ge2019|le2021</code>: as the REST layer binds the tokens</li>
	 * <li><code>str:a|b</code>: new DateRangeParam(String, String); <code>date:a|b</code>: (Date, Date)</li>
	 * <li><code>one:ne2020</code>: new DateRangeParam(DateParam)</li>
	 * <li><code>miss:lo</code>, <code>miss:up</code></li>
	 * <li><code>empty:</code></li>
	 * </ul>
	 */
	static DateRangeParam build(String theSpec) {
		String kind = theSpec.substring(0, theSpec.indexOf(':'));
		String[] args = theSpec.substring(theSpec.indexOf(':') + 1).split("\\|");
		switch (kind) {
			case "lo":
				return new DateRangeParam().setLowerBound(dp(args[0]));
			case "up":
				return new DateRangeParam().setUpperBound(dp(args[0]));
			case "both":
				return new DateRangeParam().setLowerBound(dp(args[0])).setUpperBound(dp(args[1]));
			case "parse":
				DateRangeParam parsed = new DateRangeParam();
				List<QualifiedParamList> lists = new ArrayList<>();
				for (String a : args) {
					lists.add(QualifiedParamList.singleton(a));
				}
				parsed.setValuesAsQueryTokens(null, Constants.PARAM_LASTUPDATED, lists);
				return parsed;
			case "str":
				return new DateRangeParam(args[0], args[1]);
			case "date":
				return new DateRangeParam(LastUpdatedTestData.date(args[0]), LastUpdatedTestData.date(args[1]));
			case "one":
				return new DateRangeParam(dp(args[0]));
			case "miss":
				DateRangeParam r = new DateRangeParam();
				if (args[0].equals("lo") || args[0].equals("both")) {
					r.setLowerBound(missing());
				}
				if (args[0].equals("up") || args[0].equals("both")) {
					r.setUpperBound(missing());
				}
				if (args.length > 1) {
					DateParam other = dp(args[1]);
					if (args[0].equals("lo")) {
						r.setUpperBound(other);
					} else {
						r.setLowerBound(other);
					}
				}
				return r;
			case "empty":
				return new DateRangeParam();
			default:
				throw new IllegalArgumentException(theSpec);
		}
	}

	/** The specs recorded in {@link #GOLDEN} (also used to regenerate it) */
	static List<String> specs() {
		List<String> s = new ArrayList<>();
		String[] lows = {"2019", "2019-03", "2019-03-15", "2019-03-15T10:20:30Z", "2019-03-15T10:20:30+03:00", "2019-03-15T10:20:30-05:00", "2019-03-15T10:20:30", "2019-03-15T10:20:30.123Z", "2019-03-15T10:20:30.123+03:00"};
		String[] highs = {"2021", "2021-03", "2021-03-15", "2021-03-15T10:20:30Z", "2021-03-15T10:20:30+03:00", "2021-03-15T10:20:30-05:00", "2021-03-15T10:20:30", "2021-03-15T10:20:30.123Z", "2021-03-15T10:20:30.123-05:00"};
		String[] lowPrefixes = {"ge", "gt", "sa", "eq", "ne"};
		String[] upPrefixes = {"le", "lt", "eb", "eq", "ne"};
		// lower only / upper only: every prefix accepted on each side, rotating precision and zone
		for (int i = 0; i < lowPrefixes.length; i++) {
			s.add("lo:" + lowPrefixes[i] + lows[i]);
			s.add("lo:" + lowPrefixes[i] + lows[i + 4]);
			s.add("up:" + upPrefixes[i] + highs[i]);
			s.add("up:" + upPrefixes[i] + highs[i + 4]);
		}
		// both bounds, every prefix pair, rotating the values
		int n = 0;
		for (String lp : lowPrefixes) {
			for (String up : upPrefixes) {
				if (lp.equals("ne") && up.equals("ne")) {
					continue;
				}
				s.add("both:" + lp + lows[n % lows.length] + "|" + up + highs[(n * 4 + 1) % highs.length]);
				n++;
			}
		}
		// single values as the parser binds them, every prefix and precision
		for (String p : new String[] {"", "eq", "ne", "ge", "gt", "sa", "le", "lt", "eb"}) {
			s.add("parse:" + p + lows[(n++) % lows.length]);
		}
		s.add("parse:ge2019|le2021");
		s.add("parse:gt2019-03-15T10:20:30Z|lt2021-03-15T10:20:30+03:00");
		s.add("parse:sa2019-03|eb2021-03");
		// same value on both bounds
		s.add("both:eq2020|eq2020");
		s.add("both:ne2020-05-05|ne2020-05-05");
		s.add("both:ge2020-05-05T10:00:00Z|le2020-05-05T10:00:00Z");
		// constructors and DateParam single constructor
		s.add("str:2019|2021");
		s.add("str:2019-03-15T10:20:30Z|2021-03-15T10:20:30+03:00");
		s.add("str:2020|2020");
		s.add("date:2019-03-15T10:20:30Z|2021-03-15T10:20:30Z");
		s.add("date:2019-03-15T10:20:30.123-05:00|2021-03-15T10:20:30.123+03:00");
		s.add("one:2020-05");
		s.add("one:ne2020-05-05");
		s.add("one:gt2020");
		s.add("one:le2020-05-05T10:20:30Z");
		// :missing and empty
		s.add("miss:lo");
		s.add("miss:up");
		s.add("empty:");
		return s;
	}
}
