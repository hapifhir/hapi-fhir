package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.annotation.OracleTest;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {
	EmbeddedDatabaseConfigurations.Oracle23.class
})
@OracleTest
public class DatabaseVerificationWithOracle23IT extends BaseDatabaseVerificationIT {

	@PersistenceContext
	private EntityManager myEntityManager;

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

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

	/**
	 * Oracle refuses to use a LOB column as a DISTINCT / GROUP BY comparison key (ORA-22848), and
	 * {@link TermValueSet#getExpansionError()} is mapped as a CLOB. A query which selects the whole
	 * TermValueSet entity with DISTINCT therefore fails to even parse on Oracle.
	 * <p>
	 * Oracle raises ORA-22848 at describe time, so an empty table is enough to reproduce this.
	 *
	 * @see <a href="https://gitlab.com/simpatico.ai/cdr/-/work_items/9151">GL-9151</a>
	 */
	@Test
	void testFindExpandedByCodeSystemUrl_onOracle_doesNotUseClobAsComparisonKey() {
		runInTransaction(() -> {
			List<TermValueSet> found = myTermValueSetDao.findExpandedByCodeSystemUrl(
				"http://example.com/cs",
				List.of(
					TermValueSetPreExpansionStatusEnum.EXPANDED,
					TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS));

			assertThat(found).isEmpty();
		});
	}

	/**
	 * Creating a CodeSystem invalidates any pre-calculated ValueSet expansion which draws on it, which
	 * runs the query exercised by
	 * {@link #testFindExpandedByCodeSystemUrl_onOracle_doesNotUseClobAsComparisonKey()}. This is the
	 * end to end reproduction of that failure.
	 *
	 * @see <a href="https://gitlab.com/simpatico.ai/cdr/-/work_items/9151">GL-9151</a>
	 */
	@Test
	void testCreateCodeSystem_onOracle_invalidatesValueSetExpansionsWithoutError() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/cs");
		codeSystem.setVersion("0.1.5");
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode("00").setDisplay("No proficiency");

		IIdType codeSystemId = myDaoRegistry.getResourceDao(CodeSystem.class)
			.create(codeSystem, new SystemRequestDetails())
			.getId();

		assertThat(codeSystemId.getIdPart()).isNotBlank();

		CodeSystem persisted = myDaoRegistry.getResourceDao(CodeSystem.class)
			.read(codeSystemId, new SystemRequestDetails());
		assertThat(persisted.getUrl()).isEqualTo("http://example.com/cs");
	}
}
