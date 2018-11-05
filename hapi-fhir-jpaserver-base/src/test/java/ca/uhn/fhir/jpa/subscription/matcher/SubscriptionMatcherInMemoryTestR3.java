package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.config.TestDstu3Config;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubscriptionMatcherInMemoryTestR3 extends BaseResourceProviderDstu3Test {
	@Autowired
	SubscriptionMatcherInMemory mySubscriptionMatcherInMemory;

	private void assertUnsupported(IBaseResource resource, String criteria) {
		assertFalse(mySubscriptionMatcherInMemory.match(criteria, resource).supported());
	}

	private void assertMatched(IBaseResource resource, String criteria) {
		SubscriptionMatchResult result = mySubscriptionMatcherInMemory.match(criteria, resource);
		;
		assertTrue(result.supported());
		assertTrue(result.matched());
	}

	private void assertNotMatched(IBaseResource resource, String criteria) {
		SubscriptionMatchResult result = mySubscriptionMatcherInMemory.match(criteria, resource);
		;
		assertTrue(result.supported());
		assertFalse(result.matched());
	}

		/*
	The following tests are copied from an e-mail from a site using HAPI FHIR
	 */

	@Test
	public void testCrit1() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaire().setReference("Questionnaire/HomeAbsenceHospitalizationRecord");

		String criteria = "QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord";
		assertMatched(qr, criteria);

	}
//				 String criteria = "CommunicationRequest?occurrence==2018-10-17";
//				 String criteria = "ProcedureRequest?intent=original-order";
//				 String criteria = "Observation?code=17861-6&context.type=IHD";
//				 String criteria = "MedicationRequest?intent=instance-order&category=outpatient&date==2018-10-19";
//				 String criteria = "MedicationRequest?intent=plan&category=outpatient&status=suspended,entered-in-error,cancelled,stopped";
//				 String criteria = "Observation?code=FR_Org1Blood2nd,FR_Org1Blood3rd,FR_Org%201BldCult,FR_Org2Blood2nd,FR_Org2Blood3rd,FR_Org%202BldCult,FR_Org3Blood2nd,FR_Org3Blood3rd,FR_Org3BldCult,FR_Org4Blood2nd,FR_Org4Blood3rd,FR_Org4BldCult,FR_Org5Blood2nd,FR_Org5Blood3rd,FR_Org%205BldCult,FR_Org6Blood2nd,FR_Org6Blood3rd,FR_Org6BldCult,FR_Org7Blood2nd,FR_Org7Blood3rd,FR_Org7BldCult,FR_Org8Blood2nd,FR_Org8Blood3rd,FR_Org8BldCult,FR_Org9Blood2nd,FR_Org9Blood3rd,FR_Org9BldCult,FR_Bld2ndCulture,FR_Bld3rdCulture,FR_Blood%20Culture,FR_Com1Bld3rd,FR_Com1BldCult,FR_Com2Bld2nd,FR_Com2Bld3rd,FR_Com2BldCult,FR_CultureBld2nd,FR_CultureBld3rd,FR_CultureBldCul,FR_GmStainBldCul,FR_GramStain2Bld,FR_GramStain3Bld,FR_GramStNegBac&context.type=IHD";
//				 String criteria = "Procedure?category=Hemodialysis";
//				 String criteria = "Procedure?code=HD_Standard&status=completed&location=L2647";
//				 String criteria = "Provenance?activity=http://hl7.org/fhir/v3/DocumentCompletion%7CAU";
//				 String criteria = "BodySite?accessType=Catheter,PD%20Catheter";
//				 String criteria = "Procedure?code=HD_Standard&status=completed";
//				 String criteria = "QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord,FMCSWDepressionSymptomsScreener,FMCAKIComprehensiveSW,FMCSWIntensiveScreener,FMCESRDComprehensiveSW,FMCNutritionProgressNote,FMCAKIComprehensiveRN";
//				 String criteria = "ProcedureRequest?intent=instance-order&category=Laboratory,Ancillary%20Orders,Hemodialysis&occurrence==2018-10-19";
//				 String criteria = "EpisodeOfCare?status=active";
//				 String criteria = "ProcedureRequest?intent=original-order&category=Laboratory,Ancillary%20Orders,Hemodialysis&status=suspended,entered-in-error,cancelled";
//				 String criteria = "Observation?code=70965-9&context.type=IHD";
}
