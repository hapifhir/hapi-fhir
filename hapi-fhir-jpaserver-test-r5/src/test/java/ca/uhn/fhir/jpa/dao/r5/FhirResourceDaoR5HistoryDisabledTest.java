package ca.uhn.fhir.jpa.dao.r5;

import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR5HistoryDisabledTest extends BaseJpaR5Test {

    @BeforeEach
    public void beforeEach() {
        myStorageSettings.setResourceDbHistoryEnabled(false);
    }

    @AfterEach
    public void afterEach() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setResourceDbHistoryEnabled(defaults.isResourceDbHistoryEnabled());
		myStorageSettings.setTagStorageMode(defaults.getTagStorageMode());
		myStorageSettings.setStoreMetaSourceInformation(defaults.getStoreMetaSourceInformation());
	}

	@Test
	public void testPatch() {
		// Setup
		Patient p = new Patient();
		p.setActive(true);
		IIdType id1 = myPatientDao.create(p, mySrd).getId();

		// Test
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));
		IIdType id2 = myPatientDao.patch(id1, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd).getId();

		// Verify
		runInTransaction(() -> assertEquals(1, myResourceHistoryTableDao.count()));
		assertEquals("2", id2.getVersionIdPart());
		assertDoesNotThrow(() -> myPatientDao.read(id2, mySrd));
		assertDoesNotThrow(() -> myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd));
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> myPatientDao.read(id2.withVersion("1"), mySrd));

		p = myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd);
		assertFalse(p.getActive());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());

		p = myPatientDao.read(id2.withVersion("2"), mySrd);
		assertFalse(p.getActive());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());
	}


	@Test
    public void testUpdate() {
        // Setup
        Patient p = new Patient();
        p.setActive(true);
        IIdType id1 = myPatientDao.create(p, mySrd).getId();

        // Test
        p = new Patient();
        p.setId(id1);
        p.addIdentifier().setValue("foo");
        IIdType id2 = myPatientDao.update(p, mySrd).getId();

        // Verify
        runInTransaction(() -> assertEquals(1, myResourceHistoryTableDao.count()));
		assertEquals("2", id2.getVersionIdPart());
        assertDoesNotThrow(() -> myPatientDao.read(id2, mySrd));
        assertDoesNotThrow(() -> myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd));
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> myPatientDao.read(id2.withVersion("1"), mySrd));

        p = myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());

        p = myPatientDao.read(id2.withVersion("2"), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());
    }

	@Test
    public void testUpdate_InTransaction() {
        // Setup
        Patient p = new Patient();
        p.setActive(true);
        IIdType id1 = myPatientDao.create(p, mySrd).getId();

        // Test
        p = new Patient();
        p.setId(id1);
        p.addIdentifier().setValue("foo");
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(p);
		Bundle outcome = mySystemDao.transaction(mySrd, bb.getBundleTyped());
        IIdType id2 = new IdType(outcome.getEntry().get(0).getResponse().getLocation());

        // Verify
        runInTransaction(() -> assertEquals(1, myResourceHistoryTableDao.count()));
		assertEquals("2", id2.getVersionIdPart());
        assertDoesNotThrow(() -> myPatientDao.read(id2, mySrd));
        assertDoesNotThrow(() -> myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd));
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> myPatientDao.read(id2.withVersion("1"), mySrd));

        p = myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());

        p = myPatientDao.read(id2.withVersion("2"), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());
    }

    @Test
    public void testUpdate_CurrentVersionWasExpunged() {
        // Setup
        Patient p = new Patient();
        p.setActive(true);
        IIdType id1 = myPatientDao.create(p, mySrd).getId();
        runInTransaction(() -> myResourceHistoryTableDao.deleteAll());

        // Test
        p = new Patient();
        p.setId(id1);
        p.addIdentifier().setValue("foo");
        IIdType id2 = myPatientDao.update(p, mySrd).getId();

        // Verify
        runInTransaction(() -> assertEquals(1, myResourceHistoryTableDao.count()));
		assertEquals("2", id2.getVersionIdPart());
        assertDoesNotThrow(() -> myPatientDao.read(id2, mySrd));
        assertDoesNotThrow(() -> myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd));
		assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> myPatientDao.read(id2.withVersion("1"), mySrd));

        p = myPatientDao.read(id2.toUnqualifiedVersionless(), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());

        p = myPatientDao.read(id2.withVersion("2"), mySrd);
		assertEquals("foo", p.getIdentifier().get(0).getValue());
		assertEquals("2", p.getIdElement().getVersionIdPart());
		assertEquals("2", p.getMeta().getVersionId());
    }

    @Test
    public void testUpdate_VersionedTagsMode_TagsAreCarriedForward() {
        // Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.VERSIONED);
        Patient p = new Patient();
        p.getMeta().addTag().setSystem("http://foo").setCode("bar1");
        p.getMeta().addTag().setSystem("http://foo").setCode("bar2");
        p.setActive(true);
        IIdType id1 = myPatientDao.create(p, mySrd).getId();
		runInTransaction(()-> {
            assertEquals(2, myResourceTagDao.count());
            assertEquals(2, myResourceHistoryTagDao.count());
        });

        // Test
        p = new Patient();
        p.setId(id1);
        p.getMeta().addTag().setSystem("http://foo").setCode("bar3");
        p.addIdentifier().setValue("foo");
        DaoMethodOutcome outcome = myPatientDao.update(p, mySrd);

			// Verify
		assertThat(toTagTokens(outcome.getResource())).containsExactlyInAnyOrder("http://foo|bar1", "http://foo|bar2", "http://foo|bar3");

		p = myPatientDao.read(outcome.getId(), mySrd);
			assertThat(toTagTokens(p)).containsExactlyInAnyOrder("http://foo|bar1", "http://foo|bar2", "http://foo|bar3");
		ourLog.info("Tag tokens: {}", toTagTokens(p));
		runInTransaction(()-> {
			assertEquals(3, myResourceTagDao.count());
            assertEquals(3, myResourceHistoryTagDao.count());
        });
    }

	@Test
	public void testUpdate_VersionedTagsMode_TagsCanBeDeleted() {
		// Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.VERSIONED);
		Patient p = new Patient();
		p.getMeta().addTag().setSystem("http://foo").setCode("bar1");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar2");
		p.setActive(true);
		IIdType id1 = myPatientDao.create(p, mySrd).getId();
		runInTransaction(()-> {
			assertEquals(2, myResourceTagDao.count());
			assertEquals(2, myResourceHistoryTagDao.count());
		});

		// Test
		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar2");
		myPatientDao.metaDeleteOperation(id1.toVersionless(), meta, mySrd);

		// Verify
		p = myPatientDao.read(id1.toVersionless(), mySrd);
		assertThat(toTagTokens(p)).containsExactlyInAnyOrder("http://foo|bar1");
		ourLog.info("Tag tokens: {}", toTagTokens(p));
		runInTransaction(()-> {
			assertEquals(1, myResourceTagDao.count());
			assertEquals(1, myResourceHistoryTagDao.count());
		});
	}

	@Test
	public void testUpdate_NonVersionedTagsMode_TagsAreCarriedForward() {
		// Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		Patient p = new Patient();
		p.getMeta().addTag().setSystem("http://foo").setCode("bar1");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar2");
		p.setActive(true);
		IIdType id1 = myPatientDao.create(p, mySrd).getId();
		runInTransaction(()-> {
			assertEquals(2, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
		});

		// Test
		p = new Patient();
		p.setId(id1);
		p.getMeta().addTag().setSystem("http://foo").setCode("bar3");
		p.addIdentifier().setValue("foo");
		DaoMethodOutcome outcome = myPatientDao.update(p, mySrd);

		// Verify
		assertThat(toTagTokens(outcome.getResource())).containsExactlyInAnyOrder("http://foo|bar1", "http://foo|bar2", "http://foo|bar3");

		p = myPatientDao.read(outcome.getId(), mySrd);
		assertThat(toTagTokens(p)).containsExactlyInAnyOrder("http://foo|bar1", "http://foo|bar2", "http://foo|bar3");
		ourLog.info("Tag tokens: {}", toTagTokens(p));
		runInTransaction(()-> {
			assertEquals(3, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
		});
	}

	@Test
	public void testUpdate_NonVersionedTagsMode_TagsCanBeDeleted() {
		// Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		Patient p = new Patient();
		p.getMeta().addTag().setSystem("http://foo").setCode("bar1");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar2");
		p.setActive(true);
		IIdType id1 = myPatientDao.create(p, mySrd).getId();
		runInTransaction(()-> {
			assertEquals(2, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
		});

		// Test
		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar2");
		myPatientDao.metaDeleteOperation(id1.toVersionless(), meta, mySrd);

		// Verify
		p = myPatientDao.read(id1.toVersionless(), mySrd);
		assertThat(toTagTokens(p)).containsExactlyInAnyOrder("http://foo|bar1");
		ourLog.info("Tag tokens: {}", toTagTokens(p));
		runInTransaction(()-> {
			assertEquals(1, myResourceTagDao.count());
			assertEquals(0, myResourceHistoryTagDao.count());
		});
	}

	@Test
	public void testUpdate_ProvenanceIsUpdatedInPlace() {
		// Setup
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
		Patient p = new Patient();
		p.getMeta().setSource("source-1");
		p.setActive(true);
		when(mySrd.getRequestId()).thenReturn("request-id-1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId();
		runInTransaction(()-> assertEquals(1, myResourceHistoryProvenanceDao.count()));

		// Test
		p = new Patient();
		p.setId(id1);
		p.addIdentifier().setValue("foo");
		p.getMeta().setSource("source-2");
		p.setActive(true);
		when(mySrd.getRequestId()).thenReturn("request-id-2");
		DaoMethodOutcome outcome = myPatientDao.update(p, mySrd);

		// Verify
		assertEquals("source-2#request-id-2", ((Patient) outcome.getResource()).getMeta().getSource());
		p = myPatientDao.read(outcome.getId(), mySrd);
		assertEquals("source-2#request-id-2", p.getMeta().getSource());
		runInTransaction(()-> assertEquals(1, myResourceHistoryProvenanceDao.count()));
	}

	@Nonnull
	private static List<String> toTagTokens(IBaseResource resource) {
		List<String> tags = resource.getMeta()
				.getTag()
				.stream()
				.map(t -> t.getSystem() + "|" + t.getCode())
				.toList();
		return tags;
	}


}
