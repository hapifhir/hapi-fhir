package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.jpa.api.pid.StreamTemplate;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.JpaResourceDao;
import ca.uhn.fhir.jpa.dao.r4.suites.IPatchTests;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.EntityIndexStatusEnum;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Age;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.CompartmentDefinition;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Quantity.QuantityComparator;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Timing;
import org.hl7.fhir.r4.model.UriType;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
public class FhirResourceDaoR4Test extends BaseJpaR4Test implements IPatchTests {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4Test.class);

	@AfterEach
	public void after() {
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setTreatReferencesAsLogical(new JpaStorageSettings().getTreatReferencesAsLogical());
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(new JpaStorageSettings().isEnforceReferentialIntegrityOnDelete());
		myStorageSettings.setEnforceReferenceTargetTypes(new JpaStorageSettings().isEnforceReferenceTargetTypes());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myStorageSettings.setInternalSynchronousSearchSize(new JpaStorageSettings().getInternalSynchronousSearchSize());
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		myStorageSettings.setHistoryCountMode(JpaStorageSettings.DEFAULT_HISTORY_COUNT_MODE);
		myStorageSettings.setMassIngestionMode(false);
		myStorageSettings.setWriteToSearchParamIdentityTable(false);
	}

	@BeforeEach
	public void beforeEach() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setWriteToSearchParamIdentityTable(true);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public JpaStorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	private List<String> extractNames(IBundleProvider theSearch) {
		ArrayList<String> retVal = new ArrayList<>();
		for (IBaseResource next : theSearch.getResources(0, theSearch.sizeOrThrowNpe())) {
			Patient nextPt = (Patient) next;
			retVal.add(nextPt.getName().get(0).getNameAsSingleString());
		}
		return retVal;
	}

	private CodeableConcept newCodeableConcept(String theSystem, String theCode) {
		CodeableConcept retVal = new CodeableConcept();
		retVal.addCoding().setSystem(theSystem).setCode(theCode);
		return retVal;
	}

	private void sort(ArrayList<Coding> thePublished) {
		ArrayList<Coding> tags = new ArrayList<>(thePublished);
		Collections.sort(tags, new Comparator<Coding>() {
			@Override
			public int compare(Coding theO1, Coding theO2) {
				int retVal = defaultString(theO1.getSystem()).compareTo(defaultString(theO2.getSystem()));
				if (retVal == 0) {
					retVal = defaultString(theO1.getCode()).compareTo(defaultString(theO2.getCode()));
				}
				return retVal;
			}
		});
		thePublished.clear();
		for (Coding next : tags) {
			thePublished.add(next);
		}
	}

	private void sortCodings(List<Coding> theSecLabels) {
		Collections.sort(theSecLabels, new Comparator<Coding>() {
			@Override
			public int compare(Coding theO1, Coding theO2) {
				return theO1.getSystemElement().getValue().compareTo(theO2.getSystemElement().getValue());
			}
		});
	}

	private List<CanonicalType> sortIds(List<CanonicalType> theProfiles) {
		ArrayList<CanonicalType> retVal = new ArrayList<>(theProfiles);
		Collections.sort(retVal, new Comparator<UriType>() {
			@Override
			public int compare(UriType theO1, UriType theO2) {
				return theO1.getValue().compareTo(theO2.getValue());
			}
		});
		return retVal;
	}

	@Test
	public void testUpdateResourceTwiceInSameTransaction() {
		// Update with a string param that changes, and a token param that does not
		runInTransaction(()->{
			createOrganization(withId("A"), withName("1"), withActiveTrue());
			createOrganization(withId("A"), withName("2"), withActiveTrue());
		});

		assertEquals("2", myOrganizationDao.read(new IdType("Organization/A"), mySrd).getName());
		assertTrue(myOrganizationDao.read(new IdType("Organization/A"), mySrd).getActive());

		logAllStringIndexes();
		runInTransaction(()->{
			List<String> stringIndexes = getAllStringIndexes("name")
				.stream().map(ResourceIndexedSearchParamString::getValueExact).toList();
			assertThat(stringIndexes).asList().containsExactly("2");
			List<String> tokenIndexes = getAllTokenIndexes("active")
				.stream().map(ResourceIndexedSearchParamToken::getValue).toList();
			assertThat(tokenIndexes).asList().containsExactly("true");
		});

		runInTransaction(()->{
			assertEquals(1, myResourceTableDao.count());
			assertThat(myResourceHistoryTableDao.findAll().stream().map(t->t.getVersion()).sorted().toList())
				.asList()
				.containsExactly(1L, 2L);
			assertEquals(2L, myResourceTableDao.findAll().iterator().next().getVersion());
		});

		myCaptureQueriesListener.clear();
		runInTransaction(()->{
			createOrganization(withId("A"), withName("3"), withActiveTrue());
			createOrganization(withId("A"), withName("4"), withActiveTrue());
		});
		myCaptureQueriesListener.logUpdateQueries();

		runInTransaction(()->{
			assertEquals(1, myResourceTableDao.count());
			assertThat(myResourceHistoryTableDao.findAll().stream().map(t->t.getVersion()).sorted().toList())
				.asList()
				.containsExactly(1L, 2L, 3L, 4L);
			assertEquals(4L, myResourceTableDao.findAll().iterator().next().getVersion());
		});

		assertEquals("4", myOrganizationDao.read(new IdType("Organization/A"), mySrd).getName());
		for (int i = 1; i <= 4; i++) {
			assertEquals(Integer.toString(i), myOrganizationDao.read(new IdType("Organization/A/_history/" + i), mySrd).getName());
		}
		assertEquals("4", myOrganizationDao.read(new IdType("Organization/A"), mySrd).getIdElement().getVersionIdPart());
		assertTrue(myOrganizationDao.read(new IdType("Organization/A"), mySrd).getActive());


		runInTransaction(()->{
			List<String> stringIndexes = getAllStringIndexes("name")
				.stream().map(ResourceIndexedSearchParamString::getValueExact).toList();
			assertThat(stringIndexes).asList().containsExactly("4");
			List<String> tokenIndexes = getAllTokenIndexes("active")
				.stream().map(ResourceIndexedSearchParamToken::getValue).toList();
			assertThat(tokenIndexes).asList().containsExactly("true");
		});
	}

	@Test
	public void testUpdateResource_whenTokenPropertyAssignedTooLargeValue_willTruncateLargeValueOnUpdate(){
		// given
		final String modifiedEmailPrefix = "modified";
		final String originalEmail = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH) + "@acme.corp";
		final String modifiedEmail = modifiedEmailPrefix + originalEmail;

		// when
		Patient pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAM");
		pt1.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(originalEmail);

		myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		pt1.getTelecomFirstRep().setValue(modifiedEmail);

		IIdType id1 = myPatientDao.update(pt1).getId().toUnqualifiedVersionless();

		// then
		runInTransaction(() -> {
			List<String> paramValues = myResourceIndexedSearchParamTokenDao
				.findAll()
				.stream()
				.filter(t -> defaultString(t.getSystem()).equals("email"))
				.map(t -> t.getValue())
				.collect(Collectors.toList());

			assertThat(paramValues).hasSize(2);

			for (String tokenValue : paramValues) {
				assertThat(tokenValue).startsWith(modifiedEmailPrefix);
				assertThat(tokenValue).hasSize(ResourceIndexedSearchParamToken.MAX_LENGTH);
			}
		});
	}

	@Test
	public void testDeletedResourcesAreReindexed() {
		myStorageSettings.setSchedulingDisabled(true);

		Patient pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertThat(myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(id1.getIdPartAsLong()))).isGreaterThan(0);
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			assertEquals(EntityIndexStatusEnum.INDEXED_RDBMS_ONLY, tableOpt.get().getIndexStatus());
		});

		runInTransaction(() -> {
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			ResourceTable table = tableOpt.get();
			table.setIndexStatus(null);
			table.setDeleted(new Date());
			table = myResourceTableDao.saveAndFlush(table);
			ResourceHistoryTable newHistory = table.toHistory(true);
			ResourceHistoryTable currentHistory = myResourceHistoryTableDao.findForIdAndVersion(table.getId().toFk(), 1L);
			newHistory.setEncoding(currentHistory.getEncoding());
			newHistory.setResourceTextVc(currentHistory.getResourceTextVc());
			myResourceHistoryTableDao.save(newHistory);
		});

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();

		runInTransaction(() -> {
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			assertEquals(EntityIndexStatusEnum.INDEXED_RDBMS_ONLY, tableOpt.get().getIndexStatus());
			assertThat(myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(id1.getIdPartAsLong()))).isLessThanOrEqualTo(0);
		});
	}

	@Test
	public void testStoreReferenceFromContainedToContainer() {
		Patient patient = new Patient();
		patient.setActive(true);

		Provenance provenance = new Provenance();
		provenance.setId("#1");
		provenance.addTarget().setReference("#");
		patient.getContained().add(provenance);

		Observation observation = new Observation();
		observation.setId("#2");
		observation.getSubject().setReference("#");
		patient.getContained().add(observation);

		IIdType id = myPatientDao.create(patient).getId();

		patient = myPatientDao.read(id);

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		assertThat(patient.getContained()).hasSize(2);

		provenance = (Provenance) patient.getContained().get(0);
		assertEquals("1", provenance.getId());
		assertEquals("#", provenance.getTargetFirstRep().getReference());

		observation = (Observation) patient.getContained().get(1);
		assertEquals("2", observation.getId());
		assertEquals("#", observation.getSubject().getReference());
	}

	@Tag("intermittent")
	//	@Test
	public void testTermConceptReindexingDoesntDuplicateData() {
		myStorageSettings.setSchedulingDisabled(true);

		CodeSystem cs = new CodeSystem();
		cs.setId("nhin-use");
		cs.setUrl("http://zoop.com");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("zoop1").setDisplay("zoop_disp1").setDefinition("zoop_defi1");
		cs.addConcept().setCode("zoop2").setDisplay("zoop_disp2").setDefinition("zoop_defi2");
		cs.addConcept().setCode("zoop3").setDisplay("zoop_disp3").setDefinition("zoop_defi3");

		IIdType id1 = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertEquals(3L, myTermConceptDao.count());

			SearchSession session = Search.session(myEntityManager);
			List<TermConcept> termConcepts = session.search(TermConcept.class).where(f -> f.matchAll()).fetchAllHits();
			assertEquals(3, termConcepts.size());
		});

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		runInTransaction(() -> {
			assertEquals(3L, myTermConceptDao.count());

			SearchSession session = Search.session(myEntityManager);
			List<TermConcept> termConcepts = session.search(TermConcept.class).where(f -> f.matchAll()).fetchAllHits();
			assertEquals(3, termConcepts.size());
		});
	}

	@Test
	public void testMissingVersionsAreReindexed() {
		myStorageSettings.setSchedulingDisabled(true);

		Patient pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			assertThat(myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(id1.getIdPartAsLong()))).isGreaterThan(0);
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			assertEquals(EntityIndexStatusEnum.INDEXED_RDBMS_ONLY, tableOpt.get().getIndexStatus());
		});

		/*
		 * This triggers a new version in the HFJ_RESOURCE table, but
		 * we do not create the corresponding entry in the HFJ_RES_VER
		 * table.
		 */
		runInTransaction(() -> {
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			ResourceTable table = tableOpt.get();
			table.setIndexStatus(null);
			table.setDeleted(new Date());
			myResourceTableDao.saveAndFlush(table);
		});

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();

		runInTransaction(() -> {
			Optional<ResourceTable> tableOpt = myResourceTableDao.findById(id1.getIdPartAsLong());
			assertTrue(tableOpt.isPresent());
			assertEquals(EntityIndexStatusEnum.INDEXED_RDBMS_ONLY, tableOpt.get().getIndexStatus());
			assertThat(myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(id1.getIdPartAsLong()))).isLessThanOrEqualTo(0);
		});


	}

	@Test
	public void testCantSearchForDeletedResourceByLanguageOrTag() {
		String methodName = "testCantSearchForDeletedResourceByLanguageOrTag";
		Organization org = new Organization();
		org.setLanguageElement(new CodeType("EN_ca"));
		org.setName(methodName);

		ArrayList<Coding> tl = new ArrayList<Coding>();
		tl.add(new Coding().setSystem(methodName).setCode(methodName));
		org.getMeta().getTag().addAll(tl);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_tag", new TokenParam(methodName, methodName));
		assertEquals(1, myOrganizationDao.search(map).size().intValue());

		myOrganizationDao.delete(orgId, mySrd);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_tag", new TokenParam(methodName, methodName));
		assertEquals(0, myOrganizationDao.search(map).size().intValue());
	}

	@Test
	public void testChoiceParamConcept() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(newCodeableConcept("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_CONCEPT, new TokenParam("testChoiceParam01CCS", "testChoiceParam01CCV")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id1, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamDate() {
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParam02");
		o2.setValue(new Period().setStartElement(new DateTimeType("2001-01-01")).setEndElement(new DateTimeType("2001-01-03")));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_DATE, new DateParam("2001")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id2, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamDateAlt() {
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParamDateAlt02");
		o2.setEffective(new DateTimeType("2015-03-08T11:11:11"));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId();

		{
			List<JpaPid> found = myObservationDao.searchForIds(new SearchParameterMap(Observation.SP_DATE, new DateParam(">2001-01-02")), null);
			assertThat(JpaPid.toLongList(found)).contains(id2.getIdPartAsLong());
		}
		{
			List<JpaPid> found = myObservationDao.searchForIds(new SearchParameterMap(Observation.SP_DATE, new DateParam(">2016-01-02")), null);
			assertThat(JpaPid.toLongList(found)).doesNotContain(id2.getIdPartAsLong());
		}
	}

	@Test
	public void testChoiceParamDateEquals() {
		Encounter enc = new Encounter();
		enc.getPeriod().setStartElement(new DateTimeType("2016-05-10")).setEndElement(new DateTimeType("2016-05-20"));
		String id = myEncounterDao.create(enc, mySrd).getId().toUnqualifiedVersionless().getValue();

		List<String> ids;

		/*
		 * This should not match, per the definition of eq
		 */

		ids = toUnqualifiedVersionlessIdValues(myEncounterDao.search(new SearchParameterMap(Encounter.SP_DATE, new DateParam("2016-05-15")).setLoadSynchronous(true)));
		assertThat(ids).isEmpty();

		ids = toUnqualifiedVersionlessIdValues(myEncounterDao.search(new SearchParameterMap(Encounter.SP_DATE, new DateParam("eq2016-05-15")).setLoadSynchronous(true)));
		assertThat(ids).isEmpty();

		// Should match

		ids = toUnqualifiedVersionlessIdValues(myEncounterDao.search(new SearchParameterMap(Encounter.SP_DATE, new DateParam("eq2016")).setLoadSynchronous(true)));
		assertThat(ids).containsExactly(id);

		ids = toUnqualifiedVersionlessIdValues(myEncounterDao.search(new SearchParameterMap(Encounter.SP_DATE, new DateParam("2016")).setLoadSynchronous(true)));
		assertThat(ids).containsExactly(id);

	}

	@Test
	public void testChoiceParamDateRange() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParamDateRange01");
		o1.setEffective(new Period().setStartElement(new DateTimeType("2015-01-01T00:00:00Z")).setEndElement(new DateTimeType("2015-01-10T00:00:00Z")));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParamDateRange02");
		o2.setEffective(new Period().setStartElement(new DateTimeType("2015-01-05T00:00:00Z")).setEndElement(new DateTimeType("2015-01-15T00:00:00Z")));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_DATE, new DateParam("ge2015-01-02T00:00:00Z")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id1, id2);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_DATE, new DateParam("gt2015-01-02T00:00:00Z")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id1, id2);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_DATE, new DateParam("gt2015-01-10T00:00:00Z")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id2);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_DATE, new DateParam("sa2015-01-02T00:00:00Z")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id2);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_DATE, new DateParam("eb2015-01-13T00:00:00Z")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id1);
		}
	}

	@Test
	public void testChoiceParamQuantity() {
		Observation o3 = new Observation();
		o3.getCode().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new Quantity(QuantityComparator.GREATER_THAN, 123.0, "foo", "bar", "bar"));
		IIdType id3 = myObservationDao.create(o3, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam(">100", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("gt100", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("<100", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("lt100", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0001", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("~120", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ap120", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq123", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq120", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne120", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne123", "foo", "bar")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
	}

	@Test
	public void testChoiceParamQuantityWithNormalizedQuantitySearchSupported() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation o3 = new Observation();
		o3.getCode().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new Quantity(QuantityComparator.GREATER_THAN, 123.0, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm", "cm")); // 0.0123m
		IIdType id3 = myObservationDao.create(o3, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam(">100", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("gt100", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("<100", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("lt100", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("~120", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ap120", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq123", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq120", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne120", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne123", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			assertEquals(0, found.size().intValue());
		}

	}

	@Test
	public void testChoiceParamQuantityPrecision() {
		Observation o3 = new Observation();
		o3.getCode().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new Quantity(null, 123.01, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm", "cm")); // 0.012301 m
		IIdType id3 = myObservationDao.create(o3, mySrd).getId().toUnqualifiedVersionless();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.01", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id3);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.010", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id3);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.02", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
	}

	@Test
	public void testChoiceParamQuantityPrecisionWithNormalizedQuantitySearchSupported() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation o3 = new Observation();
		o3.getCode().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new Quantity(null, 123.01, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm", "cm")); // 0.012301 m
		IIdType id3 = myObservationDao.create(o3, mySrd).getId().toUnqualifiedVersionless();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.01", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id3);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.010", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).containsExactlyInAnyOrder(id3);
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.02", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}
		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.001", UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm")).setLoadSynchronous(true));
			List<IIdType> list = toUnqualifiedVersionlessIds(found);
			assertThat(list).isEmpty();
		}

	}

	@Test
	public void testChoiceParamString() {

		Observation o4 = new Observation();
		o4.getCode().addCoding().setSystem("foo").setCode("testChoiceParam04");
		o4.setValue(new StringType("testChoiceParam04Str"));
		IIdType id4 = myObservationDao.create(o4, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_STRING, new StringParam("testChoiceParam04Str")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id4, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testCodeSystemCreateAndDelete() {
		CodeSystem cs = new CodeSystem();
		cs.setStatus(PublicationStatus.DRAFT);
		IIdType id = myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();

		myCodeSystemDao.delete(id, mySrd);

		assertGone(id.toUnqualifiedVersionless());
	}

	@Test
	public void testConflictingUpdates() throws ExecutionException, InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		try {

			Patient p = new Patient();
			p.setActive(true);
			IIdType id = myPatientDao.create(p).getId();

			List<Future<String>> futures = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				Patient updatePatient = new Patient();
				updatePatient.setId(id.toUnqualifiedVersionless());
				updatePatient.addIdentifier().setSystem("" + i);
				updatePatient.setActive(true);

				int finalI = i;
				Future<String> future = pool.submit(() -> {
					ourLog.info("Starting update {}", finalI);
					try {
						try {
							myPatientDao.update(updatePatient);
						} catch (ResourceVersionConflictException e) {
							assertEquals(Msg.code(550) + Msg.code(823) + "The operation has failed with a version constraint failure. This generally means that two clients/threads were trying to update the same resource at the same time, and this request was chosen as the failing request.", e.getMessage());
						}
					} catch (Exception e) {
						ourLog.error("Failure", e);
						return e.toString();
					}
					ourLog.info("Finished update {}", finalI);
					return null;
				});
				futures.add(future);
			}

			for (Future<String> next : futures) {
				String nextError = next.get();
				if (StringUtils.isNotBlank(nextError)) {
					fail(nextError);
				}
			}


		} finally {
			pool.shutdown();
		}
	}

	@Test
	@Disabled
	public void testCreateBuiltInProfiles() throws Exception {
		org.hl7.fhir.r4.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = ClasspathUtil.loadResource("/org/hl7/fhir/r4/model/profile/" + name + ".xml");

		bundle = myFhirContext.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();

			ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(next));
			if (next instanceof StructureDefinition) {
				myStructureDefinitionDao.update((StructureDefinition) next, mySrd);
			} else if (next instanceof CompartmentDefinition) {
				myCompartmentDefinitionDao.update((CompartmentDefinition) next, mySrd);
			} else if (next instanceof OperationDefinition) {
				myOperationDefinitionDao.update((OperationDefinition) next, mySrd);
			} else {
				fail(next.getClass().getName());
			}

		}

	}

	@Test
	public void testCreateBundleAllowsDocumentAndCollection() {
		String methodName = "testCreateBundleAllowsDocumentAndCollection";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p, mySrd).getId();
		p.setId(pid);
		ourLog.info("Created patient, got it: {}", pid);

		Bundle bundle = new Bundle();
		bundle.setType(null);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		try {
			myBundleDao.create(bundle, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(522) + "Unable to store a Bundle resource on this server with a Bundle.type value of: (missing). Note that if you are trying to perform a FHIR transaction or batch operation you should POST the Bundle resource to the Base URL of the server, not to the /Bundle endpoint.", e.getMessage());
		}

		bundle = new Bundle();
		bundle.setType(BundleType.SEARCHSET);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		try {
			myBundleDao.create(bundle, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(522) + "Unable to store a Bundle resource on this server with a Bundle.type value of: searchset. Note that if you are trying to perform a FHIR transaction or batch operation you should POST the Bundle resource to the Base URL of the server, not to the /Bundle endpoint.", e.getMessage());
		}

		bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		myBundleDao.create(bundle, mySrd);

		bundle = new Bundle();
		bundle.setType(BundleType.DOCUMENT);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		myBundleDao.create(bundle, mySrd);

	}

	@Test
	public void testCreateDifferentTypesWithSameForcedId() {
		String idName = "forcedId";

		Patient pat = new Patient();
		pat.setId(idName);
		pat.addName().setFamily("FAM");
		IIdType patId = myPatientDao.update(pat, mySrd).getId();
		assertEquals("Patient/" + idName, patId.toUnqualifiedVersionless().getValue());

		Observation obs = new Observation();
		obs.setId(idName);
		obs.getCode().addCoding().setSystem("foo").setCode("testCreateDifferentTypesWithSameForcedId");
		IIdType obsId = myObservationDao.update(obs, mySrd).getId();
		assertEquals("Observation/" + idName, obsId.toUnqualifiedVersionless().getValue());

		pat = myPatientDao.read(patId.toUnqualifiedVersionless(), mySrd);
		obs = myObservationDao.read(obsId.toUnqualifiedVersionless(), mySrd);
	}

	@Test
	public void testCreateDuplicateTagsDoesNotCauseDuplicates() {
		Patient p = new Patient();
		p.setActive(true);

		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");
		p.getMeta().addTag().setSystem("FOO").setCode("BAR");

		myPatientDao.create(p);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				assertThat(myResourceTagDao.findAll()).hasSize(1);
				assertThat(myTagDefinitionDao.findAll()).hasSize(1);
			}
		});

	}

	@Test
	public void testCreateEmptyTagsIsIgnored() {
		Patient p = new Patient();
		p.setActive(true);

		// Add an empty tag
		p.getMeta().addTag();

		// Add another empty tag
		p.getMeta().addTag().setSystem("");
		p.getMeta().addTag().setCode("");
		p.getMeta().addTag().setDisplay("");

		myPatientDao.create(p);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				assertThat(myResourceTagDao.findAll()).isEmpty();
				assertThat(myTagDefinitionDao.findAll()).isEmpty();
			}
		});

	}

	@Test
	public void testCreateLongString() {
		String input = "<NamingSystem>\n" +
			"        <name value=\"NDF-RT (National Drug File – Reference Terminology)\"/>\n" +
			"        <status value=\"draft\"/>\n" +
			"        <kind value=\"codesystem\"/>\n" +
			"        <publisher value=\"HL7, Inc\"/>\n" +
			"        <date value=\"2015-08-21\"/>\n" +
			"        <uniqueId>\n" +
			"          <type value=\"uri\"/>\n" +
			"          <value value=\"http://hl7.org/fhir/ndfrt\"/>\n" +
			"          <preferred value=\"true\"/>\n" +
			"        </uniqueId>\n" +
			"        <uniqueId>\n" +
			"          <type value=\"oid\"/>\n" +
			"          <value value=\"2.16.840.1.113883.6.209\"/>\n" +
			"          <preferred value=\"false\"/>\n" +
			"        </uniqueId>\n" +
			"      </NamingSystem>";

		NamingSystem res = myFhirContext.newXmlParser().parseResource(NamingSystem.class, input);
		IIdType id = myNamingSystemDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap(NamingSystem.SP_NAME, new StringParam("NDF")).setLoadSynchronous(true);
		IBundleProvider result = myNamingSystemDao.search(params);
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly(id.getValue());
	}

	@Test
	public void testCreateNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/9999999999999");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("clients may only assign IDs which contain at least one non-numeric");
		}
	}

	@Test
	public void testCreateOperationOutcome() {
		/*
		 * If any of this ever fails, it means that one of the OperationOutcome issue severity codes has changed code value across versions. We store the string as a constant, so something will need to
		 * be fixed.
		 */
		assertEquals(org.hl7.fhir.dstu2.model.OperationOutcome.IssueSeverity.ERROR.toCode(), BaseStorageDao.OO_SEVERITY_ERROR);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.ERROR.toCode(), BaseStorageDao.OO_SEVERITY_ERROR);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.ERROR.toCode(), BaseStorageDao.OO_SEVERITY_ERROR);
		assertEquals(org.hl7.fhir.dstu2.model.OperationOutcome.IssueSeverity.INFORMATION.toCode(), BaseStorageDao.OO_SEVERITY_INFO);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.INFORMATION.toCode(), BaseStorageDao.OO_SEVERITY_INFO);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.INFORMATION.toCode(), BaseStorageDao.OO_SEVERITY_INFO);
		assertEquals(org.hl7.fhir.dstu2.model.OperationOutcome.IssueSeverity.WARNING.toCode(), BaseStorageDao.OO_SEVERITY_WARN);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.WARNING.toCode(), BaseStorageDao.OO_SEVERITY_WARN);
		assertEquals(org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity.WARNING.toCode(), BaseStorageDao.OO_SEVERITY_WARN);
	}

	@Test
	public void testCreateOperationOutcomeError() {
		JpaResourceDao<Bundle> dao = new JpaResourceDao<Bundle>();
		dao.setContext(myFhirContext);
		OperationOutcome oo = (OperationOutcome) dao.createErrorOperationOutcome("my message", "incomplete");
		assertEquals(IssueSeverity.ERROR.toCode(), oo.getIssue().get(0).getSeverity().toCode());
		assertEquals("my message", oo.getIssue().get(0).getDiagnostics());
		assertEquals(IssueType.INCOMPLETE, oo.getIssue().get(0).getCode());
	}

	@Test
	public void testCreateOperationOutcomeInfo() {
		JpaResourceDao<Bundle> dao = new JpaResourceDao<Bundle>();
		dao.setContext(myFhirContext);
		OperationOutcome oo = (OperationOutcome) dao.createInfoOperationOutcome("my message");
		assertEquals(IssueSeverity.INFORMATION.toCode(), oo.getIssue().get(0).getSeverity().toCode());
		assertEquals("my message", oo.getIssue().get(0).getDiagnostics());
		assertEquals(IssueType.INFORMATIONAL, oo.getIssue().get(0).getCode());
	}

	@Test
	public void testCreateReferenceToDeletedResource() {
		Organization org = new Organization();
		org.setActive(true);
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		myOrganizationDao.delete(orgId);

		Patient p = new Patient();
		p.getManagingOrganization().setReferenceElement(orgId);
		try {
			myPatientDao.create(p);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1096) + "Resource Organization/" + orgId.getIdPart() + " is deleted, specified in path: Patient.managingOrganization", e.getMessage());
		}
	}

	@Test
	public void testCreateThenUpdateInSameTransaction() {
		Patient initialPatient = new Patient();
		IIdType id = myPatientDao.create(initialPatient).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.setActive(true);
			p.addName().setFamily("FAMILY");
			IIdType update1Id = myPatientDao.update(p).getId();
			assertEquals("2", update1Id.getVersionIdPart());

			p = new Patient();
			p.setId(id.getIdPart());
			p.setActive(false);
			p.addName().setFamily("FAMILY2");
			IIdType update2Id = myPatientDao.update(p).getId();
			assertEquals("3", update2Id.getVersionIdPart());
			assertEquals(update1Id.getIdPart(), update2Id.getIdPart());
		});

		runInTransaction(()->{
			List<ResourceTable> resourceTables = myResourceTableDao.findAll();
			assertEquals(1, resourceTables.size());
			assertEquals(3, resourceTables.get(0).getVersion());

			List<ResourceHistoryTable> versions = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(resourceTables.get(0).getResourceId().toFk());
			assertThat(versions.stream().map(ResourceHistoryTable::getVersion).toList()).asList().containsExactly(1L, 2L, 3L);
		});

		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous("name", new StringParam("family2"))).size());
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous("active", new TokenParam("false"))).size());

	}

	@Test
	public void testCreateSummaryFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateTextIdFails");
		p.addName().setFamily("Hello");

		p.getMeta().addTag().setSystem(Constants.TAG_SUBSETTED_SYSTEM_DSTU3).setCode(Constants.TAG_SUBSETTED_CODE);

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("subsetted");
		}
	}

	@Test
	public void testCreateTextIdDoesntFail() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateTextIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/ABC");
		String id = myPatientDao.create(p, mySrd).getId().getIdPart();
		assertThat(id).isNotEqualTo("ABC");
	}

	@Test
	public void testCreateWithIfNoneExistBasic() {
		String methodName = "testCreateWithIfNoneExistBasic";
		MethodOutcome results;

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		results = myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);
		assertEquals(id.getIdPart(), results.getId().getIdPart());
		assertFalse(results.getCreated());

		// Now create a second one

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		results = myPatientDao.create(p, mySrd);
		assertThat(results.getId().getIdPart()).isNotEqualTo(id.getIdPart());
		assertTrue(results.getCreated().booleanValue());

		// Now try to create one with the original match URL and it should fail

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		try {
			myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("Failed to CREATE");
		}

	}

	@Test
	public void testCreateWithIfNoneExistId() {
		String methodName = "testCreateWithIfNoneExistId";
		MethodOutcome results;

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualified();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		results = myPatientDao.create(p, "Patient?_id=" + id.toVersionless().getValue(), mySrd);
		assertEquals(id.getIdPart(), results.getId().getIdPart());
		assertEquals(id.getVersionIdPart(), results.getId().getVersionIdPart());
		assertFalse(results.getCreated().booleanValue());

	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCreateWithIllegalReference_InvalidTypeForElement(boolean theClientAssignedId) {

		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		IIdType id1;
		if (theClientAssignedId) {
			o1.setId("testCreateWithIllegalReference");
			id1 = myObservationDao.update(o1, mySrd).getId().toUnqualifiedVersionless();
		} else {
			id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();
		}

		try {
			Patient p = new Patient();
			p.getManagingOrganization().setReference(id1.getValue());
			myPatientDao.create(p, mySrd);
			fail("");
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(931) + "Invalid reference found at path 'Patient.managingOrganization'. Resource type 'Observation' is not valid for this path", e.getMessage());
		}

	}

	@Test
	public void testCreateWithInvalid() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(newCodeableConcept("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(new SearchParameterMap(Observation.SP_VALUE_CONCEPT, new TokenParam("testChoiceParam01CCS", "testChoiceParam01CCV")).setLoadSynchronous(true));
			assertEquals(1, found.size().intValue());
			assertEquals(id1, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().setFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new Reference("Organization/99999999"));
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("99999 not found");
		}

	}

	@Test
	public void testCreateWithInvalidReferenceNoId() {
		Patient p = new Patient();
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Organization/");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Does not contain resource ID");
		}
	}

	@Test
	public void testCreateWithReferenceBadType() {
		Patient p = new Patient();
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Blah/123");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("Invalid reference found at path 'Patient.managingOrganization'. Resource type 'Blah' is not valid for this path");
		}
	}

	@Test
	public void testCreateWithReferenceNoType() {
		Patient p = new Patient();
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("123");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Does not contain resource type");
		}
	}

	@Test
	public void testCreateWrongType() {

		// Lose typing so we can put the wrong type in
		@SuppressWarnings("rawtypes")
		IFhirResourceDao dao = myNamingSystemDao;

		Patient resource = new Patient();
		resource.addName().setFamily("My Name");
		try {
			dao.create(resource, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(520) + "Incorrect resource type detected for endpoint, found Patient but expected NamingSystem", e.getMessage());
		}
	}


	@Test
	public void testDeleteFailsIfIncomingLinks() {
		String methodName = "testDeleteFailsIfIncomingLinks";
		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		patient.getManagingOrganization().setReferenceElement(orgId);
		IIdType patId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		List<IIdType> found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found).containsExactly(orgId, patId);

		try {
			myOrganizationDao.delete(orgId, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertConflictException("Patient", e);
			ourLog.info("Expected exception thrown: " + e.getMessage());
		}

		myPatientDao.delete(patId, mySrd);

		map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found).containsExactly(orgId);

		myOrganizationDao.delete(orgId, mySrd);

		map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testDeleteResource() {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		int initialHistory = myPatientDao.history(null, null, null, mySrd).size();

		IIdType id1;
		IIdType id2;
		IIdType id2b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_testDeleteResource").addGiven("Joe");
			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testDeleteResource").addGiven("John");
			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = myPatientDao.read(id2, mySrd);
			patient.addIdentifier().setSystem("ZZZZZZZ").setValue("ZZZZZZZZZ");
			id2b = myPatientDao.update(patient, mySrd).getId();
		}
		ourLog.info("ID1:{}   ID2:{}   ID2b:{}", id1, id2, id2b);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testDeleteResource"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertThat(patients).hasSize(2);

		myPatientDao.delete(id1, mySrd);

		patients = toList(myPatientDao.search(params));
		assertThat(patients).hasSize(1);

		myPatientDao.read(id1, mySrd);
		try {
			myPatientDao.read(id1.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(4 + initialHistory, history.size().intValue());
		List<IBaseResource> resources = history.getResources(0, 4);
		assertTrue(resources.get(0).isDeleted());

		try {
			myPatientDao.delete(id2, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		myPatientDao.delete(id2.toVersionless(), mySrd);

		patients = toList(myPatientDao.search(params));
		assertThat(patients).isEmpty();

	}

	/**
     * See #773
     */
	@Test
	public void testDeleteResourceWithOutboundDeletedResources() {
		myStorageSettings.setEnforceReferentialIntegrityOnDelete(false);

		Organization org = new Organization();
		org.setId("ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pat = new Patient();
		pat.setId("PAT");
		pat.setActive(true);
		pat.setManagingOrganization(new Reference("Organization/ORG"));
		myPatientDao.update(pat);

		myOrganizationDao.delete(new IdType("Organization/ORG"));

		myPatientDao.delete(new IdType("Patient/PAT"));
	}

	@Test
	public void testDeleteThenUndelete() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		IIdType id = myPatientDao.create(patient, mySrd).getId();
		assertThat(id.getValue()).endsWith("/_history/1");

		// should be ok
		myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);

		// Delete it
		myPatientDao.delete(id.toUnqualifiedVersionless(), mySrd);

		try {
			myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// expected
		}

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		patient.setId(id.toUnqualifiedVersionless());
		IIdType id2 = myPatientDao.update(patient, mySrd).getId();

		assertThat(id2.getValue()).endsWith("/_history/3");

		IIdType gotId = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getIdElement();
		assertEquals(id2, gotId);
	}

	@Test
	public void testDeleteTwicePerformsNoOp() {
		Patient patient = new Patient();
		patient.setActive(true);

		IIdType id = myPatientDao.create(patient, mySrd).getId();
		assertNotNull(id.getIdPartAsLong());
		assertEquals("1", id.getVersionIdPart());

		IIdType id2 = myPatientDao.delete(id.toUnqualifiedVersionless()).getId();
		assertEquals(id.getIdPart(), id2.getIdPart());
		assertEquals("2", id2.getVersionIdPart());

		IIdType id3 = myPatientDao.delete(id.toUnqualifiedVersionless()).getId();
		assertEquals(id.getIdPart(), id3.getIdPart());
		assertEquals("2", id3.getVersionIdPart());

		IIdType id4 = myPatientDao.delete(id.toUnqualifiedVersionless()).getId();
		assertEquals(id.getIdPart(), id4.getIdPart());
		assertEquals("2", id4.getVersionIdPart());

		patient = new Patient();
		patient.setId(id.getIdPart());
		patient.setActive(false);
		IIdType id5 = myPatientDao.update(patient).getId();
		assertEquals(id.getIdPart(), id5.getIdPart());
		assertEquals("3", id5.getVersionIdPart());

		patient = myPatientDao.read(id.withVersion("1"));
		assertTrue(patient.getActive());

		try {
			myPatientDao.read(id.withVersion("2"));
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		patient = myPatientDao.read(id.withVersion("3"));
		assertFalse(patient.getActive());

	}

	@Test
	public void testDeleteWithHas() {
		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));
		myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		try {
			myObservationDao.deleteByUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER", mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertConflictException("DiagnosticReport", e);
		}

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);
	}

	@Test
	public void testDeleteWithMatchUrl() {
		String methodName = "testDeleteWithMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		myPatientDao.deleteByUrl("Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, null, mySrd);
		assertEquals(2, history.size().intValue());

		assertTrue(history.getResources(0, 1).get(0).isDeleted());
		assertFalse(history.getResources(1, 2).get(0).isDeleted());
	}

	@Test
	public void testDeleteWithMatchUrlChainedIdentifier() {
		String methodName = "testDeleteWithMatchUrlChainedIdentifer";

		Organization org = new Organization();
		org.setName(methodName);
		org.addIdentifier().setSystem("http://example.com").setValue(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertNotGone(orgId);

		myOrganizationDao.deleteByUrl("Organization?identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertGone(orgId);

	}

	@Test
	public void testDeleteWithMatchUrlChainedProfile() {
		String methodName = "testDeleteWithMatchUrlChainedProfile";

		Organization org = new Organization();

		org.getMeta().getProfile().add(new CanonicalType("http://foo"));
		org.setName(methodName);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._profile=http://foo", mySrd);
		assertGone(id);

		myOrganizationDao.deleteByUrl("Organization?_profile=http://foo", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1242) + "Invalid parameter chain: organization._profile.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(487) + "Invalid parameter chain: _profile.identifier", e.getMessage());
		}

	}

	@Test
	public void testDeleteWithMatchUrlChainedString() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.name=" + methodName, mySrd);

		assertGone(id);
	}

	@Test
	public void testDeleteWithMatchUrlChainedTag() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		Organization org = new Organization();
		org.getMeta().addTag().setSystem("http://foo").setCode("term");

		org.setName(methodName);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._tag=http://foo|term", mySrd);
		assertGone(id);

		myOrganizationDao.deleteByUrl("Organization?_tag=http://foo|term", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1242) + "Invalid parameter chain: organization._tag.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(487) + "Invalid parameter chain: _tag.identifier", e.getMessage());
		}

	}

	@Test
	public void testDeleteByTagWrongType() {
		Organization org = new Organization();
		org.getMeta().addTag().setCode("term");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		myPatientDao.deleteByUrl("Patient?_tag=term", mySrd);
		// The organization is still there
		myOrganizationDao.read(orgId);
	}

	@Test
	public void testSearchByTagWrongType() {
		Organization org = new Organization();
		org.getMeta().addTag().setCode("term");
		myOrganizationDao.create(org, mySrd);
		SearchParameterMap map = new SearchParameterMap();
		map.add("_tag", new UriParam("term"));
		map.setLoadSynchronous(true);
		IBundleProvider result = myPatientDao.search(map);
		List<IBaseResource> resources = result.getResources(0, 1);
		assertThat(resources).isEmpty();
	}

	@Test
	public void testDeleteWithMatchUrlQualifierMissing() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		String methodName = "testDeleteWithMatchUrlChainedProfile";

		/*
		 * Org 2 has no name
		 */

		Organization org1 = new Organization();
		org1.addIdentifier().setValue(methodName);
		IIdType org1Id = myOrganizationDao.create(org1, mySrd).getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		p1.getManagingOrganization().setReferenceElement(org1Id);
		IIdType patId1 = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		/*
		 * Org 2 has a name
		 */

		Organization org2 = new Organization();
		org2.setName(methodName);
		org2.addIdentifier().setValue(methodName);
		IIdType org2Id = myOrganizationDao.create(org2, mySrd).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.getManagingOrganization().setReferenceElement(org2Id);
		IIdType patId2 = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Pat ID 1 : {}", patId1);
		ourLog.info("Org ID 1 : {}", org1Id);
		ourLog.info("Pat ID 2 : {}", patId2);
		ourLog.info("Org ID 2 : {}", org2Id);

		myPatientDao.deleteByUrl("Patient?organization.name:missing=true", mySrd);
		assertGone(patId1);
		assertNotGone(patId2);
		assertNotGone(org1Id);
		assertNotGone(org2Id);

		myOrganizationDao.deleteByUrl("Organization?name:missing=true", mySrd);
		assertGone(patId1);
		assertNotGone(patId2);
		assertGone(org1Id);
		assertNotGone(org2Id);

		myPatientDao.deleteByUrl("Patient?organization.name:missing=false", mySrd);
		assertGone(patId1);
		assertGone(patId2);
		assertGone(org1Id);
		assertNotGone(org2Id);

		myOrganizationDao.deleteByUrl("Organization?name:missing=false", mySrd);
		assertGone(patId1);
		assertGone(patId2);
		assertGone(org1Id);
		assertGone(org2Id);
	}

	@Test
	public void testHistoryByForcedId() {
		IIdType idv1;
		IIdType idv2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("testHistoryByForcedId");
			patient.addName().setFamily("Tester").addGiven("testHistoryByForcedId");
			patient.setId("Patient/testHistoryByForcedId");
			idv1 = myPatientDao.update(patient, mySrd).getId();

			patient.addName().setFamily("Tester").addGiven("testHistoryByForcedIdName2");
			patient.setId(patient.getIdElement().toUnqualifiedVersionless());
			idv2 = myPatientDao.update(patient, mySrd).getId();
		}

		List<Patient> patients = toList(myPatientDao.history(idv1.toVersionless(), null, null, null, mySrd));
		assertThat(patients).hasSize(2);
		// Newest first
		assertEquals("Patient/testHistoryByForcedId/_history/2", patients.get(0).getIdElement().toUnqualified().getValue());
		assertEquals("Patient/testHistoryByForcedId/_history/1", patients.get(1).getIdElement().toUnqualified().getValue());
		assertThat(idv2).isNotEqualTo(idv1);
	}

	@Test
	public void testHistoryOverMultiplePages() throws Exception {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		String methodName = "testHistoryOverMultiplePages";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Date middleDate = null;
		int halfSize = 50;
		int fullSize = 100;
		for (int i = 0; i < fullSize; i++) {
			ourLog.info("Pass {}", i);
			if (i == halfSize) {
				sleepAtLeast(fullSize);
				middleDate = new Date();
				sleepAtLeast(fullSize);
			} else {
				sleepAtLeast(10);
			}
			patient.setId(id.getValue());
			patient.getName().get(0).getFamilyElement().setValue(methodName + "_i" + i);
			myPatientDao.update(patient, mySrd);
		}

		sleepAtLeast(10);

		// By instance
		IBundleProvider history = myPatientDao.history(id, null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			int finalFullSize = fullSize;
			int finalI = i;
			IBundleProvider finalHistory = history;
			assertEquals(expected, actual, ()->{
				String retVal = "i=" + finalI + ", fullSize=" + finalFullSize;
				List<IBaseResource> allResources = finalHistory.getResources(0, finalI + 1);
				for (int idx = 0; idx < allResources.size(); idx++) {
					retVal += "\n" + idx + " - " + allResources.get(idx).getIdElement().getValue();
				}
				return retVal;
			});
		}

		// By type
		history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		/*
		 * With since date
		 */

		// By instance
		history = myPatientDao.history(id, middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By type
		history = myPatientDao.history(middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		/*
		 * Now delete the most recent version and make sure everything still works
		 */

		myPatientDao.delete(id.toVersionless(), mySrd);

		fullSize++;
		halfSize++;

		// By instance
		history = myPatientDao.history(id, null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By type
		history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(null, null, null, mySrd);
		assertEquals(fullSize + 1, history.size().intValue());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		/*
		 * With since date
		 */

		// By instance
		history = myPatientDao.history(id, middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By type
		history = myPatientDao.history(middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(middleDate, null, null, mySrd);
		assertEquals(halfSize, history.size().intValue());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

	}

	@Test
	public void testHistoryReflectsMetaOperations() {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		Patient inPatient = new Patient();
		inPatient.addName().setFamily("version1");
		inPatient.getMeta().addProfile("http://example.com/1");
		IIdType id = myPatientDao.create(inPatient, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(1, history.size().intValue());
		Patient outPatient = (Patient) history.getResources(0, 1).get(0);
		assertEquals("version1", inPatient.getName().get(0).getFamily());
		List<String> profiles = toStringList(outPatient.getMeta().getProfile());
		assertThat(profiles).containsExactly("http://example.com/1");

		/*
		 * Change metadata
		 */

		inPatient.getMeta().addProfile("http://example.com/2");
		myPatientDao.metaAddOperation(id, inPatient.getMeta(), mySrd);

		history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(1, history.size().intValue());
		outPatient = (Patient) history.getResources(0, 1).get(0);
		assertEquals("version1", inPatient.getName().get(0).getFamily());
		profiles = toStringList(outPatient.getMeta().getProfile());
		assertThat(profiles).containsExactlyInAnyOrder("http://example.com/1", "http://example.com/2");

		/*
		 * Do an update
		 */

		inPatient.setId(id);
		inPatient.getMeta().addProfile("http://example.com/3");
		inPatient.getName().get(0).setFamily("version2");
		myPatientDao.update(inPatient, mySrd);

		history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(2, history.size().intValue());
		outPatient = (Patient) history.getResources(0, 2).get(0);
		assertEquals("version2", outPatient.getName().get(0).getFamily());
		profiles = toStringList(outPatient.getMeta().getProfile());
		ourLog.info(profiles.toString());
		assertThat(profiles).containsExactlyInAnyOrder("http://example.com/1", "http://example.com/2", "http://example.com/3");

		outPatient = (Patient) history.getResources(0, 2).get(1);
		assertEquals("version1", outPatient.getName().get(0).getFamily());
		profiles = toStringList(outPatient.getMeta().getProfile());
		assertThat(profiles).containsExactlyInAnyOrder("http://example.com/1", "http://example.com/2");
	}

	@Test
	public void testHistoryWithDeletedResource() {
		String methodName = "testHistoryWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id, mySrd);
		patient.setId(id);
		myPatientDao.update(patient, mySrd);

		IBundleProvider history = myPatientDao.history(id, null, null, null, mySrd);
		List<IBaseResource> entries = history.getResources(0, 3);
		ourLog.info(entries.get(0).getIdElement() + " - " + entries.get(0).getMeta().getLastUpdated());
		ourLog.info(entries.get(1).getIdElement() + " - " + entries.get(1).getMeta().getLastUpdated());
		ourLog.info(entries.get(2).getIdElement() + " - " + entries.get(2).getMeta().getLastUpdated());
		assertEquals(3, history.size().intValue());

		assertEquals(id.withVersion("3"), entries.get(0).getIdElement());
		assertEquals(id.withVersion("2"), entries.get(1).getIdElement());
		assertEquals(id.withVersion("1"), entries.get(2).getIdElement());

		assertFalse(entries.get(0).isDeleted());
		assertEquals(BundleEntryTransactionMethodEnum.PUT, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(entries.get(0)));

		assertTrue(entries.get(1).isDeleted());
		assertEquals(BundleEntryTransactionMethodEnum.DELETE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(entries.get(1)));

		assertFalse(entries.get(2).isDeleted());
		assertEquals(BundleEntryTransactionMethodEnum.POST, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(entries.get(2)));
	}

	@Test
	public void testHistoryWithFromAndTo() throws Exception {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		String methodName = "testHistoryWithFromAndTo";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);

		List<Date> preDates = Lists.newArrayList();
		List<String> ids = Lists.newArrayList();
		IIdType idCreated = myPatientDao.create(patient, mySrd).getId();
		ids.add(idCreated.toUnqualified().getValue());
		IIdType id = idCreated.toUnqualifiedVersionless();

		for (int i = 0; i < 10; i++) {
			Thread.sleep(100);
			preDates.add(new Date());
			Thread.sleep(100);
			patient.setId(id.getValue());
			patient.getName().get(0).getFamilyElement().setValue(methodName + "_i" + i);
			ids.add(myPatientDao.update(patient, mySrd).getId().toUnqualified().getValue());
		}

		List<String> idValues;

		idValues = toUnqualifiedIdValues(myPatientDao.history(id, preDates.get(0), preDates.get(3), null, mySrd));
		assertThat(idValues).containsExactly(ids.get(3), ids.get(2), ids.get(1));

		idValues = toUnqualifiedIdValues(myPatientDao.history(preDates.get(0), preDates.get(3), null, mySrd));
		assertThat(idValues).containsExactly(ids.get(3), ids.get(2), ids.get(1));

		idValues = toUnqualifiedIdValues(mySystemDao.history(preDates.get(0), preDates.get(3), null, mySrd));
		assertThat(idValues).containsExactly(ids.get(3), ids.get(2), ids.get(1));
	}

	@Test
	public void testHistoryWithFutureSinceDate() throws Exception {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		Date before = new Date();
		Thread.sleep(10);

		Patient inPatient = new Patient();
		inPatient.addName().setFamily("version1");
		inPatient.getMeta().addProfile("http://example.com/1");
		myPatientDao.create(inPatient, mySrd).getId().toUnqualifiedVersionless();

		Thread.sleep(10);
		Date after = new Date();

		// No since

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);
		assertEquals(1, history.size().intValue());
		Patient outPatient = (Patient) history.getResources(0, 1).get(0);
		assertEquals("version1", inPatient.getName().get(0).getFamily());
		List<String> profiles = toStringList(outPatient.getMeta().getProfile());
		assertThat(profiles).containsExactly("http://example.com/1");

		// Before since

		history = myPatientDao.history(before, null, null, mySrd);
		assertEquals(1, history.size().intValue());
		outPatient = (Patient) history.getResources(0, 1).get(0);
		assertEquals("version1", inPatient.getName().get(0).getFamily());
		profiles = toStringList(outPatient.getMeta().getProfile());
		assertThat(profiles).containsExactly("http://example.com/1");

		// After since

		history = myPatientDao.history(after, null, null, mySrd);
		assertEquals(0, history.size().intValue());

	}

	@Test
	public void testHistoryWithInvalidId() {
		try {
			myPatientDao.history(new IdType("Patient/FOOFOOFOO"), null, null, null, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/FOOFOOFOO is not known", e.getMessage());
		}
	}

	@Test
	public void testIdParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Date now = new Date();

		{
			Patient retrieved = myPatientDao.read(outcome.getId(), mySrd);
			Date published = retrieved.getMeta().getLastUpdated();
			assertTrue(published.before(now));
		}

		/*
		 * This ID points to a patient, so we should not be able to return othe types with it
		 */
		try {
			myEncounterDao.read(outcome.getId(), mySrd);
			fail();
		} catch (InvalidRequestException e) {
			// expected
		}
		try {
			myEncounterDao.read(new IdType(outcome.getId().getIdPart()), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// expected
		}

		// Now search by _id
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertThat(ret).hasSize(1);
			Patient p = ret.get(0);
			assertEquals("Tester", p.getName().get(0).getFamily());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertThat(ret).hasSize(1);
			Patient p = ret.get(0);
			assertEquals("Tester", p.getName().get(0).getFamily());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertThat(ret).hasSize(1);
			Patient p = ret.get(0);
			assertEquals("Tester", p.getName().get(0).getFamily());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam("000"));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertThat(ret).isEmpty();
		}
	}

	@Test
	public void testIndexConditionWithAllOnsetTypes() {
		// DateTimeType.class, Age.class, Period.class, Range.class, StringType.class

		Condition c0 = new Condition();
		c0.setOnset(new DateTimeType("2011-01-01"));
		myConditionDao.create(c0, mySrd).getId().toUnqualifiedVersionless();

		Condition c1 = new Condition();
		c1.setOnset(new Age().setValue(100L).setCode("AGECODE"));
		myConditionDao.create(c1, mySrd).getId().toUnqualifiedVersionless();

		Condition c2 = new Condition();
		c2.setOnset(new Period().setStart(new Date()).setEnd(new Date()));
		myConditionDao.create(c2, mySrd).getId().toUnqualifiedVersionless();

		Condition c3 = new Condition();
		c3.setOnset(new Range().setLow(new SimpleQuantity().setValue(200L)).setHigh(new SimpleQuantity().setValue(300L)));
		myConditionDao.create(c3, mySrd).getId().toUnqualifiedVersionless();

		Condition c4 = new Condition();
		c4.setOnset(new StringType("FOO"));
		myConditionDao.create(c4, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testInstanceMetaOperations() {
		String methodName = "testMetaRead";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.getMeta().addTag("tag_scheme1", "tag_code1", "tag_display1");
			patient.getMeta().addTag("tag_scheme2", "tag_code2", "tag_display2");

			patient.getMeta().addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1").setDisplay("seclabel_dis1");
			patient.getMeta().addSecurity().setSystem("seclabel_sys2").setCode("seclabel_code2").setDisplay("seclabel_dis2");

			patient.getMeta().addProfile(("http://profile/1"));
			patient.getMeta().addProfile(("http://profile/2"));

			id = myPatientDao.create(patient, mySrd).getId();
		}

		assertTrue(id.hasVersionIdPart());

		/*
		 * Create a second version
		 */

		Patient pt = myPatientDao.read(id, mySrd);
		pt.addName().setFamily("anotherName");
		myPatientDao.update(pt, mySrd);

		/*
		 * Meta-Delete on previous version
		 */

		Meta meta = new Meta();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		Meta newMeta = myPatientDao.metaDeleteOperation(id.withVersion("1"), meta, mySrd);
		assertThat(newMeta.getProfile()).hasSize(1);
		assertThat(newMeta.getSecurity()).hasSize(1);
		assertThat(newMeta.getTag()).hasSize(1);
		assertEquals("tag_code2", newMeta.getTag().get(0).getCode());
		assertEquals("http://profile/2", newMeta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", newMeta.getSecurity().get(0).getCode());

		/*
		 * Meta Read on Version
		 */

		meta = myPatientDao.metaGetOperation(Meta.class, id.withVersion("1"), mySrd);
		assertThat(meta.getProfile()).hasSize(1);
		assertThat(meta.getSecurity()).hasSize(1);
		assertThat(meta.getTag()).hasSize(1);
		assertEquals("tag_code2", meta.getTag().get(0).getCode());
		assertEquals("http://profile/2", meta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", meta.getSecurity().get(0).getCode());

		/*
		 * Meta-read on Version 2
		 */
		meta = myPatientDao.metaGetOperation(Meta.class, id.withVersion("2"), mySrd);
		assertThat(meta.getProfile()).hasSize(2);
		assertThat(meta.getSecurity()).hasSize(2);
		assertThat(meta.getTag()).hasSize(2);

		/*
		 * Meta-read on latest version
		 */
		meta = myPatientDao.metaGetOperation(Meta.class, id.toVersionless(), mySrd);
		assertThat(meta.getProfile()).hasSize(2);
		assertThat(meta.getSecurity()).hasSize(2);
		assertThat(meta.getTag()).hasSize(2);
		assertEquals("2", meta.getVersionId());

		/*
		 * Meta-Add on previous version
		 */

		meta = new Meta();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaAddOperation(id.withVersion("1"), meta, mySrd);
		assertThat(newMeta.getProfile()).hasSize(2);
		assertThat(newMeta.getSecurity()).hasSize(2);
		assertThat(newMeta.getTag()).hasSize(2);

		/*
		 * Meta Read on Version
		 */

		meta = myPatientDao.metaGetOperation(Meta.class, id.withVersion("1"), mySrd);
		assertThat(meta.getProfile()).hasSize(2);
		assertThat(meta.getSecurity()).hasSize(2);
		assertThat(meta.getTag()).hasSize(2);
		assertEquals("1", meta.getVersionId());

		/*
		 * Meta delete on latest
		 */

		meta = new Meta();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaDeleteOperation(id.toVersionless(), meta, mySrd);
		assertThat(newMeta.getProfile()).hasSize(1);
		assertThat(newMeta.getSecurity()).hasSize(1);
		assertThat(newMeta.getTag()).hasSize(1);
		assertEquals("tag_code2", newMeta.getTag().get(0).getCode());
		assertEquals("http://profile/2", newMeta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", newMeta.getSecurity().get(0).getCode());

		/*
		 * Meta-Add on latest version
		 */

		meta = new Meta();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaAddOperation(id.toVersionless(), meta, mySrd);
		assertThat(newMeta.getProfile()).hasSize(2);
		assertThat(newMeta.getSecurity()).hasSize(2);
		assertThat(newMeta.getTag()).hasSize(2);
		assertEquals("2", newMeta.getVersionId());

	}

	/**
     * See #196
     */
	@Test
	public void testInvalidChainNames() {
		ReferenceParam param = null;

		// OK
		param = new ReferenceParam("999999999999");
		param.setChain("organization");
		myLocationDao.search(new SearchParameterMap("partof", param).setLoadSynchronous(true));

		// OK
		param = new ReferenceParam("999999999999");
		param.setChain("organization.name");
		myLocationDao.search(new SearchParameterMap("partof", param).setLoadSynchronous(true));

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("foo");
			myLocationDao.search(new SearchParameterMap("partof", param).setLoadSynchronous(true));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid parameter chain: partof." + param.getChain());
		}

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("organization.foo");
			myLocationDao.search(new SearchParameterMap("partof", param).setLoadSynchronous(true));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid parameter chain: " + param.getChain());
		}

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("organization.name.foo");
			myLocationDao.search(new SearchParameterMap("partof", param).setLoadSynchronous(true));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Invalid parameter chain: " + param.getChain());
		}
	}

	/**
     * See #534
     */
	@Test
	public void testLogicalReferencesAreSearchable() {
		myStorageSettings.setTreatReferencesAsLogical(null);
		myStorageSettings.addTreatReferencesAsLogical("http://foo.com/identifier*");

		Patient p1 = new Patient();
		p1.getManagingOrganization().setReference("http://foo.com/identifier/1");
		String p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless().getValue();

		Patient p2 = new Patient();
		p2.getManagingOrganization().setReference("http://foo.com/identifier/2");
		String p2id = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless().getValue();

		IBundleProvider found = myPatientDao.search(new SearchParameterMap(Patient.SP_ORGANIZATION, new ReferenceParam("http://foo.com/identifier/1")).setLoadSynchronous(true));
		assertThat(toUnqualifiedVersionlessIdValues(found)).containsExactly(p1id);
		assertThat(toUnqualifiedVersionlessIdValues(found)).doesNotContain(p2id);
	}

	@Test
	public void testOrganizationName() {

		//@formatter:off
		String inputStr =
			"{" +
				"  \"resourceType\":\"Organization\",\n" +
				"  \"extension\":[\n" +
				"     {\n" +
				"       \"url\":\"http://fhir.connectinggta.ca/Profile/organization#providerIdPool\",\n" +
				"       \"valueUri\":\"urn:oid:2.16.840.1.113883.3.239.23.21.1\"\n" +
				"     }\n" +
				"  ],\n" +
				"  \"text\":{\n" +
				"     \"status\":\"empty\",\n" +
				"     \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/organization</div>\"\n" +
				"  },\n" +
				"  \"identifier\":[\n" +
				"     {\n" +
				"       \"use\":\"official\",\n" +
				"       \"system\":\"urn:cgta:hsp_ids\",\n" +
				"       \"value\":\"urn:oid:2.16.840.1.113883.3.239.23.21\"\n" +
				"     }\n" +
				"  ],\n" +
				"  \"name\":\"Peterborough Regional Health Centre\"\n" +
				"}\n";
		//@formatter:on

		List<IResourcePersistentId> val = myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam("P")), null);
		int initial = val.size();

		Organization org = myFhirContext.newJsonParser().parseResource(Organization.class, inputStr);
		myOrganizationDao.create(org, mySrd);

		val = myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam("P")), null);
		assertThat(val).hasSize(initial + 1);

	}

	@Test
	public void testDeleteWithMassInjectionModeEnabled() {
		myStorageSettings.setMassIngestionMode(true);

		// given
		Observation observation = new Observation()
			.setStatus(ObservationStatus.FINAL)
			.addCategory(newCodeableConcept("http://somesystem", "somecode"))
			.setCode(newCodeableConcept("http://loinc.org", "15074-8"));

		// when
		IIdType idDt = myObservationDao.create(observation, mySrd).getEntity().getIdDt();

		myObservationDao.delete(idDt, mySrd);

		// then
		runInTransaction(() -> {
			JpaPid observationId = JpaPid.fromId(idDt.getIdPartAsLong());
			Long resourceCurrentVersion = myResourceTableDao.findCurrentVersionByPid(observationId);
			int resourceVersionCount = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(observationId.toFk()).size();
			int indexedTokenCount = myResourceIndexedSearchParamTokenDao.countForResourceId(observationId);

			assertEquals(2L, resourceCurrentVersion);
			assertEquals(2, resourceVersionCount);
			assertEquals(0, indexedTokenCount);

		});
	}

	@Test
	public void testPersistContactPoint() {
		List<IAnyResource> found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_TELECOM, new TokenParam(null, "555-123-4567")).setLoadSynchronous(true)));
		int initialSize2000 = found.size();

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistContactPoint");
		patient.addTelecom().setValue("555-123-4567");
		myPatientDao.create(patient, mySrd);

		found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_TELECOM, new TokenParam(null, "555-123-4567")).setLoadSynchronous(true)));
		assertThat(found).hasSize(1 + initialSize2000);

	}

	@Test
	public void testPersistResourceLink() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink01");
		IIdType patientId01 = myPatientDao.create(patient, mySrd).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink02");
		IIdType patientId02 = myPatientDao.create(patient02, mySrd).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, mySrd).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, mySrd).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new Reference(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, mySrd).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", patientId01, patientId02, obsId01, obsId02, drId01);

		List<Observation> result = toList(myObservationDao.search(new SearchParameterMap(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getIdPart())).setLoadSynchronous(true)));
		assertThat(result).hasSize(1);
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(new SearchParameterMap(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getIdPart())).setLoadSynchronous(true)));
		assertThat(result).hasSize(1);
		assertEquals(obsId02.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(new SearchParameterMap(Observation.SP_SUBJECT, new ReferenceParam("999999999999")).setLoadSynchronous(true)));
		assertThat(result).isEmpty();

	}

	@Test
	public void testPersistSearchParamDate() {
		List<Patient> found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_BIRTHDATE, new DateParam(ParamPrefixEnum.GREATERTHAN, "2000-01-01")).setLoadSynchronous(true)));
		int initialSize2000 = found.size();

		found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_BIRTHDATE, new DateParam(ParamPrefixEnum.GREATERTHAN, "2002-01-01")).setLoadSynchronous(true)));
		int initialSize2002 = found.size();

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.setBirthDateElement(new DateType("2001-01-01"));

		myPatientDao.create(patient, mySrd);

		found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_BIRTHDATE, new DateParam(ParamPrefixEnum.GREATERTHAN, "2000-01-01")).setLoadSynchronous(true)));
		assertThat(found).hasSize(1 + initialSize2000);

		found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_BIRTHDATE, new DateParam(ParamPrefixEnum.GREATERTHAN, "2002-01-01")).setLoadSynchronous(true)));
		assertThat(found).hasSize(initialSize2002);

		// If this throws an exception, that would be an acceptable outcome as well..
		try {
			found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_BIRTHDATE + "AAAA", new DateParam(ParamPrefixEnum.GREATERTHAN, "2000-01-01")).setLoadSynchronous(true)));
			assertThat(found).isEmpty();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1223) + "Unknown search parameter \"birthdateAAAA\" for resource type \"Patient\". Valid search parameters for this search are: [_id, _lastUpdated, _profile, _security, _source, _tag, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}
	}

	@Test
	public void testPersistSearchParamObservationString() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new StringType("AAAABBBB"));

		myObservationDao.create(obs, mySrd);

		List<Observation> found = toList(myObservationDao.search(new SearchParameterMap("value-string", new StringParam("AAAABBBB")).setLoadSynchronous(true)));
		assertThat(found).hasSize(1);

		found = toList(myObservationDao.search(new SearchParameterMap("value-string", new StringParam("AAAABBBBCCC")).setLoadSynchronous(true)));
		assertThat(found).isEmpty();

	}

	@Test
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new Quantity(111));

		myObservationDao.create(obs, mySrd);

		List<Observation> found = toList(myObservationDao.search(new SearchParameterMap("value-quantity", new QuantityParam(111)).setLoadSynchronous(true)));
		assertThat(found).hasSize(1);

		found = toList(myObservationDao.search(new SearchParameterMap("value-quantity", new QuantityParam(112)).setLoadSynchronous(true)));
		assertThat(found).isEmpty();

		found = toList(myObservationDao.search(new SearchParameterMap("value-quantity", new QuantityParam(212)).setLoadSynchronous(true)));
		assertThat(found).isEmpty();

	}

	@Test
	public void testPersistSearchParams() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001testPersistSearchParams");
		patient.getGenderElement().setValue(AdministrativeGender.MALE);
		patient.addName().setFamily("Tester").addGiven("JoetestPersistSearchParams");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		long id = outcome.getId().getIdPartAsLong();

		TokenParam value = new TokenParam("urn:system", "001testPersistSearchParams");
		List<Patient> found = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_IDENTIFIER, value).setLoadSynchronous(true)));
		assertThat(found).hasSize(1);
		assertEquals(id, found.get(0).getIdElement().getIdPartAsLong().longValue());

		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "M"));
		// assertEquals(1, found.size());
		// assertEquals(id, found.get(0).getId().asLong().longValue());
		//
		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "F"));
		// assertEquals(0, found.size());

		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new TokenParam("urn:some:wrong:system", AdministrativeGender.MALE.toCode()));
		found = toList(myPatientDao.search(map));
		assertThat(found).isEmpty();

		// Now with no system on the gender (should match)
		map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new TokenParam(null, AdministrativeGender.MALE.toCode()));
		found = toList(myPatientDao.search(map));
		assertThat(found).hasSize(1);
		assertEquals(id, found.get(0).getIdElement().getIdPartAsLong().longValue());

		// Now with the wrong gender
		map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new TokenParam(AdministrativeGender.MALE.getSystem(), AdministrativeGender.FEMALE.toCode()));
		found = toList(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testQuestionnaireTitleGetsIndexed() {
		Questionnaire q = new Questionnaire();
		q.setTitle("testQuestionnaireTitleGetsIndexedQ_TITLE");
		IIdType qid1 = myQuestionnaireDao.create(q, mySrd).getId().toUnqualifiedVersionless();
		q = new Questionnaire();
		q.setTitle("testQuestionnaireTitleGetsIndexedQ_NOTITLE");
		IIdType qid2 = myQuestionnaireDao.create(q, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider results = myQuestionnaireDao.search(new SearchParameterMap("title", new StringParam("testQuestionnaireTitleGetsIndexedQ_TITLE")).setLoadSynchronous(true));
		assertEquals(1, results.sizeOrThrowNpe());
		assertEquals(qid1, results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		assertThat(results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless()).isNotEqualTo(qid2);

	}

	@Test
	public void testRead() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testRead");
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		/*
		 * READ
		 */

		Observation obs = myObservationDao.read(id1.toUnqualifiedVersionless(), mySrd);
		assertEquals(o1.getCode().getCoding().get(0).getCode(), obs.getCode().getCoding().get(0).getCode());

		/*
		 * VREAD
		 */
		assertTrue(id1.hasVersionIdPart()); // just to make sure..
		obs = myObservationDao.read(id1, mySrd);
		assertEquals(o1.getCode().getCoding().get(0).getCode(), obs.getCode().getCoding().get(0).getCode());

	}

	@Test
	public void testReadForcedIdVersionHistory() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testReadVorcedIdVersionHistory01");
		p1.setId("testReadVorcedIdVersionHistory");
		IIdType p1id = new IdType(myPatientDao.update(p1, mySrd).getId().getValue());
		assertEquals("testReadVorcedIdVersionHistory", p1id.getIdPart());

		p1.addIdentifier().setSystem("urn:system").setValue("testReadVorcedIdVersionHistory02");
		p1.setId(p1id);
		IIdType p1idv2 = myPatientDao.update(p1, mySrd).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1idv2.getIdPart());

		assertThat(p1idv2.getValue()).isNotEqualTo(p1id.getValue());

		Patient v1 = myPatientDao.read(p1id, mySrd);
		assertThat(v1.getIdentifier()).hasSize(1);

		Patient v2 = myPatientDao.read(p1idv2, mySrd);
		assertThat(v2.getIdentifier()).hasSize(2);

	}

	@Test
	public void testReadInvalidVersion() {
		String methodName = "testReadInvalidVersion";

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(pat, mySrd).getId();

		assertEquals(methodName, myPatientDao.read(id, mySrd).getIdentifier().get(0).getValue());

		try {
			myPatientDao.read(id.withVersion("0"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(979) + "Version \"0\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(id.withVersion("2"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(979) + "Version \"2\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(id.withVersion("H"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(978) + "Version \"H\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(new IdType("Patient/9999999999999/_history/1"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/9999999999999 is not known", e.getMessage());
		}

	}

	@Test
	public void testReadWithDeletedResource() {
		String methodName = "testReadWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().setFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toVersionless();
		myPatientDao.delete(id, mySrd);

		assertGone(id);

		patient.setId(id.getValue());
		patient.addAddress().addLine("AAA");
		myPatientDao.update(patient, mySrd);

		Patient p;

		p = myPatientDao.read(id, mySrd);
		assertThat((p).getName()).hasSize(1);

		p = myPatientDao.read(id.withVersion("1"), mySrd);
		assertThat((p).getName()).hasSize(1);

		try {
			myPatientDao.read(id.withVersion("2"), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		p = myPatientDao.read(id.withVersion("3"), mySrd);
		assertThat((p).getName()).hasSize(1);
	}

	@Test
	public void testRemoveTag() {

		String methodName = "testResourceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");
			patient.getMeta().addTag(null, "Dog", "Puppies");

			patient.getMeta().addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1");

			patient.getMeta().addProfile(("http://profile/1"));

			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag("http://foo", "Cat", "Kittens");
			patient.getMeta().addSecurity().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2");
			patient.getMeta().addProfile("http://profile/2");

			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue(methodName);
			device.getMeta().addTag("http://foo", "Foo", "Bars");
			device.getMeta().addSecurity().setSystem("seclabel:sys:3").setCode("seclabel:code:3").setDisplay("seclabel:dis:3");
			device.getMeta().addProfile("http://profile/3");
			myDeviceDao.create(device, mySrd);
		}

		Meta meta;

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		List<Coding> published = meta.getTag();
		assertThat(published).hasSize(2);
		assertNull(published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<Coding> secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<CanonicalType> profiles = meta.getProfile();
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog");
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1");
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1");

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	/**
	 * Can we handle content that was previously saved containing vocabulary that
	 * is no longer valid
	 */
	@Test
	public void testResourceInDatabaseContainsInvalidVocabulary() {
		final Patient p = new Patient();
		p.setGender(AdministrativeGender.MALE);
		final IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		TransactionTemplate tx = new TransactionTemplate(myTxManager);
		tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		tx.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				ResourceHistoryTable table = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(id.getIdPartAsLong(), null), 1L);
				String newContent = myFhirContext.newJsonParser().encodeResourceToString(p);
				newContent = newContent.replace("male", "foo");
				table.setResourceTextVc(newContent);
				table.setEncoding(ResourceEncodingEnum.JSON);
				myResourceHistoryTableDao.save(table);
			}
		});

		Patient read = myPatientDao.read(id);
		String string = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(read);
		ourLog.info(string);
		assertThat(string).contains("value=\"foo\"");
	}

	@Test
	public void testResourceInstanceMetaOperation() {

		String methodName = "testResourceInstanceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");
			id1 = myPatientDao.create(patient, mySrd).getId();

			Meta metaAdd = new Meta();
			metaAdd.addTag().setSystem(null).setCode("Dog").setDisplay("Puppies");
			metaAdd.addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1");
			metaAdd.addProfile("http://profile/1");
			myPatientDao.metaAddOperation(id1, metaAdd, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");
			patient.getMeta().addTag("http://foo", "Cat", "Kittens");

			patient.getMeta().addSecurity().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2");

			patient.getMeta().addProfile(("http://profile/2"));

			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue(methodName);
			device.getMeta().addTag("http://foo", "Foo", "Bars");

			device.getMeta().addSecurity().setSystem("seclabel:sys:3").setCode("seclabel:code:3").setDisplay("seclabel:dis:3");

			device.getMeta().addProfile("http://profile/3");

			myDeviceDao.create(device, mySrd);
		}

		Meta meta;

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		List<Coding> published = meta.getTag();
		assertThat(published).hasSize(2);
		assertNull(published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<Coding> secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<CanonicalType> profiles = meta.getProfile();
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

		{
			Meta metaDel = new Meta();
			metaDel.addTag().setSystem(null).setCode("Dog");
			metaDel.addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1");
			metaDel.addProfile("http://profile/1");
			myPatientDao.metaDeleteOperation(id1, metaDel, mySrd);
		}

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	@Test
	public void testResourceMetaOperation() {

		String methodName = "testResourceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");
			patient.getMeta().addTag(null, "Dog", "Puppies");

			patient.getMeta().addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1");

			patient.getMeta().addProfile(("http://profile/1"));

			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag("http://foo", "Cat", "Kittens");
			patient.getMeta().addSecurity().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2");
			patient.getMeta().addProfile("http://profile/2");

			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue(methodName);
			device.getMeta().addTag("http://foo", "Foo", "Bars");
			device.getMeta().addSecurity().setSystem("seclabel:sys:3").setCode("seclabel:code:3").setDisplay("seclabel:dis:3");
			device.getMeta().addProfile("http://profile/3");
			myDeviceDao.create(device, mySrd);
		}

		Meta meta;

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		List<Coding> published = meta.getTag();
		assertThat(published).hasSize(2);
		assertNull(published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<Coding> secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<CanonicalType> profiles = meta.getProfile();
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1", mySrd);

		meta = myPatientDao.metaGetOperation(Meta.class, mySrd);
		published = meta.getTag();
		assertThat(published).hasSize(1);
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertThat(secLabels).hasSize(1);
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertThat(profiles).hasSize(1);
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	@Test
	public void testReverseIncludes() {
		String methodName = "testReverseIncludes";
		Organization org = new Organization();
		org.setName("X" + methodName + "X");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId();

		Patient pat = new Patient();
		pat.addName().setFamily("X" + methodName + "X");
		pat.getManagingOrganization().setReferenceElement(orgId.toUnqualifiedVersionless());
		myPatientDao.create(pat, mySrd);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam("X" + methodName + "X"));
		map.setRevIncludes(Collections.singleton(Patient.INCLUDE_ORGANIZATION));
		IBundleProvider resultsP = myOrganizationDao.search(map);
		assertEquals(1, resultsP.size().intValue());

		List<IBaseResource> results = resultsP.getResources(0, resultsP.size());
		assertThat(results).hasSize(2);
		assertEquals(Organization.class, results.get(0).getClass());
		assertEquals(BundleEntrySearchModeEnum.MATCH, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(results.get(0)));
		assertEquals(Patient.class, results.get(1).getClass());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(results.get(1)));
	}

	@Test
	public void testSaveAndReturnCollectionBundle() throws IOException {
		String input = IOUtils.toString(FhirResourceDaoR4Test.class.getResourceAsStream("/r4/collection-bundle.json"));
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);

		myBundleDao.update(inputBundle);

		Bundle outputBundle = myBundleDao.read(new IdType("cftest"));
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		for (BundleEntryComponent next : outputBundle.getEntry()) {
			assertTrue(next.getResource().getIdElement().hasIdPart());
		}
	}

	@Test()
	public void testSortByComposite() {

		IIdType pid0;
		IIdType oid1;
		IIdType oid2;
		IIdType oid3;
		IIdType oid4;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new StringType("200"));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new StringType("300"));

			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new StringType("150"));

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			obs.getCode().addCoding().setCode("2345-7").setSystem("http://loinc.org");
			obs.setValue(new StringType("250"));

			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_CODE_VALUE_STRING));


		IBundleProvider found = myObservationDao.search(pm);

		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertThat(list).hasSize(4);
		assertEquals(oid3, list.get(0));
		assertEquals(oid1, list.get(1));
		assertEquals(oid4, list.get(2));
		assertEquals(oid2, list.get(3));
	}

	@Test
	public void testSortByDate() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().setFamily("testSortF1").addGiven("testSortG1");
		p.setBirthDateElement(new DateType("2001-01-01"));
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().setFamily("testSortF2").addGiven("testSortG2");
		p.setBirthDateElement(new DateType("2001-01-03"));
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().setFamily("testSortF3").addGiven("testSortG3");
		p.setBirthDateElement(new DateType("2001-01-02"));
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual;
		SearchParameterMap pm;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id3, id2, id1, id4);

	}

	@Test
	@Disabled
	public void testSortByEncounterLength() {
		String methodName = "testSortByNumber";

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("foo").setValue(methodName);
		e1.getLength().setSystem(SearchParamConstants.UCUM_NS).setCode("min").setValue(4.0 * 24 * 60);
		IIdType id1 = myEncounterDao.create(e1, mySrd).getId().toUnqualifiedVersionless();

		Encounter e3 = new Encounter();
		e3.addIdentifier().setSystem("foo").setValue(methodName);
		e3.getLength().setSystem(SearchParamConstants.UCUM_NS).setCode("year").setValue(3.0);
		IIdType id3 = myEncounterDao.create(e3, mySrd).getId().toUnqualifiedVersionless();

		Encounter e2 = new Encounter();
		e2.addIdentifier().setSystem("foo").setValue(methodName);
		e2.getLength().setSystem(SearchParamConstants.UCUM_NS).setCode("year").setValue(2.0);
		IIdType id2 = myEncounterDao.create(e2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<String> actual;

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Encounter.SP_LENGTH));
		actual = toUnqualifiedVersionlessIdValues(myEncounterDao.search(pm));
		assertThat(actual).containsExactly(toValues(id1, id2, id3));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Encounter.SP_LENGTH, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIdValues(myEncounterDao.search(pm));
		assertThat(actual).containsExactly(toValues(id3, id2, id1));
	}

	@Test
	public void testSortByInvalidParameter() {

		SearchParameterMap pm = SearchParameterMap.newSynchronous();
		pm.setSort(new SortSpec("hello", SortOrderEnum.DESC));
		try {
			myObservationDao.search(pm);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1194) + "Unknown _sort parameter value \"hello\" for resource type \"Observation\" (Note: sort parameters values must use a valid Search Parameter). Valid values for this search are: [_id, _lastUpdated, _profile, _security, _source, _tag, based-on, category, code, code-value-concept, code-value-date, code-value-quantity, code-value-string, combo-code, combo-code-value-concept, combo-code-value-quantity, combo-data-absent-reason, combo-value-concept, combo-value-quantity, component-code, component-code-value-concept, component-code-value-quantity, component-data-absent-reason, component-value-concept, component-value-quantity, data-absent-reason, date, derived-from, device, encounter, focus, has-member, identifier, method, part-of, patient, performer, specimen, status, subject, value-concept, value-date, value-quantity, value-string]", e.getMessage());
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSortByMissingAttribute(boolean theIndexMissingData) {
		myStorageSettings.setIndexMissingFields(theIndexMissingData ? JpaStorageSettings.IndexEnabledEnum.ENABLED : JpaStorageSettings.IndexEnabledEnum.DISABLED);

		Patient p = new Patient();
		p.setGender(AdministrativeGender.MALE);
		myPatientDao.create(p, mySrd);

		p = new Patient();
		p.setGender(AdministrativeGender.FEMALE);
		myPatientDao.create(p, mySrd);

		p = new Patient();
		myPatientDao.create(p, mySrd);

		SearchParameterMap spMap;
		List<IIdType> actual;

		spMap = SearchParameterMap.newSynchronous();
		spMap.setSort(new SortSpec(Patient.SP_GENDER));
		myCaptureQueriesListener.clear();
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(spMap));
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actual).hasSize(3);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSortByMissingAttributeWithChainedSorting(boolean theIndexMissingData) {
		myStorageSettings.setIndexMissingFields(theIndexMissingData ? JpaStorageSettings.IndexEnabledEnum.ENABLED : JpaStorageSettings.IndexEnabledEnum.DISABLED);

		Patient p = new Patient();
		p.addName().addGiven("MalePatientGivenName").setFamily("Bame");
		IIdType patient1Id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.setGender(AdministrativeGender.FEMALE);
		p.addName().addGiven("FemalePatientGivenName").setFamily("FemalePatientFamilyName");
		IIdType patient2Id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName().addGiven("MalePatientGivenName").setFamily("Aname");
		IIdType patient3Id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap spMap;
		List<IIdType> actual;

		spMap = SearchParameterMap.newSynchronous();
		spMap.setSort(new SortSpec(Patient.SP_GENDER).setChain(new SortSpec(Patient.SP_NAME)));

		myCaptureQueriesListener.clear();
		IBundleProvider searchResult = myPatientDao.search(spMap);
		actual = toUnqualifiedVersionlessIds(searchResult);
		myCaptureQueriesListener.logSelectQueries();

		// assert the sorting order
		assertThat(actual).contains(patient2Id, patient3Id, patient1Id);

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Test
	public void testSortByLastUpdated() {
		String methodName = "testSortByLastUpdated";

		Patient p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("urn:system1").setValue(methodName);
		p.addName().setFamily(methodName);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		TestUtil.sleepOneClick();

		p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("urn:system2").setValue(methodName);
		p.addName().setFamily(methodName);
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		TestUtil.sleepOneClick();

		p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("urn:system3").setValue(methodName);
		p.addName().setFamily(methodName);
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		TestUtil.sleepOneClick();

		p = new Patient();
		p.setActive(true);
		p.addIdentifier().setSystem("urn:system4").setValue(methodName);
		p.addName().setFamily(methodName);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		// With no search parameter
		pm = SearchParameterMap.newSynchronous();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		// With a search parameter
		pm = SearchParameterMap.newSynchronous();
		pm.add(Patient.SP_ACTIVE, new TokenParam("true"));
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = SearchParameterMap.newSynchronous();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = SearchParameterMap.newSynchronous();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).containsExactly(id4, id3, id2, id1);

		pm = SearchParameterMap.newSynchronous();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam(null, methodName));
		pm.setSort(new SortSpec(Patient.SP_NAME).setChain(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC)));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).containsExactly(id4, id3, id2, id1);
	}

	@Test
	public void testSortByNumber() {
		String methodName = "testSortByNumber";

		MolecularSequence e1 = new MolecularSequence();
		e1.addIdentifier().setSystem("foo").setValue(methodName);
		e1.addVariant().setStart(1);
		IIdType id1 = myMolecularSequenceDao.create(e1, mySrd).getId().toUnqualifiedVersionless();

		MolecularSequence e3 = new MolecularSequence();
		e3.addIdentifier().setSystem("foo").setValue(methodName);
		e3.addVariant().setStart(3);
		IIdType id3 = myMolecularSequenceDao.create(e3, mySrd).getId().toUnqualifiedVersionless();

		MolecularSequence e2 = new MolecularSequence();
		e2.addIdentifier().setSystem("foo").setValue(methodName);
		e2.addVariant().setStart(2);
		IIdType id2 = myMolecularSequenceDao.create(e2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<String> actual;

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(MolecularSequence.SP_VARIANT_START));
		actual = toUnqualifiedVersionlessIdValues(myMolecularSequenceDao.search(pm));
		assertThat(actual).containsExactly(toValues(id1, id2, id3));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(MolecularSequence.SP_VARIANT_START, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIdValues(myMolecularSequenceDao.search(pm));
		assertThat(actual).containsExactly(toValues(id3, id2, id1));
	}

	@Test
	public void testSortByQuantity() {
		Observation res;

		res = new Observation();
		res.setValue(new Quantity().setSystem("sys1").setCode("code1").setValue(2L));
		IIdType id2 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem("sys1").setCode("code1").setValue(1L));
		IIdType id1 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem("sys1").setCode("code1").setValue(3L));
		IIdType id3 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem("sys1").setCode("code1").setValue(4L));
		IIdType id4 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY));
		List<IIdType> actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id4, id3, id2, id1);

	}

	@Test
	@Disabled
	public void testSortByQuantityWithNormalizedQuantitySearchFullSupported() {

		Observation res;

		res = new Observation();
		res.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(2)); // 0.02m
		IIdType id2 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm").setValue(0.1)); // 0.01m
		IIdType id1 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m").setValue(0.03)); // 0.03m
		IIdType id3 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(4)); // 0.04m
		IIdType id4 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY));
		List<IIdType> actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id4, id3, id2, id1);
	}

	@Test
	public void testSortByReference() {
		String methodName = "testSortByReference";

		Organization o1 = new Organization();
		IIdType oid1 = myOrganizationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Organization o2 = new Organization();
		IIdType oid2 = myOrganizationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("testSortF1").addGiven("testSortG1");
		p.getManagingOrganization().setReferenceElement(oid1);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("testSortF2").addGiven("testSortG2");
		p.getManagingOrganization().setReferenceElement(oid2);
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("testSortF3").addGiven("testSortG3");
		p.getManagingOrganization().setReferenceElement(oid1);
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReferenceElement(oid2);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual.subList(0, 2)).containsExactlyInAnyOrder(id1, id3);
		assertThat(actual.subList(2, 4)).containsExactlyInAnyOrder(id2, id4);

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual.subList(0, 2)).containsExactlyInAnyOrder(id1, id3);
		assertThat(actual.subList(2, 4)).containsExactlyInAnyOrder(id2, id4);

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual.subList(0, 2)).containsExactlyInAnyOrder(id2, id4);
		assertThat(actual.subList(2, 4)).containsExactlyInAnyOrder(id1, id3);
	}

	@Test
	public void testSortByString01() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		myStorageSettings.setHibernateSearchIndexSearchParams(false);

		Patient p = new Patient();
		String string = "testSortByString01";
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("testSortF1").addGiven("testSortG1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("testSortF3").addGiven("testSortG3");
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("testSortF2").addGiven("testSortG2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		myCaptureQueriesListener.clear();
		pm = SearchParameterMap.newSynchronous();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);
		String sql = myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(countMatches(sql, "JOIN")).as(sql).isEqualTo(2);
		assertThat(countMatches(sql, "ORDER BY")).as(sql).isEqualTo(1);

		pm = SearchParameterMap.newSynchronous();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = SearchParameterMap.newSynchronous();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id3, id2, id1, id4);
	}

	/**
	 * See #198
	 */
	@Test
	public void testSortByString02() {
		Patient p;
		String string = "testSortByString02";

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("Fam1").addGiven("Giv1");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("Fam2").addGiven("Giv1");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("Fam2").addGiven("Giv2");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().setFamily("Fam1").addGiven("Giv2");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<String> names;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2)).containsExactlyInAnyOrder("Giv1 Fam1", "Giv2 Fam1");
		assertThat(names.subList(2, 4)).containsExactlyInAnyOrder("Giv1 Fam2", "Giv2 Fam2");

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setChain(new SortSpec(Patient.SP_GIVEN)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2)).containsExactly("Giv1 Fam1", "Giv2 Fam1");
		assertThat(names.subList(2, 4)).containsExactly("Giv1 Fam2", "Giv2 Fam2");

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setChain(new SortSpec(Patient.SP_GIVEN, SortOrderEnum.DESC)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2)).containsExactly("Giv2 Fam1", "Giv1 Fam1");
		assertThat(names.subList(2, 4)).containsExactly("Giv2 Fam2", "Giv1 Fam2");

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.DESC).setChain(new SortSpec(Patient.SP_GIVEN, SortOrderEnum.DESC)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2)).containsExactly("Giv2 Fam2", "Giv1 Fam2");
		assertThat(names.subList(2, 4)).containsExactly("Giv2 Fam1", "Giv1 Fam1");
	}

	@Test
	public void testSortByToken() {
		String methodName = "testSortByToken";

		Patient p;

		p = new Patient();
		p.addIdentifier().setSystem("urn:system2").setValue(methodName + "1");
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system1").setValue(methodName + "2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system1").setValue(methodName + "1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system2").setValue(methodName + "2");
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		TokenOrListParam sp = new TokenOrListParam();
		sp.addOr(new TokenParam("urn:system1", methodName + "1"));
		sp.addOr(new TokenParam("urn:system1", methodName + "2"));
		sp.addOr(new TokenParam("urn:system2", methodName + "1"));
		sp.addOr(new TokenParam("urn:system2", methodName + "2"));
		pm.add(Patient.SP_IDENTIFIER, sp);
		pm.setSort(new SortSpec(Patient.SP_IDENTIFIER));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		sp = new TokenOrListParam();
		sp.addOr(new TokenParam("urn:system1", methodName + "1"));
		sp.addOr(new TokenParam("urn:system1", methodName + "2"));
		sp.addOr(new TokenParam("urn:system2", methodName + "1"));
		sp.addOr(new TokenParam("urn:system2", methodName + "2"));
		pm.add(Patient.SP_IDENTIFIER, sp);
		pm.setSort(new SortSpec(Patient.SP_IDENTIFIER, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id4, id3, id2, id1);

	}

	@Test
	public void testSortByUri() {
		ConceptMap res = new ConceptMap();
		res.setUrl("http://foo2");
		IIdType id2 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.setUrl("http://foo1");
		IIdType id1 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.setUrl("http://foo3");
		IIdType id3 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.setUrl("http://foo4");
		IIdType id4 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(ConceptMap.SP_URL));
		List<IIdType> actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id1, id2, id3, id4);

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(ConceptMap.SP_URL, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertThat(actual).hasSize(4);
		assertThat(actual).containsExactly(id4, id3, id2, id1);

	}

	@Test
	public void testSortNoMatches() {
		String methodName = "testSortNoMatches";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = SearchParameterMap.newSynchronous();
		map.add(IAnyResource.SP_RES_ID, new StringParam(id1.getIdPart()));
		map.setLastUpdated(new DateRangeParam("2001", "2003"));
		map.setSort(new SortSpec(Constants.PARAM_LASTUPDATED));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map))).isEmpty();

		map = SearchParameterMap.newSynchronous();
		map.add(IAnyResource.SP_RES_ID, new StringParam(id1.getIdPart()));
		map.setLastUpdated(new DateRangeParam("2001", "2003"));
		map.setSort(new SortSpec(Patient.SP_NAME));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map))).isEmpty();

	}

	@Test
	public void testStoreUnversionedResources() {
		Organization o1 = new Organization();
		o1.getNameElement().setValue("AAA");
		IIdType o1id = myOrganizationDao.create(o1, mySrd).getId();
		assertTrue(o1id.hasVersionIdPart());

		Patient p1 = new Patient();
		p1.addName().setFamily("AAAA");
		p1.getManagingOrganization().setReferenceElement(o1id);
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		p1 = myPatientDao.read(p1id, mySrd);

		assertFalse(p1.getManagingOrganization().getReferenceElement().hasVersionIdPart());
		assertEquals(o1id.toUnqualifiedVersionless(), p1.getManagingOrganization().getReferenceElement().toUnqualifiedVersionless());
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId();

		Organization returned = myOrganizationDao.read(orgId, mySrd);
		String val = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);

		ourLog.info(val);
		assertThat(val).contains("<name value=\"測試醫院\"/>");
	}

	@Test
	public void testStringParamWhichIsTooLong() {

		Organization org = new Organization();
		String str = "testStringParamLong__" + RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamString.MAX_LENGTH);
		str = str + str;
		org.getNameElement().setValue(str);

		assertThat(str.length()).isGreaterThan(ResourceIndexedSearchParamString.MAX_LENGTH);

		List<IResourcePersistentId> val = myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam("P")), null);
		int initial = val.size();

		myOrganizationDao.create(org, mySrd);

		val = myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam("P")), null);
		assertThat(val).hasSize(initial + 0);

		val = myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH))), null);
		assertThat(val).hasSize(initial + 1);

		try {
			myOrganizationDao.searchForIds(new SearchParameterMap("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH + 1))), null);
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}
	}

	@Test
	public void testTagsAndProfilesAndSecurityLabelsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testTagsWithCreateAndReadAndSearch");
		patient.addName().setFamily("Tester").addGiven("Joe");
		List<Coding> tagList = new ArrayList<>();
		tagList.add(new Coding().setSystem(null).setCode("Dog").setDisplay("Puppies"));
		// Add this twice
		tagList.add(new Coding().setSystem("http://foo").setCode("Cat").setDisplay("Kittens"));
		tagList.add(new Coding().setSystem("http://foo").setCode("Cat").setDisplay("Kittens"));
		patient.getMeta().getTag().addAll(tagList);

		List<Coding> securityLabels = new ArrayList<>();
		securityLabels.add(new Coding().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
		securityLabels.add(new Coding().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
		patient.getMeta().getSecurity().addAll(securityLabels);

		List<CanonicalType> profiles = new ArrayList<>();
		profiles.add(new CanonicalType("http://profile/1"));
		profiles.add(new CanonicalType("http://profile/2"));
		patient.getMeta().getProfile().addAll(profiles);

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		IIdType patientId = outcome.getId();
		assertNotNull(patientId);
		assertFalse(patientId.isEmpty());

		Patient retrieved = myPatientDao.read(patientId, mySrd);
		ArrayList<Coding> published = (ArrayList<Coding>) retrieved.getMeta().getTag();
		sort(published);
		assertThat(published).hasSize(2);
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertNull(published.get(0).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());

		List<Coding> secLabels = retrieved.getMeta().getSecurity();
		sortCodings(secLabels);
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		profiles = retrieved.getMeta().getProfile();
		profiles = sortIds(profiles);
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		List<Patient> search = toList(myPatientDao.search(new SearchParameterMap(Patient.SP_IDENTIFIER, new TokenParam(patient.getIdentifier().get(0).getSystem(), patient.getIdentifier().get(0).getValue())).setLoadSynchronous(true)));
		assertThat(search).hasSize(1);
		retrieved = search.get(0);

		published = (ArrayList<Coding>) retrieved.getMeta().getTag();
		sort(published);
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertNull(published.get(0).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());

		secLabels = retrieved.getMeta().getSecurity();
		sortCodings(secLabels);
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());

		profiles = retrieved.getMeta().getProfile();
		profiles = sortIds(profiles);
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		myPatientDao.metaAddOperation(patientId, new Meta().addTag("http://foo", "Cat", "Kittens"), null);
		myPatientDao.metaAddOperation(patientId, new Meta().addTag("http://foo", "Cow", "Calves"), null);

		retrieved = myPatientDao.read(patientId, mySrd);
		published = (ArrayList<Coding>) retrieved.getMeta().getTag();
		sort(published);
		assertThat(published).hasSize(3);
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertNull(published.get(0).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cow", published.get(2).getCode());
		assertEquals("Calves", published.get(2).getDisplay());
		assertEquals("http://foo", published.get(2).getSystem());

		secLabels = retrieved.getMeta().getSecurity();
		sortCodings(secLabels);
		assertThat(secLabels).hasSize(2);
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());

		profiles = retrieved.getMeta().getProfile();
		profiles = sortIds(profiles);
		assertThat(profiles).hasSize(2);
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

	}

	@Test
	public void testTimingSearchParams() {
		Date before = new DateTimeType("2011-01-01T10:00:00Z").getValue();
		Date middle = new DateTimeType("2011-01-02T10:00:00Z").getValue();
		Date after = new DateTimeType("2011-01-03T10:00:00Z").getValue();

		CarePlan cp = new CarePlan();
		cp.addActivity().getDetail().setScheduled(new Timing().addEvent(before).addEvent(middle).addEvent(after));
		cp.addActivity().getDetail();
		IIdType id = myCarePlanDao.create(cp, mySrd).getId().toUnqualifiedVersionless();

		CarePlan cp2 = new CarePlan();
		cp2.addActivity().getDetail().setScheduled(new StringType("FOO"));
		cp2.addActivity().getDetail();
		myCarePlanDao.create(cp2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(CarePlan.SP_ACTIVITY_DATE, new DateRangeParam("2010-01-01T10:00:00Z", null));
		assertThat(toUnqualifiedVersionlessIdValues(myCarePlanDao.search(params))).containsExactly(id.getValue());

		params = new SearchParameterMap();
		params.add(CarePlan.SP_ACTIVITY_DATE, new DateRangeParam("2011-01-01T10:00:00Z", null));
		assertThat(toUnqualifiedVersionlessIdValues(myCarePlanDao.search(params))).containsExactly(id.getValue());

		params = new SearchParameterMap();
		params.add(CarePlan.SP_ACTIVITY_DATE, new DateRangeParam("2012-01-01T10:00:00Z", null));
		assertThat(toUnqualifiedVersionlessIdValues(myCarePlanDao.search(params))).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("timingDateRangeSearchParams")
	public void testTimingDateRangeSearch_ordinalAndDatetimeSearchesMatch(List<Date> theEventDates, Period thePeriod, List<Date> theExpectedDates) {
		// Given
		ServiceRequest sr = new ServiceRequest();
		sr.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		sr.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);

		for (Date date : theEventDates) {
			Timing.TimingRepeatComponent repeat = new Timing.TimingRepeatComponent();
			repeat.setBounds(thePeriod);

			Timing timing = new Timing();
			timing.addEvent(date);
			timing.setRepeat(repeat);

			sr.setOccurrence(timing);
			myServiceRequestDao.create(sr, mySrd);
		}

		SearchParameterMap params;

		// When
		params = new SearchParameterMap();
		params.add(ServiceRequest.SP_OCCURRENCE, new DateRangeParam("2025-02-08T14:00:00Z", "2025-02-09T14:00:00Z"));
		IBundleProvider datetimeSearchResponse =  myServiceRequestDao.search(params);

		// Then
		assertThat(toUnqualifiedVersionlessIdValues(datetimeSearchResponse)).hasSize(theExpectedDates.size());
		List<Date> eventDatesFromDatetimeSearch = getEventDatesFromServiceRequestsInSearchResponse(datetimeSearchResponse);
		assertThat(eventDatesFromDatetimeSearch).containsExactlyElementsOf(theExpectedDates);

		// When
		params = new SearchParameterMap();
		params.add(ServiceRequest.SP_OCCURRENCE, new DateRangeParam("2025-02-08", "2025-02-09"));
		IBundleProvider ordinalDateSearchResponse = myServiceRequestDao.search(params);

		// Then
		assertThat(toUnqualifiedVersionlessIdValues(ordinalDateSearchResponse)).hasSize(theExpectedDates.size());
		List<Date> eventDatesFromOrdinalSearch = getEventDatesFromServiceRequestsInSearchResponse(ordinalDateSearchResponse);
		assertThat(eventDatesFromOrdinalSearch).containsExactlyElementsOf(theExpectedDates);
	}

	private static List<Date> getEventDatesFromServiceRequestsInSearchResponse(IBundleProvider theDateSearchResponse) {
		return theDateSearchResponse.getAllResources().stream()
			.filter(ServiceRequest.class::isInstance)
			.map(resource -> {
				ServiceRequest serviceRequest = (ServiceRequest) resource;
				return serviceRequest.getOccurrenceTiming().getEvent().stream().findFirst().orElse(null);
			}).filter(Objects::nonNull)
			.map(DateTimeType::getValue)
			.toList();
	}

	private static Stream<Arguments> timingDateRangeSearchParams() {
		Date feb6 = new DateTimeType("2025-02-06T14:00:00Z").setTimeZoneZulu(true).getValue();
		Date feb7 = new DateTimeType("2025-02-07T14:00:00Z").setTimeZoneZulu(true).getValue();
		Date feb8 = new DateTimeType("2025-02-08T14:00:00Z").setTimeZoneZulu(true).getValue();
		Date feb9 = new DateTimeType("2025-02-09T14:00:00Z").setTimeZoneZulu(true).getValue();
		Date feb10 = new DateTimeType("2025-02-10T14:00:00Z").setTimeZoneZulu(true).getValue();
		Date feb11 = new DateTimeType("2025-02-11T14:00:00Z").setTimeZoneZulu(true).getValue();

		Period periodNoStart = new Period();
		periodNoStart.setEnd(feb11);

		Period periodNoEnd = new Period();
		periodNoEnd.setStart(feb7);

		Period periodStartEnd = new Period();
		periodStartEnd.setStart(feb7);
		periodStartEnd.setEnd(feb10);

		return Stream.of(
			// Timing period with no start
			Arguments.of(List.of(feb7, feb8, feb9, feb10), periodNoStart, List.of(feb7, feb8, feb9)),
			// Timing period with no end
			Arguments.of(List.of(feb8, feb9, feb10, feb11), periodNoEnd, List.of(feb8, feb9, feb10, feb11)),
			// Timing period with start and end, event falls within date range
			Arguments.of(List.of(feb7, feb8, feb9, feb10), periodStartEnd, List.of(feb7, feb8, feb9, feb10)),
			// Timing period with start and end, event falls before date range
			Arguments.of(List.of(feb6, feb7, feb8, feb9, feb10), periodStartEnd, List.of(feb6, feb7, feb8, feb9, feb10)),
			// Timing period with start and end, event falls after date range
			Arguments.of(List.of(feb7, feb8, feb9, feb10, feb11), periodStartEnd, List.of(feb7, feb8, feb9, feb10, feb11))
		);
	}

	@Test
	public void testTokenParamWhichIsTooLong() {

		String longStr1 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		String longStr2 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);

		Organization org = new Organization();
		org.getNameElement().setValue("testTokenParamWhichIsTooLong");
		org.addType().addCoding().setSystem(longStr1).setCode(longStr2);

		String subStr1 = longStr1.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
		String subStr2 = longStr2.substring(0, ResourceIndexedSearchParamToken.MAX_LENGTH);
		List<IResourcePersistentId> val = myOrganizationDao.searchForIds(new SearchParameterMap("type", new TokenParam(subStr1, subStr2)), null);
		int initial = val.size();

		myOrganizationDao.create(org, mySrd);

		// search should have 0 result since it is hashed before truncation
		val = myOrganizationDao.searchForIds(new SearchParameterMap("type", new TokenParam(subStr1, subStr2)), null);
		assertThat(val).hasSize(initial);

		// search using the original string should success
		val = myOrganizationDao.searchForIds(new SearchParameterMap("type", new TokenParam(longStr1, longStr2)), null);
		assertThat(val).hasSize(initial + 1);
	}

	@Test
	public void testUpdateRejectsIdWhichPointsToForcedId() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsIdWhichPointsToForcedId01");
		p1.addName().setFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId01");
		p1.setId("ABABA");
		IIdType p1id = myPatientDao.update(p1, mySrd).getId();
		assertEquals("ABABA", p1id.getIdPart());

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsIdWhichPointsToForcedId02");
		p2.addName().setFamily("Tester").addGiven("testUpdateRejectsIdWhichPointsToForcedId02");
		IIdType p2id = myPatientDao.create(p2, mySrd).getId();
		long p1longId = p2id.getIdPartAsLong() - 1;

		try {
			myPatientDao.read(new IdType("Patient/" + p1longId), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		try {
			p1.setId(new IdType("Patient/" + p1longId));
			myPatientDao.update(p1, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("clients may only assign IDs which contain at least one non-numeric");
		}

	}

	/**
	 * Make sure this can upload successfully (indexer failed at one point)
	 */
	@Test
	public void testUploadConsentWithSourceAttachment() {
		Consent consent = new Consent();
		consent.setSource(new Attachment().setUrl("http://foo"));
		myConsentDao.create(consent);
	}

	/**
	 * Make sure this can upload successfully (indexer failed at one point)
	 */
	@Test
	public void testUploadExtensionStructureDefinition() {
		StructureDefinition ext = (StructureDefinition) myValidationSupport.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/familymemberhistory-type");
		Validate.notNull(ext);
		myStructureDefinitionDao.update(ext);
	}

	@Test
	public void testDontReuseErrorSearches() {
		SearchParameterMap map = new SearchParameterMap();
		map.add("subject", new ReferenceParam("Patient/123"));
		String normalized = map.toNormalizedQueryString(myFhirContext);
		String uuid = UUID.randomUUID().toString();

		runInTransaction(() -> {
			ca.uhn.fhir.jpa.entity.Search search = new ca.uhn.fhir.jpa.entity.Search();
			QueryParameterUtils.populateSearchEntity(map, "Encounter", uuid, normalized, search, RequestPartitionId.allPartitions());
			search.setStatus(SearchStatusEnum.FAILED);
			search.setFailureCode(500);
			search.setFailureMessage("FOO");
			mySearchCacheSvc.save(search, RequestPartitionId.defaultPartition());
		});

		IBundleProvider results = myEncounterDao.search(map);
		assertEquals(0, results.size().intValue());
		assertThat(results.getUuid()).isNotEqualTo(uuid);
	}

	@Test
	public void testSearchWithReverseChainingAndOffset() {
		int amountOfPatients = 5;
		{
			for (int i = 0; i < amountOfPatients; i++) {
				Patient p = new Patient();
				p.setActive(true);
				String pid = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless().getValue();
				for (int j = 0; j < 20; j++) {
					Observation o = new Observation();
					o.setSubject(new Reference().setReference(pid));
					o.setCode(new CodeableConcept().addCoding(new Coding().setCode("sample")));
					myObservationDao.create(o, mySrd);
				}
			}
		}
		SearchParameterMap spMap;
		List<IBaseResource> actual;

		spMap = SearchParameterMap.newSynchronous();
		spMap.add(PARAM_HAS, new HasParam("Observation", "patient", "code", "sample"));
		spMap.setOffset(0);
		spMap.setCount(5);

		IBundleProvider search = myPatientDao.search(spMap);
		actual = search.getResources(0, 100);
		assertThat(actual).hasSize(amountOfPatients);
	}

	@Test
	public void testSearchWithReverseChainingOffsetAndSort() {
		int amountOfPatients = 5;
		String[] namesNotInAlpha = {"Charlie", "Tim", "Adam", "Dan", "Bob"};
		String[] namesInAlpha = {"Adam", "Bob", "Charlie", "Dan", "Tim"};
		{
			for (int i = 0; i < amountOfPatients; i++) {
				Patient p = new Patient();
				p.setActive(true);
				p.addName(new HumanName().addGiven(namesNotInAlpha[i]));
				String pid = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless().getValue();
				for (int j = 0; j < 20; j++) {
					Observation o = new Observation();
					o.setSubject(new Reference().setReference(pid));
					o.setCode(new CodeableConcept().addCoding(new Coding().setCode("sample")));
					myObservationDao.create(o, mySrd);
				}
			}
		}
		SearchParameterMap spMap;
		List<IBaseResource> actual;

		spMap = SearchParameterMap.newSynchronous();
		spMap.add(PARAM_HAS, new HasParam("Observation", "patient", "code", "sample"));
		spMap.setOffset(0);
		spMap.setCount(5);
		spMap.setSort(new SortSpec(Patient.SP_GIVEN));

		IBundleProvider search = myPatientDao.search(spMap);
		actual = search.getResources(0, 100);
		List<String> actualNameList = actual.stream().map((resource) -> ((Patient) resource).getName().get(0).getGiven().get(0).toString()).toList();
		ourLog.info("Results: {}", actualNameList);

		assertThat(actual).hasSize(amountOfPatients);
		assertThat(actualNameList).containsExactly(namesInAlpha);
	}

	@Test
	void testSearchForStream_carriesTxContext() {
		// given
		Set<String> createdIds = IntStream.range(1, 5)
			.mapToObj(i -> createObservation().getIdPart())
			.collect(Collectors.toSet());

		SystemRequestDetails request = new SystemRequestDetails();

		// call within a tx, but carry the tx definition in the StreamTemplate
		StreamTemplate<IResourcePersistentId<?>> streamTemplate =
			StreamTemplate.fromSupplier(() -> myObservationDao.searchForIdStream(new SearchParameterMap(), request, null))
				.withTransactionAdvice(newTxTemplate());


		// does the stream work?
		Set<String> ids = streamTemplate.call(stream->
			stream.map(typedId->typedId.getId().toString())
				.collect(Collectors.toSet()));

		assertEquals(ids, createdIds);
	}

	@Test
	void testSearchForStream_withIncludeDeletedResources_resultIncludesDeletedResources() {
		// given
		IIdType deletedObservationId = createObservation();
		IIdType observationId = createObservation();

		SystemRequestDetails request = new SystemRequestDetails();

		deleteResource(deletedObservationId);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setSearchIncludeDeletedMode(SearchIncludeDeletedEnum.EXCLUSIVE);

		// call within a tx, but carry the tx definition in the StreamTemplate
		StreamTemplate<IResourcePersistentId<?>> streamTemplate =
			StreamTemplate.fromSupplier(() -> myObservationDao.searchForIdStream(searchParameterMap, request, null))
				.withTransactionAdvice(newTxTemplate());

		// does the stream work?
		Set<String> ids = streamTemplate.call(stream->
			stream.map(typedId->typedId.getId().toString())
				.collect(Collectors.toSet()));

		assertThat(ids).containsExactly(deletedObservationId.getIdPart());
	}

	@Test
	public void bundle1CreatesResourceByCondition_bundle2UpdatesExistingResourceToNotMatchConditionThenCreatesBySameCondition_shouldPass() {
		// setup
		IParser parser = myFhirContext.newJsonParser();
		String idToReplace = "/Task/11852"; // in bundle 2
		String identifierSystem = "https://tempuri.org";
		Bundle bundle1;
		Bundle bundle2;
		{
			@Language("JSON")
			String bundleStr = """
					{
					    "resourceType": "Bundle",
					    "type": "transaction",
					    "entry": [
					        {
					            "fullUrl": "urn:uuid:1fee7dea-c2a8-47b1-80a9-a681457cc44f",
					            "resource": {
					                "resourceType": "Task",
					                "identifier": [
					                    {
					                        "system": "https://tempuri.org",
					                        "value": "t1"
					                    }
					                ]
					            },
					            "request": {
					                "method": "POST",
					                "url": "/Task",
					                "ifNoneExist": "identifier=https://tempuri.org|t1"
					            }
					        }
					    ]
					}
				""";
			bundle1 = parser.parseResource(Bundle.class, bundleStr);
		}
		{
			@Language("JSON")
			String bundleStr = """
					{
					    "resourceType": "Bundle",
					    "type": "transaction",
					    "entry": [
					        {
					            "fullUrl": "urn:uuid:1fee7dea-c2a8-47b1-80a9-a681457cc44f",
					            "resource": {
					                "resourceType": "Task",
					                "identifier": [
					                    {
					                        "system": "https://tempuri.org",
					                        "value": "t1"
					                    }
					                ]
					            },
					            "request": {
					                "method": "POST",
					                "url": "/Task",
					                "ifNoneExist": "identifier=https://tempuri.org|t1"
					            }
					        },
					        {
					            "fullUrl": "http://localhost:8000/Task/11852",
					            "resource": {
					                "resourceType": "Task",
					                "identifier": [
					                    {
					                        "system": "https://tempuri.org",
					                        "value": "t2"
					                    }
					                ]
					            },
					            "request": {
					                "method": "PUT",
					                "url": "/Task/11852"
					            }
					        }
					    ]
					}
				""";
			bundle2 = parser.parseResource(Bundle.class, bundleStr);
		}

		IFhirSystemDao<Bundle, ?> systemDao = myDaoRegistry.getSystemDao();
		RequestDetails reqDets = new SystemRequestDetails();

		Bundle createdBundle;
		String id;
		{
			// create bundle1
			createdBundle = systemDao.transaction(reqDets, bundle1);
			assertNotNull(createdBundle);
			assertFalse(createdBundle.getEntry().isEmpty());
			Optional<BundleEntryComponent> entry = createdBundle.getEntry()
				.stream().filter(e -> e.getResponse() != null && e.getResponse().getStatus().contains("201 Created"))
				.findFirst();
			assertTrue(entry.isPresent());
			String idAndVersion = entry.get().getResponse().getLocation();

			if (idAndVersion.contains("/_history")) {
				IIdType idt = new IdType(idAndVersion);
				id = idt.toVersionless().getValue();
			} else {
				id = idAndVersion;
			}

			// verify task creation
			Task task = getTaskForId(id, identifierSystem);
			assertTrue(task.getIdentifier().stream().anyMatch(i -> i.getSystem().equals(identifierSystem) && i.getValue().equals("t1")));
		}

		// update second bundle to use already-saved-Task's id
		Optional<BundleEntryComponent> entryComponent = bundle2.getEntry().stream()
			.filter(e -> e.getRequest().getUrl().equals(idToReplace))
			.findFirst();
		assertTrue(entryComponent.isPresent());
		BundleEntryComponent entry = entryComponent.get();
		entry.getRequest().setUrl(id);
		entry.setFullUrl(entry.getFullUrl().replace(idToReplace, id));

		{
			// post second bundle first time
			createdBundle = systemDao.transaction(reqDets, bundle2);
			assertNotNull(createdBundle);
			// check that the Task is recognized, but not recreated
			assertFalse(createdBundle.getEntry().stream().anyMatch(e -> e.getResponse() != null && e.getResponse().getStatus().contains("201")));
			// but the Task should have been updated
			// (changing it to not match the identifier anymore)
			assertTrue(createdBundle.getEntry().stream().anyMatch(e -> e.getResponse() != null && e.getResponse().getLocation().equals(id + "/_history/2")));

			// verify task update
			Task task = getTaskForId(id, identifierSystem);
			assertTrue(task.getIdentifier().stream().anyMatch(i -> i.getSystem().equals(identifierSystem) && i.getValue().equals("t2")));

			// post again; should succeed (not throw)
			createdBundle = systemDao.transaction(reqDets, bundle2);
			assertNotNull(createdBundle);
			// should have created the second task
			assertTrue(createdBundle.getEntry().stream()
				.anyMatch(e -> e.getResponse() != null && e.getResponse().getStatus().contains("201 Created")));
		}
	}

	private Task getTaskForId(String theId, String theIdentifier) {
		Task task = myTaskDao.read(new IdType(theId), new SystemRequestDetails());
		assertNotNull(task);
		assertFalse(task.getIdentifier().isEmpty());
		assertTrue(task.getIdentifier().stream().anyMatch(i -> i.getSystem().equals(theIdentifier)));

		return task;
	}

	public static void assertConflictException(String theResourceType, ResourceVersionConflictException e) {
		assertThat(e.getMessage()).matches(Msg.code(550) + Msg.code(515) + "Unable to delete [a-zA-Z]+/[0-9]+ because at least one resource has a reference to this resource. First reference found was resource " + theResourceType + "/[0-9]+ in path [a-zA-Z]+.[a-zA-Z]+");

	}

	private static List<String> toStringList(List<CanonicalType> theUriType) {
		ArrayList<String> retVal = new ArrayList<>();
		for (UriType next : theUriType) {
			retVal.add(next.getValue());
		}
		return retVal;
	}

}
