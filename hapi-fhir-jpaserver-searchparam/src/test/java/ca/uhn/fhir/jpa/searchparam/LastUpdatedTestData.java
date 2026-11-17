package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.assertj.core.api.SoftAssertions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DateTimeType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.APPROXIMATE;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.ENDS_BEFORE;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.STARTS_AFTER;

/**
 * Generates the (large) matrix of {@link DateRangeParam} shapes that can be built today, and the expectations for
 * what {@link SearchParameterMap#setLastUpdated(DateRangeParam)} / {@link SearchParameterMap#getLastUpdated()} give back.
 */
final class LastUpdatedTestData {

	record RangeCase(String name, Supplier<DateRangeParam> supplier) {
		@Override
		public String toString() {
			return name;
		}
	}

	static final List<String> LOW = pool("2019");
	static final List<String> HIGH = pool("2021");
	static final List<String> ALL_VALUES = new ArrayList<>();
	/** Descriptions of the shapes the generator could not build (rejected by DateRangeParam itself) */
	static final List<String> SKIPPED = new ArrayList<>();

	static {
		ALL_VALUES.addAll(LOW);
		ALL_VALUES.addAll(HIGH);
	}

	private LastUpdatedTestData() {}

	private static List<String> pool(String theYear) {
		List<String> candidates = List.of(
				theYear,
				theYear + "-03",
				theYear + "-03-15",
				theYear + "-03-15T10:20:30Z",
				theYear + "-03-15T10:20:30+03:00",
				theYear + "-03-15T10:20:30-05:00",
				theYear + "-03-15T10:20:30",
				theYear + "-03-15T10:20:30.123Z",
				theYear + "-03-15T10:20:30.123+03:00",
				theYear + "-03-15T10:20:30.123-05:00",
				theYear + "-03-15T10:20:30.123");
		List<String> retVal = new ArrayList<>();
		for (String next : candidates) {
			try {
				new DateParam(GREATERTHAN_OR_EQUALS, next).getValue();
				retVal.add(next);
			} catch (Exception e) {
				SKIPPED.add("value " + next + ": " + e.getMessage());
			}
		}
		return retVal;
	}

	private static void add(List<RangeCase> theList, String theName, Supplier<DateRangeParam> theSupplier) {
		try {
			DateRangeParam r = theSupplier.get();
			if (r == null) {
				throw new IllegalStateException("null");
			}
			// force the evaluation of everything a caller could evaluate on the range
			r.toString();
			theList.add(new RangeCase(theName, theSupplier));
		} catch (Exception e) {
			SKIPPED.add(theName + " -> " + e.getClass().getSimpleName());
		}
	}

	private static DateParam dp(ParamPrefixEnum thePrefix, String theValue) {
		return new DateParam(thePrefix, theValue);
	}

	private static DateParam missing() {
		DateParam retVal = new DateParam();
		retVal.setMissing(true);
		return retVal;
	}

	private static DateRangeParam parse(String... theTokens) {
		DateRangeParam retVal = new DateRangeParam();
		List<QualifiedParamList> lists = new ArrayList<>();
		for (String next : theTokens) {
			lists.add(QualifiedParamList.singleton(next));
		}
		retVal.setValuesAsQueryTokens(null, Constants.PARAM_LASTUPDATED, lists);
		return retVal;
	}

