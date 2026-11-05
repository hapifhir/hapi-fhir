package ca.uhn.fhir.jpa.provider.dstu3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ObservationLastnDstu3Test extends BaseResourceProviderDstu3Test {

	/**
	 * See #3986. Verifies that supplying the max parameter in the $lastn does not cause a validation error.
	 */
	@Test
	public void testSupplyingMaxToTheLastNOPerationDoesNotCauseAValidationError() {
		String outcome = executeApiCall("/Observation/$lastn?max=1");
		assertThat(outcome).doesNotContain("HAPI-524");
	}

	private String executeApiCall(String thePath) {
		return myServer.fhirRequest(thePath).get().getBody();
	}
}
