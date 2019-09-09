package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.hl7.fhir.r5.model.BaseDateTimeType;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class InMemoryResourceMatcherTest {
	public static final String OBS_DATE = "1970-10-17";
	private static final String EARLY_DATE = "1965-08-09";
	private static final String LATE_DATE = "2000-06-29";

	@Autowired
	private
	InMemoryResourceMatcher myInMemoryResourceMatcher;

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
	}

	@Before
	public void before() {
		RuntimeSearchParam searchParams = new RuntimeSearchParam(null, null, null, null, "Observation.effective", RestSearchParameterTypeEnum.DATE, null, null, null, RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
		when(mySearchParamRegistry.getSearchParamByName(any(), any())).thenReturn(searchParams);
		when(mySearchParamRegistry.getActiveSearchParam("Observation", "date")).thenReturn(searchParams);
		myObservation = new Observation();
		myObservation.setEffective(new DateTimeType(OBS_DATE));
		mySearchParams = extractDateSearchParam(myObservation);
	}


	@Test
	public void testDateUnsupportedOps() {
		testDateUnsupportedOp(ParamPrefixEnum.APPROXIMATE);
		testDateUnsupportedOp(ParamPrefixEnum.STARTS_AFTER);
		testDateUnsupportedOp(ParamPrefixEnum.ENDS_BEFORE);
		testDateUnsupportedOp(ParamPrefixEnum.NOT_EQUAL);
	}

	private void testDateUnsupportedOp(ParamPrefixEnum theOperator) {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=" + theOperator.getValue() + OBS_DATE, myObservation, mySearchParams);
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
			InMemoryMatchResult result = myInMemoryResourceMatcher.match(equation + OBS_DATE, myObservation, mySearchParams);
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
	public void testNow() {
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=lt%now", myObservation, mySearchParams);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
	}

	private ResourceIndexedSearchParams extractDateSearchParam(Observation theObservation) {
		ResourceIndexedSearchParams retval = new ResourceIndexedSearchParams();
		BaseDateTimeType dateValue = (BaseDateTimeType) theObservation.getEffective();
		ResourceIndexedSearchParamDate dateParam = new ResourceIndexedSearchParamDate("date", dateValue.getValue(), dateValue.getValue(), dateValue.getValueAsString());
		retval.myDateParams.add(dateParam);
		return retval;
	}

}
