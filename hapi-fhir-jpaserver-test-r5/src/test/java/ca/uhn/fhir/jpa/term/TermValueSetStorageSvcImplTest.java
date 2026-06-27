package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.term.TermValueSetStorageSvcImpl.INTENDED_VERSION_ID_NULL;
import static ca.uhn.fhir.test.utilities.UuidUtils.UUID_PATTERN;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TermValueSetStorageSvcImplTest extends BaseJpaR5Test {

	public static final String CS_URL = "http://system-0";
	private static final String VS_URI = "http://vs";
	private static final String THE_VALUE_SET_NAME = "The ValueSet Name";
	private static final String VS_ID = "VS0";
	private static final String VS_ID_TYPED_AND_VERSION_1 = "ValueSet/" + VS_ID + "/_history/1";

	@Autowired
	private ITermValueSetStorageSvc mySvc;
	private int myNextOrder = 0;

	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(-1);
	}

	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void testStartStagingVersion(String theVersion) {
		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);

		// Test
		String stagingVersion = mySvc.startStagingVersion(VS_URI, version);

		// Validate
		assertThat(stagingVersion).matches(UUID_PATTERN);
		runInTransaction(() -> {
			List<TermValueSet> valueSets = myTermValueSetDao.findAll();

			// Non-staging first, staging is second
			valueSets.sort(Comparator.comparing(TermValueSet::getId));
			assertEquals(version, valueSets.get(0).getVersion());
			assertEquals(stagingVersion, valueSets.get(1).getVersion());
			assertEquals(VS_URI, valueSets.get(0).getUrl());
			assertEquals(VS_URI, valueSets.get(1).getUrl());
			assertNull(valueSets.get(0).getIntendedVersionId());
			assertEquals(getIfNull(version, INTENDED_VERSION_ID_NULL), valueSets.get(1).getIntendedVersionId());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS, valueSets.get(0).getExpansionStatus());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS, valueSets.get(1).getExpansionStatus());
			assertEquals(THE_VALUE_SET_NAME, valueSets.get(0).getName());
			assertEquals(THE_VALUE_SET_NAME, valueSets.get(1).getName());
			assertEquals(VS_ID, valueSets.get(0).getResource().getFhirId());
			assertEquals(VS_ID, valueSets.get(1).getResource().getFhirId());
		});
	}

	@Test
	void testAddConceptsToExpansion_Flat() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1");
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(2, outcome.getAddedConceptCount());
		assertEquals(1, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[1000]
				  -Designation[lang=en, value=Code 0 en]
				 -CODE1 (Code 1) Order[1001]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(1, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_Hierarchy() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		ValueSet.ValueSetExpansionContainsComponent code0 = toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0");
		code0.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0-0")
			.setDisplay("Code 0 Child 0");
		code0.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0-1")
			.setDisplay("Code 0 Child 1");
		myCaptureQueriesListener.clear();
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);
		myCaptureQueriesListener.logInsertQueries();

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(3, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedConceptLinkCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[1000]
				   -CODE0-0 (Code 0 Child 0) Order[1001]
				   -CODE0-1 (Code 0 Child 1) Order[1002]
				""";
			assertEquals(expected, actual);

			assertEquals(3, valueSet.getTotalConcepts());
			assertEquals(0, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_AddHierarchyToExistingConcepts() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Create codes but not relationships between them
		createTermValueSetConcept(stagingVersion, "CODE0", "Code 0");
		createTermValueSetConcept(stagingVersion, "CODE0-0", "Code 0 Child 0");
		createTermValueSetConcept(stagingVersion, "CODE0-1", "Code 0 Child 1");

		// Test - Create the same codes but with parent/child relationships
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		ValueSet.ValueSetExpansionContainsComponent code0 = toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0");
		code0.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0-0")
			.setDisplay("Code 0 Child 0");
		code0.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0-1")
			.setDisplay("Code 0 Child 1");
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedConceptLinkCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[0]
				   -CODE0-0 (Code 0 Child 0) Order[1]
				   -CODE0-1 (Code 0 Child 1) Order[2]
				""";
			assertEquals(expected, actual);

			assertEquals(3, valueSet.getTotalConcepts());
			assertEquals(0, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_SomeCodesAlreadyExist() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		createTermValueSetConceptAndDesignation(stagingVersion, "CODE0", "Code 0", null, "en", "Code 0 en", null);

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(1, outcome.getAddedConceptCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				 -CODE1 (Code 1) Order[1000]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_CodesAlreadyExistButWithoutDisplayOrDesignation() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Create code with no display or designation
		createTermValueSetConceptAndDesignation(stagingVersion, "CODE0", null, null, null, null, null);

		// Test - Create code with display and designation
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(1, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				""";
			assertEquals(expected, actual);

			assertEquals(1, valueSet.getTotalConcepts());
			assertEquals(1, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_VersionedSystem() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);

		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setVersion("1.0")
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL + "|2.0")
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(2, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0|1.0]
				 -CODE0 (Code 0) Order[1000]
				  -Designation[lang=en, value=Code 0 en]
				-System[http://system-0|2.0]
				 -CODE1 (Code 1) Order[1001]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testAddConceptsToExpansion_VersionedSystem_AlreadyExists() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		createTermValueSetConcept(stagingVersion, "CODE0", "Code 0", "1.0");
		createTermValueSetConcept(stagingVersion, "CODE1", "Code 1", "2.0");

		// Test
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);

		// We'll add the same code from two versions of the system. This isn't generally
		// something people will do, but let's make sure we don't throw any constraint errors.
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setVersion("1.0")
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL + "|2.0")
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		UploadStatistics outcome = mySvc.addConceptsToExpansion(toAdd, 1000);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		assertEquals(0, outcome.getAddedConceptCount());
		assertEquals(2, outcome.getAddedDesignationCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			// FIXME: ensure we include versions
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0|1.0]
				 -CODE0 (Code 0) Order[0]
				  -Designation[lang=en, value=Code 0 en]
				-System[http://system-0|2.0]
				 -CODE1 (Code 1) Order[1]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(2, valueSet.getTotalConcepts());
			assertEquals(2, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testRemoveConceptsFromExpansion() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		for (int i = 0; i < 5; i++) {
			TermValueSetConcept parent = createTermValueSetConceptAndDesignation(stagingVersion, "CODE" + i, "Code " + i, null, "en", "Code " + i + " en", null);
			createTermValueSetConceptAndDesignation(stagingVersion, "CODE" + i + "-0", "Code " + i + " Child 0", null, null, null, parent);
		}

		// Test
		ValueSet toDelete = new ValueSet();
		toDelete.setUrl(VS_URI);
		toDelete.setVersion(stagingVersion);
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0"); // Will be deleted, plus its child
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1-0"); // This is a child, parent will be left alone
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE-ABC-0"); // Doesn't exist
		toDelete.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE-ABC-1"); // Doesn't exist
		UploadStatistics outcome = mySvc.removeConceptsFromExpansion(toDelete);

		// Verify
		assertEquals(VS_ID_TYPED_AND_VERSION_1, outcome.getTarget().getValue());
		// Code0 (1 + 1 for its child), Code1 (1) = 3
		assertEquals(3, outcome.getRemovedConceptCount());
		// Code0->Code0-1 Code1->Code1-0
		assertEquals(2, outcome.getRemovedConceptLinkCount());
		runInTransaction(() -> {
			TermValueSet valueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();
			String actual = renderExpansionHierarchy(valueSet);
			String expected = """
				-System[http://system-0]
				 -CODE1 (Code 1) Order[2]
				  -Designation[lang=en, value=Code 1 en]
				 -CODE2 (Code 2) Order[4]
				  -Designation[lang=en, value=Code 2 en]
				   -CODE2-0 (Code 2 Child 0) Order[5]
				 -CODE3 (Code 3) Order[6]
				  -Designation[lang=en, value=Code 3 en]
				   -CODE3-0 (Code 3 Child 0) Order[7]
				 -CODE4 (Code 4) Order[8]
				  -Designation[lang=en, value=Code 4 en]
				   -CODE4-0 (Code 4 Child 0) Order[9]
				""";
			assertEquals(expected, actual);

			assertEquals(7, valueSet.getTotalConcepts());
			assertEquals(4, valueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@ParameterizedTest
	@ValueSource(strings = {"1.0", ""})
	void testActivateStagingVersion(String theVersion) {
		// Setup

		String version = defaultIfBlank(theVersion, null);
		createValueSetResource(version);

		// Add some codes to version 1.0, including a code with a child code
		// and a designation
		runInTransaction(() -> {
			TermValueSet termValueSet;
			if (version == null) {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URI).orElseThrow();
			} else {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, version).orElseThrow();
			}

			TermValueSetConcept code0 = new TermValueSetConcept();
			code0.setSystem(CS_URL);
			code0.setCode("CODE0");
			code0.setDisplay("Code 0");
			code0.setValueSet(termValueSet);
			code0.setOrder(0);
			myTermValueSetConceptDao.save(code0);

			TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
			designation.setLanguage("en");
			designation.setValue("Code 0 en");
			designation.setConcept(code0);
			designation.setValueSet(termValueSet);
			code0.getDesignations().add(designation);
			myTermValueSetConceptDesignationDao.save(designation);

			TermValueSetConcept code0child0 = new TermValueSetConcept();
			code0child0.setSystem(CS_URL);
			code0child0.setCode("CODE0-0");
			code0child0.setDisplay("Code 0 child 0");
			code0child0.setValueSet(termValueSet);
			code0child0.setOrder(1);
			myTermValueSetConceptDao.save(code0child0);

			TermValueSetConceptParentChildLink childLink = new TermValueSetConceptParentChildLink();
			childLink.setValueSet(termValueSet);
			TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk pk = new TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk();
			pk.setParentPid(code0.getId());
			pk.setChildPid(code0child0.getId());
			childLink.setId(pk);
			myITermValueSetConceptParentChildLinkDao.save(childLink);

			termValueSet.setTotalConcepts(2L);
			termValueSet.setTotalConceptDesignations(1L);
			myTermValueSetDao.save(termValueSet);
		});

		// Create a staging version with different codes
		String stagingVersion = mySvc.startStagingVersion(VS_URI, version);
		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 1 en"));
		mySvc.addConceptsToExpansion(toAdd, 1000);

		// Test

		mySvc.activateStagingVersion(VS_URI, stagingVersion);

		// Verify

		runInTransaction(() -> {
			TermValueSet termValueSet;
			if (version == null) {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(VS_URI).orElseThrow();
			} else {
				termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, version).orElseThrow();
			}
			String actual = renderExpansionHierarchy(termValueSet);
			String expected = """
				-System[http://system-0]
				 -CODE1 (Code 1) Order[1000]
				  -Designation[lang=en, value=Code 1 en]
				""";
			assertEquals(expected, actual);

			assertEquals(1, termValueSet.getTotalConcepts());
			assertEquals(1, termValueSet.getTotalConceptDesignations());
		});
		assertPartitionSet();
	}

	@Test
	void testActivateStagingVersion_NonStagingVersionPassed() {
		// Setup

		createValueSetResource("1.0");

		// Test

		assertThatThrownBy(()->mySvc.activateStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("ValueSet URL[http://vs] Version[1.0] is not a staging version");

	}

	@Test
	void testActivateStagingVersion_UnknownVersionPassed() {
		// Test

		assertThatThrownBy(()->mySvc.activateStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("No ValueSet found with URL[http://vs] and Version[1.0]");

	}

	@Test
	void testDropStagingVersion() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		ValueSet toAdd = new ValueSet();
		toAdd.setUrl(VS_URI);
		toAdd.setVersion(stagingVersion);
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE0")
			.setDisplay("Code 0")
			.addDesignation(new ValueSet.ConceptReferenceDesignationComponent().setLanguage("en").setValue("Code 0 en"));
		toAdd.getExpansion()
			.addContains()
			.setSystem(CS_URL)
			.setCode("CODE1")
			.setDisplay("Code 1");
		mySvc.addConceptsToExpansion(toAdd, 1000);

		// Test

		mySvc.dropStagingVersion(VS_URI, stagingVersion);

		// Verify

		runInTransaction(()->{
			assertEquals(1, myTermValueSetDao.count());
			assertEquals(0, myTermValueSetConceptDao.count());
		});
	}

	@Test
	void testDropStagingVersion_NonStagingVersionSpecified() {
		// Setup
		createValueSetResource("1.0");

		// Test

		assertThatThrownBy(()->mySvc.dropStagingVersion(VS_URI, "1.0"))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Cannot drop staging version of ValueSet[url=http://vs, version=1.0] because it is not a staging version");

	}

	private void assertPartitionSet() {
		runInTransaction(() -> {
			for (TermValueSet valueSet : myTermValueSetDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
			for (TermValueSetConcept valueSet : myTermValueSetConceptDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
			for (TermValueSetConceptDesignation valueSet : myTermValueSetConceptDesignationDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
			for (TermValueSetConceptParentChildLink valueSet : myITermValueSetConceptParentChildLinkDao.findAll()) {
				assertEquals(-1, valueSet.getPartitionId().getPartitionId());
			}
		});
	}

	private void createTermValueSetConcept(String theStagingVersion, String theCode, String theDisplay) {
		createTermValueSetConcept(theStagingVersion, theCode, theDisplay, null);
	}

	private void createTermValueSetConcept(String theStagingVersion, String theCode, String theDisplay, String theSystemVersion) {
		createTermValueSetConceptAndDesignation(theStagingVersion, theCode, theDisplay, theSystemVersion, null, null, null);
	}

	private TermValueSetConcept createTermValueSetConceptAndDesignation(String theStagingVersion, String theCode, String theDisplay, String theSystemVersion, String theDesignationLang, String theDesignationValue, TermValueSetConcept theParent) {
		return runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, theStagingVersion).orElseThrow();

			TermValueSetConcept concept = new TermValueSetConcept();
			concept.setSystem(CS_URL);
			concept.setSystemVersion(theSystemVersion);
			concept.setCode(theCode);
			concept.setDisplay(theDisplay);
			concept.setValueSet(termValueSet);
			concept.setOrder(myNextOrder++);

			concept = myTermValueSetConceptDao.save(concept);

			if (theDesignationLang != null) {
				TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
				designation.setLanguage(theDesignationLang);
				designation.setValue(theDesignationValue);
				designation.setConcept(concept);
				designation.setValueSet(termValueSet);
				concept.getDesignations().add(designation);
				myTermValueSetConceptDesignationDao.save(designation);

				termValueSet.setTotalConceptDesignations(getIfNull(termValueSet.getTotalConceptDesignations(), 0L) + 1);
			}

			termValueSet.setTotalConcepts(getIfNull(termValueSet.getTotalConcepts(), 0L) + 1);
			myTermValueSetDao.save(termValueSet);

			if (theParent != null) {
				TermValueSetConceptParentChildLink childLink = new TermValueSetConceptParentChildLink();
				childLink.setValueSet(termValueSet);
				TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk pk = new TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk();
				pk.setParentPid(theParent.getId());
				pk.setChildPid(concept.getId());
				childLink.setId(pk);
				myITermValueSetConceptParentChildLinkDao.save(childLink);
			}

			return concept;
		});
	}


	private void createValueSetResource(String version) {
		org.hl7.fhir.r5.model.ValueSet vs = new org.hl7.fhir.r5.model.ValueSet();
		vs.setId(VS_ID);
		vs.setUrl(VS_URI);
		vs.setVersion(version);
		vs.setName(THE_VALUE_SET_NAME);
		myValueSetDao.update(vs, newSrd());
	}

	String renderExpansionHierarchy(TermValueSet theValueSet) {
		HapiTransactionService.requireTransaction();

		StringBuilder target = new StringBuilder();
		List<TermValueSetConcept> rootConcepts = theValueSet
			.getConcepts()
			.stream()
			.filter(t -> t.getParents().isEmpty())
			.toList();

		appendToHierarchy(rootConcepts, target);
		return target.toString();
	}

	private void appendToHierarchy(List<TermValueSetConcept> theConceptList, StringBuilder theTarget) {

		ImmutableListMultimap<UrlUtil.CanonicalUrlParts, TermValueSetConcept> systemToConcepts = Multimaps.index(theConceptList, c -> new UrlUtil.CanonicalUrlParts(c.getSystem(), c.getSystemVersion()));
		for (Map.Entry<UrlUtil.CanonicalUrlParts, Collection<TermValueSetConcept>> entry : systemToConcepts.asMap().entrySet()) {
			UrlUtil.CanonicalUrlParts system = entry.getKey();
			Collection<TermValueSetConcept> concepts = entry.getValue();
			theTarget.append("-System[");
			theTarget.append(system.toString()).append("]\n");
			appendConceptsToHierarchy(0, theTarget, concepts);
		}
	}

	private static void appendConceptsToHierarchy(int theDepth, StringBuilder theTarget, Collection<TermValueSetConcept> concepts) {
		for (TermValueSetConcept concept : concepts) {
			theTarget.append("  ".repeat(theDepth));
			theTarget.append(" -");
			theTarget.append(concept.getCode());
			if (isNotBlank(concept.getDisplay())) {
				theTarget.append(" (").append(concept.getDisplay()).append(")");
			}
			theTarget.append(" Order[").append(concept.getOrder()).append("]");
			theTarget.append("\n");

			for (TermValueSetConceptDesignation designation : concept.getDesignations()) {
				theTarget.append("  ".repeat(theDepth));
				theTarget.append("  -Designation[lang=");
				theTarget.append(designation.getLanguage());
				theTarget.append(", value=");
				theTarget.append(designation.getValue());
				theTarget.append("]\n");
			}

			List<TermValueSetConcept> childConcepts = concept.getChildConcepts();
			appendConceptsToHierarchy(theDepth + 1, theTarget, childConcepts);
		}
	}

}
