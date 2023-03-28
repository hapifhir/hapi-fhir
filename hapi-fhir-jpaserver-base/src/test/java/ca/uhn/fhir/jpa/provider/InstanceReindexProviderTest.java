package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.search.reindex.IInstanceReindexService;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REINDEX_DRYRUN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstanceReindexProviderTest {

	@Mock
	private IInstanceReindexService myDryRunService;
	@RegisterExtension
	@Order(0)
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.R4)
		.withServer(server -> server.registerProvider(new InstanceReindexProvider(myDryRunService)));
	@RegisterExtension
	@Order(1)
	private HashMapResourceProviderExtension<Patient> myPatientProvider = new HashMapResourceProviderExtension<>(myServer, Patient.class);

	@Test
	public void testDryRun() {
		Parameters parameters = new Parameters();
		parameters.addParameter("foo", "bar");
		when(myDryRunService.reindexDryRun(any(),any())).thenReturn(parameters);

		Parameters outcome = myServer
			.getFhirClient()
			.operation()
			.onInstance(new IdType("Patient/123"))
			.named(OPERATION_REINDEX_DRYRUN)
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		assertEquals("foo", outcome.getParameter().get(0).getName());
	}

}
