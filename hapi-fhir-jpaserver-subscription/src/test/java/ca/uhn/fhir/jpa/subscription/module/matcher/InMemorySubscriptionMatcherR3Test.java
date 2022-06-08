package ca.uhn.fhir.jpa.subscription.module.matcher;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.dstu3.model.BodySite;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.CommunicationRequest;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.EpisodeOfCare;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import org.hl7.fhir.dstu3.model.Provenance;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.dstu3.model.Timing;
import org.hl7.fhir.dstu3.model.codesystems.MedicationRequestCategory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemorySubscriptionMatcherR3Test extends BaseSubscriptionDstu3Test {
	@Autowired
	SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;
	@Autowired
	SearchParamMatcher mySearchParamMatcher;
	@Autowired
	ModelConfig myModelConfig;

	private void assertUnsupported(IBaseResource resource, String criteria) {
		assertFalse(mySearchParamMatcher.match(criteria, resource, null).supported());
		assertEquals(SubscriptionMatchingStrategy.DATABASE, mySubscriptionStrategyEvaluator.determineStrategy(criteria));
	}

	private void assertMatched(IBaseResource resource, String criteria) {
		InMemoryMatchResult result = mySearchParamMatcher.match(criteria, resource, null);

		assertTrue(result.supported());
		assertTrue(result.matched());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY, mySubscriptionStrategyEvaluator.determineStrategy(criteria));
	}

	private void assertNotMatched(IBaseResource resource, String criteria) {
		assertNotMatched(resource, criteria, SubscriptionMatchingStrategy.IN_MEMORY);
	}

	private void assertNotMatched(IBaseResource resource, String criteria, SubscriptionMatchingStrategy theSubscriptionMatchingStrategy) {
		InMemoryMatchResult result = mySearchParamMatcher.match(criteria, resource, null);

		assertTrue(result.supported());
		assertFalse(result.matched());

		assertEquals(theSubscriptionMatchingStrategy, mySubscriptionStrategyEvaluator.determineStrategy(criteria));
	}

	@AfterEach
	public void after() {
		myModelConfig.setTreatBaseUrlsAsLocal(new ModelConfig().getTreatBaseUrlsAsLocal());
	}

	/**
	 * Technically this is an invalid reference in most cases, but this shouldn't choke
	 * the matcher in the case that it gets used.
	 */
	@Test
	public void testPlaceholderIdInReference() {

		ProcedureRequest pr = new ProcedureRequest();
		pr.setId("ProcedureRequest/123");
		pr.setIntent(ProcedureRequest.ProcedureRequestIntent.ORIGINALORDER);

		pr.setSubject(new Reference("urn:uuid:aaaaaaaaaa"));
		assertMatched(pr, "ProcedureRequest?intent=original-order");
		assertNotMatched(pr, "ProcedureRequest?subject=Patient/123");

		pr.setSubject(new Reference("Foo/123"));
		assertMatched(pr, "ProcedureRequest?intent=original-order");
		assertNotMatched(pr, "ProcedureRequest?subject=Patient/123");

		pr.setSubject(new Reference("Patient/"));
		assertMatched(pr, "ProcedureRequest?intent=original-order");
		assertNotMatched(pr, "ProcedureRequest?subject=Patient/123");
	}

	@Test
	public void testResourceById() {

		ProcedureRequest pr = new ProcedureRequest();
		pr.setId("ProcedureRequest/123");
		pr.setIntent(ProcedureRequest.ProcedureRequestIntent.ORIGINALORDER);

		assertMatched(pr, "ProcedureRequest?_id=123");
		assertMatched(pr, "ProcedureRequest?_id=ProcedureRequest/123");
		assertMatched(pr, "ProcedureRequest?_id=ProcedureRequest/123,ProcedureRequest/999");
		assertMatched(pr, "ProcedureRequest?_id=ProcedureRequest/123&_id=ProcedureRequest/123");
		assertNotMatched(pr, "ProcedureRequest?_id=ProcedureRequest/888");
		assertNotMatched(pr, "ProcedureRequest?_id=ProcedureRequest/888,ProcedureRequest/999");
		assertNotMatched(pr, "ProcedureRequest?_id=ProcedureRequest/123&_id=ProcedureRequest/888");

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
			assertNotMatched(obs, criteria, SubscriptionMatchingStrategy.DATABASE);
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
			assertNotMatched(obs, criteria, SubscriptionMatchingStrategy.DATABASE);
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
			assertNotMatched(obs, criteria, SubscriptionMatchingStrategy.DATABASE);
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
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);

		initSearchParamRegistry(sp);

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
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);

		initSearchParamRegistry(sp);

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
		sp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);

		initSearchParamRegistry(sp);

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

	@Test
	public void testCommunicationRequestWithRefAndDate() {
		String criteria = "CommunicationRequest?requester=O1271,O1276&occurrence=ge2019-02-08T00:00:00-05:00&occurrence=le2019-02-09T00:00:00-05:00";
		CommunicationRequest cr = new CommunicationRequest();
		cr.getRequester().getAgent().setReference("Organization/O1276");
		cr.setOccurrence(new DateTimeType("2019-02-08T00:01:00-05:00"));
		assertMatched(cr, criteria);
	}


	@Test
	public void testCommunicationRequestWithRef() {
		String criteria = "CommunicationRequest?requester=O1271,O1276";
		CommunicationRequest cr = new CommunicationRequest();
		cr.getRequester().getAgent().setReference("Organization/O1276");
		assertMatched(cr, criteria);
	}

	@Test
	public void testSystemWithNullValue() {
		String criteria = "Observation?code=17861-6";
		Observation observation = new Observation();
		CodeableConcept code = new CodeableConcept();
		observation.getCode().addCoding().setSystem("http://loinc.org");

		assertNotMatched(observation, criteria);
	}

	@Test
	public void testNullSystemNotNullValue() {
		String criteria = "Observation?code=17861-6";
		Observation observation = new Observation();
		CodeableConcept code = new CodeableConcept();
		observation.getCode().addCoding().setCode("look ma no system");

		assertNotMatched(observation, criteria);
	}

	@Test
	public void testExternalReferenceMatches() {
		String goodReference = "http://example.com/base/Organization/FOO";
		String goodCriteria = "Patient?organization=" + UrlUtil.escapeUrlParam(goodReference);

		String badReference1 = "http://example.com/bad/Organization/FOO";
		String badCriteria1 = "Patient?organization=" + UrlUtil.escapeUrlParam(badReference1);

		String badReference2 = "http://example.org/base/Organization/FOO";
		String badCriteria2 = "Patient?organization=" + UrlUtil.escapeUrlParam(badReference2);

		String badReference3 = "https://example.com/base/Organization/FOO";
		String badCriteria3 = "Patient?organization=" + UrlUtil.escapeUrlParam(badReference3);

		String badReference4 = "http://example.com/base/Organization/GOO";
		String badCriteria4 = "Patient?organization=" + UrlUtil.escapeUrlParam(badReference4);

		Set<String> urls = new HashSet<>();
		urls.add("http://example.com/base/");
		myModelConfig.setTreatBaseUrlsAsLocal(urls);

		Patient patient = new Patient();
		patient.getManagingOrganization().setReference("Organization/FOO");

		assertMatched(patient, goodCriteria);
		assertNotMatched(patient, badCriteria1);
		assertNotMatched(patient, badCriteria2);
		assertNotMatched(patient, badCriteria3);
		assertNotMatched(patient, badCriteria4);
	}

	@Test
	public void testLocationPositionNotSupported() {
		Location loc = new Location();
		double latitude = 30.0;
		double longitude = 40.0;
		Location.LocationPositionComponent position = new Location.LocationPositionComponent().setLatitude(latitude).setLongitude(longitude);
		loc.setPosition(position);
		double bigEnoughDistance = 100.0;
		String badCriteria =
			"Location?" +
				Location.SP_NEAR + "=" + latitude + ":" + longitude +
				"&" +
				Location.SP_NEAR_DISTANCE + "=" + bigEnoughDistance + "|http://unitsofmeasure.org|km";

		assertUnsupported(loc, badCriteria);
	}
}
