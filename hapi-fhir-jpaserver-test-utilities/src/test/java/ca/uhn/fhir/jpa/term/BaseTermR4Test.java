package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class BaseTermR4Test extends BaseJpaR4Test {

	IIdType myExtensionalCsId;
	IIdType myExtensionalVsId;
	Long myExtensionalCsIdOnResourceTable;
	Long myExtensionalVsIdOnResourceTable;

	@BeforeEach
	public void before() {
		myDaoConfig.setAllowExternalReferences(true);
	}

	@AfterEach
	public void after() {
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
		myDaoConfig.setMaximumExpansionSize(DaoConfig.DEFAULT_MAX_EXPANSION_SIZE);
	}

	IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		return runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

			TermCodeSystemVersion cs = new TermCodeSystemVersion();
			cs.setResource(table);

			TermConcept parent;
			parent = new TermConcept(cs, "ParentWithNoChildrenA");
			cs.getConcepts().add(parent);
			parent = new TermConcept(cs, "ParentWithNoChildrenB");
			cs.getConcepts().add(parent);
			parent = new TermConcept(cs, "ParentWithNoChildrenC");
			cs.getConcepts().add(parent);

			TermConcept parentA = new TermConcept(cs, "ParentA");
			cs.getConcepts().add(parentA);

			TermConcept childAA = new TermConcept(cs, "childAA");
			parentA.addChild(childAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

			TermConcept childAAA = new TermConcept(cs, "childAAA");
			childAAA.addPropertyString("propA", "valueAAA");
			childAAA.addPropertyString("propB", "foo");
			childAA.addChild(childAAA, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

			TermConcept childAAB = new TermConcept(cs, "childAAB");
			childAAB.addPropertyString("propA", "valueAAB");
			childAAB.addPropertyString("propB", "foo");
			childAAB.addDesignation()
				.setUseSystem("D1S")
				.setUseCode("D1C")
				.setUseDisplay("D1D")
				.setValue("D1V");
			childAA.addChild(childAAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

			TermConcept childAB = new TermConcept(cs, "childAB");
			parentA.addChild(childAB, TermConceptParentChildLink.RelationshipTypeEnum.ISA);

			TermConcept parentB = new TermConcept(cs, "ParentB");
			cs.getConcepts().add(parentB);

			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

			return id;
		});
	}

	void loadAndPersistCodeSystemAndValueSet() throws IOException {
		loadAndPersistCodeSystem();
		loadAndPersistValueSet(HttpVerb.POST);
	}

	void loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb theVerb) throws IOException {
		loadAndPersistCodeSystemWithDesignations(theVerb);
		loadAndPersistValueSet(theVerb);
	}

	void loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb theVerb) throws IOException {
		loadAndPersistCodeSystemWithDesignations(theVerb);
		loadAndPersistValueSetWithExclude(theVerb);
	}

	void loadAndPersistCodeSystem() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, HttpVerb.POST);
	}

	void loadAndPersistCodeSystemWithDesignations(HttpVerb theVerb) throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs-with-designations.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, theVerb);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistCodeSystem(CodeSystem theCodeSystem, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.update(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
		myExtensionalCsIdOnResourceTable = myCodeSystemDao.readEntity(myExtensionalCsId, null).getId();
	}

	void loadAndPersistValueSet(HttpVerb theVerb) throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, theVerb);
	}

	private void loadAndPersistValueSetWithExclude(HttpVerb theVerb) throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs-with-exclude.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, theVerb);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistValueSet(ValueSet theValueSet, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.update(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
		myExtensionalVsIdOnResourceTable = myValueSetDao.readEntity(myExtensionalVsId, null).getId();
	}

}
