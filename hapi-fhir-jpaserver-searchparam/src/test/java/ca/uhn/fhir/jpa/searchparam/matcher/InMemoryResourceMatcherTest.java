package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
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
	@Autowired
	InMemoryResourceMatcher myInMemoryResourceMatcher;

	@MockBean
	ISearchParamRegistry mySearchParamRegistry;

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
	}

	@Test
	public void testDateAp() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1970-01-01"));
		ResourceIndexedSearchParams searchParams = new ResourceIndexedSearchParams();
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=ap1970-01-01", observation, searchParams);
		assertFalse(result.supported());
		assertEquals("Parameter: <date> Reason: The prefix APPROXIMATE is not supported for param type DATE", result.getUnsupportedReason());
	}

	@Test
	public void testDateGe() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1970-01-01"));
		ResourceIndexedSearchParams searchParams = new ResourceIndexedSearchParams();
		InMemoryMatchResult result = myInMemoryResourceMatcher.match("date=ge1965-08-09", observation, searchParams);
		assertTrue(result.getUnsupportedReason(), result.supported());
		assertTrue(result.matched());
	}
}
