package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import org.hl7.fhir.r4.model.Bundle;
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

class IgRepositoryExternalTest {

    private static IRepository repository;

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs/externalResource", tempDir);
        repository = new IgRepository(FhirContext.forR4Cached(), tempDir);
    }

    @Test
    void readValueSet() {
        var id = IdUtils.newId(ValueSet.class, "456");
        var vs = repository.read(ValueSet.class, id);

        assertNotNull(vs);
        assertEquals(vs.getIdPart(), vs.getIdElement().getIdPart());
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
