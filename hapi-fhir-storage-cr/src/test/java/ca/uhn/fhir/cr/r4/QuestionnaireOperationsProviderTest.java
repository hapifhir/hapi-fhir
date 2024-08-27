package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePopulateProvider;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionnaireOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	QuestionnairePopulateProvider myQuestionnairePopulateProvider;

	@Autowired
	QuestionnairePackageProvider myQuestionnairePackageProvider;

	@Test
	void testPopulate() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");
		var requestDetails = setupRequestDetails();
		var theSubject = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = myQuestionnairePopulateProvider.populate(new IdType("Questionnaire", "ASLPA1"),
			null, null, null, null, theSubject, parameters, null, null,
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
		var result = myQuestionnairePopulateProvider.prepopulate(new IdType("Questionnaire", "ASLPA1"),
			null, null, null, null, theSubject, parameters, null, null,
			null, null, null, null,
			requestDetails);

		assertNotNull(result);
		assertTrue(result.getItem().get(0).getItem().get(0).hasInitial());
	}

	@Test
	void testQuestionnairePackage() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		var requestDetails = setupRequestDetails();
		var result = myQuestionnairePackageProvider.packageQuestionnaire(null,
			"http://example.org/sdh/dtr/aslp/Questionnaire/ASLPA1", null, null, new BooleanType("true"),
			requestDetails);

		assertNotNull(result);
		assertThat(result.getEntry()).hasSize(11);
		assertEquals(Enumerations.FHIRAllTypes.QUESTIONNAIRE.toCode(), result.getEntry().get(0).getResource().fhirType());
	}
}
