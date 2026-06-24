package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.hl7.fhir.r5.model.ValueSet;
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
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
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
			if (version == null) {
				assertEquals(INTENDED_VERSION_ID_NULL, valueSets.get(1).getIntendedVersionId());
			} else {
				assertEquals(version, valueSets.get(1).getIntendedVersionId());
			}
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
	void testAddConceptsToExpansion_SomeCodesAlreadyExist() {
		// Setup
		createValueSetResource("1.0");
		String stagingVersion = mySvc.startStagingVersion(VS_URI, "1.0");

		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();

			TermValueSetConcept code0 = new TermValueSetConcept();
			code0.setSystem(CS_URL);
			code0.setCode("CODE0");
			code0.setDisplay("Code 0");
			code0.setValueSet(termValueSet);
			myTermValueSetConceptDao.save(code0);

			TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
			designation.setLanguage("en");
			designation.setValue("Code 0 en");
			designation.setConcept(code0);
			designation.setValueSet(termValueSet);
			code0.getDesignations().add(designation);
			myTermValueSetConceptDesignationDao.save(designation);

			termValueSet.setTotalConcepts(1L);
			termValueSet.setTotalConceptDesignations(1L);
			myTermValueSetDao.save(termValueSet);
		});

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

		runInTransaction(() -> {
			TermValueSet termValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(VS_URI, stagingVersion).orElseThrow();

			TermValueSetConcept code0 = new TermValueSetConcept();
			code0.setSystem(CS_URL);
			code0.setSystemVersion("1.0");
			code0.setCode("CODE0");
			code0.setDisplay("Code 0");
			code0.setValueSet(termValueSet);
			code0.setOrder(0);
			myTermValueSetConceptDao.save(code0);

			TermValueSetConcept code1 = new TermValueSetConcept();
			code1.setSystem(CS_URL);
			code1.setSystemVersion("2.0");
			code1.setCode("CODE1");
			code1.setDisplay("Code 1");
			code1.setValueSet(termValueSet);
			code1.setOrder(1);
			myTermValueSetConceptDao.save(code1);

			termValueSet.setTotalConcepts(2L);
			myTermValueSetDao.save(termValueSet);
		});

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

		appendToHierarchy(rootConcepts, 0, target);
		return target.toString();
	}

	private void appendToHierarchy(List<TermValueSetConcept> theConceptList, int theDepth, StringBuilder theTarget) {

		ImmutableListMultimap<UrlUtil.CanonicalUrlParts, TermValueSetConcept> systemToConcepts = Multimaps.index(theConceptList, c -> new UrlUtil.CanonicalUrlParts(c.getSystem(), c.getSystemVersion()));
		for (Map.Entry<UrlUtil.CanonicalUrlParts, Collection<TermValueSetConcept>> entry : systemToConcepts.asMap().entrySet()) {
			UrlUtil.CanonicalUrlParts system = entry.getKey();
			Collection<TermValueSetConcept> concepts = entry.getValue();
			theTarget.append("  ".repeat(theDepth));
			theTarget.append("-System[");
			theTarget.append(system.toString()).append("]\n");
			appendConceptsToHierarchy(theDepth, theTarget, concepts);
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
