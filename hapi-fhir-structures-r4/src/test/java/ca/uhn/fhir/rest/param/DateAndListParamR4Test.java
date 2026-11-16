package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateAndListParamR4Test {

	private static DateAndListParam ourLastActual;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR4Cached())
		 .registerProvider(new ObservationProvider());


	@Test
	void testOr() {
		// Test
		myServer.fhirRequest("/Observation?date=2020,2023").get().assertStatus(200);

		// Verify
		DateAndListParam actual = ourLastActual;
		assertEquals(1, actual.size());
		assertEquals(2, actual.getValuesAsQueryTokens().get(0).size());
		assertEquals("2020", actual.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValueAsString());
		assertEquals("2023", actual.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValueAsString());
	}


	public static class ObservationProvider implements IResourceProvider {

		@Search
		public List<Observation> search(@RequiredParam(name = "date") DateAndListParam theDateAndListParam) {
			ourLastActual = theDateAndListParam;
			return List.of();
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}
	}

}
