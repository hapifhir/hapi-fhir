package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ExtendWith(HoverflyExtension.class)
class MeasureOperationsProviderTest extends BaseCrR4Test {
	@Autowired
    MeasureOperationsProvider myMeasureOperationsProvider;

	@Test
	void testMeasureEvaluate() throws IOException {
		loadBundle("Exm104FhirR4MeasureBundle.json");

		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM104-8.2.000"),
			"2019-01-01",
			"2020-01-01",
			"subject",
			"Patient/numer-EXM104",
			null,
			"2019-12-12",
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}

	@Test
	void testMeasureEvaluateWithTerminologyEndpoint(Hoverfly hoverfly) throws IOException {
		loadBundle("Exm104FhirR4MeasureBundle.json");

		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM104-8.2.000"),
			"2019-01-01",
			"2020-01-01",
			"subject",
			"Patient/numer-EXM104",
			null,
			"2019-12-12",
			null,
			null,
			null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);
	}

	private void runWithPatient(String measureId, String patientId, int initialPopulationCount, int denominatorCount,
										 int denominatorExclusionCount, int numeratorCount, boolean enrolledDuringParticipationPeriod,
										 String participationPeriod) {
		var returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", measureId),
			"2022-01-01",
			"2022-12-31",
			"subject",
			patientId,
			null,
			"2019-12-12",
			null, null, null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);

		for (MeasureReport.MeasureReportGroupPopulationComponent population : returnMeasureReport.getGroupFirstRep()
			.getPopulation()) {
			switch (population.getCode().getCodingFirstRep().getCode()) {
				case "initial-population":
					assertEquals(initialPopulationCount, population.getCount());
					break;
				case "denominator":
					assertEquals(denominatorCount, population.getCount());
					break;
				case "denominator-exclusion":
					assertEquals(denominatorExclusionCount, population.getCount());
					break;
				case "numerator":
					assertEquals(numeratorCount, population.getCount());
					break;
			}
		}

		Observation enrolledDuringParticipationPeriodObs = null;
		Observation participationPeriodObs = null;
		for (Resource r : returnMeasureReport.getContained()) {
			if (r instanceof Observation o) {
				if (o.getCode().getText().equals("Enrolled During Participation Period")) {
					enrolledDuringParticipationPeriodObs = o;
				} else if (o.getCode().getText().equals("Participation Period")) {
					participationPeriodObs = o;
				}
			}
		}

		assertNotNull(enrolledDuringParticipationPeriodObs);
		assertEquals(Boolean.toString(enrolledDuringParticipationPeriod).toLowerCase(),
			enrolledDuringParticipationPeriodObs.getValueCodeableConcept().getCodingFirstRep().getCode());

		assertNotNull(participationPeriodObs);
		assertEquals(participationPeriod, participationPeriodObs.getValueCodeableConcept().getCodingFirstRep().getCode());
	}

	@Test
	void testBCSEHEDISMY2022() {
		this.loadBundle("BCSEHEDISMY2022-bundle.json");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-5", 0, 0, 0, 0, false,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-7", 1, 1, 0, 0, true,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-9", 0, 0, 0, 0, true,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-21", 1, 0, 1, 0, true,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-23", 1, 1, 0, 0, true,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
		runWithPatient("BCSEHEDISMY2022", "Patient/Patient-65", 1, 1, 0, 1, true,
			"Interval[2020-10-01T00:00:00.000, 2022-12-31T23:59:59.999]");
	}

	@Test
	void testClientNonPatientBasedMeasureEvaluate() {
		this.loadBundle("ClientNonPatientBasedMeasureBundle.json");

		var measure = read(new IdType("Measure", "InitialInpatientPopulation"));
		assertNotNull(measure);

		MeasureReport returnMeasureReport = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "InitialInpatientPopulation"),
			"2019-01-01",
			"2020-01-01",
			"subject",
			"Patient/97f27374-8a5c-4aa1-a26f-5a1ab03caa47",
			null,
			null,
			null, null, null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReport);

		String populationName = "initial-population";
		int expectedCount = 2;

		Optional<MeasureReport.MeasureReportGroupPopulationComponent> population = returnMeasureReport.getGroup().get(0)
			.getPopulation().stream().filter(x -> x.hasCode() && x.getCode().hasCoding()
				&& x.getCode().getCoding().get(0).getCode().equals(populationName))
			.findFirst();

		assertTrue(population.isPresent(), String.format("Unable to locate a population with id \"%s\"", populationName));
		assertEquals(population.get().getCount(), expectedCount,
			String.format("expected count for population \"%s\" did not match", populationName));
	}

	@Test
	void testMeasureEvaluateMultiVersion() {
		this.loadBundle("multiversion/EXM124-7.0.000-bundle.json");
		this.loadBundle("multiversion/EXM124-9.0.000-bundle.json");

		MeasureReport returnMeasureReportVersion7 = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM124-7.0.000"),
			"2019-01-01",
			"2020-01-01",
			"subject",
			"Patient/numer-EXM124",
			null,
			"2019-12-12",
			null, null, null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReportVersion7);

		MeasureReport returnMeasureReportVersion9 = this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", "measure-EXM124-9.0.000"),
			"2019-01-01",
			"2020-01-01",
			"subject",
			"Patient/numer-EXM124",
			null,
			"2019-12-12",
			null, null, null,
			new SystemRequestDetails()
		);

		assertNotNull(returnMeasureReportVersion9);
	}

}
