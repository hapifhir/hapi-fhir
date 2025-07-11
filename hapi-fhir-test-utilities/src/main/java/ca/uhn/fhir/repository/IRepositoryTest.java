package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.RepositoryTestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.bundle.BundleResponseEntryParts;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_201_CREATED;
import static ca.uhn.fhir.util.ParametersUtil.addParameterToParameters;
import static ca.uhn.fhir.util.ParametersUtil.addPart;
import static ca.uhn.fhir.util.ParametersUtil.addPartCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Generic test of repository functionality */
@SuppressWarnings({
	"java:S5960" // this is a test jar
	,
	"java:S1199" // this is a test jar
})
public interface IRepositoryTest {
	Logger ourLog = LoggerFactory.getLogger(IRepositoryTest.class);
	String BIRTHDATE1 = "1970-02-14";
	String BIRTHDATE2 = "1975-01-01";

	@Test
	default void testCreate_readById_contentsPersisted() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate(BIRTHDATE1));
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
				.isEqualTo(BIRTHDATE1);
	}

	@Test
	default void testCreateClientAssignedId_readBySameId_findsResource() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withId("pat123"), b.withBirthdate(BIRTHDATE1));
		IRepository repository = getRepository();

		// when
		MethodOutcome methodOutcome = repository.update(patient);
		IBaseResource read = repository.read(patient.getClass(), methodOutcome.getId());

		// then
		assertThat(read).isNotNull();
		assertThat(getTerser().getSinglePrimitiveValueOrNull(read, "birthDate")).isEqualTo(BIRTHDATE1);
		assertThat(read.getIdElement().getIdPart()).isEqualTo("pat123");
	}

	@Test
	default void testCreate_update_readById_verifyUpdatedContents() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate(BIRTHDATE1));
		IRepository repository = getRepository();

		// when - create
		MethodOutcome createOutcome = repository.create(patient);
		IIdType patientId = createOutcome.getId().toVersionless();
		assertThat(createOutcome.getCreated()).isTrue();
		assertThat(createOutcome.getResponseStatusCode()).isEqualTo(STATUS_HTTP_201_CREATED);
		assertThat(createOutcome.getResource()).isNotNull();

		// update with different birthdate
		var updatedPatient = b.buildPatient(b.withId(patientId), b.withBirthdate(BIRTHDATE2));
		var updateOutcome = repository.update(updatedPatient);
		assertThat(updateOutcome.getId().toVersionless().getValueAsString()).isEqualTo(patientId.getValueAsString());
		assertThat(updateOutcome.getCreated()).isFalse();
		assertThat(updateOutcome.getResponseStatusCode()).isEqualTo(Constants.STATUS_HTTP_200_OK);

		// read
		IBaseResource read = repository.read(patient.getClass(), patientId);

		// then
		assertThat(read)
				.isNotNull()
				.extracting(p -> getTerser().getSinglePrimitiveValueOrNull(p, "birthDate"))
				.as("resource body read matches updated value")
				.isEqualTo(BIRTHDATE2);
	}

	@Test
	default void testCreate_clientAssignedId_outcome() {
		// given
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withId("pat123"), b.withBirthdate(BIRTHDATE1));
		IRepository repository = getRepository();

		// when
		var updateOutcome = repository.update(patient);

		// then
		assertThat(updateOutcome.getId().toUnqualifiedVersionless().getValueAsString())
				.isEqualTo("Patient/pat123");
		assertThat(updateOutcome.getCreated()).isTrue();
		assertThat(updateOutcome.getResponseStatusCode()).isEqualTo(STATUS_HTTP_201_CREATED);
	}

	@Test
	default void testCreate_delete_readById_throwsException() {
		// given a patient resource
		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate(BIRTHDATE1));
		IRepository repository = getRepository();
		MethodOutcome createOutcome = repository.create(patient);
		IIdType patientId = createOutcome.getId().toVersionless();

		// when deleted
		repository.delete(patient.getClass(), patientId);

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

	default boolean isPatchSupported() {
		// SOMEDAY: ideally this would come from the repository capabilities
		return true;
	}

	@Test
	@EnabledIf("isPatchSupported")
	default void testPatch_changesValue() {
		// given
		var repository = getRepository();
		var fhirContext = getRepository().fhirContext();
		IBaseParameters parameters = ParametersUtil.newInstance(fhirContext);
		var operation = addParameterToParameters(fhirContext, parameters, "operation");
		addPartCode(fhirContext, operation, "type", "replace");
		addPartCode(fhirContext, operation, "path", "Patient.birthDate");
		addPart(fhirContext, operation, "value", new DateType(BIRTHDATE2));

		var b = getTestDataBuilder();
		var patient = b.buildPatient(b.withBirthdate(BIRTHDATE1));
		MethodOutcome createOutcome = repository.create(patient);
		IIdType patientId = createOutcome.getId().toVersionless();

		// when
		repository.patch(patientId, parameters);

		// then
		IBaseResource read = repository.read(patient.getClass(), patientId);
		assertThat(read)
				.isNotNull()
				.extracting(p -> getTerser().getSinglePrimitiveValueOrNull(p, "birthDate"))
				.as("resource body read matches updated value")
				.isEqualTo(BIRTHDATE2);
	}

	@Test
	default void testSimpleTxBundle() {
		// given
		var repository = getRepository();
		var fhirContext = getRepository().fhirContext();
		var b = getTestDataBuilder();
		var patient = b.buildPatient();
		var patientWithId = b.buildPatient(b.withId("abc"));
		BundleBuilder bundleBuilder = new BundleBuilder(fhirContext);

		bundleBuilder.addTransactionCreateEntry(patient, "urn:uuid:0198234701923");
		bundleBuilder.addTransactionUpdateEntry(patientWithId);
		IBaseBundle bundle = bundleBuilder.getBundle();

		// when
		IBaseBundle resultBundle = repository.transaction(bundle);

		// then
		assertThat(resultBundle).isNotNull();
		assertThat(BundleUtil.getBundleType(fhirContext, resultBundle))
				.isEqualTo(BundleUtil.BUNDLE_TYPE_TRANSACTION_RESPONSE);

		List<BundleResponseEntryParts> bundleResponseEntryParts = BundleUtil.toListOfEntries(
				fhirContext, resultBundle, BundleResponseEntryParts.getConverter(fhirContext));
		assertThat(bundleResponseEntryParts).hasSize(2);
		{
			BundleResponseEntryParts createResponseEntry = bundleResponseEntryParts.get(0);

			assertThat(createResponseEntry.fullUrl())
					.satisfiesAnyOf(Assertions::assertNull, fullUrl -> assertThat(fullUrl)
							.isEqualTo("urn:uuid:0198234701923"));
			assertThat(createResponseEntry.responseStatus()).startsWith("" + STATUS_HTTP_201_CREATED);
			assertThat(createResponseEntry.responseLocation()).isNotBlank();
		}
		{
			BundleResponseEntryParts updateResponseEntry = bundleResponseEntryParts.get(1);

			assertThat(updateResponseEntry.fullUrl())
					.satisfiesAnyOf(Assertions::assertNull, fullUrl -> assertThat(fullUrl)
							.contains("Patient/abc"));
			assertThat(updateResponseEntry.responseStatus()).startsWith("" + STATUS_HTTP_201_CREATED);
			assertThat(updateResponseEntry.responseLocation()).isNotBlank();
		}
	}

	// fixme add test for search all of type.

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
