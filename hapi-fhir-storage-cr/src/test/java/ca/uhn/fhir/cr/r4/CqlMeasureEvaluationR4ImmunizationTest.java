package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the functionality of $evaluate-measure for the ImmunizationStatus use case
 */
@ExtendWith(SpringExtension.class)
public class CqlMeasureEvaluationR4ImmunizationTest extends BaseCrR4Test {

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
	protected MeasureReport evaluateMeasureByMeasure(String theMeasureId, String thePractitionerRef, String thePatientRef) {

		return this.myMeasureOperationsProvider.evaluateMeasure(
			new IdType("Measure", theMeasureId),
			null,
			null,
			"subject",
			thePatientRef,
			thePractitionerRef,
			null,
			null,
			null,
			null,
			new SystemRequestDetails());
	}

	@Test
	public void test_Immunization_Ontario_Schedule() throws IOException {
		//given
		loadBundle(MY_FHIR_COMMON);
		loadBundle(MY_FHIR_HELPERS);
		loadBundle(MY_TEST_DATA);
		loadBundle(MY_VALUE_SETS);
		loadBundle(MY_IMMUNIZATION_CQL_RESOURCES);

		//when
		MeasureReport reportBasic = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, null);
		MeasureReport reportByPractitioner = evaluateMeasureByMeasure("ImmunizationStatusRoutine", "Practitioner/ImmunizationStatus-practitioner-3", null);
		MeasureReport reportIndividualImmunized = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, "ImmunizationStatus-1-year-patient-1");
		MeasureReport reportIndividualNotImmunized = evaluateMeasureByMeasure("ImmunizationStatusRoutine", null, "ImmunizationStatus-1-year-patient-2");

		//then
		assertMeasureScore(reportBasic, 0.3442623); //21 out of 61 patients are fully immunized on 2022-09-16
		assertMeasureScore(reportByPractitioner, 0.23077); //3 out of 13 patients are fully immunized on 2022-09-16
		assertMeasureScore(reportIndividualImmunized, 1.0); // the patient is fully immunized on on 2022-09-16
		assertMeasureScore(reportIndividualNotImmunized, 0.0); // the patient is not fully immunized on 2022-09-16
	}
}
