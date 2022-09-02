package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoDstu3ConceptMapTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ConceptMapTest.class);

	private IIdType myConceptMapId;

	@BeforeEach
	@Transactional
	public void before02() {
		myConceptMapId = myConceptMapDao.create(createConceptMap(), mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testDeleteConceptMap() {
		myConceptMapDao.delete(myConceptMapId);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertFalse(translationResult.getResult());
			}
		});

	}

	@Test
	public void testTranslateByCodeSystemsAndSourceCodeOneToMany() {
		ConceptMap conceptMap = myConceptMapDao.read(myConceptMapId);

		ourLog.info("ConceptMap:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(conceptMap));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to multiple target codes">
				TranslationRequest translationRequest = new TranslationRequest();
				translationRequest.getCodeableConcept().addCoding()
					.setSystem(CS_URL)
					.setCode("12345");
				translationRequest.setTargetSystem(new UriType(CS_URL_3));

				TranslateConceptResults translationResult = myConceptMapDao.translate(translationRequest, null);

				assertTrue(translationResult.getResult());
				assertEquals("Matches found", translationResult.getMessage());

				assertEquals(2, translationResult.getResults().size());

				TranslateConceptResult translationMatch = translationResult.getResults().get(0);
				assertEquals(Enumerations.ConceptMapEquivalence.EQUAL.toCode(), translationMatch.getEquivalence());
				assertEquals("56789", translationMatch.getCode());
				assertEquals("Target Code 56789", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());

				translationMatch = translationResult.getResults().get(1);
				assertEquals(Enumerations.ConceptMapEquivalence.WIDER.toCode(), translationMatch.getEquivalence());
				assertEquals("67890", translationMatch.getCode());
				assertEquals("Target Code 67890", translationMatch.getDisplay());
				assertEquals(CS_URL_3, translationMatch.getSystem());
				assertEquals("Version 4", translationMatch.getSystemVersion());
				assertEquals(CM_URL, translationMatch.getConceptMapUrl());
				// </editor-fold>
			}
		});
	}

	@Test
	public void testConceptMapFindTermConceptMapByUrl() {

		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpConceptMapList = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, CM_URL);
		assertEquals(1, theExpConceptMapList.size());
		assertEquals(CM_URL, theExpConceptMapList.get(0).getUrl());

	}

	@Test
	public void testConceptMapTwoConceptMapWithSameUrlDifferentVersion() {

		String theUrl = "http://loinc.org/property/analyte-suffix";
		ConceptMap theConceptMap1 = new ConceptMap();
		ConceptMap theConceptMap2 = new ConceptMap();

		theConceptMap1.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name1").setVersion("v1");
		theConceptMap2.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name2").setVersion("v2");

		myConceptMapDao.create(theConceptMap1);
		myConceptMapDao.create(theConceptMap2);

		Optional<TermConceptMap> theExpConceptMapV1 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v1");
		Optional<TermConceptMap> theExpConceptMapV2 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v2");

		assertTrue(theExpConceptMapV1.isPresent());
		assertEquals(theUrl, theExpConceptMapV1.get().getUrl());
		assertEquals("v1", theExpConceptMapV1.get().getVersion());

		assertTrue(theExpConceptMapV2.isPresent());
		assertEquals(theUrl, theExpConceptMapV2.get().getUrl());
		assertEquals("v2", theExpConceptMapV2.get().getVersion());

		// should return the latest one which is v2
		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpSecondOne = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, theUrl);

		assertEquals(1, theExpSecondOne.size());
		assertEquals(theUrl, theExpSecondOne.get(0).getUrl());
		assertEquals("v2", theExpSecondOne.get(0).getVersion());
	}

	@Test
	public void testConceptMapTwoConceptMapWithSameUrlOneWithoutVersion() {

		String theUrl = "http://loinc.org/property/analyte-suffix";
		ConceptMap theConceptMap1 = new ConceptMap();
		ConceptMap theConceptMap2 = new ConceptMap();

		theConceptMap1.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name1").setVersion("v1");
		theConceptMap2.setUrl(theUrl).setStatus(PublicationStatus.ACTIVE).setName("name2");

		myConceptMapDao.create(theConceptMap1);
		myConceptMapDao.create(theConceptMap2);

		Optional<TermConceptMap> theExpConceptMapV1 = myTermConceptMapDao.findTermConceptMapByUrlAndVersion(theUrl, "v1");

		assertTrue(theExpConceptMapV1.isPresent());
		assertEquals(theUrl, theExpConceptMapV1.get().getUrl());
		assertEquals("v1", theExpConceptMapV1.get().getVersion());

		// should return the latest one which in this case is not versioned
		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theExpSecondOne = myTermConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page, theUrl);

		assertEquals(1, theExpSecondOne.size());
		assertEquals(theUrl, theExpSecondOne.get(0).getUrl());
		assertNull(theExpSecondOne.get(0).getVersion());
	}
}
