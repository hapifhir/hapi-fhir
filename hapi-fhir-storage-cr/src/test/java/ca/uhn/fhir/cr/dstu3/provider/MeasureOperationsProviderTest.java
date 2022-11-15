package ca.uhn.fhir.cr.dstu3.provider;

import ca.uhn.fhir.cr.CrDstu3Test;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.dsl.StubServiceBuilder;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ExtendWith(HoverflyExtension.class)
class MeasureOperationsProviderTest extends CrDstu3Test {
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
			null,
			null,
			null
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
//			null,
//			additionalData,
//			null
//		);
//
//		assertNotNull(returnMeasureReport);
//	}

	@Test
	void testMeasureEvaluateWithTerminology(Hoverfly hoverfly) throws IOException {
		loadBundle("Exm105Fhir3Measure.json");
		List<StubServiceBuilder> reads = new ArrayList<>();
		reads.addAll(mockValueSet("2.16.840.1.114222.4.11.3591", "http://cts.nlm.nih.gov/fhir/ValueSet"));
		reads.addAll(mockValueSet("2.16.840.1.113883.3.117.1.7.1.424", "http://cts.nlm.nih.gov/fhir/ValueSet"));
		hoverfly.simulate(dsl(reads.toArray(new StubServiceBuilder[0])));

		var terminologyEndpoint = loadResource(ourFhirContext, Endpoint.class, "Endpoint.json");

		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM105-FHIR3-8.0.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/denom-EXM105-FHIR3",
			null,
			"2019-12-12",
			null,
			null,
			terminologyEndpoint
		);

		assertNotNull(returnMeasureReport);
	}
}
