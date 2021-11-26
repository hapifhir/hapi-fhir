package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexer;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestPropertySource(properties = {
	BaseJpaTest.CONFIG_ENABLE_LUCENE_FALSE
})
public class ReindexNullTargetLinksR4Test extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexNullTargetLinksR4Test.class);

	@Autowired
	ResourceReindexer myResourceReindexer;

	@Test
	public void testReplaceNullLinks() {
		Patient target = new Patient();
		target.setId("Patient/target");
		final String targetId = myPatientDao.update(target, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation source = new Observation();
		source.setId("Observation/source");
		source.setSubject(new Reference("Patient/target"));

		DaoMethodOutcome updateOutcome = myObservationDao.update(source, mySrd);
		final String sourceId = updateOutcome.getId().toUnqualifiedVersionless().getValue();

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionMgr);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		assertCorrectLinks(targetId, sourceId, txTemplate);

		// Null out the targets in the database:
		txTemplate.executeWithoutResult(tx -> {
//			Class<ResourceLink> type = ResourceLink.class;
			int result = myEntityManager.createQuery("UPDATE ResourceLink rl set rl.myTargetResource = null").executeUpdate();
			ourLog.info("Update result: {}", result);
		});

		assertCorruptLinks(sourceId, txTemplate);

		txTemplate.executeWithoutResult(tx -> {
			ResourceTable observationEntity = (ResourceTable) myObservationDao.readEntity(new IdDt(sourceId), mySrd);
			myResourceReindexer.reindexResourceEntity(observationEntity);
		});

		assertCorrectLinks(targetId, sourceId, txTemplate);
	}

	@Test
	public void testReplaceMultipleNullLinks() {
		List<String> performerIds = new ArrayList<>();
		List<Reference> performerReferences = new ArrayList<>();
		Practitioner target_1 = new Practitioner();
		target_1.setId("Practitioner/target-1");
		final String targetId_1 = myPractitionerDao.update(target_1, mySrd).getId().toUnqualifiedVersionless().getValue();
		performerIds.add(targetId_1);
		performerReferences.add(new Reference("Practitioner/target-1"));

		Practitioner target_2 = new Practitioner();
		target_2.setId("Practitioner/target-2");
		final String targetId_2 = myPractitionerDao.update(target_2, mySrd).getId().toUnqualifiedVersionless().getValue();
		performerIds.add(targetId_2);
		performerReferences.add(new Reference("Practitioner/target-2"));

		Practitioner target_3 = new Practitioner();
		target_3.setId("Practitioner/target-3");
		final String targetId_3 = myPractitionerDao.update(target_3, mySrd).getId().toUnqualifiedVersionless().getValue();
		performerIds.add(targetId_3);
		performerReferences.add(new Reference("Practitioner/target-3"));

		Observation source = new Observation();
		source.setId("Observation/source");
		source.setPerformer(performerReferences);

		DaoMethodOutcome updateOutcome = myObservationDao.update(source, mySrd);
		final String sourceId = updateOutcome.getId().toUnqualifiedVersionless().getValue();

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionMgr);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		assertCorrectLinks(performerIds, sourceId, txTemplate, 3);

		// Null out the targets in the database:
		txTemplate.executeWithoutResult(tx -> {
			int result = myEntityManager.createQuery("UPDATE ResourceLink rl set rl.myTargetResource = null").executeUpdate();
			ourLog.info("Update result: {}", result);
		});

		assertCorruptLinks(sourceId, txTemplate, 3);

		txTemplate.executeWithoutResult(tx -> {
			ResourceTable observationEntity = (ResourceTable) myObservationDao.readEntity(new IdDt(sourceId), mySrd);
			myResourceReindexer.reindexResourceEntity(observationEntity);
		});

		assertCorrectLinks(performerIds, sourceId, txTemplate, 3);
	}

	private void assertCorruptLinks(String sourceId, TransactionTemplate txTemplate) {
		assertCorruptLinks(sourceId, txTemplate, 2);
	}

	private void assertCorruptLinks(String sourceId, TransactionTemplate txTemplate, int size) {
		txTemplate.executeWithoutResult(tx -> {
			List<ResourceLink> results = getResourceLinks(size);
			ResourceLink result0 = results.get(0);
			assertNull(result0.getTargetResource());
			assertEquals(sourceId, result0.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
			ResourceLink result1 = results.get(1);
			assertNull(result1.getTargetResource());
			assertEquals(sourceId, result1.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
		});
	}

	private List<ResourceLink> getResourceLinks() {
		return getResourceLinks(2);
	}

	private List<ResourceLink> getResourceLinks(int size) {
		List<ResourceLink> retval = myEntityManager.createQuery("SELECT rl FROM ResourceLink rl", ResourceLink.class).getResultList();
		assertThat(retval, hasSize(size));
		return retval;
	}

	private void assertCorrectLinks(String targetId, String sourceId, TransactionTemplate txTemplate) {
		txTemplate.executeWithoutResult(tx -> {
			List<ResourceLink> results = getResourceLinks();
			ResourceLink result0 = results.get(0);
			assertEquals(targetId, result0.getTargetResource().getIdDt().toUnqualifiedVersionless().toString());
			assertEquals(sourceId, result0.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
			ResourceLink result1 = results.get(1);
			assertEquals(targetId, result1.getTargetResource().getIdDt().toUnqualifiedVersionless().toString());
			assertEquals(sourceId, result1.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
		});
	}

	private void assertCorrectLinks(List<String> targetIds, String sourceId, TransactionTemplate txTemplate, int size) {
		txTemplate.executeWithoutResult(tx -> {
			List<ResourceLink> results = getResourceLinks(size);
			ResourceLink result0 = results.get(0);
			assertThat(result0.getTargetResource().getIdDt().toUnqualifiedVersionless().toString(), in(targetIds));
			assertEquals(sourceId, result0.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
			ResourceLink result1 = results.get(1);
			assertThat(result1.getTargetResource().getIdDt().toUnqualifiedVersionless().toString(), in(targetIds));
			assertEquals(sourceId, result1.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
			ResourceLink result2 = results.get(2);
			assertThat(result2.getTargetResource().getIdDt().toUnqualifiedVersionless().toString(), in(targetIds));
			assertEquals(sourceId, result2.getSourceResource().getIdDt().toUnqualifiedVersionless().toString());
		});
	}

}
