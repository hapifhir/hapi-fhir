package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.ObservationLastNIndexPersistSvc;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersistObservationIndexedSearchParamLastNR4IT {

	private final String SINGLE_SUBJECT_ID = "4567";
	private final String SINGLE_OBSERVATION_PID = "123";
	private final Date SINGLE_EFFECTIVEDTM = new Date();
	private final String SINGLE_OBSERVATION_CODE_TEXT = "Test Codeable Concept Field for Code";
	private final String CATEGORYFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-category";
	private final String FIRSTCATEGORYFIRSTCODINGCODE = "test-heart-rate";
	private final String CODEFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-code";
	private final String CODEFIRSTCODINGCODE = "test-code";
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	protected FhirContext myFhirCtx;
	@Autowired
	ObservationLastNIndexPersistSvc testObservationPersist;
	@Autowired
	private ElasticsearchSvcImpl elasticsearchSvc;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> myDao;
	@Autowired
	private DaoConfig myDaoConfig;
	private ReferenceAndListParam multiSubjectParams = null;

	@BeforeEach
	public void before() throws IOException {
		myDaoConfig.setLastNEnabled(true);

		elasticsearchSvc.deleteAllDocumentsForTest(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.deleteAllDocumentsForTest(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

	}

	@AfterEach
	public void afterDisableLastN() {
		myDaoConfig.setLastNEnabled(new DaoConfig().isLastNEnabled());
	}

	@Order(3)
	@Test
	public void testIndexObservationSingle() throws IOException {
		indexSingleObservation();
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(10);
		List<ObservationJson> persistedObservationEntities = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirCtx);
		assertEquals(1, persistedObservationEntities.size());
		ObservationJson persistedObservationEntity = persistedObservationEntities.get(0);
		assertEquals(SINGLE_SUBJECT_ID, persistedObservationEntity.getSubject());
		assertEquals(SINGLE_OBSERVATION_PID, persistedObservationEntity.getIdentifier());
		assertEquals(SINGLE_EFFECTIVEDTM, persistedObservationEntity.getEffectiveDtm());

		String observationCodeNormalizedId = persistedObservationEntity.getCode_concept_id();

		// List<CodeJson> persistedObservationCodes = elasticsearchSvc.queryAllIndexedObservationCodesForTest();
		// assertEquals(1, persistedObservationCodes.size());

		// Check that we can retrieve code by hash value.
		String codeSystemHash = persistedObservationEntity.getCode_coding_code_system_hash();
		CodeJson persistedObservationCode = elasticsearchSvc.getObservationCodeDocument(codeSystemHash, null);
		assertNotNull(persistedObservationCode);
		assertEquals(observationCodeNormalizedId, persistedObservationCode.getCodeableConceptId());
		assertEquals(SINGLE_OBSERVATION_CODE_TEXT, persistedObservationCode.getCodeableConceptText());

		// Also confirm that we can retrieve code by text value.
		persistedObservationCode = elasticsearchSvc.getObservationCodeDocument(null, SINGLE_OBSERVATION_CODE_TEXT);
		assertNotNull(persistedObservationCode);

		searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", SINGLE_SUBJECT_ID);
		searchParameterMap.add(Observation.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().addOr(subjectParam)));
		TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));
		TokenParam codeParam = new TokenParam(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CODE, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(codeParam)));
		searchParameterMap.setLastNMax(3);

		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 100);

		assertEquals(1, observationIdsOnly.size());
		assertEquals(SINGLE_OBSERVATION_PID, observationIdsOnly.get(0));
	}

	private void indexSingleObservation() throws IOException {

		Observation myObservation = new Observation();
		IdType observationID = new IdType("Observation", SINGLE_OBSERVATION_PID, "1");
		myObservation.setId(observationID);
		Reference subjectId = new Reference(SINGLE_SUBJECT_ID);
		myObservation.setSubject(subjectId);
		myObservation.setEffective(new DateTimeType(SINGLE_EFFECTIVEDTM));

		myObservation.setCategory(getCategoryCode());

		myObservation.setCode(getObservationCode());

		testObservationPersist.indexObservation(myObservation);

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

	}

	private List<CodeableConcept> getCategoryCode() {
		// Add three CodeableConcepts for category
		List<CodeableConcept> categoryConcepts = new ArrayList<>();
		// Create three codings and first category CodeableConcept
		List<Coding> category = new ArrayList<>();
		CodeableConcept categoryCodeableConcept1 = new CodeableConcept().setText("Test Codeable Concept Field for first category");
		category.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE, "test-heart-rate display"));
		category.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-heart-rate", "test-alt-heart-rate display"));
		category.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-heart-rate", "test-2nd-alt-heart-rate display"));
		categoryCodeableConcept1.setCoding(category);
		categoryConcepts.add(categoryCodeableConcept1);
		// Create three codings and second category CodeableConcept
		List<Coding> category2 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept2 = new CodeableConcept().setText("Test Codeable Concept Field for for second category");
		category2.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, "test-vital-signs", "test-vital-signs display"));
		category2.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals", "test-alt-vitals display"));
		category2.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals", "test-2nd-alt-vitals display"));
		categoryCodeableConcept2.setCoding(category2);
		categoryConcepts.add(categoryCodeableConcept2);
		// Create three codings and third category CodeableConcept
		List<Coding> category3 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept3 = new CodeableConcept().setText("Test Codeable Concept Field for third category");
		category3.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, "test-vitals-panel", "test-vitals-panel display"));
		category3.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals-panel", "test-alt-vitals-panel display"));
		category3.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals-panel", "test-2nd-alt-vitals-panel display"));
		categoryCodeableConcept3.setCoding(category3);
		categoryConcepts.add(categoryCodeableConcept3);
		return categoryConcepts;
	}

	private CodeableConcept getObservationCode() {
		// Create CodeableConcept for Code with three codings.
		CodeableConcept codeableConceptField = new CodeableConcept().setText(SINGLE_OBSERVATION_CODE_TEXT);
		codeableConceptField.addCoding(new Coding(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE, "test-code display"));
		return codeableConceptField;
	}

	@Order(2)
	@Test
	public void testIndexObservationMultiple() throws IOException {
		indexMultipleObservations();
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(100);
		List<ObservationJson> observationDocuments = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirCtx);
		assertEquals(100, observationDocuments.size());

		// Check that all observations were indexed.
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, multiSubjectParams);

		searchParameterMap.setLastNMax(10);

		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);
		assertEquals(100, observationIdsOnly.size());

		// Filter the results by category code.
		TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));


		observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 100);

		assertEquals(50, observationIdsOnly.size());

	}

	private void indexMultipleObservations() throws IOException {

		// Create two CodeableConcept values each for a Code with three codings.
		CodeableConcept codeableConceptField1 = new CodeableConcept().setText("Test Codeable Concept Field for First Code");
		codeableConceptField1.addCoding(new Coding(CODEFIRSTCODINGSYSTEM, "test-code-1", "test-code-1 display"));
		codeableConceptField1.addCoding(new Coding("http://myalternatecodes.org/fhir/observation-code", "test-alt-code-1", "test-alt-code-1 display"));
		codeableConceptField1.addCoding(new Coding("http://mysecondaltcodes.org/fhir/observation-code", "test-second-alt-code-1", "test-second-alt-code-1 display"));

		CodeableConcept codeableConceptField2 = new CodeableConcept().setText("Test Codeable Concept Field for Second Code");
		codeableConceptField2.addCoding(new Coding(CODEFIRSTCODINGSYSTEM, "test-code-2", "test-code-2 display"));
		codeableConceptField2.addCoding(new Coding("http://myalternatecodes.org/fhir/observation-code", "test-alt-code-2", "test-alt-code-2 display"));
		codeableConceptField2.addCoding(new Coding("http://mysecondaltcodes.org/fhir/observation-code", "test-second-alt-code-2", "test-second-alt-code-2 display"));

		// Create two CodeableConcept entities for category, each with three codings.
		List<Coding> category1 = new ArrayList<>();
		// Create three codings and first category CodeableConcept
		category1.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE, "test-heart-rate display"));
		category1.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-heart-rate", "test-alt-heart-rate display"));
		category1.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-heart-rate", "test-2nd-alt-heart-rate display"));
		List<CodeableConcept> categoryConcepts1 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept1 = new CodeableConcept().setText("Test Codeable Concept Field for first category");
		categoryCodeableConcept1.setCoding(category1);
		categoryConcepts1.add(categoryCodeableConcept1);
		// Create three codings and second category CodeableConcept
		List<Coding> category2 = new ArrayList<>();
		category2.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, "test-vital-signs", "test-vital-signs display"));
		category2.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals", "test-alt-vitals display"));
		category2.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals", "test-2nd-alt-vitals display"));
		List<CodeableConcept> categoryConcepts2 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept2 = new CodeableConcept().setText("Test Codeable Concept Field for second category");
		categoryCodeableConcept2.setCoding(category2);
		categoryConcepts2.add(categoryCodeableConcept2);

		ReferenceOrListParam subjectParams = new ReferenceOrListParam();
		for (int patientCount = 0; patientCount < 10; patientCount++) {

			String subjectId = String.valueOf(patientCount);

			ReferenceParam subjectParam = new ReferenceParam("Patient", "", subjectId);
			subjectParams.addOr(subjectParam);

			for (int entryCount = 0; entryCount < 10; entryCount++) {

				Observation observation = new Observation();
				IdType observationId = new IdType("Observation", String.valueOf(entryCount + patientCount * 10), "1");
				observation.setId(observationId);
				Reference subject = new Reference(subjectId);
				observation.setSubject(subject);

				if (entryCount % 2 == 1) {
					observation.setCategory(categoryConcepts1);
					observation.setCode(codeableConceptField1);
				} else {
					observation.setCategory(categoryConcepts2);
					observation.setCode(codeableConceptField2);
				}

				Calendar observationDate = new GregorianCalendar();
				observationDate.add(Calendar.HOUR, -10 + entryCount);
				Date effectiveDtm = observationDate.getTime();
				observation.setEffective(new DateTimeType(effectiveDtm));

				testObservationPersist.indexObservation(observation);
			}

		}

		multiSubjectParams = new ReferenceAndListParam().addAnd(subjectParams);

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

	}

	@Order(0)
	@Test
	public void testDeleteObservation() throws IOException {
		indexMultipleObservations();
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(100);
		List<ObservationJson> observationDocuments = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirCtx);
		assertEquals(100, observationDocuments.size());
		// Check that fifth observation for fifth patient has been indexed.
		ObservationJson observation = elasticsearchSvc.getObservationDocument("55");
		assertNotNull(observation);

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, multiSubjectParams);
		searchParameterMap.setLastNMax(10);
		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);
		assertEquals(100, observationIdsOnly.size());
		assertTrue(observationIdsOnly.contains("55"));

		// Delete fifth observation for fifth patient.
		ResourceTable entity = new ResourceTable();
		entity.setId(55L);
		entity.setResourceType("Observation");
		entity.setVersion(0L);

		testObservationPersist.deleteObservationIndex(entity);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);

		// Confirm that observation was deleted.
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(100);
		observationDocuments = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirCtx);
		assertEquals(99, observationDocuments.size());
		observation = elasticsearchSvc.getObservationDocument("55");
		assertNull(observation);

		observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);
		assertEquals(99, observationIdsOnly.size());
		assertTrue(!observationIdsOnly.contains("55"));

	}

	@Order(4)
	@Test
	public void testUpdateObservation() throws IOException {
		indexSingleObservation();
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(10);
		ObservationJson observationIndexEntity = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirCtx).get(0);
		assertEquals(SINGLE_OBSERVATION_PID, observationIndexEntity.getIdentifier());
		assertEquals(SINGLE_SUBJECT_ID, observationIndexEntity.getSubject());
		assertEquals(SINGLE_EFFECTIVEDTM, observationIndexEntity.getEffectiveDtm());

		searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", SINGLE_SUBJECT_ID);
		searchParameterMap.add(Observation.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().addOr(subjectParam)));
		TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));
		TokenParam codeParam = new TokenParam(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CODE, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(codeParam)));
		searchParameterMap.setLastNMax(10);

		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);
		assertEquals(1, observationIdsOnly.size());
		assertTrue(observationIdsOnly.contains(SINGLE_OBSERVATION_PID));

		// Update the Observation with a new Subject and effective date:
		Observation updatedObservation = new Observation();
		IdType observationId = new IdType("Observation", observationIndexEntity.getIdentifier(), "2");
		updatedObservation.setId(observationId);
		Reference subjectId = new Reference("1234");
		updatedObservation.setSubject(subjectId);
		DateTimeType newEffectiveDtm = new DateTimeType(new Date());
		updatedObservation.setEffective(newEffectiveDtm);
		updatedObservation.setCategory(getCategoryCode());
		updatedObservation.setCode(getObservationCode());

		testObservationPersist.indexObservation(updatedObservation);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);

		ObservationJson updatedObservationEntity = elasticsearchSvc.getObservationDocument(SINGLE_OBSERVATION_PID);
		assertEquals("1234", updatedObservationEntity.getSubject());
		assertEquals(newEffectiveDtm.getValue(), updatedObservationEntity.getEffectiveDtm());

		// Repeat earlier Elasticsearch query. This time, should return no matches.
		observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);
		assertEquals(0, observationIdsOnly.size());

		// Try again with the new patient ID.
		searchParameterMap = new SearchParameterMap();
		subjectParam = new ReferenceParam("Patient", "", "1234");
		searchParameterMap.add(Observation.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().addOr(subjectParam)));
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));
		searchParameterMap.add(Observation.SP_CODE, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(codeParam)));
		searchParameterMap.setLastNMax(10);
		observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);

		// Should see the observation returned now.
		assertEquals(1, observationIdsOnly.size());
		assertTrue(observationIdsOnly.contains(SINGLE_OBSERVATION_PID));

	}

	@Order(1)
	@Test
	public void testSampleBundleInTransaction() throws IOException {
		FhirContext myFhirCtx = FhirContext.forR4Cached();

		PathMatchingResourcePatternResolver provider = new PathMatchingResourcePatternResolver();
		final Resource[] bundleResources;
		bundleResources = provider.getResources("lastntestbundle.json");

		AtomicInteger index = new AtomicInteger();

		Arrays.stream(bundleResources).forEach(
			resource -> {
				index.incrementAndGet();

				InputStream resIs = null;
				String nextBundleString;
				try {
					resIs = resource.getInputStream();
					nextBundleString = IOUtils.toString(resIs, Charsets.UTF_8);
				} catch (IOException e) {
					return;
				} finally {
					try {
						if (resIs != null) {
							resIs.close();
						}
					} catch (final IOException ioe) {
						// ignore
					}
				}

				IParser parser = myFhirCtx.newJsonParser();
				Bundle bundle = parser.parseResource(Bundle.class, nextBundleString);

				myDao.transaction(null, bundle);

			}
		);

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);

		SearchParameterMap searchParameterMap = new SearchParameterMap();

		// execute Observation ID search - Composite Aggregation
		searchParameterMap.setLastNMax(1);
		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);

		assertEquals(20, observationIdsOnly.size());

		searchParameterMap.setLastNMax(3);
		observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirCtx, 200);

		assertEquals(38, observationIdsOnly.size());

	}

}
