package ca.uhn.fhir.cr.r4;


import ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePopulateProvider;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

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
			null, null, theSubject, parameters,
			null, null, null, null,
			requestDetails);

		assertThat(result).isNotNull();
		assertThat(result.getSubject().getReference()).isEqualTo("Patient/" + theSubject);
		assertThat(result.getItem().get(0).getItem().get(0).hasAnswer()).isTrue();
	}

	@Test
	void testPrePopulate() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");
		var requestDetails = setupRequestDetails();
		var theSubject = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = myQuestionnairePopulateProvider.prepopulate(new IdType("Questionnaire", "ASLPA1"),
			null, null, theSubject, parameters,
			null, null, null, null,
			requestDetails);

		assertThat(result).isNotNull();
		assertThat(result.getItem().get(0).getItem().get(0).hasInitial()).isTrue();
	}

	@Test
	void testQuestionnairePackage() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-QuestionnairePackage.json");
		var requestDetails = setupRequestDetails();
		var result = myQuestionnairePackageProvider.packageQuestionnaire(null,
			"http://example.org/sdh/dtr/aslp/Questionnaire/ASLPA1", "true",
			requestDetails);

		assertThat(result).isNotNull();
		assertThat(result.getEntry().size()).isEqualTo(11);
		assertThat(result.getEntry().get(0).getResource().fhirType().equals(Enumerations.FHIRAllTypes.QUESTIONNAIRE.toCode())).isTrue();
	}
}
