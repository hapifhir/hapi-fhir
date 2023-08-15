package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionnaireOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	QuestionnaireOperationsProvider questionnaireOperationsProvider;

	@Test
	void testPopulate() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");
		var requestDetails = setupRequestDetails();
		var theSubject = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = this.questionnaireOperationsProvider.populate(new IdType("Questionnaire", "ASLPA1"),
			null, null, theSubject, parameters,
			null, null, null, null,
			requestDetails);

		assertNotNull(result);
		assertEquals("Patient/" + theSubject, result.getSubject().getReference());
		assertTrue(result.getItem().get(0).getItem().get(0).hasAnswer());
	}

	@Test
	void testPrePopulate() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");
		var requestDetails = setupRequestDetails();
		var theSubject = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = this.questionnaireOperationsProvider.prepopulate(new IdType("Questionnaire", "ASLPA1"),
			null, null, theSubject, parameters,
			null, null, null, null,
			requestDetails);

		assertNotNull(result);
		assertTrue(result.getItem().get(0).getItem().get(0).hasInitial());
	}

	@Test
	void testQuestionnairePackage() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		var requestDetails = setupRequestDetails();
		var result = this.questionnaireOperationsProvider.packageQuestionnaire(null,
			"http://example.org/sdh/dtr/aslp/Questionnaire/ASLPA1", "true",
			requestDetails);

		assertNotNull(result);
		assertEquals(11, result.getEntry().size());
		assertTrue(result.getEntry().get(0).getResource().fhirType().equals(Enumerations.FHIRAllTypes.QUESTIONNAIRE.toCode()));
	}
}
