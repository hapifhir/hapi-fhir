package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4CodeSystemTest extends BaseJpaR4Test {

	@Autowired private Batch2JobHelper myBatchJobHelper;

	@Test
	public void testIndexContained() throws Exception {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		String input = IOUtils.toString(getClass().getResource("/r4/codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirContext.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemDao.create(cs, mySrd);

		myResourceReindexingSvc.markAllResourcesForReindexing();
		int outcome = myResourceReindexingSvc.forceReindexingPass();
		assertThat(outcome).isNotEqualTo(-1); // -1 means there was a failure

		myTerminologyDeferredStorageSvc.saveDeferred();

	}

	@Test
	public void testDeleteLargeCompleteCodeSystem() {
		IIdType id = createLargeCodeSystem(null);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		myCodeSystemDao.delete(id);

		// Nothing is deleted initially but the URI is changed so it can't be found
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});

	}

	@Test
	public void testDeleteCodeSystemVersion() {

		// Create code system with two versions.
		IIdType id_first = createLargeCodeSystem("1");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(222, myTermConceptDao.count());
			assertEquals(1, resourceList.size());
			assertNull(resourceList.get(0).getDeleted());
		});

		IIdType id_second = createLargeCodeSystem("2");

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(444, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(2, active);
		});

		// Attempt to delete first version
		myCodeSystemDao.delete(id_first, mySrd);

		// Only the resource will be deleted initially
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(444, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(1, active);
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME);

		// Entities for first resource should be gone now.
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222, myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(1, active);
		});

		// Attempt to delete second version
		myCodeSystemDao.delete(id_second, mySrd);

		// Only the resource will be deleted initially, but the URL for the TermCodeSystem will be cleared and not searchable.
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(222,  myTermConceptDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(0, active);
		});

		// Now the background scheduler will do its thing
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);

		// The remaining versions and Code System entities should be gone now.
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(0, myTermCodeSystemVersionDao.count());
			List<ResourceTable> resourceList = myResourceTableDao.findAll();
			assertEquals(2, resourceList.size());
			long active = resourceList
				.stream()
				.filter(t -> t.getDeleted() == null).count();
			assertEquals(0, active);
		});

	}

	private IIdType createLargeCodeSystem(String theVersion) {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		if (theVersion != null) {
			cs.setVersion(theVersion);
		}
		for (int i = 0; i < 222; i++) {
			cs.addConcept().setCode("CODE" + i);
		}
		IIdType id = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		return id;
	}

	@Test
	public void testCodeSystemWithDuplicateCode() {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.setVersion("1.0");
		cs.addConcept().setCode("CODE0").setDisplay("Code0");
		cs.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE2").setDisplay("Code2");

		try {
			myCodeSystemDao.create(cs, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("Duplicate concept detected in CodeSystem: CODE1");
		}
	}

	@Test
	public void testCodeSystemWithDuplicateCodeInChild() {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		cs.setVersion("1.0");

		CodeSystem.ConceptDefinitionComponent parent = cs.addConcept().setCode("CODE0").setDisplay("Code0");
		parent.addConcept().setCode("CODE1").setDisplay("Code1");
		parent.addConcept().setCode("CODE1").setDisplay("Code1");
		cs.addConcept().setCode("CODE2").setDisplay("Code2");

		try {
			myCodeSystemDao.create(cs, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage()).contains("Duplicate concept detected in CodeSystem: CODE1");
		}
	}


	@AfterAll
	public static void afterClassClearContext() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}


}
