package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.BaseCrDstu3Test;
import ca.uhn.fhir.cr.dstu3.activitydefinition.ActivityDefinitionOperationsProvider;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityDefinitionOperationsProviderTest extends BaseCrDstu3Test {
	@Autowired
	ActivityDefinitionOperationsProvider activityDefinitionOperationsProvider;

	@Test
	void testActivityDefinitionApply() {
		loadBundle("ca/uhn/fhir/cr/dstu3/Bundle-ActivityDefinitionTest.json");
		var requestDetails = setupRequestDetails();
		var result = this.activityDefinitionOperationsProvider.apply(
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
			null,
			null,
			null,
			requestDetails);
		assertTrue(result instanceof ProcedureRequest);
		ProcedureRequest request = (ProcedureRequest) result;
		assertTrue(request.getDoNotPerform());
	}
}
