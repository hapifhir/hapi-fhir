package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.hl7.fhir.r4.model.Questionnaire;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlanDefinitionOperationsProviderTest extends BaseCrR4Test {
	@Autowired
	PlanDefinitionOperationsProvider planDefinitionOperationsProvider;

	@Test
	void testCdsHooksMultiAction() {
		loadBundle("ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_patient_data.json");
		loadBundle("ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_plan_definition.json");

		var requestDetails = setupRequestDetails();
		var planDefinitionID = new IdType("PlanDefinition","CdsHooksMultipleActions-PlanDefinition-1.0.0");
		var patientID = "patient-CdsHooksMultipleActions";
		var result = this.planDefinitionOperationsProvider.apply(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, null, null,
			null, null, null,
			requestDetails);

		assertNotNull(result);
		var carePlan = readResource(CarePlan.class, "ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_careplan.json");
		assertEquals(ourParser.encodeResourceToString(carePlan), ourParser.encodeResourceToString(result));


		var resultR5 = this.planDefinitionOperationsProvider.applyR5(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, null, null,
			null, null, null,
			requestDetails);

		var bundle = readResource(Bundle.class, "ca/uhn/fhir/cr/r4/cds-hooks-multi-action/cds_hooks_multiple_actions_bundle.json");
		assertEquals(ourParser.encodeResourceToString(bundle), ourParser.encodeResourceToString(resultR5));
	}

	@Test
	void testGenerateQuestionnaire() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireContent.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireStructures.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");

		var requestDetails = setupRequestDetails();
		var planDefinitionID = new IdType(Enumerations.FHIRAllTypes.PLANDEFINITION.toCode(), "ASLPA1");
		var patientID = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = (CarePlan) this.planDefinitionOperationsProvider.apply(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, parameters, null,
			null, null, null,
			requestDetails);

		assertNotNull(result);
		assertEquals("Sleep Study",
			((Questionnaire) result.getContained().get(1))
				.getItem().get(0)
				.getItem().get(0)
				.getText());

		var resultR5 = (Bundle) this.planDefinitionOperationsProvider.applyR5(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, parameters, null,
			null, null, null,
			requestDetails);

		assertNotNull(resultR5);
		assertEquals("Sleep Study",
			((Questionnaire) resultR5.getEntry().get(1)
				.getResource()).getItem().get(0)
				.getItem().get(0)
				.getText());
	}
}
