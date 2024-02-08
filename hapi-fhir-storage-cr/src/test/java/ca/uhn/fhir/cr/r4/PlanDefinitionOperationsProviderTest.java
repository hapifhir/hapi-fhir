package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PlanDefinitionOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	PlanDefinitionApplyProvider myPlanDefinitionApplyProvider;

	@Test
	void testGenerateQuestionnaire() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireContent.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireStructures.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");

		var requestDetails = setupRequestDetails();
		var planDefinitionID = new IdType(Enumerations.FHIRAllTypes.PLANDEFINITION.toCode(), "ASLPA1");
		var patientID = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = (CarePlan) myPlanDefinitionApplyProvider.apply(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, parameters, new BooleanType(true), null,
			null, null, null,
			requestDetails);

		assertThat(result).isNotNull();
		assertThat(((Questionnaire) result.getContained().get(1))
				.getItem().get(0)
				.getItem().get(0)
				.getText()).isEqualTo("Sleep Study");

		var resultR5 = (Bundle) myPlanDefinitionApplyProvider.applyR5(planDefinitionID, null, null, patientID,
			null, null, null, null, null,
			null, null, null, parameters, new BooleanType(true), null,
			null, null, null,
			requestDetails);

		assertThat(resultR5).isNotNull();
		assertThat(((Questionnaire) resultR5.getEntry().get(1)
				.getResource()).getItem().get(0)
				.getItem().get(0)
				.getText()).isEqualTo("Sleep Study");
	}
}
