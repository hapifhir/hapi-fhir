package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.FilterOperator;
import org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("Duplicates")
public class FhirResourceDaoR4TerminologyTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TerminologyTest.class);


	@AfterEach
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());

		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	@BeforeEach
	public void before() {
		myDaoConfig.setMaximumExpansionSize(5000);
		myCachingValidationSupport.invalidateCaches();
	}

	private CodeSystem createExternalCs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		return runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

			TermCodeSystemVersion cs = new TermCodeSystemVersion();
			cs.setResource(table);

			TermConcept parentA = new TermConcept(cs, "ParentA").setDisplay("Parent A");
			cs.getConcepts().add(parentA);

			TermConcept childAA = new TermConcept(cs, "childAA").setDisplay("Child AA");
			parentA.addChild(childAA, RelationshipTypeEnum.ISA);

			TermConcept childAAA = new TermConcept(cs, "childAAA").setDisplay("Child AAA");
			childAA.addChild(childAAA, RelationshipTypeEnum.ISA);

			TermConcept childAAB = new TermConcept(cs, "childAAB").setDisplay("Child AAB");
			childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

			TermConcept childAB = new TermConcept(cs, "childAB").setDisplay("Child AB");
			parentA.addChild(childAB, RelationshipTypeEnum.ISA);

			TermConcept parentB = new TermConcept(cs, "ParentB").setDisplay("Parent B");
			cs.getConcepts().add(parentB);

			TermConcept childBA = new TermConcept(cs, "childBA").setDisplay("Child BA");
			childBA.addChild(childAAB, RelationshipTypeEnum.ISA);
			parentB.addChild(childBA, RelationshipTypeEnum.ISA);

			TermConcept parentC = new TermConcept(cs, "ParentC").setDisplay("Parent C");
			cs.getConcepts().add(parentC);

			TermConcept childCA = new TermConcept(cs, "childCA").setDisplay("Child CA");
			parentC.addChild(childCA, RelationshipTypeEnum.ISA);

			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
			return codeSystem;
		});
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private CodeSystem createExternalCsDogs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		return runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

			TermCodeSystemVersion cs = new TermCodeSystemVersion();
			cs.setResource(table);

			TermConcept hello = new TermConcept(cs, "hello").setDisplay("Hello");
			cs.getConcepts().add(hello);

			TermConcept goodbye = new TermConcept(cs, "goodbye").setDisplay("Goodbye");
			cs.getConcepts().add(goodbye);

			TermConcept dogs = new TermConcept(cs, "dogs").setDisplay("Dogs");
			cs.getConcepts().add(dogs);

			TermConcept labrador = new TermConcept(cs, "labrador").setDisplay("Labrador");
			dogs.addChild(labrador, RelationshipTypeEnum.ISA);

			TermConcept beagle = new TermConcept(cs, "beagle").setDisplay("Beagle");
			dogs.addChild(beagle, RelationshipTypeEnum.ISA);

			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
			return codeSystem;
		});
	}

	private void createExternalCsLarge() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = runInTransaction(() -> myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new));

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "codeA").setDisplay("CodeA");
		cs.getConcepts().add(parentA);

		for (int i = 0; i < 450; i++) {
			TermConcept childI = new TermConcept(cs, "subCodeA" + i).setDisplay("Sub-code A" + i);
			parentA.addChild(childI, RelationshipTypeEnum.ISA);
		}

		TermConcept parentB = new TermConcept(cs, "codeB").setDisplay("CodeB");
		cs.getConcepts().add(parentB);

		for (int i = 0; i < 450; i++) {
			TermConcept childI = new TermConcept(cs, "subCodeB" + i).setDisplay("Sub-code B" + i);
			parentB.addChild(childI, RelationshipTypeEnum.ISA);
		}

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void logAndValidateValueSet(ValueSet theResult) {
		IParser parser = myFhirContext.newXmlParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(theResult);
		ourLog.info(encoded);

		FhirValidator validator = myFhirContext.newValidator();
		validator.setValidateAgainstStandardSchema(true);
		validator.setValidateAgainstStandardSchematron(true);
		ValidationResult result = validator.validateWithResult(theResult);

		assertEquals(0, result.getMessages().size());

	}

	@Test
	public void testCodeSystemCreateDuplicateFails() {
		// No version.
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		try {
			myCodeSystemDao.create(codeSystem, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/" + id.getIdPart(), e.getMessage());
		}

		// With version.
		codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("1");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("1");
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		try {
			myCodeSystemDao.create(codeSystem, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(848) + "Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"1\", already have one with resource ID: CodeSystem/" + id.getIdPart(), e.getMessage());
		}

	}

	@Test
	public void testCodeSystemWithDefinedCodes() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
			.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA"))
			.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code A")
			.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code AA"))
			.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code AB"));
		//@formatter:on

		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		Set<TermConcept> codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "A");
		assertThat(toCodes(codes), containsInAnyOrder("A", "AA", "AB"));

	}

	@Test
	public void testConceptTimestamps() {
		long start = System.currentTimeMillis() - 10;

		runInTransaction(() -> {
			List<TermConcept> concepts = myTermConceptDao.findAll();
			assertThat(concepts, empty());
		});

		createExternalCsDogs();

		runInTransaction(() -> {
			List<TermConcept> concepts = myTermConceptDao.findAll();
			for (TermConcept next : concepts) {
				assertTrue(next.getUpdated().getTime() > start, new InstantType(new Date(next.getUpdated().getTime())) + " <= " + new InstantType(new Date(start)));
			}
		});
	}

	@Test
	public void testExpandInvalid() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter();
		include.addFilter().setOp(FilterOperator.ISA).setValue("childAA");

		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(891) + "Invalid filter, must have fields populated: property op value", e.getMessage());
		}
	}

	@Test
	public void testExpandWithIncludeConceptHaveCodeAndDisplay() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("hello").setDisplay("Display-VS"))
			.addConcept(new ConceptReferenceComponent().setCode("goodbye").setDisplay("Goodbye-VS"));

		myValueSetDao.create(valueSet, mySrd);

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter(""));
		logAndValidateValueSet(result);

		assertEquals(2, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("hello", "goodbye"));
		for (ValueSetExpansionContainsComponent vsConcept : result.getExpansion().getContains()) {
			assertTrue(vsConcept.getDisplay().contains("VS"));
		}


	}


	@Test
	public void testExpandWithCodesAndDisplayFilterBlank() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("hello"))
			.addConcept(new ConceptReferenceComponent().setCode("goodbye"));
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addFilter()
			.setProperty("concept")
			.setOp(FilterOperator.ISA)
			.setValue("dogs");

		myValueSetDao.create(valueSet, mySrd);

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter(""));
		logAndValidateValueSet(result);

		assertEquals(4, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("hello", "goodbye", "labrador", "beagle"));

	}

	@Test
	public void testExpandWithCodesAndDisplayFilterPartialOnCodes() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("hello"))
			.addConcept(new ConceptReferenceComponent().setCode("goodbye"));
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addFilter()
			.setProperty("concept")
			.setOp(FilterOperator.ISA)
			.setValue("dogs");

		myValueSetDao.create(valueSet, mySrd);

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter("hel"));
		logAndValidateValueSet(result);

		assertEquals(1, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("hello"));

	}

	@Test
	public void testExpandWithCodesAndDisplayFilterPartialOnExpansion() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.create(valueSet, mySrd);

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter("lab"));
		logAndValidateValueSet(result);

		assertEquals(1, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("labrador"));

	}

	@Test
	public void testExpandWithCodesAndDisplayFilterPartialOnFilter() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("hello"))
			.addConcept(new ConceptReferenceComponent().setCode("goodbye"));
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addFilter()
			.setProperty("concept")
			.setOp(FilterOperator.ISA)
			.setValue("dogs");

		myValueSetDao.create(valueSet, mySrd);

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter("lab"));
		logAndValidateValueSet(result);

		assertEquals(1, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("labrador"));

	}

	@Test
	public void testExpandWithDisplayInExternalValueSetFuzzyMatching() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue("parent a");
		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentA"));

		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue("pare");
		result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);
		codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentA", "ParentB", "ParentC"));

		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("display:exact").setOp(FilterOperator.EQUAL).setValue("pare");
		result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);
		codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, empty());

	}

	@Test
	public void testExpandWithExcludeInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		ConceptSetComponent exclude = vs.getCompose().addExclude();
		exclude.setSystem(URL_MY_CODE_SYSTEM);
		exclude.addConcept().setCode("childAA");
		exclude.addConcept().setCode("childAAA");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentA", "ParentB", "childAB", "childAAB", "ParentC", "childBA", "childCA"));
	}

	@Test
	public void testExpandWithIncludeContainingDashesInInclude() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = runInTransaction(() -> myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new));

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept concept;
		concept = new TermConcept(cs, "LA1111-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA2222-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA3333-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA1122-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA1133-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA4444-2");
		cs.getConcepts().add(concept);
		concept = new TermConcept(cs, "LA9999-7");
		cs.getConcepts().add(concept);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl())
			.addConcept(new ConceptReferenceComponent().setCode("LA2222-2"))
			.addConcept(new ConceptReferenceComponent().setCode("LA1122-2"));
		IIdType vsId = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		ValueSet expansion = myValueSetDao.expand(vsId, null, null);
		Set<String> codes = expansion
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.collect(Collectors.toSet());
		ourLog.info("Codes: {}", codes);
		assertThat(codes, containsInAnyOrder("LA2222-2", "LA1122-2"));
	}

	@Test
	public void testExpandWithInvalidExclude() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		/*
		 * No system set on exclude
		 */
		ConceptSetComponent exclude = vs.getCompose().addExclude();
		exclude.addConcept().setCode("childAA");
		exclude.addConcept().setCode("childAAA");
		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(890) + "ValueSet contains exclude criteria with no system defined", e.getMessage());
		}
	}

	@Test
	public void testExpandWithIsAInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setOp(FilterOperator.ISA).setValue("childAA").setProperty("concept");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAA", "childAAB"));

	}

	@Test
	public void testExpandWithIsAInExternalValueSetReindex() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		createExternalCsAndLocalVs();

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setOp(FilterOperator.ISA).setValue("childAA").setProperty("concept");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAA", "childAAB"));

	}

	@Test
	@Disabled
	public void testExpandWithNoResultsInLocalValueSet1() {
		createLocalCsAndVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("ZZZZ");

		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unable to find code 'ZZZZ' in code system http://example.com/my_code_system", e.getMessage());
		}

	}

	// TODO: get this working
	@Disabled
	@Test
	public void testExpandWithOpEquals() {


		ValueSet result = myValueSetDao.expandByIdentifier("http://hl7.org/fhir/ValueSet/doc-typecodes", new ValueSetExpansionOptions().setFilter(""));
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(result));
	}

	@Test
	public void testExpandWithSystemAndCodesAndFilterKeywordInLocalValueSet() {
		createLocalCsAndVs();

		{
			ValueSet vs = new ValueSet();
			ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(URL_MY_CODE_SYSTEM);
			include.addConcept().setCode("AAA");

			include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue("AAA");

			ValueSet result = myValueSetDao.expand(vs, null);

			// Technically it's not valid to expand a ValueSet with both includes and filters so the
			// result fails validation because of the input.. we're being permissive by allowing both
			// though, so we won't validate the input
			result.setCompose(new ValueSetComposeComponent());

			logAndValidateValueSet(result);

			ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
			assertThat(codes, containsInAnyOrder("AAA"));

			int idx = codes.indexOf("AAA");
			assertEquals("AAA", result.getExpansion().getContains().get(idx).getCode());
			assertEquals("Code AAA", result.getExpansion().getContains().get(idx).getDisplay());
			assertEquals(URL_MY_CODE_SYSTEM, result.getExpansion().getContains().get(idx).getSystem());
		}

		// Now with a disjunction
		{
			ValueSet vs = new ValueSet();
			ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(URL_MY_CODE_SYSTEM);
			include.addConcept().setCode("A");

			include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue("AAA");

			ValueSet result = myValueSetDao.expand(vs, null);

			// Technically it's not valid to expand a ValueSet with both includes and filters so the
			// result fails validation because of the input.. we're being permissive by allowing both
			// though, so we won't validate the input
			result.setCompose(new ValueSetComposeComponent());

			logAndValidateValueSet(result);

			ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
			assertThat(codes, empty());

		}
	}

	@Test
	public void testExpandWithSystemAndCodesInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("ParentA");
		include.addConcept().setCode("childAA");
		include.addConcept().setCode("childAAA");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentA", "childAA", "childAAA"));

		int idx = codes.indexOf("childAA");
		assertEquals("childAA", result.getExpansion().getContains().get(idx).getCode());
		assertEquals("Child AA", result.getExpansion().getContains().get(idx).getDisplay());
		assertEquals(URL_MY_CODE_SYSTEM, result.getExpansion().getContains().get(idx).getSystem());
	}

	@Test
	public void testExpandWithSystemAndCodesInLocalValueSet() {
		createLocalCsAndVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("A");
		include.addConcept().setCode("AA");
		include.addConcept().setCode("AAA");
		include.addConcept().setCode("AB");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("A", "AA", "AAA", "AB"));

		int idx = codes.indexOf("AAA");
		assertEquals("AAA", result.getExpansion().getContains().get(idx).getCode());
		assertEquals("Code AAA", result.getExpansion().getContains().get(idx).getDisplay());
		assertEquals(URL_MY_CODE_SYSTEM, result.getExpansion().getContains().get(idx).getSystem());
		// ValueSet expansion = myValueSetDao.expandByIdentifier(URL_MY_VALUE_SET, "cervical");
		// ValueSet expansion = myValueSetDao.expandByIdentifier(URL_MY_VALUE_SET, "cervical");
		//
	}

	@Test
	public void testExpandWithSystemAndDisplayFilterBlank() {
		CodeSystem codeSystem = createExternalCsDogs();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose()
			.addInclude()
			.setSystem(codeSystem.getUrl());

		ValueSet result = myValueSetDao.expand(valueSet, new ValueSetExpansionOptions().setFilter(""));
		logAndValidateValueSet(result);

		assertEquals(5, result.getExpansion().getTotal());
		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("hello", "goodbye", "dogs", "labrador", "beagle"));

	}

	@Test
	public void testExpandWithSystemAndFilterInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);

		include.addFilter().setProperty("display").setOp(FilterOperator.EQUAL).setValue("Parent B");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentB"));

	}

	@Test
	public void testIndexingIsDeferredForLargeCodeSystems() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(1);

		myTerminologyDeferredStorageSvc.setProcessDeferred(false);

		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("ParentA");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		assertEquals(0, result.getExpansion().getContains().size());

		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("ParentA");
		result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		assertEquals(4, result.getExpansion().getContains().size());

		String encoded = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
		assertThat(encoded, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
	}

	@Test
	public void testLookupSnomed() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://snomed.info/sct");
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = runInTransaction(() -> myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new));

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		TermConcept parentA = new TermConcept(cs, "ParentA").setDisplay("Parent A");
		cs.getConcepts().add(parentA);
		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), "http://snomed.info/sct", "Snomed CT", "SYSTEM VERSION", cs, table);

		StringType code = new StringType("ParentA");
		StringType system = new StringType("http://snomed.info/sct");
		IValidationSupport.LookupCodeResult outcome = myCodeSystemDao.lookupCode(code, system, null, mySrd);
		assertEquals(true, outcome.isFound());
	}

	/**
	 * Can't currently abort costly
	 */
	@Test
	@Disabled
	public void testRefuseCostlyExpansionFhirCodesystem() {
		createLocalCsAndVs();
		myDaoConfig.setMaximumExpansionSize(1);

		SearchParameterMap params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/audit-event-type").setModifier(TokenParamModifier.IN));
		try {
			myAuditEventDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public void testRefuseCostlyExpansionLocalCodesystem() {
		createLocalCsAndVs();
		myDaoConfig.setMaximumExpansionSize(1);

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.ABOVE));
		try {
			myObservationDao.search(params).size();
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString(Msg.code(885) + "Expansion of ValueSet produced too many codes (maximum 1) - Operation aborted!"));
		}
	}

	@Test
	public void testReindex() {
		createLocalCsAndVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("ZZZZ");

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		// Again
		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

	}

	@Test
	public void testSearchCodeAboveLocalCodesystem() {
		createLocalCsAndVs();

		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue()));

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "A").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowAndAboveUnknownCodeSystem() {

		SearchParameterMap params = new SearchParameterMap();

		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "childAA").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "childAA").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowBuiltInCodesystem() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active");
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("resolved");
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("inactive");
		myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "resolved").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "resolved"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		// Unknown code
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "fooooo"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// Unknown system
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical222222", "fooooo"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowBuiltInCodesystemUnqualified() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active");
		ai1.addCategory(org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory.MEDICATION);
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("resolved");
		ai2.addCategory(org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory.BIOLOGIC);
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("inactive");
		ai3.addCategory(org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory.FOOD);
		myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "active"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "active").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CATEGORY, new TokenParam(null, AllergyIntoleranceCategory.MEDICATION.toCode()).setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "resolved").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "resolved"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowExternalCodesystemLarge() {
		createExternalCsLarge();

		Observation obs0 = new Observation();
		obs0.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("codeA");
		IIdType id0 = myObservationDao.create(obs0, mySrd).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("subCodeA1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("subCodeA2");
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("subCodeB3");
		myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "codeA").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(id0.getValue(), id1.getValue(), id2.getValue()));

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "subCodeB1").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowLocalCodesystem() {
		createLocalCsAndVs();

		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "A").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue()));

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeInBuiltInValueSet() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active");
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("resolved");
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("inactive");
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergyintolerance-clinical").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2, id3));

		// No codes in this one
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-category").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// Invalid VS
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/FOO").setModifier(TokenParamModifier.IN));
		try {
			myAllergyIntoleranceDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals("Unable to find imported value set http://hl7.org/fhir/ValueSet/FOO", e.getMessage());
		}

	}

	@Test
	public void testSearchCodeInEmptyValueSet() {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		myValueSetDao.create(valueSet, mySrd);

		SearchParameterMap params;

		ourLog.info("testSearchCodeInEmptyValueSet without status");

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

		ourLog.info("testSearchCodeInEmptyValueSet with status");

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		params.add(Observation.SP_STATUS, new TokenParam(null, "final"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

		ourLog.info("testSearchCodeInEmptyValueSet done");
	}

	@Test
	public void testSearchCodeInExternalCodesystem() {
		createExternalCsAndLocalVs();

		Observation obsPA = new Observation();
		obsPA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("ParentA");
		IIdType idPA = myObservationDao.create(obsPA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsAAA = new Observation();
		obsAAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("childAAA");
		IIdType idAAA = myObservationDao.create(obsAAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsAAB = new Observation();
		obsAAB.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("childAAB");
		IIdType idAAB = myObservationDao.create(obsAAB, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "childAA").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAAA.getValue(), idAAB.getValue()));

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "childAA").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idPA.getValue()));

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idPA.getValue(), idAAA.getValue(), idAAB.getValue()));

	}

	@Test
	public void testSearchCodeInFhirCodesystem() {
		createLocalCsAndVs();

		AuditEvent aeIn1 = new AuditEvent();
		aeIn1.getType().setSystem("http://dicom.nema.org/resources/ontology/DCM").setCode("110102");
		IIdType idIn1 = myAuditEventDao.create(aeIn1, mySrd).getId().toUnqualifiedVersionless();

		AuditEvent aeIn2 = new AuditEvent();
		aeIn2.getType().setSystem("http://terminology.hl7.org/CodeSystem/audit-event-type").setCode("rest");
		IIdType idIn2 = myAuditEventDao.create(aeIn2, mySrd).getId().toUnqualifiedVersionless();

		AuditEvent aeOut1 = new AuditEvent();
		aeOut1.getType().setSystem("http://example.com").setCode("foo");
		myAuditEventDao.create(aeOut1, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/audit-event-type").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAuditEventDao.search(params)), containsInAnyOrder(idIn1.getValue(), idIn2.getValue()));

		try {
			params = new SearchParameterMap();
			params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/v3-PurposeOfUse").setModifier(TokenParamModifier.IN));
		} catch (ResourceNotFoundException e) {
			//noinspection SpellCheckingInspection
			assertEquals("Unknown ValueSet: http%3A%2F%2Fhl7.org%2Ffhir%2FValueSet%2Fv3-PurposeOfUse", e.getMessage());
		}
	}

	@Test
	public void testSearchCodeInLocalCodesystem() {
		createLocalCsAndVs();

		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue(), idBA.getValue()));

		myCaptureQueriesListener.clear();
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue(), idBA.getValue()));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

	}

	@Test
	public void testSearchCodeNotInLocalCodesystem() {
		createLocalCsAndVs();

		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.NOT_IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idCA.getValue()));

		myCaptureQueriesListener.clear();
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idCA.getValue()));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();

	}

	@Test
	public void testSearchCodeInUnknownCodeSystem() {
		SearchParameterMap params = new SearchParameterMap();

		try {
			params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());
		} catch (ResourceNotFoundException e) {
			//noinspection SpellCheckingInspection
			assertEquals(Msg.code(2024) + "Unknown ValueSet: http%3A%2F%2Fexample.com%2Fmy_value_set", e.getMessage());
		}
	}

	@Test
	public void testSearchCodeInValueSetThatImportsInvalidCodeSystem() {
		ValueSet valueSet = new ValueSet();
		valueSet.getCompose().addInclude().addValueSet("http://non_existant_VS");
		valueSet.setUrl(URL_MY_VALUE_SET);
		IIdType vsId = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params;

		ourLog.info("testSearchCodeInEmptyValueSet without status");

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		try {
			myObservationDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals("Unable to expand imported value set: Unable to find imported value set http://non_existant_VS", e.getMessage());
		}

		// Now let's update 
		valueSet = new ValueSet();
		valueSet.setId(vsId);
		valueSet.getCompose().addInclude().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").addConcept().setCode("A");
		valueSet.setUrl(URL_MY_VALUE_SET);
		myValueSetDao.update(valueSet, mySrd).getId().toUnqualifiedVersionless();

		try {
			params = new SearchParameterMap();
			params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
			params.add(Observation.SP_STATUS, new TokenParam(null, "final"));
		} catch (ResourceNotFoundException e) {
			//noinspection SpellCheckingInspection
			assertEquals("Unknown ValueSet: http%3A%2F%2Fnon_existant_VS", e.getMessage());
		}

	}

	/**
	 * Todo: not yet implemented
	 */
	@Test
	@Disabled
	public void testSearchCodeNotInBuiltInValueSet() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active");
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("resolved");
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("inactive");
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-status").setModifier(TokenParamModifier.NOT_IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// No codes in this one
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality").setModifier(TokenParamModifier.NOT_IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2, id3));

		// Invalid VS
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_CLINICAL_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/FOO").setModifier(TokenParamModifier.NOT_IN));
		try {
			myAllergyIntoleranceDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals("Unable to find imported value set http://hl7.org/fhir/ValueSet/FOO", e.getMessage());
		}

	}

	private ArrayList<String> toCodesContains(List<ValueSetExpansionContainsComponent> theContains) {
		ArrayList<String> retVal = new ArrayList<>();
		for (ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}
		return retVal;
	}


}
