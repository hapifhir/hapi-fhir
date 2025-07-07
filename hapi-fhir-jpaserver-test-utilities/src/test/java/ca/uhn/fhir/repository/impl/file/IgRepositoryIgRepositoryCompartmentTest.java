package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;
import org.opencds.cqf.fhir.test.Resources;
import org.opencds.cqf.fhir.utility.search.Searches;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IgRepositoryIgRepositoryCompartmentTest {

    private static IRepository repository;

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs/compartment", tempDir);
        repository = new IgRepository(FhirContext.forR4Cached(), tempDir);
    }

    @Test
    void readLibrary() {
        var id = IdUtils.newId(Library.class, "123");
        var lib = repository.read(Library.class, id);
        assertNotNull(lib);
        assertEquals(id.getIdPart(), lib.getIdElement().getIdPart());
    }

    @Test
    void readLibraryNotExists() {
        var id = IdUtils.newId(Library.class, "DoesNotExist");
        assertThrows(ResourceNotFoundException.class, () -> repository.read(Library.class, id));
    }

    @Test
    void searchLibrary() {
        var libs = repository.search(Bundle.class, Library.class, Searches.ALL);

        assertNotNull(libs);
        assertEquals(2, libs.getEntry().size());
    }

    @Test
    void searchLibraryNotExists() {
        var libs = repository.search(Bundle.class, Library.class, Searches.byUrl("not-exists"));
        assertNotNull(libs);
        assertEquals(0, libs.getEntry().size());
    }

    @Test
    void readPatientNoCompartment() {
        var id = IdUtils.newId(Patient.class, "123");
        assertThrows(ResourceNotFoundException.class, () -> repository.read(Patient.class, id));
    }

    @Test
    void readPatient() {
        var id = IdUtils.newId(Patient.class, "123");
        var p = repository.read(Patient.class, id, Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/123"));

        assertNotNull(p);
        assertEquals(id.getIdPart(), p.getIdElement().getIdPart());
    }

    @Test
    void searchEncounterNoCompartment() {
        var encounters = repository.search(Bundle.class, Encounter.class, Searches.ALL);
        assertNotNull(encounters);
        assertEquals(0, encounters.getEntry().size());
    }

    @Test
    void searchEncounter() {
        var encounters = repository.search(
                Bundle.class,
                Encounter.class,
                Searches.ALL,
                Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/123"));
        assertNotNull(encounters);
        assertEquals(1, encounters.getEntry().size());
    }

    @Test
    void readValueSetNoCompartment() {
        var id = IdUtils.newId(ValueSet.class, "456");
        var vs = repository.read(ValueSet.class, id);

        assertNotNull(vs);
        assertEquals(vs.getIdPart(), vs.getIdElement().getIdPart());
    }

    // Terminology resources are not in compartments
    @Test
    void readValueSet() {
        var id = IdUtils.newId(ValueSet.class, "456");
        var vs = repository.read(ValueSet.class, id, Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/123"));

        assertNotNull(vs);
        assertEquals(vs.getIdPart(), vs.getIdElement().getIdPart());
    }

    @Test
    void searchValueSet() {
        var sets = repository.search(Bundle.class, ValueSet.class, Searches.byUrl("example.com/ValueSet/456"));
        assertNotNull(sets);
        assertEquals(1, sets.getEntry().size());
    }

    @Test
    void createAndDeleteLibrary() {
        var lib = new Library();
        lib.setId("new-library");
        var o = repository.create(lib);
        var created = repository.read(Library.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("resources/library/new-library.json");
        assertTrue(Files.exists(loc));

        repository.delete(Library.class, created.getIdElement());
        assertFalse(Files.exists(loc));
    }

    @Test
    void createAndDeletePatient() {
        var p = new Patient();
        p.setId("new-patient");
        var header = Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/new-patient");
        var o = repository.create(p, header);
        var created = repository.read(Patient.class, o.getId(), header);
        assertNotNull(created);

        var loc = tempDir.resolve("tests/patient/new-patient/patient/new-patient.json");
        assertTrue(Files.exists(loc));

        repository.delete(Patient.class, created.getIdElement(), header);
        assertFalse(Files.exists(loc));
    }

    @Test
    void createAndDeleteValueSet() {
        var v = new ValueSet();
        v.setId("new-valueset");
        var o = repository.create(v);
        var created = repository.read(ValueSet.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("vocabulary/valueset/new-valueset.json");
        assertTrue(Files.exists(loc));

        repository.delete(ValueSet.class, created.getIdElement());
        assertFalse(Files.exists(loc));
    }

    @Test
    void updatePatient() {
        var id = IdUtils.newId(Patient.class, "123");
        var p = repository.read(Patient.class, id, Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/123"));
        assertFalse(p.hasActive());

        p.setActive(true);
        repository.update(p);

        var updated = repository.read(Patient.class, id, Map.of(IgRepository.FHIR_COMPARTMENT_HEADER, "Patient/123"));
        assertTrue(updated.hasActive());
        assertTrue(updated.getActive());
    }

    @Test
    void deleteNonExistentPatient() {
        var id = IdUtils.newId(Patient.class, "DoesNotExist");
        assertThrows(ResourceNotFoundException.class, () -> repository.delete(Patient.class, id));
    }

    @Test
    void searchNonExistentType() {
        var results = repository.search(Bundle.class, Encounter.class, Searches.ALL);
        assertNotNull(results);
        assertEquals(0, results.getEntry().size());
    }

    @Test
    void searchById() {
        var bundle = repository.search(Bundle.class, Library.class, Searches.byId("123"));
        assertNotNull(bundle);
        assertEquals(1, bundle.getEntry().size());
    }

    @Test
    void searchByIdNotFound() {
        var bundle = repository.search(Bundle.class, Library.class, Searches.byId("DoesNotExist"));
        assertNotNull(bundle);
        assertEquals(0, bundle.getEntry().size());
    }

    @Test
    @Order(1) // Do this test first because it puts the filesystem (temporarily) in an invalid state
    void resourceMissingWhenCacheCleared() throws IOException {
        var id = new IdType("Library", "ToDelete");
        var lib = new Library().setIdElement(id);
        var path = tempDir.resolve("resources/library/ToDelete.json");

        repository.create(lib);
        assertTrue(path.toFile().exists());

        // Read back, should exist
        lib = repository.read(Library.class, id);
        assertNotNull(lib);

        // Overwrite the file on disk.
        Files.writeString(path, "");

        // Read from cache, repo doesn't know the content is gone.
        lib = repository.read(Library.class, id);
        assertNotNull(lib);
        assertEquals("ToDelete", lib.getIdElement().getIdPart());

        ((IgRepository) repository).clearCache();

        // Try to read again, should be gone because it's not in the cache and the content is gone.
        assertThrows(ResourceNotFoundException.class, () -> repository.read(Library.class, id));

        // Clean up so that we don't affect other tests
        path.toFile().delete();
    }
}
