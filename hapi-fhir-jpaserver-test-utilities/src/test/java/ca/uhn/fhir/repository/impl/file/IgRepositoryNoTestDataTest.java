package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Measure;
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

/**
 * This set of tests ensures that we can create new directories as needed if
 * they don't exist ahead of time
 */
class IgRepositoryNoTestDataTest {

    private static IRepository repository;

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs/noTestData", tempDir);
        repository = new IgRepository(FhirContext.forR4Cached(), tempDir);
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
    void createAndDeleteMeasure() {
        var measure = new Measure();
        measure.setId("new-measure");
        var o = repository.create(measure);
        var created = repository.read(Measure.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("resources/measure/new-measure.json");
        assertTrue(Files.exists(loc));

        repository.delete(Measure.class, created.getIdElement());
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
    void createAndDeleteCondition() {
        var p = new Condition();
        p.setId("new-condition");
        var o = repository.create(p);
        var created = repository.read(Condition.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("tests/condition/new-condition.json");
        assertTrue(Files.exists(loc));

        repository.delete(Condition.class, created.getIdElement());
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
    void createAndDeleteCodeSystem() {
        var c = new CodeSystem();
        c.setId("new-codesystem");
        var o = repository.create(c);
        var created = repository.read(CodeSystem.class, o.getId());
        assertNotNull(created);

        var loc = tempDir.resolve("vocabulary/codesystem/new-codesystem.json");
        assertTrue(Files.exists(loc));

        repository.delete(CodeSystem.class, created.getIdElement());
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
}
