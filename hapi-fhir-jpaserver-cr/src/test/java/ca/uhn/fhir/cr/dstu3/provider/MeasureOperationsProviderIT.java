package ca.uhn.fhir.cr.dstu3.provider;

import ca.uhn.fhir.cr.CrDstu3Test;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class MeasureOperationsProviderIT extends CrDstu3Test {
	@Autowired
    MeasureOperationsProvider measureOperationsProvider;

	@Test
	void testMeasureEvaluate() throws IOException {
		loadBundle("Exm105Fhir3Measure.json");

		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/denom-EXM105-FHIR3",
			null,
			"2019-12-12",
			null, null, null
		);

		assertNotNull(returnMeasureReport);
	}

	// This test is failing because the Dstu3MeasureProcessor in the evaluator is not checking the additionalData bundle for the patient
//	@Test
//	void testMeasureEvaluateWithAdditionalData() throws IOException {
//		loadBundle("Exm105FhirR3MeasurePartBundle.json");
//		var additionalData = loadBundle("Exm105FhirR3MeasureAdditionalData.json");
//
//		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
//			new SystemRequestDetails(),
//			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
//			"2019-01-01",
//			"2020-01-01",
//			"individual",
//			"Patient/denom-EXM105-FHIR3",
//			null,
//			"2019-12-12",
//			null, additionalData, null
//		);
//
//		assertNotNull(returnMeasureReport);
//	}

//	@Test
//	void testMeasureEvaluateWithTerminology() throws IOException {
//		loadBundle("Exm105Fhir3Measure.json");
//
//		Endpoint terminologyEndpoint = loadResource(ourFhirContext, Endpoint.class, "Endpoint.json");
//		terminologyEndpoint.setAddress(String.format("http://localhost:%s/fhir/", getPort()));
//
//		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
//			new SystemRequestDetails(),
//			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
//			"2019-01-01",
//			"2020-01-01",
//			"individual",
//			"Patient/denom-EXM105-FHIR3",
//			null,
//			"2019-12-12",
//			null, null, terminologyEndpoint
//		);
//
//		assertNotNull(returnMeasureReport);
//	}
}
