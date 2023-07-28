package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseR4TestServer;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class R4MeasureOperationProviderIT extends BaseR4TestServer
{

	private static final String MY_FHIR_COMMON = "ca/uhn/fhir/cr/r4/immunization/Fhir_Common.json";
	private static final String MY_FHIR_HELPERS = "ca/uhn/fhir/cr/r4/immunization/Fhir_Helper.json";
	private static final String MY_TEST_DATA = "ca/uhn/fhir/cr/r4/immunization/Patients_Encounters_Immunizations_Practitioners.json";
	private static final String MY_IMMUNIZATION_CQL_RESOURCES = "ca/uhn/fhir/cr/r4/immunization/Measure_Library_Ontario_ImmunizationStatus.json";
	private static final String MY_VALUE_SETS = "ca/uhn/fhir/cr/r4/immunization/Terminology_ValueSets.json";


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

}
