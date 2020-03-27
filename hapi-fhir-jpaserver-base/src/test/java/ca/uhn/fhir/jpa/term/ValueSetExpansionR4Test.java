package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValueSetExpansionR4Test extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetExpansionR4Test.class);

	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@Test
	public void testDeletePreExpandedValueSet() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertEquals(24, expandedValueSet.getExpansion().getContains().size());

		runInTransaction(()->{
			assertEquals(24, myTermValueSetConceptDao.count());
		});

		myValueSetDao.delete(valueSet.getIdElement());

		runInTransaction(()->{
			assertEquals(0, myTermValueSetConceptDao.count());
		});

		expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertEquals(24, expandedValueSet.getExpansion().getContains().size());
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Test
	public void testExpandTermValueSetAndChildren() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread(), empty());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildren2() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		CodeSystem codeSystem = loadResource(myFhirCtx, CodeSystem.class, "/r4/CodeSystem-iar-chymh-cb-calculated-cap-10.xml");
		myCodeSystemDao.create(codeSystem);

		ValueSet valueSet = loadResource(myFhirCtx, ValueSet.class, "/r4/ValueSet-iar-chymh-cb-calculated-cap-10.xml");
		myValueSetDao.create(valueSet);


		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(3, expandedValueSet.getExpansion().getContains().size());
	}


	@Test
	public void testExpandExistingValueSetNotPreExpanded() throws Exception {
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		myDaoConfig.setPreExpandValueSets(true);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(23);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8450-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure--expiration", containsComponent.getDisplay());
		assertEquals(2, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk - expiratie", designationComponent.getValue());

		designationComponent = containsComponent.getDesignation().get(1);
		assertEquals("sv", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systoliskt blodtryck - utgång", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZero() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		String expanded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet);
		ourLog.info("Expanded ValueSet:\n" + expanded);

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(expanded, 2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals(expanded, "offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(expanded, 0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals(expanded, "count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(expanded, 0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZeroWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffset() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());

		containsComponent = expandedValueSet.getExpansion().getContains().get(22);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8492-1", containsComponent.getCode());
		assertEquals("Systolic blood pressure 8 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandValueSetWithUnknownCodeSystem() {
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem("http://unknown-system");
		ValueSet outcome = myTermSvc.expandValueSet(new ValueSetExpansionOptions().setFailOnMissingCodeSystem(false), vs);
		assertEquals(0, outcome.getExpansion().getContains().size());
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);

		Extension extensionByUrl = outcome.getExpansion().getExtensionByUrl(JpaConstants.EXT_VALUESET_EXPANSION_MESSAGE);
		assertEquals("Unknown CodeSystem URI \"http://unknown-system\" referenced from ValueSet", extensionByUrl.getValueAsPrimitive().getValueAsString());
	}


	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent containsComponent = expandedValueSet.getExpansion().getContains().get(0);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("11378-7", containsComponent.getCode());
		assertEquals("Systolic blood pressure at First encounter", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		containsComponent = expandedValueSet.getExpansion().getContains().get(1);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8493-9", containsComponent.getCode());
		assertEquals("Systolic blood pressure 10 hour minimum", containsComponent.getDisplay());
		assertFalse(containsComponent.hasDesignation());

		// ...

		containsComponent = expandedValueSet.getExpansion().getContains().get(21);
		assertEquals("http://acme.org", containsComponent.getSystem());
		assertEquals("8491-3", containsComponent.getCode());
		assertEquals("Systolic blood pressure 1 hour minimum", containsComponent.getDisplay());
		assertEquals(1, containsComponent.getDesignation().size());

		ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.getDesignation().get(0);
		assertEquals("nl", designationComponent.getLanguage());
		assertEquals("http://snomed.info/sct", designationComponent.getUse().getSystem());
		assertEquals("900000000000013009", designationComponent.getUse().getCode());
		assertEquals("Synonym", designationComponent.getUse().getDisplay());
		assertEquals("Systolische bloeddruk minimaal 1 uur", designationComponent.getValue());
	}

	@Test
	public void testExpandValueSetInMemoryRespectsMaxSize() {
		createCodeSystem();

		// Add lots more codes
		CustomTerminologySet additions = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			additions.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL, additions);


		// Codes available exceeds the max
		myDaoConfig.setMaximumExpansionSize(50);
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		try {
			myTermSvc.expandValueSet(null, vs);
			fail();
		} catch (InternalErrorException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!", e.getMessage());
		}

		// Increase the max so it won't exceed
		myDaoConfig.setMaximumExpansionSize(150);
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);
		assertEquals(109, outcome.getExpansion().getContains().size());

	}

	@Test
	public void testExpandValueSetWithValueSetCodeAccumulator() {
		createCodeSystem();

		when(myValueSetCodeAccumulator.getCapacityRemaining()).thenReturn(100);

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);

		myTermSvc.expandValueSet(null, vs, myValueSetCodeAccumulator);
		verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection());
	}

	@Test
	public void testStoreTermCodeSystemAndChildren() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());
				assertThat(concept.toString(), containsString("8450"));

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndChildrenWithClientAssignedId() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndNestedChildren() {
		IIdType codeSystemId = createCodeSystem();
		CodeSystem codeSystemResource = myCodeSystemDao.read(codeSystemId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystemResource));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				ResourceTable resourceTable = (ResourceTable) myCodeSystemDao.readEntity(codeSystemResource.getIdElement(), null);
				Long codeSystemResourcePid = resourceTable.getId();
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(codeSystemResourcePid);
				assertEquals(CS_URL, codeSystem.getCodeSystemUri());
				assertEquals("SYSTEM NAME", codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(9, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept parentWithNoChildrenA = concepts.get(0);
				assertEquals("ParentWithNoChildrenA", parentWithNoChildrenA.getCode());
				assertNull(parentWithNoChildrenA.getDisplay());
				assertEquals(0, parentWithNoChildrenA.getChildren().size());
				assertEquals(0, parentWithNoChildrenA.getParents().size());
				assertEquals(0, parentWithNoChildrenA.getDesignations().size());
				assertEquals(0, parentWithNoChildrenA.getProperties().size());

				TermConcept parentWithNoChildrenB = concepts.get(1);
				assertEquals("ParentWithNoChildrenB", parentWithNoChildrenB.getCode());
				assertNull(parentWithNoChildrenB.getDisplay());
				assertEquals(0, parentWithNoChildrenB.getChildren().size());
				assertEquals(0, parentWithNoChildrenB.getParents().size());
				assertEquals(0, parentWithNoChildrenB.getDesignations().size());
				assertEquals(0, parentWithNoChildrenB.getProperties().size());

				TermConcept parentWithNoChildrenC = concepts.get(2);
				assertEquals("ParentWithNoChildrenC", parentWithNoChildrenC.getCode());
				assertNull(parentWithNoChildrenC.getDisplay());
				assertEquals(0, parentWithNoChildrenC.getChildren().size());
				assertEquals(0, parentWithNoChildrenC.getParents().size());
				assertEquals(0, parentWithNoChildrenC.getDesignations().size());
				assertEquals(0, parentWithNoChildrenC.getProperties().size());

				TermConcept parentA = concepts.get(3);
				assertEquals("ParentA", parentA.getCode());
				assertNull(parentA.getDisplay());
				assertEquals(2, parentA.getChildren().size());
				assertEquals(0, parentA.getParents().size());
				assertEquals(0, parentA.getDesignations().size());
				assertEquals(0, parentA.getProperties().size());

				TermConcept childAA = concepts.get(4);
				assertEquals("childAA", childAA.getCode());
				assertNull(childAA.getDisplay());
				assertEquals(2, childAA.getChildren().size());
				assertEquals(1, childAA.getParents().size());
				assertSame(parentA, childAA.getParents().iterator().next().getParent());
				assertEquals(0, childAA.getDesignations().size());
				assertEquals(0, childAA.getProperties().size());

				TermConcept childAAA = concepts.get(5);
				assertEquals("childAAA", childAAA.getCode());
				assertNull(childAAA.getDisplay());
				assertEquals(0, childAAA.getChildren().size());
				assertEquals(1, childAAA.getParents().size());
				assertSame(childAA, childAAA.getParents().iterator().next().getParent());
				assertEquals(0, childAAA.getDesignations().size());
				assertEquals(2, childAAA.getProperties().size());

				TermConcept childAAB = concepts.get(6);
				assertEquals("childAAB", childAAB.getCode());
				assertNull(childAAB.getDisplay());
				assertEquals(0, childAAB.getChildren().size());
				assertEquals(1, childAAB.getParents().size());
				assertSame(childAA, childAAB.getParents().iterator().next().getParent());
				assertEquals(1, childAAB.getDesignations().size());
				assertEquals(2, childAAB.getProperties().size());

				TermConcept childAB = concepts.get(7);
				assertEquals("childAB", childAB.getCode());
				assertNull(childAB.getDisplay());
				assertEquals(0, childAB.getChildren().size());
				assertEquals(1, childAB.getParents().size());
				assertSame(parentA, childAB.getParents().iterator().next().getParent());
				assertEquals(0, childAB.getDesignations().size());
				assertEquals(0, childAB.getProperties().size());

				TermConcept parentB = concepts.get(8);
				assertEquals("ParentB", parentB.getCode());
				assertNull(parentB.getDisplay());
				assertEquals(0, parentB.getChildren().size());
				assertEquals(0, parentB.getParents().size());
				assertEquals(0, parentB.getDesignations().size());
				assertEquals(0, parentB.getProperties().size());
			}
		});
	}

	@Test
	public void testStoreTermValueSetAndChildren() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());
			assertEquals(0, concept.getOrder());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(1, concept.getOrder());

			// ...

			concept = termValueSet.getConcepts().get(22);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());
			assertEquals(22, concept.getOrder());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(23, concept.getOrder());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());
			assertEquals(0, concept.getOrder());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(1, concept.getOrder());

			// ...

			concept = termValueSet.getConcepts().get(22);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());
			assertEquals(22, concept.getOrder());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(23);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(23, concept.getOrder());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithExclude() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());
			assertEquals(0, concept.getOrder());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(1, concept.getOrder());

			// ...

			concept = termValueSet.getConcepts().get(20);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());
			assertEquals(20, concept.getOrder());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(21);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(21, concept.getOrder());
		});
	}

	@Test
	public void testStoreTermValueSetAndChildrenWithExcludeWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

			TermValueSetConcept concept = termValueSet.getConcepts().get(0);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8450-9", concept.getCode());
			assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
			assertEquals(2, concept.getDesignations().size());
			assertEquals(0, concept.getOrder());

			TermValueSetConceptDesignation designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

			designation = concept.getDesignations().get(1);
			assertEquals("sv", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

			concept = termValueSet.getConcepts().get(1);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("11378-7", concept.getCode());
			assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(1, concept.getOrder());

			// ...

			concept = termValueSet.getConcepts().get(20);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8491-3", concept.getCode());
			assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
			assertEquals(1, concept.getDesignations().size());
			assertEquals(20, concept.getOrder());

			designation = concept.getDesignations().get(0);
			assertEquals("nl", designation.getLanguage());
			assertEquals("http://snomed.info/sct", designation.getUseSystem());
			assertEquals("900000000000013009", designation.getUseCode());
			assertEquals("Synonym", designation.getUseDisplay());
			assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

			concept = termValueSet.getConcepts().get(21);
			ourLog.info("Concept:\n" + concept.toString());
			assertEquals("http://acme.org", concept.getSystem());
			assertEquals("8492-1", concept.getCode());
			assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
			assertEquals(0, concept.getDesignations().size());
			assertEquals(21, concept.getOrder());
		});
	}

}
