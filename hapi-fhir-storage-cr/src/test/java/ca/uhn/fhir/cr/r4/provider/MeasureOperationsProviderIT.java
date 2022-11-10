package ca.uhn.fhir.cr.r4.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import ca.uhn.fhir.cr.CrR4Test;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class MeasureOperationsProviderIT extends CrR4Test {
	@Autowired
	MeasureOperationsProvider measureOperationsProvider;

	@Test
	void testMeasureEvaluate() throws IOException {
		loadBundle("Exm104FhirR4MeasureBundle.json");

		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM104-8.2.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/numer-EXM104",
			null,
			"2019-12-12",
			null,
			null,
			null
		);

		assertNotNull(returnMeasureReport);
	}

	private void mockValueSet(String theId) {
		var valueSet = (ValueSet) read(new IdType("ValueSet", theId));
		mockFhirRead("/ValueSet?url=http%3A%2F%2Flocalhost%3A8080%2Ffhir%2FValueSet%2F" + theId, makeBundle(valueSet));
		mockFhirRead(String.format("/ValueSet/%s/$expand", theId), valueSet);
	}

	@Test
	void testMeasureEvaluateWithTerminologyEndpoint() throws IOException {
		loadBundle("Exm104FhirR4MeasureBundle.json");
		mockValueSet("2.16.840.1.114222.4.11.3591");
		mockValueSet("2.16.840.1.113883.3.117.1.7.1.424");

		var terminologyEndpointValid = readResource(Endpoint.class, "Endpoint.json");
		terminologyEndpointValid.setAddress(newClient().getServerBase());

		var terminologyEndpointInvalid = readResource(Endpoint.class, "Endpoint.json");
		terminologyEndpointInvalid.setAddress("https://tx.nhsnlink.org/fhir234");

		var returnMeasureReport = this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM104-8.2.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/numer-EXM104",
			null,
			"2019-12-12",
			null,
			null,
			terminologyEndpointValid
		);

		assertNotNull(returnMeasureReport);

		var ex = assertThrows(Exception.class, () -> this.measureOperationsProvider.evaluateMeasure(
			new SystemRequestDetails(),
			new IdType("Measure", "measure-EXM104-8.2.000"),
			"2019-01-01",
			"2020-01-01",
			"individual",
			"Patient/numer-EXM104",
			null,
			"2019-12-12",
			null,
			null,
			terminologyEndpointInvalid
		));

		assertTrue(ex.getMessage().contains("Error performing expansion"));
	}

