package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import ca.uhn.fhir.jpa.embedded.Oracle23EmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {
	DatabaseVerificationWithOracle23IT.TestConfig.class
})
@OracleTest
public class DatabaseVerificationWithOracle23IT extends BaseDatabaseVerificationIT {

	@PersistenceContext
	private EntityManager myEntityManager;

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject(){
			return new JpaDatabaseContextConfigParamObject(
				new Oracle23EmbeddedDatabase(),
				HapiFhirOracleDialect.class.getName()
			);
		}
	}

	/**
	 * Tests boolean field transitions: true → false → null.
	 *
	 * <p>Ensures all boolean value transitions work correctly with Oracle 23ai.
	 */
	@Test
	void testBooleanFieldTransitions_shouldHandleAllStates() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();

		Patient persisted = myPatientDao.read(patientId, new SystemRequestDetails());
		assertThat(persisted.getActive()).isTrue();

		persisted.setActive(false);
		myPatientDao.update(persisted, new SystemRequestDetails());

		persisted = myPatientDao.read(patientId, new SystemRequestDetails());
		assertThat(persisted.getActive()).isFalse();

		// Update to active=null (this triggers the Oracle 23ai issue if using driver <= 21.5.0.0)
		persisted.setActiveElement(null);
		myPatientDao.update(persisted, new SystemRequestDetails());

		persisted = myPatientDao.read(patientId, new SystemRequestDetails());
		assertThat(persisted.hasActive()).isFalse();
	}

	@Test
	void testCreateEntityWithNullBooleanField_shouldNotThrowInvalidColumnType() {

		runInTransaction(() -> {
			TagDefinition tag = new TagDefinition();
			tag.setTagType(TagTypeEnum.TAG);
			tag.setSystem("http://example.com");
			tag.setCode("test-tag-create");
			tag.setUserSelected(null);  // NULL Boolean - triggers setNull(index, Types.BOOLEAN)

			myEntityManager.persist(tag);
			myEntityManager.flush();
			return tag.getId();
		});

		runInTransaction(() -> {
			TagDefinition found = myEntityManager
				.createQuery("SELECT t FROM TagDefinition t WHERE t.myCode = :code", TagDefinition.class)
				.setParameter("code", "test-tag-create")
				.getSingleResult();

			assertThat(found).isNotNull();
			assertThat(found.getUserSelected()).isNull();
			return null;
		});
	}

	@Test
	void testUpdateEntityToSetBooleanFieldNull_shouldNotThrowInvalidColumnType() {
		Long tagId = runInTransaction(() -> {
			TagDefinition tag = new TagDefinition();
			tag.setTagType(TagTypeEnum.TAG);
			tag.setSystem("http://example.com");
			tag.setCode("test-tag-update");
			tag.setUserSelected(Boolean.TRUE);

			myEntityManager.persist(tag);
			myEntityManager.flush();
			return tag.getId();
		});

		runInTransaction(() -> {
			TagDefinition found = myEntityManager.find(TagDefinition.class, tagId);
			assertThat(found.getUserSelected()).isTrue();
			return null;
		});

		// Update to set userSelected=null (triggers setNull with Types.BOOLEAN)
		runInTransaction(() -> {
			TagDefinition found = myEntityManager.find(TagDefinition.class, tagId);
			found.setUserSelected(null);  // NULL Boolean - triggers setNull(index, Types.BOOLEAN)
			myEntityManager.flush();
			return null;
		});

		runInTransaction(() -> {
			TagDefinition found = myEntityManager.find(TagDefinition.class, tagId);
			assertThat(found.getUserSelected()).isNull();
			return null;
		});
	}
}
