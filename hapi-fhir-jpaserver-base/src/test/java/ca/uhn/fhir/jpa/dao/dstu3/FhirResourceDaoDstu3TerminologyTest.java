package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.AllergyIntolerance.AllergyIntoleranceStatus;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirResourceDaoDstu3TerminologyTest extends BaseJpaDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3TerminologyTest.class);
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";

	@After
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());
	}

	@Before
	public void before() {
		myDaoConfig.setMaximumExpansionSize(5000);
//		my
	}

	private CodeSystem createExternalCs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());

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

		myTermSvc.storeNewCodeSystemVersion(table.getId(), URL_MY_CODE_SYSTEM, cs);
		return codeSystem;
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private void createLocalCsAndVs() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA")
					.addConcept(new ConceptDefinitionComponent().setCode("AAA").setDisplay("Code AAA"))
				)
				.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code B")
				.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code BA"))
				.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code BB"));
		//@formatter:on
		myCodeSystemDao.create(codeSystem, mySrd);

		createLocalVs(codeSystem);
	}

	private void createLocalVs(CodeSystem codeSystem) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.create(valueSet, mySrd);
	}

	@Test
	public void testCodeSystemCreateDuplicateFails() {
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
			assertEquals("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/" + id.getIdPart(), e.getMessage());
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

	private void logAndValidateValueSet(ValueSet theResult) {
		IParser parser = myFhirCtx.newXmlParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(theResult);
		ourLog.info(encoded);

		FhirValidator validator = myFhirCtx.newValidator();
		validator.setValidateAgainstStandardSchema(true);
		validator.setValidateAgainstStandardSchematron(true);
		ValidationResult result = validator.validateWithResult(theResult);

		if (!result.isSuccessful()) {
			ourLog.info(parser.encodeResourceToString(result.toOperationOutcome()));
			fail(parser.encodeResourceToString(result.toOperationOutcome()));
		}
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
			assertEquals("ValueSet contains exclude criteria with no system defined", e.getMessage());
		}
	}

	@Test
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

	@Test
	public void testReindex() {
		createLocalCsAndVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("ZZZZ");

		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(null);
		myTermSvc.saveDeferred();
		mySystemDao.performReindexingPass(null);
		myTermSvc.saveDeferred();
		
		// Again
		mySystemDao.markAllResourcesForReindexing();
		mySystemDao.performReindexingPass(null);
		myTermSvc.saveDeferred();
		mySystemDao.performReindexingPass(null);
		myTermSvc.saveDeferred();

	}

	@Test
	public void testExpandWithNoResultsInLocalValueSet2() {
		createLocalCsAndVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM + "AA");
		include.addConcept().setCode("A");

		try {
			myValueSetDao.expand(vs, null);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("unable to find code system http://example.com/my_code_systemAA", e.getMessage());
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
	public void testExpandWithSystemAndCodesAndFilterKeywordInLocalValueSet() {
		createLocalCsAndVs();

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
		assertThat(codes, containsInAnyOrder("A", "AAA"));

		int idx = codes.indexOf("AAA");
		assertEquals("AAA", result.getExpansion().getContains().get(idx).getCode());
		assertEquals("Code AAA", result.getExpansion().getContains().get(idx).getDisplay());
		assertEquals(URL_MY_CODE_SYSTEM, result.getExpansion().getContains().get(idx).getSystem());
		//
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
	public void testIndexingIsDeferredForLargeCodeSystems() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(1);

		myTermSvc.setProcessDeferred(false);

		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("ParentA");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		assertEquals(0, result.getExpansion().getContains().size());

		myTermSvc.setProcessDeferred(true);
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();

		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("ParentA");
		result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		assertEquals(4, result.getExpansion().getContains().size());

		String encoded = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result);
		assertThat(encoded, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
	}

	/**
	 * Can't currently abort costly
	 */
	@Test
	@Ignore
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
			myObservationDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 1) - Operation aborted!", e.getMessage());
		}
	}

	@Test
	public void testSearchCodeAboveLocalCodesystem() {
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

		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
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
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

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
		ai1.setStatus(AllergyIntoleranceStatus.ACTIVE);
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.setStatus(AllergyIntoleranceStatus.ACTIVECONFIRMED);
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.setStatus(AllergyIntoleranceStatus.INACTIVE);
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-status").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2, id3));

		// No codes in this one
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// Invalid VS
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/FOO").setModifier(TokenParamModifier.IN));
		try {
			myAllergyIntoleranceDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals("Unable to find imported value set http://hl7.org/fhir/ValueSet/FOO", e.getMessage());
		}

	}

	
	/**
	 * Todo: not yet implemented
	 */
	@Test
	@Ignore
	public void testSearchCodeNotInBuiltInValueSet() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.setStatus(AllergyIntoleranceStatus.ACTIVE);
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.setStatus(AllergyIntoleranceStatus.ACTIVECONFIRMED);
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.setStatus(AllergyIntoleranceStatus.INACTIVE);
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-status").setModifier(TokenParamModifier.NOT_IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// No codes in this one
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality").setModifier(TokenParamModifier.NOT_IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2, id3));

		// Invalid VS
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, "http://hl7.org/fhir/ValueSet/FOO").setModifier(TokenParamModifier.NOT_IN));
		try {
			myAllergyIntoleranceDao.search(params);
		} catch (InvalidRequestException e) {
			assertEquals("Unable to find imported value set http://hl7.org/fhir/ValueSet/FOO", e.getMessage());
		}

	}

	@Test
	public void testSearchCodeBelowBuiltInCodesystem() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.setStatus(AllergyIntoleranceStatus.ACTIVE);
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.setStatus(AllergyIntoleranceStatus.ACTIVECONFIRMED);
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.setStatus(AllergyIntoleranceStatus.INACTIVE);
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", AllergyIntoleranceStatus.ACTIVE.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", AllergyIntoleranceStatus.ACTIVE.toCode()).setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", AllergyIntoleranceStatus.ACTIVECONFIRMED.toCode()).setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", AllergyIntoleranceStatus.ACTIVECONFIRMED.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", AllergyIntoleranceStatus.ENTEREDINERROR.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// Unknown code
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status", "fooooo"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

		// Unknown system
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam("http://hl7.org/fhir/allergy-intolerance-status222222", "fooooo"));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

	}

	@Test
	public void testSearchCodeBelowBuiltInCodesystemUnqualified() {
		AllergyIntolerance ai1 = new AllergyIntolerance();
		ai1.setStatus(AllergyIntoleranceStatus.ACTIVE);
		String id1 = myAllergyIntoleranceDao.create(ai1, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai2 = new AllergyIntolerance();
		ai2.setStatus(AllergyIntoleranceStatus.ACTIVECONFIRMED);
		String id2 = myAllergyIntoleranceDao.create(ai2, mySrd).getId().toUnqualifiedVersionless().getValue();

		AllergyIntolerance ai3 = new AllergyIntolerance();
		ai3.setStatus(AllergyIntoleranceStatus.INACTIVE);
		String id3 = myAllergyIntoleranceDao.create(ai3, mySrd).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, AllergyIntoleranceStatus.ACTIVE.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, AllergyIntoleranceStatus.ACTIVE.toCode()).setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id1, id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, AllergyIntoleranceStatus.ACTIVECONFIRMED.toCode()).setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, AllergyIntoleranceStatus.ACTIVECONFIRMED.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(AllergyIntolerance.SP_STATUS, new TokenParam(null, AllergyIntoleranceStatus.ENTEREDINERROR.toCode()));
		assertThat(toUnqualifiedVersionlessIdValues(myAllergyIntoleranceDao.search(params)), empty());

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
	public void testSearchCodeInValueSetThatImportsInvalidCodeSystem() {
		ValueSet valueSet = new ValueSet();
		valueSet.getCompose().addImport("http://non_existant_VS");
		valueSet.setUrl(URL_MY_VALUE_SET);
		IIdType vsid = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params;

		ourLog.info("testSearchCodeInEmptyValueSet without status");
		
		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		try {
			myObservationDao.search(params);
		} catch(InvalidRequestException e) {
			assertEquals("Unable to expand imported value set: Unable to find imported value set http://non_existant_VS", e.getMessage());
		}

		// Now let's update 
		valueSet = new ValueSet();
		valueSet.setId(vsid);
		valueSet.getCompose().addInclude().setSystem("http://hl7.org/fhir/v3/MaritalStatus").addConcept().setCode("A");
		valueSet.setUrl(URL_MY_VALUE_SET);
		myValueSetDao.update(valueSet, mySrd).getId().toUnqualifiedVersionless();

		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		params.add(Observation.SP_STATUS, new TokenParam(null, "final"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

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
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

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
		aeIn1.getType().setSystem("http://nema.org/dicom/dicm").setCode("110102");
		IIdType idIn1 = myAuditEventDao.create(aeIn1, mySrd).getId().toUnqualifiedVersionless();

		AuditEvent aeIn2 = new AuditEvent();
		aeIn2.getType().setSystem("http://hl7.org/fhir/audit-event-type").setCode("rest");
		IIdType idIn2 = myAuditEventDao.create(aeIn2, mySrd).getId().toUnqualifiedVersionless();

		AuditEvent aeOut1 = new AuditEvent();
		aeOut1.getType().setSystem("http://example.com").setCode("foo");
		IIdType idOut1 = myAuditEventDao.create(aeOut1, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/audit-event-type").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAuditEventDao.search(params)), containsInAnyOrder(idIn1.getValue(), idIn2.getValue()));

		params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/v3-PurposeOfUse").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAuditEventDao.search(params)), empty());
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
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue(), idBA.getValue()));

	}

	private ArrayList<String> toCodesContains(List<ValueSetExpansionContainsComponent> theContains) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
