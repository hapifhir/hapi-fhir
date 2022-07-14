package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
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
import org.hl7.fhir.r5.model.Coding;
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

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class InMemoryResourceMatcherR5Test {
	public static final String OBSERVATION_DATE = "1970-10-17";
	public static final String OBSERVATION_DATETIME = OBSERVATION_DATE + "T01:00:00-08:30";
	public static final String OBSERVATION_CODE = "MATCH";
	public static final String OBSERVATION_CODE_SYSTEM = "http://hl7.org/some-cs";
	public static final String OBSERVATION_CODE_DISPLAY = "Match";
	public static final String OBSERVATION_CODE_VALUE_SET_URI = "http://hl7.org/some-vs";
	private static final String EARLY_DATE = "1965-08-09";
	private static final String LATE_DATE = "2000-06-29";
	private static final String EARLY_DATETIME = EARLY_DATE + "T12:00:00Z";
	private static final String LATE_DATETIME = LATE_DATE + "T12:00:00Z";
	private static final String SOURCE_URI = "urn:source:0";
	private static final String REQUEST_ID = "a_request_id";
	private static final String TEST_SOURCE = SOURCE_URI + "#" + REQUEST_ID;

	@MockBean
	ISearchParamRegistry mySearchParamRegistry;
	@MockBean
	IValidationSupport myValidationSupport;
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
		codeableConcept.addCoding().setCode(OBSERVATION_CODE)
			.setSystem(OBSERVATION_CODE_SYSTEM).setDisplay(OBSERVATION_CODE_DISPLAY);
		myObservation.setCode(codeableConcept);
		mySearchParams = extractSearchParams(myObservation);
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
	public void testSupportedNot() {
		String criteria = "code" + TokenParamModifier.NOT.getValue() + "=" + OBSERVATION_CODE + ",a_different_code";
		InMemoryMatchResult result = myInMemoryResourceMatcher.match(criteria, myObservation, mySearchParams);
		assertTrue(result.supported());
		assertFalse(result.matched(), ":not must not match any of the OR-list");

		result = myInMemoryResourceMatcher.match("code:not=a_different_code,and_another", myObservation, mySearchParams);
		assertTrue(result.supported());
		assertTrue(result.matched(), ":not matches when NONE match");
	}

	@Test
	public void testSupportedIn() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult().setCode(OBSERVATION_CODE);
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams);
		assertTrue(result.supported());
		assertTrue(result.matched());

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
	}

	@Test
	public void testSupportedIn_NoMatch() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult();
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams);
		assertTrue(result.supported());
		assertFalse(result.matched());

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
	}

	@Test
	public void testSupportedNotIn() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult();
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.NOT_IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams);
		assertTrue(result.supported());
		assertTrue(result.matched());

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
	}

	@Test
	public void testSupportedNotIn_NoMatch() {
		IValidationSupport.CodeValidationResult matchResult = new IValidationSupport.CodeValidationResult().setCode(OBSERVATION_CODE);
		IValidationSupport.CodeValidationResult noMatchResult = new IValidationSupport.CodeValidationResult()
			.setSeverity(IValidationSupport.IssueSeverity.ERROR)
			.setMessage("not in");

		// mock 2 value sets.  Once containing the code, and one not.
		String otherValueSet = OBSERVATION_CODE_VALUE_SET_URI + "-different";
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), eq(OBSERVATION_CODE_VALUE_SET_URI))).thenReturn(matchResult);
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), eq(otherValueSet))).thenReturn(noMatchResult);

		String criteria = "code" + TokenParamModifier.NOT_IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI + "," + otherValueSet;
		InMemoryMatchResult result = myInMemoryResourceMatcher.match(criteria, myObservation, mySearchParams);
		assertTrue(result.supported());
		assertFalse(result.matched(), ":not-in matches when NONE of the OR-list match");

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
  }
  
   @Test
	public void testUnrecognizedParam() {
		try {
			InMemoryMatchResult result = myInMemoryResourceMatcher.match("foo=bar", myObservation, mySearchParams);
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			// expected
		}
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
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

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
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

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
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertTrue(result.matched());
	}

	@Test
	public void testTodayYesterday() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().minus(Duration.ofDays(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

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
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.supported(), result.getUnsupportedReason());
		assertFalse(result.matched());
	}

	@Test
	public void testInPeriod() {
		Observation insidePeriodObservation = new Observation();
		insidePeriodObservation.setEffective(new DateTimeType("1985-01-01T00:00:00Z"));
		ResourceIndexedSearchParams insidePeriodSearchParams = extractSearchParams(insidePeriodObservation);

		Observation outsidePeriodObservation = new Observation();
		outsidePeriodObservation.setEffective(new DateTimeType("2010-01-01T00:00:00Z"));
		ResourceIndexedSearchParams outsidePeriodSearchParams = extractSearchParams(outsidePeriodObservation);

		String search = "date=gt" + EARLY_DATE + "&date=le" + LATE_DATE;

		InMemoryMatchResult resultInsidePeriod = myInMemoryResourceMatcher.match(search, insidePeriodObservation, insidePeriodSearchParams);
		assertTrue(resultInsidePeriod.supported(), resultInsidePeriod.getUnsupportedReason());
		assertTrue(resultInsidePeriod.matched());

		InMemoryMatchResult resultOutsidePeriod = myInMemoryResourceMatcher.match(search, outsidePeriodObservation, outsidePeriodSearchParams);
		assertTrue(resultOutsidePeriod.supported(), resultOutsidePeriod.getUnsupportedReason());
		assertFalse(resultOutsidePeriod.matched());
	}


	private ResourceIndexedSearchParams extractSearchParams(Observation theObservation) {
		ResourceIndexedSearchParams retval = new ResourceIndexedSearchParams();
		retval.myDateParams.add(extractEffectiveDateParam(theObservation));
		retval.myTokenParams.add(extractCodeTokenParam(theObservation));
		return retval;
	}

	@Nonnull
	private ResourceIndexedSearchParamDate extractEffectiveDateParam(Observation theObservation) {
		BaseDateTimeType dateValue = (BaseDateTimeType) theObservation.getEffective();
		return new ResourceIndexedSearchParamDate(new PartitionSettings(), "Patient", "date", dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValueAsString());
	}

	private ResourceIndexedSearchParamToken extractCodeTokenParam(Observation theObservation) {
		Coding coding = theObservation.getCode().getCodingFirstRep();
		return new ResourceIndexedSearchParamToken(new PartitionSettings(), "Observation", "code", coding.getSystem(), coding.getCode());
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
