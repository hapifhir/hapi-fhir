package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteExpungeJobParametersValidatorTest {

	@Mock
	private IDeleteExpungeSvc<?> myDeleteExpungeSvc;
	@Mock
	private IUrlListValidator myUrlListValidator;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@InjectMocks
	private DeleteExpungeJobParametersValidator mySvc;

	@Test
	public void testRejectCascadeIfNotSupported() {
		// Setup
		when(myDeleteExpungeSvc.isCascadeSupported()).thenReturn(false);

		DeleteExpungeJobParameters parameters = new DeleteExpungeJobParameters();
		parameters.addUrl("Patient?active=true");
		parameters.setCascade(true);

		// Test
		List<String> outcome = mySvc.validate(new SystemRequestDetails(), parameters);

		// Verify
		assertThat(outcome).as(outcome.toString()).containsExactly("Cascading delete is not supported on this server");
	}

	@Test
	public void testValidateSuccess() {
		// Setup
		when(myDeleteExpungeSvc.isCascadeSupported()).thenReturn(true);

		DeleteExpungeJobParameters parameters = new DeleteExpungeJobParameters();
		parameters.addUrl("Patient?active=true");
		parameters.setCascade(true);

		// Test
		List<String> outcome = mySvc.validate(new SystemRequestDetails(), parameters);

		// Verify
		assertThat(outcome).isEmpty();
	}

}
