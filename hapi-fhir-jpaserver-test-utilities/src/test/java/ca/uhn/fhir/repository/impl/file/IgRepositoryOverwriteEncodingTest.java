package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.r4.model.Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opencds.cqf.fhir.test.Resources;
import org.opencds.cqf.fhir.utility.repository.ig.EncodingBehavior.PreserveEncoding;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IgRepositoryOverwriteEncodingTest {

    private static IRepository repository;

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs/mixedEncoding", tempDir);
        var conventions = IgConventions.autoDetect(tempDir);
        repository = new IgRepository(
                FhirContext.forR4Cached(),
                tempDir,
                conventions,
                new EncodingBehavior(EncodingEnum.XML, PreserveEncoding.OVERWRITE_WITH_PREFERRED_ENCODING),
                null);
    }

    @Test
    void readLibrary() {
        var id = IdUtils.newId(Library.class, "123");
        var lib = repository.read(Library.class, id);
        assertNotNull(lib);
        assertEquals(id.getIdPart(), lib.getIdElement().getIdPart());
    }

    @Test
    void updateLibrary() {
        var id = IdUtils.newId(Library.class, "123");
        var lib = repository.read(Library.class, id);
        assertNotNull(lib);
        assertEquals(id.getIdPart(), lib.getIdElement().getIdPart());

        lib.addAuthor().setName("Test Author");

        repository.update(lib);
        assertFalse(tempDir.resolve("resources/library/123.json").toFile().exists());
        assertTrue(tempDir.resolve("resources/library/123.xml").toFile().exists());
    }
}
