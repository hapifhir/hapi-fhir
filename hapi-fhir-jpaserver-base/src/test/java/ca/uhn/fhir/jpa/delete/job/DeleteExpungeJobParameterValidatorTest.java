package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteExpungeJobParameterValidatorTest {
	static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Mock
	MatchUrlService myMatchUrlService;
	@Mock
	DaoRegistry myDaoRegistry;

	DeleteExpungeJobParameterValidator mySvc;

	@BeforeEach
	public void initMocks() {
		mySvc = new DeleteExpungeJobParameterValidator(ourFhirContext, myMatchUrlService, myDaoRegistry);
	}

	@Test
	public void testValidate() throws JobParametersInvalidException, JsonProcessingException {
		// setup
		JobParameters parameters = buildJobParameters("Patient?address=memory", "Patient?name=smith");
		ResourceSearch resourceSearch = new ResourceSearch(ourFhirContext.getResourceDefinition("Patient"), new SearchParameterMap());
		when(myMatchUrlService.getResourceSearch(anyString())).thenReturn(resourceSearch);
		when(myDaoRegistry.isResourceTypeSupported("Patient")).thenReturn(true);

		// execute
		mySvc.validate(parameters);
		// verify
		verify(myMatchUrlService, times(2)).getResourceSearch(anyString());
	}

	@Test
	public void testValidateBadType() throws JobParametersInvalidException, JsonProcessingException {
		JobParameters parameters = buildJobParameters("Patient?address=memory");
		ResourceSearch resourceSearch = new ResourceSearch(ourFhirContext.getResourceDefinition("Patient"), new SearchParameterMap());
		when(myMatchUrlService.getResourceSearch(anyString())).thenReturn(resourceSearch);
		when(myDaoRegistry.isResourceTypeSupported("Patient")).thenReturn(false);

		try {
			mySvc.validate(parameters);
			fail();
		} catch (JobParametersInvalidException e) {
			assertEquals("The resource type Patient is not supported on this server.", e.getMessage());
		}
	}

	@Nonnull
	private JobParameters buildJobParameters(String... theUrls) {
		return DeleteExpungeJobConfig.buildJobParameters(2401, "TENANT_A", Lists.newArrayList(theUrls));
	}

}
