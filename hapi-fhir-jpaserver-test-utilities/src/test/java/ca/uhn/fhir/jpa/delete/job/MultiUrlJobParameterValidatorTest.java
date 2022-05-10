package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterUtil;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterValidator;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiUrlJobParameterValidatorTest {
	static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Mock
	MatchUrlService myMatchUrlService;
	@Mock
	DaoRegistry myDaoRegistry;

	MultiUrlJobParameterValidator mySvc;

	@BeforeEach
	public void initMocks() {
		mySvc = new MultiUrlJobParameterValidator(myMatchUrlService, myDaoRegistry);
	}

	@Test
	public void testValidate() throws JobParametersInvalidException, JsonProcessingException {
		// setup
		JobParameters parameters = MultiUrlJobParameterUtil.buildJobParameters("Patient?address=memory", "Patient?name=smith");
		ResourceSearch resourceSearch = new ResourceSearch(ourFhirContext.getResourceDefinition("Patient"), new SearchParameterMap(), RequestPartitionId.defaultPartition());
		when(myMatchUrlService.getResourceSearch(anyString(), any())).thenReturn(resourceSearch);
		when(myDaoRegistry.isResourceTypeSupported("Patient")).thenReturn(true);

		// execute
		mySvc.validate(parameters);
		// verify
		verify(myMatchUrlService, times(2)).getResourceSearch(anyString(), any());
	}

	@Test
	public void testValidateBadType() throws JobParametersInvalidException, JsonProcessingException {
		JobParameters parameters = MultiUrlJobParameterUtil.buildJobParameters("Patient?address=memory");
		ResourceSearch resourceSearch = new ResourceSearch(ourFhirContext.getResourceDefinition("Patient"), new SearchParameterMap(), RequestPartitionId.defaultPartition());
		when(myMatchUrlService.getResourceSearch(anyString(), any())).thenReturn(resourceSearch);
		when(myDaoRegistry.isResourceTypeSupported("Patient")).thenReturn(false);

		try {
			mySvc.validate(parameters);
			fail();
		} catch (JobParametersInvalidException e) {
			assertEquals(Msg.code(1281) + "The resource type Patient is not supported on this server.", e.getMessage());
		}
	}
}
