package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.codesystems.MedicationRequestCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
	public void testQuestionnaireResponse() {
		String criteria = "QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord";

		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/HomeAbsenceHospitalizationRecord");
			assertMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/Other");
			assertNotMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setDisplay("Questionnaire/HomeAbsenceHospitalizationRecord");
			assertNotMatched(qr, criteria);
		}
	}

	@Test
	public void testCommunicationRequest() {
		String criteria = "CommunicationRequest?occurrence==2018-10-17";

		{
			CommunicationRequest cr = new CommunicationRequest();
			cr.setOccurrence(new DateTimeType("2018-10-17"));
			assertMatched(cr, criteria);
		}
		{
			CommunicationRequest cr = new CommunicationRequest();
			cr.setOccurrence(new DateTimeType("2018-10-16"));
			assertNotMatched(cr, criteria);
		}
		{
			CommunicationRequest cr = new CommunicationRequest();
			cr.setOccurrence(new DateTimeType("2018-10-16"));
			assertNotMatched(cr, criteria);
		}
	}

	@Test
	public void testProcedureRequest() {
		String criteria = "ProcedureRequest?intent=original-order";

		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.ORIGINALORDER);
			assertMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.ORDER);
			assertNotMatched(pr, criteria);
		}
	}

	@Test
	public void testObservationContextTypeUnsupported() {
		String criteria = "Observation?code=17861-6&context.type=IHD";
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("XXX");
			assertNotMatched(obs, criteria);
		}
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("17861-6");
			assertUnsupported(obs, criteria);
		}
	}

	// Check that it still fails fast even if the chained parameter is first
	@Test
	public void testObservationContextTypeUnsupportedReverse() {
		String criteria = "Observation?context.type=IHD&code=17861-6";
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("XXX");
			assertNotMatched(obs, criteria);
		}
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("17861-6");
			assertUnsupported(obs, criteria);
		}
	}

	@Test
	public void medicationRequestOutpatient() {
		// Note the date== evaluates to date=eq which is a legacy format supported by hapi fhir
		String criteria = "MedicationRequest?intent=instance-order&category=outpatient&date==2018-10-19";

		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.INSTANCEORDER);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			Dosage dosage = new Dosage();
			Timing timing = new Timing();
			timing.getEvent().add(new DateTimeType("2018-10-19"));
			dosage.setTiming(timing);
			mr.getDosageInstruction().add(dosage);
			assertMatched(mr, criteria);
		}

		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.INSTANCEORDER);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.INPATIENT.toCode());
			Dosage dosage = new Dosage();
			Timing timing = new Timing();
			timing.getEvent().add(new DateTimeType("2018-10-19"));
			dosage.setTiming(timing);
			mr.getDosageInstruction().add(dosage);
			assertNotMatched(mr, criteria);
		}

		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.INSTANCEORDER);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			Dosage dosage = new Dosage();
			Timing timing = new Timing();
			timing.getEvent().add(new DateTimeType("2018-10-20"));
			dosage.setTiming(timing);
			mr.getDosageInstruction().add(dosage);
			assertNotMatched(mr, criteria);
		}
	}

	@Test
	public void testMedicationRequestStatuses() {
		String criteria = "MedicationRequest?intent=plan&category=outpatient&status=suspended,entered-in-error,cancelled,stopped";

		// Note suspended is an invalid status and will never match
		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.PLAN);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			mr.setStatus(MedicationRequest.MedicationRequestStatus.ENTEREDINERROR);
			assertMatched(mr, criteria);
		}
		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.PLAN);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			mr.setStatus(MedicationRequest.MedicationRequestStatus.CANCELLED);
			assertMatched(mr, criteria);
		}
		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.PLAN);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			mr.setStatus(MedicationRequest.MedicationRequestStatus.STOPPED);
			assertMatched(mr, criteria);
		}
		{
			MedicationRequest mr = new MedicationRequest();
			mr.setIntent(MedicationRequest.MedicationRequestIntent.PLAN);
			mr.getCategory().addCoding().setCode(MedicationRequestCategory.OUTPATIENT.toCode());
			mr.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
			assertNotMatched(mr, criteria);
		}
	}

	@Test
	public void testBloodTest() {
		String criteria = "Observation?code=FR_Org1Blood2nd,FR_Org1Blood3rd,FR_Org%201BldCult,FR_Org2Blood2nd,FR_Org2Blood3rd,FR_Org%202BldCult,FR_Org3Blood2nd,FR_Org3Blood3rd,FR_Org3BldCult,FR_Org4Blood2nd,FR_Org4Blood3rd,FR_Org4BldCult,FR_Org5Blood2nd,FR_Org5Blood3rd,FR_Org%205BldCult,FR_Org6Blood2nd,FR_Org6Blood3rd,FR_Org6BldCult,FR_Org7Blood2nd,FR_Org7Blood3rd,FR_Org7BldCult,FR_Org8Blood2nd,FR_Org8Blood3rd,FR_Org8BldCult,FR_Org9Blood2nd,FR_Org9Blood3rd,FR_Org9BldCult,FR_Bld2ndCulture,FR_Bld3rdCulture,FR_Blood%20Culture,FR_Com1Bld3rd,FR_Com1BldCult,FR_Com2Bld2nd,FR_Com2Bld3rd,FR_Com2BldCult,FR_CultureBld2nd,FR_CultureBld3rd,FR_CultureBldCul,FR_GmStainBldCul,FR_GramStain2Bld,FR_GramStain3Bld,FR_GramStNegBac&context.type=IHD";

		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("FR_Org1Blood2nd");
			assertUnsupported(obs, criteria);
		}
		{
			Observation obs = new Observation();
			obs.getCode().addCoding().setCode("XXX");
			assertNotMatched(obs, criteria);
		}
	}

	@Test
	public void testProcedureHemodialysis() {
		String criteria = "Procedure?category=Hemodialysis";

		{
			Procedure proc = new Procedure();
			proc.getCategory().addCoding().setCode("Hemodialysis");
			assertMatched(proc, criteria);
		}
		{
			Procedure proc = new Procedure();
			proc.getCategory().addCoding().setCode("XXX");
			assertNotMatched(proc, criteria);
		}
	}

	@Test
	public void testProcedureHDStandard() {
		String criteria = "Procedure?code=HD_Standard&status=completed&location=Lab123";

		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("HD_Standard");
			proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
			IIdType locId = new IdType("Location", "Lab123");
			proc.getLocation().setReference(locId.getValue());
			assertMatched(proc, criteria);
		}
		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("HD_Standard");
			proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
			IIdType locId = new IdType("Location", "XXX");
			proc.getLocation().setReference(locId.getValue());
			assertNotMatched(proc, criteria);
		}
		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("XXX");
			proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
			IIdType locId = new IdType("Location", "Lab123");
			proc.getLocation().setReference(locId.getValue());
			assertNotMatched(proc, criteria);
		}
	}

	@Test
	public void testProvenance() {
		String criteria = "Provenance?activity=http://hl7.org/fhir/v3/DocumentCompletion%7CAU";

		SearchParameter sp = new SearchParameter();
		sp.addBase("Provenance");
		sp.setCode("activity");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Provenance.activity");
		sp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegsitry.forceRefresh();

		{
			Provenance prov = new Provenance();
			prov.setActivity(new Coding().setSystem("http://hl7.org/fhir/v3/DocumentCompletion").setCode("AU"));
			assertMatched(prov, criteria);
		}
		{
			Provenance prov = new Provenance();
			assertNotMatched(prov, criteria);
		}
		{
			Provenance prov = new Provenance();
			prov.setActivity(new Coding().setCode("XXX"));
			assertNotMatched(prov, criteria);
		}

	}

	@Test
	public void testBodySite() {
		String criteria = "BodySite?accessType=Catheter,PD%20Catheter";

		SearchParameter sp = new SearchParameter();
		sp.addBase("BodySite");
		sp.setCode("accessType");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("BodySite.extension('BodySite#accessType')");
		sp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegsitry.forceRefresh();

		{
			BodySite bodySite = new BodySite();
			bodySite.addExtension().setUrl("BodySite#accessType").setValue(new Coding().setCode("Catheter"));
			assertMatched(bodySite, criteria);
		}
		{
			BodySite bodySite = new BodySite();
			bodySite.addExtension().setUrl("BodySite#accessType").setValue(new Coding().setCode("PD Catheter"));
			assertMatched(bodySite, criteria);
		}
		{
			BodySite bodySite = new BodySite();
			assertNotMatched(bodySite, criteria);
		}
		{
			BodySite bodySite = new BodySite();
			bodySite.addExtension().setUrl("BodySite#accessType").setValue(new Coding().setCode("XXX"));
			assertNotMatched(bodySite, criteria);
		}

	}

	@Test
	public void testProcedureAnyLocation() {
		String criteria = "Procedure?code=HD_Standard&status=completed";
		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("HD_Standard");
			proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
			IIdType locId = new IdType("Location", "Lab456");
			proc.getLocation().setReference(locId.getValue());
			assertMatched(proc, criteria);
		}
		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("HD_Standard");
			proc.setStatus(Procedure.ProcedureStatus.ABORTED);
			assertNotMatched(proc, criteria);
		}
		{
			Procedure proc = new Procedure();
			proc.getCode().addCoding().setCode("XXX");
			proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
			assertNotMatched(proc, criteria);
		}
	}

	@Test
	public void testQuestionnaireResponseLong() {
		String criteria = "QuestionnaireResponse?questionnaire=HomeAbsenceHospitalizationRecord,ARIncenterAbsRecord,FMCSWDepressionSymptomsScreener,FMCAKIComprehensiveSW,FMCSWIntensiveScreener,FMCESRDComprehensiveSW,FMCNutritionProgressNote,FMCAKIComprehensiveRN";

		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/HomeAbsenceHospitalizationRecord");
			assertMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/FMCSWIntensiveScreener");
			assertMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/FMCAKIComprehensiveRN");
			assertMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			assertNotMatched(qr, criteria);
		}
		{
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.getQuestionnaire().setReference("Questionnaire/FMCAKIComprehensiveRM");
			assertNotMatched(qr, criteria);
		}
	}

	@Test
	public void testProcedureRequestCategory() {
		String criteria = "ProcedureRequest?intent=instance-order&category=Laboratory,Ancillary%20Orders,Hemodialysis&occurrence==2018-10-19";

		SearchParameter sp = new SearchParameter();
		sp.addBase("ProcedureRequest");
		sp.setCode("category");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("ProcedureRequest.category");
		sp.setXpathUsage(org.hl7.fhir.dstu3.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);
		mySearchParamRegsitry.forceRefresh();

		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("Laboratory");
			pr.getCategory().add(code);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("Ancillary Orders");
			pr.getCategory().add(code);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("Hemodialysis");
			pr.getCategory().add(code);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertNotMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("Hemodialysis");
			pr.getCategory().add(code);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertNotMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("Hemodialysis");
			pr.getCategory().add(code);
			assertNotMatched(pr, criteria);
		}
		{
			ProcedureRequest pr = new ProcedureRequest();
			pr.setIntent(ProcedureRequest.ProcedureRequestIntent.INSTANCEORDER);
			CodeableConcept code = new CodeableConcept();
			code.addCoding().setCode("XXX");
			pr.getCategory().add(code);
			pr.setOccurrence(new DateTimeType("2018-10-19"));
			assertNotMatched(pr, criteria);
		}
	}

	@Test
	public void testEposideOfCare() {
		String criteria = "EpisodeOfCare?status=active";
		{
			EpisodeOfCare eoc = new EpisodeOfCare();
			eoc.setStatus(EpisodeOfCare.EpisodeOfCareStatus.ACTIVE);
			assertMatched(eoc, criteria);
		}
		{
			EpisodeOfCare eoc = new EpisodeOfCare();
			assertNotMatched(eoc, criteria);
		}
		{
			EpisodeOfCare eoc = new EpisodeOfCare();
			eoc.setStatus(EpisodeOfCare.EpisodeOfCareStatus.CANCELLED);
			assertNotMatched(eoc, criteria);
		}
	}

	// These last two are covered by other tests above
	//				 String criteria = "ProcedureRequest?intent=original-order&category=Laboratory,Ancillary%20Orders,Hemodialysis&status=suspended,entered-in-error,cancelled";
	//				 String criteria = "Observation?code=70965-9&context.type=IHD";
}
