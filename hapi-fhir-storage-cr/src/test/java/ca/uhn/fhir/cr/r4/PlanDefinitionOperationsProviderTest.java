package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlanDefinitionOperationsProviderTest extends BaseCrR4Test {
	@Autowired
	PlanDefinitionOperationsProvider planDefinitionOperationsProvider;

	private RequestDetails setupRequestDetails() {
		var requestDetails = new ServletRequestDetails();
		requestDetails.setServletRequest(new MockHttpServletRequest());
		requestDetails.setServer(ourRestServer);
		requestDetails.setFhirServerBase(ourServerBase);
		return requestDetails;
	}

	@Test
	void testCdsHooksMultiAction() {
		loadBundle("ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_patient_data.json");
		loadBundle("ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_plan_definition.json");

		var requestDetails = setupRequestDetails();
		var planDefinitionID = new IdType("PlanDefinition","CdsHooksMultipleActions-PlanDefinition-1.0.0");
		var patientID = "patient-CdsHooksMultipleActions";
		var data = "cds-hooks-multiple-actions/cds_hooks_multiple_actions_patient_data.json";
		var content = "cds-hooks-multiple-actions/cds_hooks_multiple_actions_plan_definition.json";
		var result = this.planDefinitionOperationsProvider.apply(
			planDefinitionID,
			patientID,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			requestDetails);

		assertNotNull(result);

	}
}
