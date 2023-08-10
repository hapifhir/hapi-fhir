package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuestionnaireResponseOperationsProviderTest extends BaseCrR4TestServer {
	@Autowired
	QuestionnaireResponseOperationsProvider questionnaireResponseOperationsProvider;

	@Test
	void testExtract() throws IOException {
		var requestDetails = setupRequestDetails();
		loadResource(Questionnaire.class, "ca/uhn/fhir/cr/r4/Questionnaire-MyPainQuestionnaire.json", requestDetails);
		var questionnaireResponse = readResource(QuestionnaireResponse.class, "ca/uhn/fhir/cr/r4/QuestionnaireResponse-QRSharonDecision.json");
		var result = (Bundle) this.questionnaireResponseOperationsProvider.extract(null, questionnaireResponse, requestDetails);

		assertNotNull(result);
		assertEquals(5, result.getEntry().size());
	}
}
