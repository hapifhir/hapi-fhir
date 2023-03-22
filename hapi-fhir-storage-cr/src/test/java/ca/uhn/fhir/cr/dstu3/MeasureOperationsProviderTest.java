package ca.uhn.fhir.cr.dstu3;

import ca.uhn.fhir.cr.BaseCrDstu3Test;
import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ExtendWith(HoverflyExtension.class)
class MeasureOperationsProviderTest extends BaseCrDstu3Test {
	@Autowired
    MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	void testMeasureEvaluate() throws IOException {
		loadBundle("Exm105Fhir3Measure.json");

		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/denom-EXM105-FHIR3",
			null,
			"2019-12-12",
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}

	// This test is failing because the Dstu3MeasureProcessor in the evaluator is not checking the additionalData bundle for the patient
	@Test
	void testMeasureEvaluateWithAdditionalData() throws IOException {
		loadBundle("Exm105FhirR3MeasurePartBundle.json");
		var additionalData = readResource(Bundle.class, "Exm105FhirR3MeasureAdditionalData.json");

		var patient = "Patient/denom-EXM105-FHIR3";
		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			patient,
			null,
			"2019-12-12",
			null,
			additionalData,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
		assertEquals(patient, returnMeasureReport.getPatient().getReference());
	}

	@Test
	void testMeasureEvaluateWithTerminology() throws IOException {
		loadBundle("Exm105Fhir3Measure.json");

		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"patient",
			"Patient/denom-EXM105-FHIR3",
			null,
			null,
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}
}
