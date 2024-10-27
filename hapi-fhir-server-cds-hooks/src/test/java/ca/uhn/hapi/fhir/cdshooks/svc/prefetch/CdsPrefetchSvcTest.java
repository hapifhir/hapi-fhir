package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.hapi.fhir.cdshooks.api.ICdsHooksDaoAuthorizationSvc;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CdsPrefetchSvcTest {

	@Mock
	private CdsResolutionStrategySvc myCdsResolutionStrategySvc;
	@Mock
	private CdsPrefetchDaoSvc myCdsPrefetchDaoSvc;
	@Mock
	private CdsPrefetchFhirClientSvc myCdsPrefetchFhirClientSvc;
	@Mock
	private ICdsHooksDaoAuthorizationSvc myCdsHooksDaoAuthorizationSvc;
	@InjectMocks
	private CdsPrefetchSvc myCdsPrefetchSvc;

	@Test
	void testFindMissingPrefetch() {
		Set<String> result;
		CdsServiceJson spec = new CdsServiceJson();
		CdsServiceRequestJson input = new CdsServiceRequestJson();

		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).hasSize(0);

		spec.addPrefetch("foo", "fooval");
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("foo");

		input.addPrefetch("foo", new Patient());
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).hasSize(0);

		spec.addPrefetch("bar", "barval");
		spec.addPrefetch("baz", "bazval");
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("bar", "baz");

		/**
		 * From the Spec:
		 *
		 * The CDS Client MUST NOT send any prefetch template key that it chooses not to satisfy. Similarly, if the CDS Client
		 * encounters an error while prefetching any data, the prefetch template key MUST NOT be sent to the CDS Service. If the
		 * CDS Client has no data to populate a template prefetch key, the prefetch template key MUST have a value of null. Note
		 * that the null result is used rather than a bundle with zero entries to account for the possibility that the prefetch
		 * url is a single-resource request.
		 *
		 */

		// Per the spec above, null is not considered missing
		input.addPrefetch("baz", null);
		result = myCdsPrefetchSvc.findMissingPrefetch(spec, input);
		assertThat(result).containsExactly("bar");
	}
}
