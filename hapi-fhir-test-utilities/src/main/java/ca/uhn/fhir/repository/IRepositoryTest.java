package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.RepositoryTestDataBuilder;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Generic test of repository functionality */
@SuppressWarnings({"java:S5960" // this is a test jar
})
public interface IRepositoryTest {
	Logger ourLog = LoggerFactory.getLogger(IRepositoryTest.class);

	@Test
	default void testCreate_readById_contentsPersisted() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate("1970-02-14"));
		IRepository repository = getRepository();

		// when
		MethodOutcome methodOutcome = repository.create(patient);
		ourLog.info("Created resource with id: {} created:{}", methodOutcome.getId(), methodOutcome.getCreated());
		IBaseResource read =
				repository.read(patient.getClass(), methodOutcome.getId().toVersionless());

		// then
		assertThat(read)
				.isNotNull()
				.extracting(p -> getTerser().getSinglePrimitiveValueOrNull(p, "birthDate"))
				.as("resource body read matches persisted value")
				.isEqualTo("1970-02-14");
	}

	@Test
	default void testCreateClientAssignedId_readBySameId_findsResource() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withId("pat123"), b.withBirthdate("1970-02-14"));
		IRepository repository = getRepository();

		// when
		MethodOutcome methodOutcome = repository.update(patient);
		IBaseResource read = repository.read(patient.getClass(), methodOutcome.getId());

		// then
		assertThat(read).isNotNull();
		assertThat(getTerser().getSinglePrimitiveValueOrNull(read, "birthDate")).isEqualTo("1970-02-14");
		assertThat(read.getIdElement().getIdPart()).isEqualTo("pat123");
	}

	@Test
	default void testCreate_update_readById_verifyUpdatedContents() {
		// given
		String initialBirthdate = "1970-02-14";
		String updatedBirthdate = "1980-03-15";
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate(initialBirthdate));
		IRepository repository = getRepository();

		// when - create
		MethodOutcome createOutcome = repository.create(patient);
		IIdType patientId = createOutcome.getId().toVersionless();

		// update with different birthdate
		var updatedPatient = b.buildPatient(b.withId(patientId), b.withBirthdate(updatedBirthdate));
		var updateOutcome = repository.update(updatedPatient);
		assertThat(updateOutcome.getId().toVersionless()).isEqualTo(patientId);
		assertThat(updateOutcome.getCreated()).isFalse();

		// read
		IBaseResource read = repository.read(patient.getClass(), patientId);

		// then
		assertThat(read)
				.isNotNull()
				.extracting(p -> getTerser().getSinglePrimitiveValueOrNull(p, "birthDate"))
				.as("resource body read matches updated value")
				.isEqualTo(updatedBirthdate);
	}

	@Test
	default void testCreate_delete_readById_throwsException() {
		// given a patient resource
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate("1970-02-14"));
		IRepository repository = getRepository();
		MethodOutcome createOutcome = repository.create(patient);
		IIdType patientId = createOutcome.getId().toVersionless();

		// when deleted
		var outcome = repository.delete(patient.getClass(), patientId);

		// then - read should throw ResourceNotFoundException or ResourceGoneException
		// Repositories with history should probably throw ResourceGoneException
		// But repositories without history can't tell the difference and will throw ResourceNotFoundException
		var exception =
				assertThrows(BaseServerResponseException.class, () -> repository.read(patient.getClass(), patientId));
		assertThat(exception).isInstanceOfAny(ResourceNotFoundException.class, ResourceGoneException.class);
	}

	@Test
	default void testDelete_noCreate_returnsOutcome() {
		// given
		IRepository repository = getRepository();
		var patientClass = getTestDataBuilder().buildPatient().getClass();

		// when
		var outcome = repository.delete(patientClass, new IdDt("Patient/123"));

		// then
		assertThat(outcome).isNotNull();
	}

	/** Implementors of this test template must provide a RepositoryTestSupport instance */
	RepositoryTestSupport getRepositoryTestSupport();

	record RepositoryTestSupport(IRepository repository) {
		@Nonnull
		public FhirTerser getFhirTerser() {
			return getFhirContext().newTerser();
		}

		@Nonnull
		public FhirContext getFhirContext() {
			return repository().fhirContext();
		}

		@Nonnull
		private RepositoryTestDataBuilder getRepositoryTestDataBuilder() {
			return RepositoryTestDataBuilder.forRepository(repository());
		}
	}

	private IRepository getRepository() {
		return getRepositoryTestSupport().repository();
	}

	private ITestDataBuilder getTestDataBuilder() {
		return getRepositoryTestSupport().getRepositoryTestDataBuilder();
	}

	private FhirTerser getTerser() {
		return getRepositoryTestSupport().getFhirTerser();
	}
}
