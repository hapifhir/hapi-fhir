package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class MeasureOperationProviderTest extends BaseCrR4TestServer {
	@BeforeEach
	void setup() {
		// load resources
		loadBundle("ColorectalCancerScreeningsFHIR-bundle.json");
		loadBundle("Exm104FhirR4MeasureBundle.json");
		loadBundle("ClientNonPatientBasedMeasureBundle.json");
		loadBundle("multiversion/EXM124-7.0.000-bundle.json");
		loadBundle("multiversion/EXM124-9.0.000-bundle.json");
		loadBundle("largeValueSetMeasureTest-Bundle.json");
		loadBundle("BCSEHEDISMY2022-bundle.json");
	}
	@Test
	void runMeasureTests(){
		// run tests
		assertNotNull(testMeasureEvaluateExm130());
		assertNotNull(testMeasureEvaluateExm104());
		testClientNonPatientBasedMeasureEvaluate();
		testMeasureEvaluateMultiVersion();
		testLargeValuesetMeasure();
		testHedis2022();
	}

	public MeasureReport runEvaluateMeasure(String periodStart, String periodEnd, String subject, String measureId, String reportType, String practitioner){

		var parametersEval = new Parameters();
		parametersEval.addParameter("periodStart", new DateType(periodStart));
		parametersEval.addParameter("periodEnd", new DateType(periodEnd));
		parametersEval.addParameter("practitioner", practitioner);
		parametersEval.addParameter("reportType", reportType);
		parametersEval.addParameter("subject", subject);

		return  ourClient.operation().onInstance("Measure/" + measureId)
			.named(ProviderConstants.CR_OPERATION_EVALUATE_MEASURE)
			.withParameters(parametersEval)
			.returnResourceType(MeasureReport.class)
			.execute();
	}
	MeasureReport testMeasureEvaluateExm130() {
		return runEvaluateMeasure("2019-01-01", "2019-12-31", "Patient/numer-EXM130", "ColorectalCancerScreeningsFHIR", "Individual", null);
	}

	MeasureReport testMeasureEvaluateExm104() {
		return runEvaluateMeasure("2019-01-01", "2019-12-31", "Patient/numer-EXM104", "measure-EXM104-8.2.000", "Individual", null);
	}

	void testHedis2022() {

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

	void testClientNonPatientBasedMeasureEvaluate() {

		var measure = read(new IdType("Measure", "InitialInpatientPopulation"));
		assertNotNull(measure);

		var returnMeasureReport = runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/97f27374-8a5c-4aa1-a26f-5a1ab03caa47", "InitialInpatientPopulation", "Individual", null);


		String populationName = "initial-population";
		int expectedCount = 2;

		Optional<MeasureReport.MeasureReportGroupPopulationComponent> population = returnMeasureReport.getGroup().get(0)
			.getPopulation().stream().filter(x -> x.hasCode() && x.getCode().hasCoding()
				&& x.getCode().getCoding().get(0).getCode().equals(populationName))
			.findFirst();

		assertThat(population.isPresent()).as(String.format("Unable to locate a population with id \"%s\"",populationName)).isTrue();
		assertThat(expectedCount).as(String.format("expected count for population \"%s\" did not match",populationName)).isEqualTo(population.get().getCount());
	}
	void testMeasureEvaluateMultiVersion() {

		assertNotNull(runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/numer-EXM124", "measure-EXM124-7.0.000", "Individual", null));
		assertNotNull(runEvaluateMeasure("2019-01-01", "2020-01-01", "Patient/numer-EXM124", "measure-EXM124-9.0.000", "Individual", null));

	}

	void testLargeValuesetMeasure() throws NoSuchElementException {

		var returnMeasureReport = runEvaluateMeasure("2023-01-01", "2024-01-01", null, "CMSTest", "population", null);

		String populationName = "numerator";
		int expectedCount = 1;

		Optional<MeasureReport.MeasureReportGroupPopulationComponent> population = returnMeasureReport.getGroup().get(0)
			.getPopulation().stream().filter(x -> x.hasCode() && x.getCode().hasCoding()
				&& x.getCode().getCoding().get(0).getCode().equals(populationName))
			.findFirst();
		assertThat(population.isPresent()).as(String.format("population \"%s\" not found in report",populationName)).isTrue();
		assertThat(population.get().getCount()).as(String.format("expected count for population \"%s\" did not match",populationName)).isEqualTo(expectedCount);
	}
	private void runWithPatient(String measureId, String patientId, int initialPopulationCount, int denominatorCount,
								int denominatorExclusionCount, int numeratorCount, boolean enrolledDuringParticipationPeriod,
								String participationPeriod) {


		var returnMeasureReport = runEvaluateMeasure("2022-01-01", "2022-12-31", patientId, measureId, "Individual", null);

		for (MeasureReport.MeasureReportGroupPopulationComponent population : returnMeasureReport.getGroupFirstRep()
			.getPopulation())
			switch (population.getCode().getCodingFirstRep().getCode()) {
				case "initial-population" -> assertEquals(initialPopulationCount, population.getCount());
				case "denominator" -> assertEquals(denominatorCount, population.getCount());
				case "denominator-exclusion" -> assertEquals(denominatorExclusionCount, population.getCount());
				case "numerator" -> assertEquals(numeratorCount, population.getCount());
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
}
