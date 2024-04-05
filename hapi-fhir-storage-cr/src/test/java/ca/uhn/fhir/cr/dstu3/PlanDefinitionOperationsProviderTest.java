package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.dstu3.plandefinition.PlanDefinitionApplyProvider;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlanDefinitionOperationsProviderTest extends BaseCrDstu3TestServer {
	@Autowired
	PlanDefinitionApplyProvider myPlanDefinitionApplyProvider;

	@Test
	void testApply() {
		loadBundle("ca/uhn/fhir/cr/dstu3/hello-world/hello-world-patient-view-bundle.json");
		loadBundle("ca/uhn/fhir/cr/dstu3/hello-world/hello-world-patient-data.json");
		var planDefinition = (PlanDefinition) read(new IdType("PlanDefinition", "hello-world-patient-view"));

		var requestDetails = setupRequestDetails();
		var patientId = "Patient/helloworld-patient-1";
		var result = (CarePlan) myPlanDefinitionApplyProvider.apply(null, planDefinition, null, null, null, patientId,
			null, null, null, null, null,
			null, null, null, null, new BooleanType(true), null,
			null, null, null,
			requestDetails);

		assertNotNull(result);
	}
}
