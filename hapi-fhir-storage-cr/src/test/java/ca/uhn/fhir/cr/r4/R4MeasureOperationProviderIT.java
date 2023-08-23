package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class R4MeasureOperationProviderIT extends BaseCrR4TestServer
{
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;

	public MeasureReport runEvaluateMeasure(String periodStart, String periodEnd, String subject, String measureId, String reportType, String practitioner){

		var parametersEval = new Parameters();
		parametersEval.addParameter("periodStart", new DateType(periodStart));
		parametersEval.addParameter("periodEnd", new DateType(periodEnd));
		parametersEval.addParameter("practitioner", practitioner);
		parametersEval.addParameter("reportType", reportType);
		parametersEval.addParameter("subject", subject);

		var report = ourClient.operation().onInstance("Measure/" + measureId)
			.named("$evaluate-measure")
			.withParameters(parametersEval)
			.returnResourceType(MeasureReport.class)
			.execute();

		assertNotNull(report);

		return report;
	}

	@Test
	void testMeasureEvaluate_EXM130()  {
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		runEvaluateMeasure("2019-01-01", "2019-12-31", "Patient/numer-EXM130", "ColorectalCancerScreeningsFHIR", "Individual", null);
	}

	private void runWithPatient(String measureId, String patientId, int initialPopulationCount, int denominatorCount,
										 int denominatorExclusionCount, int numeratorCount, boolean enrolledDuringParticipationPeriod,
										 String participationPeriod) {


		var returnMeasureReport = runEvaluateMeasure("2022-01-01", "2022-12-31", patientId, measureId, "Individual", null);

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
		loadBundle("BCSEHEDISMY2022-bundle.json");

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

		var returnMeasureReport = runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/97f27374-8a5c-4aa1-a26f-5a1ab03caa47", "InitialInpatientPopulation", "Individual", null);


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

		runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/numer-EXM124", "measure-EXM124-7.0.000", "Individual", null);
		runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/numer-EXM124", "measure-EXM124-9.0.000", "Individual", null);

	}

	@Test
	void testLargeValuesetMeasure() {
		this.loadBundle("largeValueSetMeasureTest-Bundle.json");

		var returnMeasureReport = runEvaluateMeasure("2023-01-01", "2024-01-01", null, "CMSTest", "population", null);

		String populationName = "numerator";
		int expectedCount = 1;

		Optional<MeasureReport.MeasureReportGroupPopulationComponent> population = returnMeasureReport.getGroup().get(0)
			.getPopulation().stream().filter(x -> x.hasCode() && x.getCode().hasCoding()
				&& x.getCode().getCoding().get(0).getCode().equals(populationName))
			.findFirst();

		assertEquals(population.get().getCount(), expectedCount,
			String.format("expected count for population \"%s\" did not match", populationName));

	}

}
