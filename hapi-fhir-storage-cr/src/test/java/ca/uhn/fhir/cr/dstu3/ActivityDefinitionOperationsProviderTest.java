package ca.uhn.fhir.cr.dstu3;


import ca.uhn.fhir.cr.dstu3.activitydefinition.ActivityDefinitionApplyProvider;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityDefinitionOperationsProviderTest extends BaseCrDstu3TestServer {


	@Autowired
	ActivityDefinitionApplyProvider myActivityDefinitionApplyProvider;
	@Test
	void testActivityDefinitionApply() {
		loadBundle("ca/uhn/fhir/cr/dstu3/Bundle-ActivityDefinitionTest.json");
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
		assertTrue(result instanceof ProcedureRequest);
		ProcedureRequest request = (ProcedureRequest) result;
		assertTrue(request.getDoNotPerform());
	}
}
