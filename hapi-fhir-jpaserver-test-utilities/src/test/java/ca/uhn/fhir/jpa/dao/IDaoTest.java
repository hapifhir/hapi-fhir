package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.BaseDaoIT;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

public interface IDaoTest {

	@Test
	default void testReadWriteResourceHistoryTable(){
		// given
		disableConstraints();
		EntityManager entityManager = getEntityManager();
		ResourceHistoryTable resourceHistoryTableEntity = new ResourceHistoryTable();

		populateResourceHistoryEntity(resourceHistoryTableEntity);
		entityManager.getTransaction().begin();

		// when
		try {
			ResourceTable resource = new ResourceTable();
			resource.setResourceType("Patient");
			resource.setPublished(now);
			resource.setUpdated(now);

			resourceHistoryTableEntity.setResourceTable(resource);

			entityManager.persist(resource);
			entityManager.persist(resourceHistoryTableEntity);

			entityManager.flush();

		} catch (Exception e){
			getSupport().getLogger().error(e.getMessage());
			fail();
		} finally {
			getEntityManager().getTransaction().rollback();
		}

		ResourceHistoryTable persistedResourceHistoryEntity = entityManager.find(ResourceHistoryTable.class, resourceHistoryTableEntity.getId());

		// then
		{
			assertThat(persistedResourceHistoryEntity.getResourceId(), equalTo(resourceHistoryTableEntity.getResourceId()));
			assertThat(persistedResourceHistoryEntity.getResourceType(), equalTo(resourceHistoryTableEntity.getResourceType()));
			assertThat(persistedResourceHistoryEntity.getVersion(), equalTo(resourceHistoryTableEntity.getVersion()));
			assertThat(persistedResourceHistoryEntity.getEncoding(), equalTo(resourceHistoryTableEntity.getEncoding()));
			assertThat(persistedResourceHistoryEntity.isHasTags(), equalTo(resourceHistoryTableEntity.isHasTags()));
			assertThat(persistedResourceHistoryEntity.getPublishedDate(), equalTo(resourceHistoryTableEntity.getPublishedDate()));
			assertThat(persistedResourceHistoryEntity.getUpdatedDate(), equalTo(resourceHistoryTableEntity.getUpdatedDate()));
		}
		enableConstraints();
	}

	default void populateResourceHistoryEntity(ResourceHistoryTable theResourceHistoryTable){

		theResourceHistoryTable.setResourceId(1L);
		theResourceHistoryTable.setResourceType("Patient");
		theResourceHistoryTable.setVersion(1L);
		theResourceHistoryTable.setEncoding(ResourceEncodingEnum.JSON);
		theResourceHistoryTable.setHasTags(false);
		theResourceHistoryTable.setPublished(new Date());
		theResourceHistoryTable.setUpdated(new Date());
	}

	void disableConstraints();
	void enableConstraints();

	org.slf4j.Logger getLogger();
	EntityManager getEntityManager();
}
