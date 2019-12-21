package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import org.hl7.fhir.r5.model.BaseDateTimeType;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class InMemoryResourceMatcherR5Test {
	public static final String OBSERVATION_DATE = "1970-10-17";
	private static final String EARLY_DATE = "1965-08-09";
	private static final String LATE_DATE = "2000-06-29";
	public static final String OBSERVATION_CODE = "MATCH";
	private static final String SOURCE_URI = "urn:source:0";
	private static final String REQUEST_ID = "a_request_id";
	private static final String TEST_SOURCE = SOURCE_URI + "#" + REQUEST_ID;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@MockBean
	ISearchParamRegistry mySearchParamRegistry;
	private Observation myObservation;
	private ResourceIndexedSearchParams mySearchParams;

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

	@Before
	public void before() {
		RuntimeSearchParam dateSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.effective", RestSearchParameterTypeEnum.DATE, null, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		when(mySearchParamRegistry.getSearchParamByName(any(), eq("date"))).thenReturn(dateSearchParam);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "date")).thenReturn(dateSearchParam);

		RuntimeSearchParam codeSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.code", RestSearchParameterTypeEnum.TOKEN, null, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		when(mySearchParamRegistry.getSearchParamByName(any(), eq("code"))).thenReturn(codeSearchParam);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "code")).thenReturn(codeSearchParam);

		RuntimeSearchParam encSearchParam = new RuntimeSearchParam(null, null, null, null, "Observation.encounter", RestSearchParameterTypeEnum.REFERENCE, null, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		when(mySearchParamRegistry.getSearchParamByName(any(), eq("encounter"))).thenReturn(encSearchParam);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "encounter")).thenReturn(encSearchParam);

		myObservation = new Observation();
		myObservation.getMeta().setSource(TEST_SOURCE);
		myObservation.setEffective(new DateTimeType(OBSERVATION_DATE));
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
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=" + theOperator.getValue() + OBSERVATION_DATE, myObservation, mySearchParams);
		assertFalse(result.supported());
		assertEquals("Parameter: <date> Reason: The prefix " + theOperator + " is not supported for param type DATE", result.getUnsupportedReason());
	}

	@Test
	public void testDateSupportedOps() {
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, true, true, false);
		testDateSupportedOp(ParamPrefixEnum.GREATERTHAN, true, false, false);
		testDateSupportedOp(ParamPrefixEnum.EQUAL, false, true, false);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN_OR_EQUALS, false, true, true);
		testDateSupportedOp(ParamPrefixEnum.LESSTHAN, false, false, true);
	}

	private void testDateSupportedOp(ParamPrefixEnum theOperator, boolean theEarly, boolean theSame, boolean theLater) {
		String equation = "date=" + theOperator.getValue();
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + EARLY_DATE, myObservation, mySearchParams);
			assertTrue(result.getUnsupportedReason(), result.supported());
			assertEquals(result.matched(), theEarly);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + OBSERVATION_DATE, myObservation, mySearchParams);
			assertTrue(result.getUnsupportedReason(), result.supported());
			assertEquals(result.matched(), theSame);
		}
		{
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + LATE_DATE, myObservation, mySearchParams);
			assertTrue(result.getUnsupportedReason(), result.supported());
			assertEquals(result.matched(), theLater);
		}
	}

	@Test
	public void testNowPast() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt" + BaseDateTimeDt.NOW_DATE_CONSTANT, myObservation, mySearchParams);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
	}

	@Test
	public void testNowNextWeek() {
		Observation futureObservation = new Observation();
		Instant nextWeek = Instant.now().plus(Duration.ofDays(7));
		futureObservation.setEffective(new DateTimeType(Date.from(nextWeek)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
	}

	@Test
	public void testNowNextMinute() {
		Observation futureObservation = new Observation();
		Instant nextMinute = Instant.now().plus(Duration.ofMinutes(1));
		futureObservation.setEffective(new DateTimeType(Date.from(nextMinute)));
		ResourceIndexedSearchParams searchParams = extractDateSearchParam(futureObservation);

		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=gt" + BaseDateTimeDt.NOW_DATE_CONSTANT, futureObservation, searchParams);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
	}

	private ResourceIndexedSearchParams extractDateSearchParam(Observation theObservation) {
		ResourceIndexedSearchParams retval = new ResourceIndexedSearchParams();
		BaseDateTimeType dateValue = (BaseDateTimeType) theObservation.getEffective();
		ResourceIndexedSearchParamDate dateParam = new ResourceIndexedSearchParamDate("Patient", "date", dateValue.getValue(), dateValue.getValue(), dateValue.getValueAsString());
		retval.myDateParams.add(dateParam);
		return retval;
	}

}