	static List<RangeCase> allRangeCases() {
		List<RangeCase> retVal = new ArrayList<>();
		List<ParamPrefixEnum> lowerPrefixes = List.of(GREATERTHAN_OR_EQUALS, GREATERTHAN, STARTS_AFTER, EQUAL, NOT_EQUAL);
		List<ParamPrefixEnum> upperPrefixes = List.of(LESSTHAN_OR_EQUALS, LESSTHAN, ENDS_BEFORE, EQUAL, NOT_EQUAL);
		List<ParamPrefixEnum> lonelyLower = new ArrayList<>(lowerPrefixes);
		lonelyLower.addAll(List.of(ENDS_BEFORE, APPROXIMATE));
		List<ParamPrefixEnum> lonelyUpper = new ArrayList<>(upperPrefixes);
		lonelyUpper.addAll(List.of(STARTS_AFTER, APPROXIMATE));

		// 1. Both bounds, built with setLowerBound/setUpperBound, different values
		for (int k : new int[] {0, 3, 7}) {
			for (int i = 0; i < LOW.size(); i++) {
				String lo = LOW.get(i);
				String hi = HIGH.get((i + k) % HIGH.size());
				for (ParamPrefixEnum lp : lowerPrefixes) {
					for (ParamPrefixEnum up : upperPrefixes) {
						add(retVal, "both(" + lp.getValue() + lo + "," + up.getValue() + hi + ")", () -> {
							DateRangeParam r = new DateRangeParam();
							r.setLowerBound(dp(lp, lo));
							r.setUpperBound(dp(up, hi));
							return r;
						});
					}
				}
			}
		}
		// 2. Both bounds with the same value
		for (String v : ALL_VALUES) {
			for (ParamPrefixEnum lp : lowerPrefixes) {
				for (ParamPrefixEnum up : upperPrefixes) {
					add(retVal, "same(" + lp.getValue() + v + "," + up.getValue() + v + ")", () -> {
						DateRangeParam r = new DateRangeParam();
						r.setLowerBound(dp(lp, v));
						r.setUpperBound(dp(up, v));
						return r;
					});
				}
			}
		}
		// 3. Only lower / only upper (including prefixes of the wrong side)
		for (String v : ALL_VALUES) {
			for (ParamPrefixEnum p : lonelyLower) {
				add(retVal, "lower(" + p.getValue() + v + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setLowerBound(dp(p, v));
					return r;
				});
			}
			for (ParamPrefixEnum p : lonelyUpper) {
				add(retVal, "upper(" + p.getValue() + v + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setUpperBound(dp(p, v));
					return r;
				});
			}
			// 4. Single DateParam constructor, all prefixes
			for (ParamPrefixEnum p : ParamPrefixEnum.values()) {
				add(retVal, "ctorDateParam(" + p.getValue() + v + ")", () -> new DateRangeParam(dp(p, v)));
			}
			add(retVal, "ctorDateParam(noprefix " + v + ")", () -> new DateRangeParam(new DateParam(v)));
			// 5. setLowerBound(String)/setUpperBound(String)
			add(retVal, "setLowerBound(String " + v + ")", () -> new DateRangeParam().setLowerBound(v));
			add(retVal, "setUpperBound(String " + v + ")", () -> new DateRangeParam().setUpperBound(v));
		}
		// 6. String,String, Date,Date, IPrimitiveType and setRangeFromDatesInclusive, all combinations of the pools
		for (int k : new int[] {0, 2, 5, 9}) {
			for (int i = 0; i < LOW.size(); i++) {
				String lo = LOW.get(i);
				String hi = HIGH.get((i + k) % HIGH.size());
				add(retVal, "ctorStrings(" + lo + "," + hi + ")", () -> new DateRangeParam(lo, hi));
				add(retVal, "ctorDates(" + lo + "," + hi + ")", () -> new DateRangeParam(date(lo), date(hi)));
				add(retVal, "ctorDateParams(" + lo + "," + hi + ")", () -> new DateRangeParam(
						dp(GREATERTHAN_OR_EQUALS, lo), dp(LESSTHAN_OR_EQUALS, hi)));
				add(retVal, "ctorPrimitives(" + lo + "," + hi + ")", () -> new DateRangeParam(
						(IPrimitiveType<Date>) new DateTimeType(lo), (IPrimitiveType<Date>) new DateTimeType(hi)));
				add(retVal, "inclusiveDates(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setRangeFromDatesInclusive(date(lo), date(hi));
					return r;
				});
				add(retVal, "inclusiveDateParams(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setRangeFromDatesInclusive(dp(GREATERTHAN_OR_EQUALS, lo), dp(LESSTHAN_OR_EQUALS, hi));
					return r;
				});
				add(retVal, "inclusivePrimitives(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setRangeFromDatesInclusive(
							(IPrimitiveType<Date>) new DateTimeType(lo), (IPrimitiveType<Date>) new DateTimeType(hi));
					return r;
				});
				add(retVal, "inclusiveStrings(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setRangeFromDatesInclusive(lo, hi);
					return r;
				});
				add(retVal, "boundStrings(" + lo + "," + hi + ")", () -> new DateRangeParam()
						.setLowerBound(lo)
						.setUpperBound(hi));
				// Inclusive/Exclusive setters
				add(retVal, "lowerIncl+upperIncl(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setLowerBoundInclusive(date(lo));
					r.setUpperBoundInclusive(date(hi));
					return r;
				});
				add(retVal, "lowerExcl+upperExcl(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setLowerBoundExclusive(date(lo));
					r.setUpperBoundExclusive(date(hi));
					return r;
				});
				add(retVal, "lowerIncl+upperExcl(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setLowerBoundInclusive(date(lo));
					r.setUpperBoundExclusive(date(hi));
					return r;
				});
				add(retVal, "lowerExcl+upperIncl(" + lo + "," + hi + ")", () -> {
					DateRangeParam r = new DateRangeParam();
					r.setLowerBoundExclusive(date(lo));
					r.setUpperBoundInclusive(date(hi));
					return r;
				});
			}
		}
		for (String v : ALL_VALUES) {
			add(retVal, "ctorStrings(same " + v + ")", () -> new DateRangeParam(v, v));
			add(retVal, "ctorDates(same " + v + ")", () -> new DateRangeParam(date(v), date(v)));
			add(retVal, "ctorString(null," + v + ")", () -> new DateRangeParam((String) null, v));
			add(retVal, "ctorString(" + v + ",null)", () -> new DateRangeParam(v, (String) null));
			add(retVal, "ctorDate(" + v + ",null)", () -> new DateRangeParam(date(v), null));
			add(retVal, "ctorDate(null," + v + ")", () -> new DateRangeParam(null, date(v)));
			add(retVal, "lowerIncl(" + v + ")", () -> {
				DateRangeParam r = new DateRangeParam();
				r.setLowerBoundInclusive(date(v));
				return r;
			});
			add(retVal, "lowerExcl(" + v + ")", () -> {
				DateRangeParam r = new DateRangeParam();
				r.setLowerBoundExclusive(date(v));
				return r;
			});
			add(retVal, "upperIncl(" + v + ")", () -> {
				DateRangeParam r = new DateRangeParam();
				r.setUpperBoundInclusive(date(v));
				return r;
			});
			add(retVal, "upperExcl(" + v + ")", () -> {
				DateRangeParam r = new DateRangeParam();
				r.setUpperBoundExclusive(date(v));
				return r;
			});
		}
		// 7. Empty and :missing
		add(retVal, "empty()", DateRangeParam::new);
		add(retVal, "emptyBothNull", () -> new DateRangeParam().setLowerBound((DateParam) null));
		add(retVal, "missing(lower)", () -> new DateRangeParam().setLowerBound(missing()));
		add(retVal, "missing(upper)", () -> new DateRangeParam().setUpperBound(missing()));
		add(retVal, "missing(both)", () -> new DateRangeParam().setLowerBound(missing()).setUpperBound(missing()));
		for (String v : ALL_VALUES) {
			for (ParamPrefixEnum p : List.of(GREATERTHAN_OR_EQUALS, GREATERTHAN, EQUAL, NOT_EQUAL)) {
				add(retVal, "missing(lower)+upper(" + p.getValue() + v + ")", () -> new DateRangeParam()
						.setLowerBound(missing())
						.setUpperBound(dp(p == GREATERTHAN_OR_EQUALS ? LESSTHAN_OR_EQUALS : p, v)));
				add(retVal, "lower(" + p.getValue() + v + ")+missing(upper)", () -> new DateRangeParam()
						.setLowerBound(dp(p, v))
						.setUpperBound(missing()));
			}
		}
		// 8. What the parser produces for a search URL (a single value, or two bounds)
		for (String v : ALL_VALUES) {
			for (String p : List.of("", "eq", "ne", "ge", "gt", "sa", "le", "lt", "eb")) {
				add(retVal, "parsed(" + p + v + ")", () -> parse(p + v));
			}
		}
		for (int k : new int[] {0, 4, 8}) {
			for (int i = 0; i < LOW.size(); i++) {
				String lo = LOW.get(i);
				String hi = HIGH.get((i + k) % HIGH.size());
				for (String lp : List.of("ge", "gt", "sa")) {
					for (String up : List.of("le", "lt", "eb")) {
						add(retVal, "parsed(" + lp + lo + "&" + up + hi + ")", () -> parse(lp + lo, up + hi));
					}
				}
			}
		}
		return retVal;
	}

	static byte[] serialize(Object theObject) {
		try {
			java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
			try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos)) {
				oos.writeObject(theObject);
			}
			return bos.toByteArray();
		} catch (java.io.IOException e) {
			throw new IllegalStateException(e);
		}
	}

	static DateParam token(String theToken) {
		DateParam retVal = new DateParam();
		retVal.setValueAsQueryToken(null, Constants.PARAM_LASTUPDATED, null, theToken);
		return retVal;
	}

	static Date date(String theValue) {
		return new DateParam(theValue).getValue();
	}

	// ---- Expectations ------------------------------------------------------------------------------------------

	private static boolean hasValue(DateParam theBound) {
		return theBound != null && theBound.getValue() != null;
	}

	static boolean neutral(ParamPrefixEnum thePrefix) {
		return thePrefix == null || thePrefix == EQUAL || thePrefix == NOT_EQUAL;
	}

	static boolean isWrongSide(DateRangeParam theRange) {
		DateParam lo = theRange.getLowerBound();
		DateParam up = theRange.getUpperBound();
		Set<ParamPrefixEnum> wrongLower = Set.of(LESSTHAN, LESSTHAN_OR_EQUALS, ENDS_BEFORE, APPROXIMATE);
		Set<ParamPrefixEnum> wrongUpper = Set.of(GREATERTHAN, GREATERTHAN_OR_EQUALS, STARTS_AFTER, APPROXIMATE);
		return (hasValue(lo) && lo.getPrefix() != null && wrongLower.contains(lo.getPrefix()))
				|| (hasValue(up) && up.getPrefix() != null && wrongUpper.contains(up.getPrefix()));
	}

	static boolean isSingleValue(DateRangeParam theRange) {
		DateParam lo = theRange.getLowerBound();
		DateParam up = theRange.getUpperBound();
		return hasValue(lo)
				&& hasValue(up)
				&& Objects.equals(lo.getPrefix(), up.getPrefix())
				&& neutral(lo.getPrefix())
				&& Objects.equals(lo.getValueAsString(), up.getValueAsString());
	}

	static boolean isNeNeDifferent(DateRangeParam theRange) {
		DateParam lo = theRange.getLowerBound();
		DateParam up = theRange.getUpperBound();
		return hasValue(lo)
				&& hasValue(up)
				&& lo.getPrefix() == NOT_EQUAL
				&& up.getPrefix() == NOT_EQUAL
				&& !Objects.equals(lo.getValueAsString(), up.getValueAsString());
	}

	static ParamPrefixEnum expectedPrefix(DateRangeParam theOrig, boolean theLower) {
		DateParam bound = theLower ? theOrig.getLowerBound() : theOrig.getUpperBound();
		if (isSingleValue(theOrig)) {
			return bound.getPrefix();
		}
		if (neutral(bound.getPrefix())) {
			return theLower ? GREATERTHAN_OR_EQUALS : LESSTHAN_OR_EQUALS;
		}
		return bound.getPrefix();
	}

	/** True if a bound with a value has a different prefix in the expected result (the shapes normalized in section 15) */
	static boolean isNormalized(DateRangeParam theOrig) {
		for (boolean lower : new boolean[] {true, false}) {
			DateParam bound = lower ? theOrig.getLowerBound() : theOrig.getUpperBound();
			if (hasValue(bound) && !Objects.equals(bound.getPrefix(), expectedPrefix(theOrig, lower))) {
				return true;
			}
		}
		return false;
	}

	/** The range that master would hold for the expected (normalized, with valueless bounds dropped) result */
	static DateRangeParam expectedRange(DateRangeParam theOrig) {
		DateRangeParam retVal = new DateRangeParam();
		DateParam lo = theOrig.getLowerBound();
		DateParam up = theOrig.getUpperBound();
		if (isSingleValue(theOrig)) {
			// the parser produces both bounds from the same token
			retVal = parse(lo.getPrefix() == null ? lo.getValueAsString() : lo.getPrefix().getValue() + lo.getValueAsString());
			return retVal;
		}
		if (hasValue(lo)) {
			retVal.setLowerBound(new DateParam(expectedPrefix(theOrig, true), lo.getValueAsString()));
		}
		if (hasValue(up)) {
			retVal.setUpperBound(new DateParam(expectedPrefix(theOrig, false), up.getValueAsString()));
		}
		return retVal;
	}

	static boolean hasAnyValue(DateRangeParam theRange) {
		return hasValue(theRange.getLowerBound()) || hasValue(theRange.getUpperBound());
	}

	static void assertBound(
			SoftAssertions theSoft, String theLabel, DateRangeParam theOrig, DateRangeParam theGot, boolean theLower) {
		DateParam orig = theLower ? theOrig.getLowerBound() : theOrig.getUpperBound();
		DateParam got = theLower ? theGot.getLowerBound() : theGot.getUpperBound();
		if (!hasValue(orig)) {
			theSoft.assertThat(hasValue(got)).as(theLabel + " valueless bound must be dropped").isFalse();
			return;
		}
		theSoft.assertThat(got).as(theLabel + " bound exists").isNotNull();
		if (got == null) {
			return;
		}
		theSoft.assertThat(got.getPrefix()).as(theLabel + " prefix").isEqualTo(expectedPrefix(theOrig, theLower));
		theSoft.assertThat(got.getValueAsString()).as(theLabel + " valueAsString").isEqualTo(orig.getValueAsString());
		theSoft.assertThat(got.getPrecision()).as(theLabel + " precision").isEqualTo(orig.getPrecision());
		theSoft.assertThat(got.getMissing()).as(theLabel + " missing").isEqualTo(orig.getMissing());
		theSoft.assertThat(got.getValue()).as(theLabel + " value").isEqualTo(orig.getValue());
	}

	/** Same rule as SearchQueryBuilder.isNotEqualsComparator, which decides the shape of the SQL */
	static boolean bothNotEqual(DateRangeParam theRange) {
		DateParam lb = theRange.getLowerBound();
		DateParam ub = theRange.getUpperBound();
		return lb != null && ub != null && NOT_EQUAL.equals(lb.getPrefix()) && NOT_EQUAL.equals(ub.getPrefix());
	}

	/**
	 * SearchQueryBuilder.addPredicateLastUpdated depends only on these three values: the SQL of the original range
	 * and of the stored one is the same iff they are identical.
	 */
	static void assertInstants(SoftAssertions theSoft, DateRangeParam theOrig, DateRangeParam theGot) {
		theSoft.assertThat(bothNotEqual(theGot)).as("SQL shape (both ne)").isEqualTo(bothNotEqual(theOrig));
		theSoft.assertThat(theGot.getLowerBoundAsInstant())
				.as("lower instant")
				.isEqualTo(theOrig.getLowerBoundAsInstant());
		theSoft.assertThat(theGot.getUpperBoundAsInstant())
				.as("upper instant")
				.isEqualTo(theOrig.getUpperBoundAsInstant());
	}
}
