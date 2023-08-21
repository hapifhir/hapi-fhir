package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4TestServer;
<<<<<<< HEAD:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/CrR4MeasureOperationProviderIT.java
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.hl7.fhir.r4.model.Bundle;
=======
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
>>>>>>> master:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/R4MeasureOperationProviderIT.java
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

<<<<<<< HEAD:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/CrR4MeasureOperationProviderIT.java

import java.io.IOException;
=======
>>>>>>> master:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/R4MeasureOperationProviderIT.java
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
<<<<<<< HEAD:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/CrR4MeasureOperationProviderIT.java
@ExtendWith(HoverflyExtension.class)
class CrR4MeasureOperationProviderIT extends BaseCrR4TestServer
{

	private static final String MY_FHIR_COMMON = "ca/uhn/fhir/cr/r4/immunization/Fhir_Common.json";
	private static final String MY_FHIR_HELPERS = "ca/uhn/fhir/cr/r4/immunization/Fhir_Helper.json";
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";
	private static final String MY_IMMUNIZATION_CQL_RESOURCES = "ca/uhn/fhir/cr/r4/immunization/Measure_Library_Ontario_ImmunizationStatus.json";
	private static final String MY_VALUE_SETS = "ca/uhn/fhir/cr/r4/immunization/Terminology_ValueSets.json";

	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;


	//compare 2 double values to assert no difference between expected and actual measure score
	protected void assertMeasureScore(MeasureReport theReport, double theExpectedScore) {
		//find the predefined expected score by looking up the report identifier
		double epsilon = 0.000001d;
		double actualScore = theReport.getGroupFirstRep().getMeasureScore().getValue().doubleValue();
		assertEquals(theExpectedScore, actualScore, epsilon);
	}

	//evaluates a Measure to produce one certain MeasureReport


	@Test
	public void test_Immunization_Ontario_Schedule() {
		//given
		var bundleFhirCommon = (Bundle) readResource(MY_FHIR_COMMON);
		ourClient.transaction().withBundle(bundleFhirCommon).execute();

		var bundleFhirHelpers = (Bundle) readResource(MY_FHIR_HELPERS);
		ourClient.transaction().withBundle(bundleFhirHelpers).execute();

		var bundleTestData = (Bundle) readResource(MY_TEST_DATA);
		ourClient.transaction().withBundle(bundleTestData).execute();

		var bundleValueSets = (Bundle) readResource(MY_VALUE_SETS);
		ourClient.transaction().withBundle(bundleValueSets).execute();

		var bundleCqlRsc = (Bundle) readResource(MY_IMMUNIZATION_CQL_RESOURCES);
		ourClient.transaction().withBundle(bundleCqlRsc).execute();

		//non-cached run, 1 patient
		var parametersEval1 = new Parameters();
		parametersEval1.addParameter("periodStart", new DateType("2020-01-01"));
		parametersEval1.addParameter("periodEnd", new DateType("2020-12-31"));
		//parametersEval.addParameter("practitioner", null);
		parametersEval1.addParameter("subject", "Patient/ImmunizationStatus-1-year-patient-1");

		var reportBasic = ourClient.operation().onInstance("Measure/ImmunizationStatusRoutine")
			.named("$evaluate-measure")
			.withParameters(parametersEval1)
			.returnResourceType(MeasureReport.class)
			.execute();

		assertNotNull(reportBasic);
/*
		//cached run, 13 patients
		var parametersEval2 = new Parameters();
		parametersEval2.addParameter("periodStart", new DateType("2020-01-01"));
		parametersEval2.addParameter("periodEnd", new DateType("2020-12-31"));
		parametersEval2.addParameter("practitioner", "Practitioner/ImmunizationStatus-practitioner-3");
		//parametersEval2.addParameter("subject", "Patient/ImmunizationStatus-1-year-patient-1");

		var reportBasic2 = ourClient.operation().onInstance("Measure/ImmunizationStatusRoutine")
			.named("$evaluate-measure")
			.withParameters(parametersEval2)
			.returnResourceType(MeasureReport.class)
			.execute();

		assertNotNull(reportBasic2);*/

	}
=======
class R4MeasureOperationProviderIT extends BaseCrR4TestServer
{
	@Autowired
	MeasureOperationsProvider myMeasureOperationsProvider;
>>>>>>> master:hapi-fhir-storage-cr/src/test/java/ca/uhn/fhir/cr/r4/R4MeasureOperationProviderIT.java

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


}
