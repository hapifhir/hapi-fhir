package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.BaseDateTimeType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryResourceMatcherR5Test.SpringConfig.class})
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
	@MockBean
	SearchParamExtractorService mySearchParamExtractorService;
	@MockBean
	IndexedSearchParamExtractor myIndexedSearchParamExtractor;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;
	@Autowired
	StorageSettings myStorageSettings;
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
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + TEST_SOURCE, myObservation, mySearchParams, newRequest());
			assertTrue(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + SOURCE_URI, myObservation, mySearchParams, newRequest());
			assertTrue(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + REQUEST_ID, myObservation, mySearchParams, newRequest());
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=#" + REQUEST_ID, myObservation, mySearchParams, newRequest());
			assertTrue(result.matched());
		}
	}

	static RequestDetails newRequest() {
		return new SystemRequestDetails();
	}

	@Test
	public void testSupportedSource_ResourceWithNoSourceValue() {
		myObservation.getMeta().getSourceElement().setValue(null);
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + TEST_SOURCE, myObservation, mySearchParams, newRequest());
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + SOURCE_URI, myObservation, mySearchParams, newRequest());
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=" + REQUEST_ID, myObservation, mySearchParams, newRequest());
			assertFalse(result.matched());
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(Constants.PARAM_SOURCE + "=#" + REQUEST_ID, myObservation, mySearchParams, newRequest());
			assertFalse(result.matched());
		}
	}

	@ParameterizedTest
	@CsvSource({
		"http://host.com/v1/v2, _source:contains=HOST.com/v1,           true",
		"http://host.com/v1/v2, _source:contains=http://host.com/v1/v2, true",
		"http://host.com/v1/v2, _source:contains=anotherHost.com,       false",
		"http://host.com/v1/v2, _source:above=http://host.com/v1/v2/v3, true",
		"http://host.com/v1/v2, _source:above=http://host.com/v1/v2,    true",
		"http://host.com,       _source:above=http://host.com/v1/v2,    true",
		"http://host.com/v1/v2, _source:above=http://host.com/v1,       false",
		"http://host.com/v1/v2, _source:below=http://host.com/v1,       true",
		"http://host.com/v1/v2, _source:below=http://host.com/v1/v2,    true",
		"http://host.com/v1/v2, _source:below=http://host.com/v1/v2/v3, false",
		"                     , _source:missing=true,                   true",
		"http://host.com/v1/v2, _source:missing=true,                   false",
		"http://host.com/v1/v2, _source:missing=false,                  true",
		"                     , _source:missing=false,                  false"
	})
	public void testMatch_sourceWithModifiers_matchesSuccessfully(String theSourceValue, String theSearchCriteria, boolean theShouldMatch) {
		myObservation.getMeta().setSource(theSourceValue);

		ResourceIndexedSearchParams searchParams = ResourceIndexedSearchParams.withSets();
		searchParams.myUriParams.add(extractSourceUriParam(myObservation));

		InMemoryMatchResult resultInsidePeriod = myInMemoryResourceMatcher.match(theSearchCriteria, myObservation, searchParams, newRequest());
		assertEquals(theShouldMatch, resultInsidePeriod.matched());
	}

	@Test
	public void testUnsupportedChained() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("encounter.class=FOO", myObservation, mySearchParams, newRequest());
		assertFalse(result.supported());
		assertEquals("Parameter: <encounter.class> Reason: Chained parameters are not supported", result.getUnsupportedReason());
	}

	@Test
	public void testSupportedNot() {
		String criteria = "code" + TokenParamModifier.NOT.getValue() + "=" + OBSERVATION_CODE + ",a_different_code";
		InMemoryMatchResult result = myInMemoryResourceMatcher.match(criteria, myObservation, mySearchParams, newRequest());
		assertTrue(result.supported());
		assertThat(result.matched()).as(":not must not match any of the OR-list").isFalse();

		result = myInMemoryResourceMatcher.match("code:not=a_different_code,and_another", myObservation, mySearchParams, newRequest());
		assertTrue(result.supported());
		assertThat(result.matched()).as(":not matches when NONE match").isTrue();
	}

	@Test
	public void testSupportedIn() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult().setCode(OBSERVATION_CODE);
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
		assertTrue(result.supported());
		assertTrue(result.matched());

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
	}

	@Test
	public void testSupportedIn_NoMatch() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult();
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
		assertTrue(result.supported());
		assertFalse(result.matched());

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
	}

	@Test
	public void testSupportedNotIn() {
		IValidationSupport.CodeValidationResult codeValidationResult = new IValidationSupport.CodeValidationResult();
		when(myValidationSupport.validateCode(any(), any(), any(), any(), any(), any())).thenReturn(codeValidationResult);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("code" + TokenParamModifier.NOT_IN.getValue() + "=" + OBSERVATION_CODE_VALUE_SET_URI, myObservation, mySearchParams, newRequest());
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
		InMemoryMatchResult result = myInMemoryResourceMatcher.match(criteria, myObservation, mySearchParams, newRequest());
		assertTrue(result.supported());
		assertThat(result.matched()).as(":not-in matches when NONE of the OR-list match").isFalse();

		verify(myValidationSupport).validateCode(any(), any(), eq(OBSERVATION_CODE_SYSTEM), eq(OBSERVATION_CODE), isNull(), eq(OBSERVATION_CODE_VALUE_SET_URI));
  }
  
   @Test
	public void testUnrecognizedParam() {
		try {
			InMemoryMatchResult result = myInMemoryResourceMatcher.match("foo=bar", myObservation, mySearchParams, newRequest());
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
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=" + theOperator.getValue() + OBSERVATION_DATETIME, myObservation, mySearchParams, newRequest());
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
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + earlyDate, myObservation, mySearchParams, newRequest());
			assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
			assertEquals(result.matched(), theEarly);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + observationDate, myObservation, mySearchParams, newRequest());
			assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
			assertEquals(result.matched(), theSame);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + lateDate, myObservation, mySearchParams, newRequest());
			assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
			assertEquals(result.matched(), theLater);
		}
	}

	@Test
	public void testNowPast() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt" + BaseDateTimeDt.NOW_DATE_CONSTANT, myObservation, mySearchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
		assertTrue(result.matched());
	}

	@Test
	public void testNowNextWeek() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().plus(Duration.ofDays(7));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
		assertTrue(result.matched());
	}

	@Test
	public void testNowNextMinute() {
		Observation futureObservation = new Observation();
		Instant now = Instant.now();
		DateTimeType nowDT = new DateTimeType(Date.from(now));
		Instant nextMinute = now.plus(Duration.ofMinutes(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextMinute)));
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
		assertThat(searchParams.myDateParams).hasSize(1);
		ResourceIndexedSearchParamDate searchParamDate = searchParams.myDateParams.iterator().next();
		assertThat(result.matched()).as("Expected resource data " + futureObservation.getEffectiveDateTimeType().getValueAsString() +
				" and resource indexed searchparam date " + searchParamDate +
				" to be greater than " + now + " and " + nowDT.getValueAsString()).isTrue();
	}

	@Test
	public void testTodayPast() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, myObservation, mySearchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
		assertTrue(result.matched());
	}

	@Test
	public void testTodayNextWeek() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().plus(Duration.ofDays(7));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
		assertTrue(result.matched());
	}

	@Test
	public void testTodayYesterday() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().minus(Duration.ofDays(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractSearchParams(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.TODAY_DATE_CONSTANT, futureObservation, searchParams, newRequest());
		assertThat(result.supported()).as(result.getUnsupportedReason()).isTrue();
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

		InMemoryMatchResult resultInsidePeriod = myInMemoryResourceMatcher.match(search, insidePeriodObservation, insidePeriodSearchParams, newRequest());
		assertThat(resultInsidePeriod.supported()).as(resultInsidePeriod.getUnsupportedReason()).isTrue();
		assertTrue(resultInsidePeriod.matched());

		InMemoryMatchResult resultOutsidePeriod = myInMemoryResourceMatcher.match(search, outsidePeriodObservation, outsidePeriodSearchParams, newRequest());
		assertThat(resultOutsidePeriod.supported()).as(resultOutsidePeriod.getUnsupportedReason()).isTrue();
		assertFalse(resultOutsidePeriod.matched());
	}


	private ResourceIndexedSearchParams extractSearchParams(Observation theObservation) {
		ResourceIndexedSearchParams retval = ResourceIndexedSearchParams.withSets();
		retval.myDateParams.add(extractEffectiveDateParam(theObservation));
		retval.myTokenParams.add(extractCodeTokenParam(theObservation));
		return retval;
	}

	@Nonnull
	protected ResourceIndexedSearchParamDate extractEffectiveDateParam(Observation theObservation) {
		BaseDateTimeType dateValue = (BaseDateTimeType) theObservation.getEffective();
		return new ResourceIndexedSearchParamDate(new PartitionSettings(), "Observation", "date", dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValue(), dateValue.getValueAsString(), dateValue.getValueAsString());
	}

	protected ResourceIndexedSearchParamToken extractCodeTokenParam(Observation theObservation) {
		Coding coding = theObservation.getCode().getCodingFirstRep();
		return new ResourceIndexedSearchParamToken(new PartitionSettings(), "Observation", "code", coding.getSystem(), coding.getCode());
	}

	protected ResourceIndexedSearchParamUri extractSourceUriParam(Observation theObservation) {
		String source = theObservation.getMeta().getSource();
		return new ResourceIndexedSearchParamUri(new PartitionSettings(), "Observation", "_source", source);
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
        StorageSettings storageSettings() {
			return new StorageSettings();
		}
	}

}
