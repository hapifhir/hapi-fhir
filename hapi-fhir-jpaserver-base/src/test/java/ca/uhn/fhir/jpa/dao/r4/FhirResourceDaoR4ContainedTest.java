package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.util.TestUtil;

import static org.junit.Assert.assertEquals;

public class FhirResourceDaoR4ContainedTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ContainedTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void before() {
		myDaoConfig.setIndexContainedResources(true);
	}

	@Test
	public void testIndexContained() {
		Patient p = new Patient();
		p.setId("#some_patient");
		p.addName().setFamily("MYFAMILY").addGiven("MYGIVEN");

		Observation o1 = new Observation();
		o1.getCode().setText("Some Observation");
		o1.setSubject(new Reference(p));
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getCode().setText("Some Observation");
		o2.setSubject(new Reference(p));
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("MYFAMILY").addGiven("MYGIVEN");
		IIdType pid2 = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(o2));


		SearchParameterMap map;

//		map = new SearchParameterMap();
//		map.add(Observation.SP_CODE, new TokenParam(null, "some observation").setModifier(TokenParamModifier.TEXT));
//		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));

	}
	//TODO: make sure match URLs don't delete

	@Test
	public void testContainedValueSetQuestionnaire() {
		Questionnaire questionnaire = new Questionnaire().setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet valueSet = new ValueSet();
		valueSet.setId("myId");
		valueSet.getCompose().getIncludeFirstRep().getConceptFirstRep().setDisplay("display");
		valueSet.getCompose().getIncludeFirstRep().getConceptFirstRep().setDisplay("code");
		questionnaire.addContained(valueSet);
		questionnaire.getItemFirstRep().setAnswerValueSet("#myid");

		System.out.println(myFhirCtx.newXmlParser().setParserErrorHandler(new StrictErrorHandler()).setPrettyPrint(true).encodeResourceToString(questionnaire));

		IIdType id = myQuestionnaireDao.create(questionnaire).getId().toUnqualified();
		Questionnaire readQuestionnaire = myQuestionnaireDao.read(id);
		assertEquals(1, readQuestionnaire.getContained().size());
	}

	@Test
	public void testContainedPatientQuestionnaire() {
		Questionnaire questionnaire = new Questionnaire().setStatus(Enumerations.PublicationStatus.ACTIVE);
		Questionnaire containedQuest = new Questionnaire().setStatus(Enumerations.PublicationStatus.ACTIVE);
		containedQuest.setId("myId");
		questionnaire.addContained(containedQuest);
		questionnaire.addDerivedFrom("#myId");

		System.out.println(myFhirCtx.newXmlParser().setParserErrorHandler(new StrictErrorHandler()).setPrettyPrint(true).encodeResourceToString(questionnaire));

		IIdType id = myQuestionnaireDao.create(questionnaire).getId().toUnqualified();
		Questionnaire readQuestionnaire = myQuestionnaireDao.read(id);
		assertEquals(1, readQuestionnaire.getContained().size());
	}

	@Test
	public void testContainedPatientObservation() {
		Observation obs = new Observation();
		Patient containedPat = new Patient();
		containedPat.setId("myId");
		obs.addContained(containedPat);
		obs.getSubject().setReference("#myId");

		System.out.println(myFhirCtx.newXmlParser().setParserErrorHandler(new StrictErrorHandler()).setPrettyPrint(true).encodeResourceToString(obs));

		IIdType id = myObservationDao.create(obs).getId().toUnqualified();
		Observation readObs = myObservationDao.read(id);
		assertEquals(1, readObs.getContained().size());
	}
}
