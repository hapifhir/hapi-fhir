package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r5.model.BaseDateTimeType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class InMemoryResourceMatcherR5Test {
	public static final String OBSERVATION_DATE = "1970-10-17";
	public static final String OBSERVATION_DATETIME = OBSERVATION_DATE + "T01:00:00-08:30";
	public static final String OBSERVATION_CODE = "MATCH";
	private static final String EARLY_DATE = "1965-08-09";
	private static final String LATE_DATE = "2000-06-29";
	private static final String EARLY_DATETIME = EARLY_DATE + "T12:00:00Z";
	private static final String LATE_DATETIME = LATE_DATE + "T12:00:00Z";
	private static final String SOURCE_URI = "urn:source:0";
	private static final String REQUEST_ID = "a_request_id";
	private static final String TEST_SOURCE = SOURCE_URI + "#" + REQUEST_ID;

	@MockBean
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	private Observation myObservation;
	private ResourceIndexedSearchParams mySearchParams;

	@BeforeEach
	public void before() {
		RuntimeSearchParam dateSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.effective", RestSearchParameterTypeEnum.DATE, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "date")).thenReturn(dateSearchParam);

		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.code", RestSearchParameterTypeEnum.TOKEN, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "code")).thenReturn(codeSearchParam);

		RuntimeSearchParam encSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.encounter", RestSearchParameterTypeEnum.REFERENCE, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, null, null, null);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "encounter")).thenReturn(encSearchParam);

		myObservation = new Observation();
		myObservation.getMeta().setSource(TEST_SOURCE);
		myObservation.setEffective(new DateTimeType(OBSERVATION_DATETIME));
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding().setCode(OBSERVATION_CODE);
		myObservation.setCode(codeableConcept);
		mySearchParams = extractDateSearchParam(myObservation);
	}

	@Test
	public void testSupportedSource() {
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + TEST_SOURCE, myObservation, mySearchParams);
			assertTrue(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + SOURCE_URI, myObservation, mySearchParams);
			assertTrue(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + REQUEST_ID, myObservation, mySearchParams);
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=#" + REQUEST_ID, myObservation, mySearchParams);
			assertTrue(result.matched());
		}
	}

	@Test
	public void testSupportedSource_ResourceWithNoSourceValue() {
		myObservation.getMeta().getSourceElement().setValue(null);
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + TEST_SOURCE, myObservation, mySearchParams);
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + SOURCE_URI, myObservation, mySearchParams);
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + REQUEST_ID, myObservation, mySearchParams);
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=#" + REQUEST_ID, myObservation, mySearchParams);
			assertFalse(result.matched());
		}
	}

	@Test
	public void testUnsupportedChained() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("encounter.class=FOO", myObservation, mySearchParams);
		assertFalse(result.supported());
		assertEquals("Parameter: <encounter.class> Reason: Chained parameters are not supported", result.getUnsupportedReason());
	}

	@Test
	public void testUnsupportedNot() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.NOT.getValue() + "=" + OBSERVATION_CODE, myObservation, mySearchParams);
		assertFalse(result.supported());
		assertEquals("Parameter: <code:not> Reason: Qualified parameter not supported", result.getUnsupportedReason());
	}

	@Test
	public void testDateUnsupportedDateOps() {
		testDateUnsupportedDateOp(ParamPrefixEnum.APPROXIMATE);
		testDateUnsupportedDateOp(ParamPrefixEnum.STARTS_AFTER);
		testDateUnsupportedDateOp(ParamPrefixEnum.ENDS_BEFORE);
		testDateUnsupportedDateOp(ParamPrefixEnum.NOT_EQUAL);
	}

	private void testDateUnsupportedDateOp(ParamPrefixEnum theOperator) {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=" + theOperator.getValue() + OBSERVATION_DATETIME, myObservation, mySearchParams);
		assertFalse(result.supported());
		assertEquals("Parameter: <date> Reason: The prefix " + theOperator + " is not supported for param type DATE", result.getUnsupportedReason());
	}

	@Test
	public void testDateSupportedOps() {
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN, false, true, false, false);
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, false, true, true, false);
		testDateSupportedOp(ParamPrefixEnum.EQUAL, false, false, true, false);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN_OR_EQUALS, false, false, true, true);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN, false, false, false, true);
	}

	@Test
	public void testDateTimeSupportedOps() {
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN, true, true, false, false);
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, true, true, true, false);
		testDateSupportedOp(ParamPrefixEnum.EQUAL, true, false, true, false);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN_OR_EQUALS, true, false, true, true);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN, true, false, false, true);
	}

	private void testDateSupportedOp(ParamPrefixEnum theOperator, boolean theIncludeTime, boolean theEarly, boolean theSame, boolean theLater) {
		String earlyDate = EARLY_DATE;
		String observationDate = OBSERVATION_DATE;
		String lateDate = LATE_DATE;
		if (theIncludeTime) {
			earlyDate = EARLY_DATETIME;
			observationDate = OBSERVATION_DATETIME;
			lateDate = LATE_DATETIME;
		}

		String equation = "date=" + theOperator.getValue();
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + earlyDate, myObservation, mySearchParams);
			assertTrue(result.supported(), result.getUnsupportedReason());
			assertEquals(result.matched(), theEarly);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + observationDate, myObservation, mySearchParams);
			assertTrue(result.supported(), result.getUnsupportedReason());
			assertEquals(result.matched(), theSame);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + lateDate, myObservation, mySearchParams);
			assertTrue(result.supported(), result.getUnsupportedReason());
			assertEquals(result.matched(), theLater);
		}
	}

	@Test
	public void testNowPast() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt" + BaseDateTimeDt.NOW_DATE_CONSTANT, myObservation, mySearchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	public void testNowNextWeek() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().plus(Duration.ofDays(7));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	// TODO KHS reenable
	@Disabled
	public void testNowNextMinute() {
		Observation futureObservation = new Observation();
		Instant nextMinute = Instant.now().plus(Duration.ofMinutes(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextMinute)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	public void testTodayPast() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, myObservation, mySearchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	public void testTodayNextWeek() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().plus(Duration.ofDays(7));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	public void testTodayYesterday() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().minus(Duration.ofDays(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertFalse(result.matched());
	}


	@Test
	// TODO KHS why did this test start failing?
	@Disabled
	public void testTodayNextMinute() {
		Observation futureObservation = new Observation();
		ZonedDateTime now = ZonedDateTime.now();
		if (now.getHour() == 23 && now.getMinute() == 59) {
			// this test fails between 23:59 and midnight...
			return;
		}
		Instant nextMinute = now.toInstant().plus(Duration.ofMinutes(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextMinute)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertFalse(result.matched());
	}

	@Test
	public void testInPeriod() {
		Observation insidePeriodObservation = new Observation();
		insidePeriodObservation.setEffective(new DateTimeType("1985-01-01T00:00:00Z"));
		ResourceIndexedSearchParams insidePeriodSearchParams = extractDateSearchParam(insidePeriodObservation);

		Observation outsidePeriodObservation = new Observation();
		outsidePeriodObservation.setEffective(new DateTimeType("2010-01-01T00:00:00Z"));
		ResourceIndexedSearchParams outsidePeriodSearchParams = extractDateSearchParam(outsidePeriodObservation);

		String search = "date=gt" + EARLY_DATE + "&date=le" + LATE_DATE;

		InMemoryMatchResult resultInsidePeriod = myInMemoryResourceMatcher.match(search, insidePeriodObservation, insidePeriodSearchParams);
		assertTrue(resultInsidePeriod.supported(), resultInsidePeriod.getUnsupportedReason());
		assertTrue(resultInsidePeriod.matched());

		InMemoryMatchResult resultOutsidePeriod = myInMemoryResourceMatcher.match(search, outsidePeriodObservation, outsidePeriodSearchParams);
		assertTrue(resultOutsidePeriod.supported(), resultOutsidePeriod.getUnsupportedReason());
		assertFalse(resultOutsidePeriod.matched());
	}


	private ResourceIndexedSearchParams extractDateSearchParam(Observation theObservation) {
		ResourceIndexedSearchParams retval = new ResourceIndexedSearchParams();
		BaseDateTimeType dateValue = (BaseDateTimeType) theObservation.getEffective();
		ResourceIndexedSearchParamDate dateParam = new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "date", dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValueAsString());
		retval.myDateParams.add(dateParam);
		return retval;
	}

	@Configuration
	public static class SpringConfig {
		@Bean
		InMemoryResourceMatcher inMemoryResourceMatcher() {
			return new InMemoryResourceMatcher();
		}

		@Bean
		MatchUrlService matchUrlService() {
			return new MatchUrlService();
		}

		@Bean
		FhirContext fhirContext() {
			return FhirContext.forR5();
		}

		@Bean
		ModelConfig modelConfig() {
			return new ModelConfig();
		}
	}

}
