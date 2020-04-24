package ca.uhn.fhir.jpa.subscription.module.matcher;

import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.matchers.JUnitMatchers.containsString;

public class SubscriptionStrategyEvaluatorTest extends BaseSubscriptionDstu3Test {
	@Autowired
	SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testInMemory() {
		assertInMemory("Observation?");
		assertInMemory("QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord");
		assertInMemory("CommunicationRequest?occurrence==2018-10-17");
		assertInMemory("ProcedureRequest?intent=original-order");
		assertInMemory("MedicationRequest?intent=instance-order&category=outpatient&date==2018-10-19");
		assertInMemory("MedicationRequest?intent=plan&category=outpatient&status=suspended,entered-in-error,cancelled,stopped");
		assertDatabase("Observation?code=FR_Org1Blood2nd,FR_Org1Blood3rd,FR_Org%201BldCult,FR_Org2Blood2nd,FR_Org2Blood3rd,FR_Org%202BldCult,FR_Org3Blood2nd,FR_Org3Blood3rd,FR_Org3BldCult,FR_Org4Blood2nd,FR_Org4Blood3rd,FR_Org4BldCult,FR_Org5Blood2nd,FR_Org5Blood3rd,FR_Org%205BldCult,FR_Org6Blood2nd,FR_Org6Blood3rd,FR_Org6BldCult,FR_Org7Blood2nd,FR_Org7Blood3rd,FR_Org7BldCult,FR_Org8Blood2nd,FR_Org8Blood3rd,FR_Org8BldCult,FR_Org9Blood2nd,FR_Org9Blood3rd,FR_Org9BldCult,FR_Bld2ndCulture,FR_Bld3rdCulture,FR_Blood%20Culture,FR_Com1Bld3rd,FR_Com1BldCult,FR_Com2Bld2nd,FR_Com2Bld3rd,FR_Com2BldCult,FR_CultureBld2nd,FR_CultureBld3rd,FR_CultureBldCul,FR_GmStainBldCul,FR_GramStain2Bld,FR_GramStain3Bld,FR_GramStNegBac&context.type=IHD");
		assertInMemory("Procedure?category=Hemodialysis");
		assertInMemory("Procedure?code=HD_Standard&status=completed&location=Lab123");
		assertInMemory("Procedure?code=HD_Standard&status=completed");
		assertInMemory("QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord,FMCSWDepressionSymptomsScreener,FMCAKIComprehensiveSW,FMCSWIntensiveScreener,FMCESRDComprehensiveSW,FMCNutritionProgressNote,FMCAKIComprehensiveRN");
		assertInMemory("EpisodeOfCare?status=active");
		assertInMemory("Observation?code=111111111&_format=xml");
		assertInMemory("Observation?code=SNOMED-CT|123&_format=xml");

		assertDatabase("Observation?code=17861-6&context.type=IHD");
		assertDatabase("Observation?context.type=IHD&code=17861-6");

		exception.expect(InvalidRequestException.class);
		exception.expectMessage(containsString("Resource type Observation does not have a parameter with name: codeee"));
		assertInMemory("Observation?codeee=SNOMED-CT|123&_format=xml");
	}

	private void assertDatabase(String theCriteria) {
		assertEquals(SubscriptionMatchingStrategy.DATABASE, mySubscriptionStrategyEvaluator.determineStrategy(theCriteria));
	}

	private void assertInMemory(String theCriteria) {
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY, mySubscriptionStrategyEvaluator.determineStrategy(theCriteria));
	}
}

