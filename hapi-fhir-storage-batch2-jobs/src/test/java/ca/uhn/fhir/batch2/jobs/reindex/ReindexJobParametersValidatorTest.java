package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ReindexJobParametersValidatorTest {

	@Mock
	private IResourceReindexSvc myResourceReindexSvc;

	@InjectMocks
	private ReindexJobParametersValidator mySvc;

	@Test
	public void testAllResourceTypeSupportedTrue() {
		when(myResourceReindexSvc.isAllResourceTypeSupported()).thenReturn(true);

		assertThat(mySvc.validate(new ReindexJobParameters()), empty());
		assertThat(mySvc.validate(new ReindexJobParameters().addUrl("Patient?")), empty());
	}

	@Test
	public void testAllResourceTypeSupportedFalse() {
		when(myResourceReindexSvc.isAllResourceTypeSupported()).thenReturn(false);

		assertThat(mySvc.validate(new ReindexJobParameters()), Matchers.contains("At least one type-specific search URL must be provided for $reindex on this server"));
		assertThat(mySvc.validate(new ReindexJobParameters().addUrl("Patient?")), empty());
	}

	@Test
	public void validate_invalidUrls_returnsErrors() {
		// setup
		ReindexJobParameters params = new ReindexJobParameters();
		params.addUrl("?Patient, ?Task");

		// test
		List<String> errors = mySvc.validate(params);

		// verify
		assertFalse(errors.isEmpty());
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).contains("URL cannot contain spaces"));
		assertTrue(errors.get(1).contains("URL contains multiple '?' characters"));
	}
}
