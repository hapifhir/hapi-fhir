package ca.uhn.fhir.util;

import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateRangeUtilTest {

	static Date dateOne = Date.from(Instant.parse("2021-01-01T01:00:00Z"));
	static Date dateTwo = Date.from(Instant.parse("2021-01-01T02:00:00Z"));
	static Date dateThree = Date.from(Instant.parse("2021-01-01T03:00:00Z"));
	static Date dateFour = Date.from(Instant.parse("2021-01-01T04:00:00Z"));
	static Date dateFive = Date.from(Instant.parse("2021-01-01T05:00:00Z"));
	static Date dateSix = Date.from(Instant.parse("2021-01-01T06:00:00Z"));

	static class NarrowCase {
		final String message;
		final DateRangeParam range;
		final Date narrowStart;
		final Date narrowEnd;
		final DateParam resultStart;
		final DateParam resultEnd;

		public NarrowCase(String theMessage, DateRangeParam theRange, Date theNarrowStart, Date theNarrowEnd, DateParam theResultStart, DateParam theResultEnd) {
			message = theMessage;
			range = theRange;
			narrowStart = theNarrowStart;
			narrowEnd = theNarrowEnd;
			resultStart = theResultStart;
			resultEnd = theResultEnd;
		}


		static NarrowCase from(String theMessage, DateRangeParam theRange, Date theNarrowStart, Date theNarrowEnd, Date theResultStart, Date theResultEnd) {
			return new NarrowCase(theMessage, theRange, theNarrowStart, theNarrowEnd,
				theResultStart == null?null:new DateParam(GREATERTHAN_OR_EQUALS, theResultStart),
				theResultEnd == null?null:new DateParam(LESSTHAN, theResultEnd));
		}

		static NarrowCase from(String theMessage, DateRangeParam theRange, Date theNarrowStart, Date theNarrowEnd,
									  ParamPrefixEnum theResultStartPrefix, Date theResultStart, ParamPrefixEnum theResultEndPrefix, Date theResultEnd) {
			return new NarrowCase(theMessage, theRange, theNarrowStart, theNarrowEnd,
				new DateParam(theResultStartPrefix, theResultStart), new DateParam(theResultEndPrefix, theResultEnd));
		}

		static NarrowCase from(String theMessage, DateRangeParam theRange, Date theNarrowStart, Date theNarrowEnd,
									  DateParam theResultStart, DateParam theResultEnd) {
			return new NarrowCase(theMessage, theRange, theNarrowStart, theNarrowEnd, theResultStart, theResultEnd);
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append(message)
				.append("range", range)
				.append("narrowStart", narrowStart)
				.append("narrowEnd", narrowEnd)
				.append("resultStart", resultStart)
				.append("resultEnd", resultEnd)
				.toString();
		}
	}

	static public List<NarrowCase> narrowCases() {

		return Arrays.asList(
			// null range cases
			new NarrowCase("nulls on null yields null", null,  null,null, null, null),
			NarrowCase.from("start and end narrow null", null,  dateTwo,dateThree, dateTwo, dateThree),
			NarrowCase.from("start on null provides open range", null,  dateTwo, null, dateTwo, null),
			NarrowCase.from("end on null provides open range", null,  null,dateThree, null, dateThree),
			// middle range
			// default range is inclusive at top
			NarrowCase.from("start and end outside leaves range unchanged", new DateRangeParam(dateTwo, dateFive),  dateOne, dateSix, GREATERTHAN_OR_EQUALS, dateTwo, LESSTHAN_OR_EQUALS ,dateFive),
			NarrowCase.from("start inside narrows start", new DateRangeParam(dateTwo, dateFive),  dateThree, dateSix, GREATERTHAN_OR_EQUALS, dateThree, LESSTHAN_OR_EQUALS ,dateFive),

			NarrowCase.from("end inside narrows end", new DateRangeParam(dateTwo, dateFive),  dateOne, dateFour, dateTwo, dateFour),
			// half-open cases
			NarrowCase.from("end inside open end", new DateRangeParam(dateTwo, null),  null, dateFour, dateTwo, dateFour),
			NarrowCase.from("start inside open start", new DateRangeParam(null, dateFour),  dateTwo, null, GREATERTHAN_OR_EQUALS, dateTwo, LESSTHAN_OR_EQUALS, dateFour),
			NarrowCase.from("gt case preserved", new DateRangeParam(new DateParam(GREATERTHAN, dateTwo), null),  null, dateFour, GREATERTHAN, dateTwo, LESSTHAN, dateFour),

			NarrowCase.from("lt date level precision date, narrow from is inside date",
				new DateRangeParam(new DateParam(LESSTHAN, "2023-05-06")),
				Date.from(Instant.parse("2023-05-06T10:00:20.512+00:00")),
				Date.from(Instant.parse("2023-05-10T00:00:00.000+00:00")),
				new DateParam(GREATERTHAN_OR_EQUALS, Date.from(Instant.parse("2023-05-06T10:00:20.512+00:00"))),
				new DateParam(LESSTHAN, "2023-05-06")
			),

			NarrowCase.from("gt date level precision date, narrow to is inside date",
				new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, "2023-05-06")),
				Date.from(Instant.parse("2023-05-01T00:00:00.000+00:00")),
				Date.from(Instant.parse("2023-05-06T10:00:20.512+00:00")),
				new DateParam(GREATERTHAN_OR_EQUALS, "2023-05-06"),
				new DateParam(LESSTHAN, Date.from(Instant.parse("2023-05-06T10:00:20.512+00:00")))
			)

		);
	}

	@ParameterizedTest
	@MethodSource("narrowCases")
	public void testNarrowCase(NarrowCase c) {
		DateRangeParam result = DateRangeUtil.narrowDateRange(c.range, c.narrowStart, c.narrowEnd);

		if (c.resultStart == null && c.resultEnd == null) {
			assertNull(result);
		} else {
			assertNotNull(result);
			assertThat(result.getLowerBound()).as("range start").isEqualTo(c.resultStart);
			assertThat(result.getUpperBound()).as("range end").isEqualTo(c.resultEnd);
		}
	}

}
