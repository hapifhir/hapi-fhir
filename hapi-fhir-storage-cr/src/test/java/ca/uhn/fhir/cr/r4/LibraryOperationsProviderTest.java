package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.r4.library.LibraryEvaluateProvider;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LibraryOperationsProviderTest extends BaseCrR4TestServer{
	@Autowired
	LibraryEvaluateProvider myLibraryEvaluateProvider;

	@Test
	void testEvaluateLibrary() {
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireContent.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-GenerateQuestionnaireStructures.json");
		loadBundle("ca/uhn/fhir/cr/r4/Bundle-PatientData.json");

		var requestDetails = setupRequestDetails();
		var url = "http://example.org/sdh/dtr/aslp/Library/ASLPDataElements";
		var patientId = "positive";
		var parameters = new Parameters().addParameter("Service Request Id", "SleepStudy").addParameter("Service Request Id", "SleepStudy2");
		var result = myLibraryEvaluateProvider.evaluate(url, patientId, null, parameters, new BooleanType(true),
			null, null, null, null, null,
			requestDetails);

		assertNotNull(result);
		assertEquals(16, result.getParameter().size());
	}
}
