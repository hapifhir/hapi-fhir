package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UrlListParametersValidatorTest {

	@Mock
	private IBatch2DaoSvc myBatch2DaoSvc;

	private UrlListValidator mySvc;

	@BeforeEach
	public void before() {
		mySvc = new UrlListValidator("TESTOP", myBatch2DaoSvc);
	}

	@Test
	public void testAllResourceTypeSupportedTrue() {
		when(myBatch2DaoSvc.isAllResourceTypeSupported()).thenReturn(true);

		assertThat(mySvc.validateUrls(Collections.emptyList()), empty());
		assertThat(mySvc.validateUrls(List.of("Patient?")), empty());
	}

	@Test
	public void testAllResourceTypeSupportedFalse() {
		when(myBatch2DaoSvc.isAllResourceTypeSupported()).thenReturn(false);

		assertThat(mySvc.validateUrls(Collections.emptyList()), Matchers.contains("At least one type-specific search URL must be provided for TESTOP on this server"));
		assertThat(mySvc.validateUrls(List.of("Patient?")), empty());
	}

}
