package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityDefinitionOperationsProviderTest extends BaseCrR4TestServer {


	@Autowired
	ActivityDefinitionApplyProvider myActivityDefinitionApplyProvider;
	@Test
	void testActivityDefinitionApply() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-ActivityDefinitionTest.json");
		var requestDetails = setupRequestDetails();
		var result = myActivityDefinitionApplyProvider.apply(
			new IdType("activityDefinition-test"),
			null,
			null,
			"patient-1",
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			new BooleanType(true),
			null,
			null,
			null,
			null,
			requestDetails);
		assertTrue(result instanceof MedicationRequest);
		MedicationRequest request = (MedicationRequest) result;
		assertTrue(request.getDoNotPerform());
	}
}
