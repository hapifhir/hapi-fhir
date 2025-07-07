package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opencds.cqf.fhir.test.Resources;
import org.opencds.cqf.fhir.utility.search.Searches;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IgRepositoryMixedEncodingTest {

    private static IRepository repository;

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs/mixedEncoding", tempDir);
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
        assertEquals(1, libs.getEntry().size());
    }

    @Test
    void searchLibraryWithFilter() {
        var libs = repository.search(Bundle.class, Library.class, Searches.byUrl("http://example.com/Library/123"));

        assertNotNull(libs);
        assertEquals(1, libs.getEntry().size());
    }

    @Test
    void searchLibraryNotExists() {
        var libs = repository.search(Bundle.class, Library.class, Searches.byUrl("not-exists"));
        assertNotNull(libs);
        assertEquals(0, libs.getEntry().size());
    }

    @Test
    void readValueSet() {
        var id = IdUtils.newId(ValueSet.class, "456");
        var vs = repository.read(ValueSet.class, id);

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
    void searchWithExternalValueSet() {
        var sets = repository.search(Bundle.class, ValueSet.class, Searches.ALL);
        assertNotNull(sets);
        assertEquals(2, sets.getEntry().size());
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
        var o = repository.create(p);
        var created = repository.read(Patient.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("tests/patient/new-patient.json");
        assertTrue(Files.exists(loc));

        repository.delete(Patient.class, created.getIdElement());
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
    void readExternalValueSet() {
        var id = IdUtils.newId(ValueSet.class, "789");
        var vs = repository.read(ValueSet.class, id);
        assertNotNull(vs);
        assertEquals(vs.getIdPart(), vs.getIdElement().getIdPart());

        // Should be tagged with its source path
        var path = (Path) vs.getUserData(IgRepository.SOURCE_PATH_TAG);
        assertNotNull(path);
        assertTrue(path.toFile().exists());
        assertTrue(path.toString().contains("external"));
    }

    @Test
    void searchExternalValueSet() {
        var sets = repository.search(Bundle.class, ValueSet.class, Searches.byUrl("example.com/ValueSet/789"));
        assertNotNull(sets);
        assertEquals(1, sets.getEntry().size());
    }

    @Test
    void updateExternalValueSetFails() {
        var id = IdUtils.newId(ValueSet.class, "789");
        var vs = repository.read(ValueSet.class, id);
        assertThrows(ForbiddenOperationException.class, () -> repository.update(vs));
    }
}
