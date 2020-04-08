package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.bulk.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticSearch;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4ConfigWithElasticSearch.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4SearchWithElasticSearchTest extends BaseJpaTest {
	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	public static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchWithElasticSearchTest.class);
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> myValueSetDao;
	@Autowired
	protected ITermReadSvcR4 myTermSvc;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Before
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Test
	public void testResourceTextSearch() throws InterruptedException {
		Observation obs1 = new Observation();
		obs1.getCode().setText("Systolic Blood Pressure");
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setValue(new Quantity(123));
		obs1.getNoteFirstRep().setText("obs1");
		IIdType id1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getCode().setText("Diastolic Blood Pressure");
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(81));
		IIdType id2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam("systolic"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1)));

		map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("blood"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id1, id2)));

	}

	@Test
	public void testExpandWithIsAInExternalValueSet() {
		createExternalCsAndLocalVs();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addFilter().setOp(ValueSet.FilterOperator.ISA).setValue("childAA").setProperty("concept");

		ValueSet result = myValueSetDao.expand(vs, null);
		logAndValidateValueSet(result);

		ArrayList<String> codes = toCodesContains(result.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAA", "childAAB"));


	}


	@Test
	public void testExpandVsWithMultiInclude_All() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes.toString(), codes, Matchers.contains("advice", "message", "note", "notification"));
	}


	@Test
	public void testExpandVsWithMultiInclude_Some() throws IOException {
		CodeSystem cs = loadResource(myFhirCtx, CodeSystem.class, "/r4/expand-multi-cs.json");
		myCodeSystemDao.update(cs);

		ValueSet vs = loadResource(myFhirCtx, ValueSet.class, "/r4/expand-multi-vs-all.json");
		vs.getCompose().getInclude().get(0).getConcept().remove(0);
		vs.getCompose().getInclude().get(0).getConcept().remove(0);

		ValueSet expanded = myValueSetDao.expand(vs, null);

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));

		// All codes
		List<String> codes = expanded
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.sorted()
			.collect(Collectors.toList());
		assertThat(codes.toString(), codes, Matchers.contains("advice", "note"));
	}

	private CodeSystem createExternalCs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "ParentA").setDisplay("Parent A");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA").setDisplay("Child AA");
		parentA.addChild(childAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA").setDisplay("Child AAA");
		childAA.addChild(childAAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB").setDisplay("Child AAB");
		childAA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB").setDisplay("Child AB");
		parentA.addChild(childAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB").setDisplay("Parent B");
		cs.getConcepts().add(parentB);

		TermConcept childBA = new TermConcept(cs, "childBA").setDisplay("Child BA");
		childBA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		parentB.addChild(childBA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		TermConcept parentC = new TermConcept(cs, "ParentC").setDisplay("Parent C");
		cs.getConcepts().add(parentC);

		TermConcept childCA = new TermConcept(cs, "childCA").setDisplay("Child CA");
		parentC.addChild(childCA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
		return codeSystem;
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private void createLocalVs(CodeSystem codeSystem) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.create(valueSet, mySrd);
	}

	private ArrayList<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		ArrayList<String> retVal = new ArrayList<>();
		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}
		return retVal;
	}


	private void logAndValidateValueSet(ValueSet theResult) {
		IParser parser = myFhirCtx.newXmlParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(theResult);
		ourLog.info(encoded);

		FhirValidator validator = myFhirCtx.newValidator();
		validator.setValidateAgainstStandardSchema(true);
		validator.setValidateAgainstStandardSchematron(true);
		ValidationResult result = validator.validateWithResult(theResult);

		assertEquals(0, result.getMessages().size());

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
