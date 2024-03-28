package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlanDefinitionOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	PlanDefinitionApplyProvider myPlanDefinitionApplyProvider;

	@Test
	void testGenerateQuestionnaire() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireContent.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireStructures.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");

		var requestDetails = setupRequestDetails();
		var url = "http://example.org/sdh/dtr/aslp/PlanDefinition/ASLPA1";
		var version = "1.0.0";
		var patientID = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = (CarePlan) myPlanDefinitionApplyProvider.apply(null, null, null, url, version, patientID,
			null, null, null, null, null,
			null, null, null, parameters, new BooleanType(true), null,
			null, null, null,
			requestDetails);

		assertNotNull(result);
		assertEquals("Sleep Study",
			((Questionnaire) result.getContained().get(1))
				.getItem().get(0)
				.getItem().get(0)
				.getText());

		var resultR5 = (Bundle) myPlanDefinitionApplyProvider.applyR5(null, null, null, url, version, patientID,
			null, null, null, null, null,
			null, null, null, parameters, new BooleanType(true), null,
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
