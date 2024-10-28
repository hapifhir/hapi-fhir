package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionDataRequirementsProvider;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanDefinitionOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	PlanDefinitionApplyProvider myPlanDefinitionApplyProvider;

	@Autowired
	PlanDefinitionDataRequirementsProvider myPlanDefinitionDataRequirementsProvider;

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
			null, null, null, parameters, new BooleanType(true), null, null,
			null, null, null,
			requestDetails);

		assertNotNull(result);
		assertEquals(1, result.getContained().size());

		var resultR5 = (Bundle) myPlanDefinitionApplyProvider.applyR5(null, null, null, url, version, patientID,
			null, null, null, null, null,
			null, null, null, parameters, new BooleanType(true), null, null,
			null, null, null,
			requestDetails);

		assertNotNull(resultR5);
		var questionnaireResponse = (QuestionnaireResponse) resultR5.getEntry().get(1).getResource();
		assertNotNull(questionnaireResponse);
		var questionnaire = (Questionnaire) questionnaireResponse.getContained().get(0);
		assertNotNull(questionnaire);
		assertThat(questionnaire.getItem().get(0)
				.getItem().get(0)
				.getText()).isEqualTo("Sleep Study");
		assertTrue(questionnaireResponse.getItem().get(0).getItem().get(0).hasAnswer());
		assertTrue(questionnaireResponse.getItem().get(0).getItem().get(1).hasAnswer());
	}

	@Test
	void testDataRequirements() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireContent.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireStructures.json");
		var requestDetails = setupRequestDetails();
		var result = myPlanDefinitionDataRequirementsProvider.getDataRequirements("PlanDefinition/ASLPA1", null, null, null, requestDetails);
		assertInstanceOf(Library.class, result);
		assertEquals("module-definition", ((Library) result).getType().getCodingFirstRep().getCode());
	}
}