//	private void runWithPatient(String measureId, String patientId, int initialPopulationCount, int denominatorCount,
//										 int denominatorExclusionCount, int numeratorCount, boolean enrolledDuringParticipationPeriod,
//										 String participationPeriod) {
//		Parameters params = parameters(
//			stringPart("periodStart", "2022-01-01"),
//			stringPart("periodEnd", "2022-12-31"),
//			stringPart("reportType", "individual"),
//			stringPart("subject", patientId));
//
//		MeasureReport returnMeasureReport = getClient().operation()
//			.onInstance(new IdType("Measure", measureId))
//			.named("$evaluate-measure")
//			.withParameters(params)
//			.returnResourceType(MeasureReport.class)
//			.execute();
//
//		assertNotNull(returnMeasureReport);
//
//		for (MeasureReport.MeasureReportGroupPopulationComponent population : returnMeasureReport.getGroupFirstRep()
//			.getPopulation()) {
//			switch (population.getCode().getCodingFirstRep().getCode()) {
//				case "initial-population":
//					assertEquals(initialPopulationCount, population.getCount());
//					break;
//				case "denominator":
//					assertEquals(denominatorCount, population.getCount());
//					break;
//				case "denominator-exclusion":
//					assertEquals(denominatorExclusionCount, population.getCount());
//					break;
//				case "numerator":
//					assertEquals(numeratorCount, population.getCount());
//					break;
//			}
//		}
//
//		Observation enrolledDuringParticipationPeriodObs = null;
//		Observation participationPeriodObs = null;
//		for (Resource r : returnMeasureReport.getContained()) {
//			if (r instanceof Observation) {
//				Observation o = (Observation) r;
//				if (o.getCode().getText().equals("Enrolled During Participation Period")) {
//					enrolledDuringParticipationPeriodObs = o;
//				} else if (o.getCode().getText().equals("Participation Period")) {
//					participationPeriodObs = o;
//				}
//			}
//		}
//
//		assertNotNull(enrolledDuringParticipationPeriodObs);
//		assertEquals(Boolean.toString(enrolledDuringParticipationPeriod).toLowerCase(),
//			enrolledDuringParticipationPeriodObs.getValueCodeableConcept().getCodingFirstRep().getCode());
//
//		assertNotNull(participationPeriodObs);
//		assertEquals(participationPeriod, participationPeriodObs.getValueCodeableConcept().getCodingFirstRep().getCode());
//	}
//
//	@Test
//	void testBCSEHEDISMY2022() {
//		loadTransaction("BCSEHEDISMY2022-bundle.json");
//
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-5", 0, 0, 0, 0, false,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-7", 1, 1, 0, 0, true,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-9", 0, 0, 0, 0, true,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-21", 1, 0, 1, 0, true,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-23", 1, 1, 0, 0, true,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-65", 1, 1, 0, 1, true,
//			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
//	}
//
//	@Test
//	void testClientNonPatientBasedMeasureEvaluate() {
//		loadTransaction("ClientNonPatientBasedMeasureBundle.json");
//
//		Measure measure = getClient().read().resource(Measure.class).withId("InitialInpatientPopulation").execute();
//		assertNotNull(measure);
//
//		Parameters params = parameters(
//			stringPart("periodStart", "2019-01-01"),
//			stringPart("periodEnd", "2020-01-01"),
//			stringPart("reportType", "subject"),
//			stringPart("subject", "Patient/97f27374-8a5c-4aa1-a26f-5a1ab03caa47"));
//
//		MeasureReport returnMeasureReport = getClient().operation()
//			.onInstance(new IdType("Measure", "InitialInpatientPopulation"))
//			.named("$evaluate-measure")
//			.withParameters(params)
//			.returnResourceType(MeasureReport.class)
//			.execute();
//
//		assertNotNull(returnMeasureReport);
//
//		String populationName = "initial-population";
//		int expectedCount = 2;
//
//		Optional<MeasureReport.MeasureReportGroupPopulationComponent> population = returnMeasureReport.getGroup().get(0)
//			.getPopulation().stream().filter(x -> x.hasCode() && x.getCode().hasCoding()
//				&& x.getCode().getCoding().get(0).getCode().equals(populationName))
//			.findFirst();
//
//		assertTrue(population.isPresent(), String.format("Unable to locate a population with id \"%s\"", populationName));
//		assertEquals(population.get().getCount(), expectedCount,
//			String.format("expected count for population \"%s\" did not match", populationName));
//	}
//
//	@Disabled("The cql/elm in the Bundles is incorrect. It references ValueSets by localhost url, which is not valid")
//	@Test
//	void testMeasureEvaluateMultiVersion() {
//		loadTransaction("multiversion/EXM124-7.0.000-bundle.json");
//		loadTransaction("multiversion/EXM124-9.0.000-bundle.json");
//		Parameters params = parameters(
//			stringPart("reportType", "individual"),
//			stringPart("subject", "Patient/numer-EXM124"),
//			stringPart("lastReceivedOn", "2019-12-12"));
//
//		MeasureReport returnMeasureReportVersion7 = getClient().operation()
//			.onInstance(new IdType("Measure", "measure-EXM124-7.0.000"))
//			.named("$evaluate-measure")
//			.withParameters(params)
//			.returnResourceType(MeasureReport.class)
//			.execute();
//
//		assertNotNull(returnMeasureReportVersion7);
//
//		MeasureReport returnMeasureReportVersion9 = getClient().operation()
//			.onInstance(new IdType("Measure", "measure-EXM124-9.0.000"))
//			.named("$evaluate-measure")
//			.withParameters(params)
//			.returnResourceType(MeasureReport.class)
//			.execute();
//
//		assertNotNull(returnMeasureReportVersion9);
//	}

}
