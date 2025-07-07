package ca.uhn.fhir.repository.impl.file;

import ca.uhn.fhir.repository.impl.file.IgConventions.CategoryLayout;
import ca.uhn.fhir.repository.impl.file.IgConventions.CompartmentLayout;
import ca.uhn.fhir.repository.impl.file.IgConventions.FhirTypeLayout;
import ca.uhn.fhir.repository.impl.file.IgConventions.FilenameMode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opencds.cqf.fhir.test.Resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IgConventionsTest {

    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setup() throws URISyntaxException, IOException, ClassNotFoundException {
        // This copies the sample IG to a temporary directory so that
        // we can test against an actual filesystem
        Resources.copyFromJar("/sampleIgs", tempDir);
    }


    @Test
    void autoDetectDefault() {
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(null));
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(tempDir.resolve("does_not_exist")));
    }

    @Test
    void autoDetectStandard() {
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(tempDir.resolve("directoryPerType/standard")));
    }

    @Test
    void autoDetectPrefix() {
        var config = IgConventions.autoDetect(tempDir.resolve("directoryPerType/prefixed"));
        assertEquals(FilenameMode.TYPE_AND_ID, config.filenameMode());
        assertEquals(CategoryLayout.DIRECTORY_PER_CATEGORY, config.categoryLayout());
        assertEquals(CompartmentLayout.FLAT, config.compartmentLayout());
        assertEquals(FhirTypeLayout.DIRECTORY_PER_TYPE, config.typeLayout());
    }

    @Test
    void autoDetectFlat() {
        assertEquals(IgConventions.FLAT, IgConventions.autoDetect(tempDir.resolve("flat")));
    }

    @Test
    void autoDetectFlatNoTypeNames() {
        var config = IgConventions.autoDetect(tempDir.resolve("flatNoTypeNames"));
        assertEquals(FilenameMode.ID_ONLY, config.filenameMode());
        assertEquals(CategoryLayout.FLAT, config.categoryLayout());
        assertEquals(CompartmentLayout.FLAT, config.compartmentLayout());
        assertEquals(FhirTypeLayout.FLAT, config.typeLayout());
    }

    @Test
    void autoDetectWithMisleadingFileName() {
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(tempDir.resolve("misleadingFileName")));
    }

    @Test
    void autoDetectWithEmptyContent() {
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(tempDir.resolve("emptyContent")));
    }

    @Test
    void autoDetectWithNonFhirFilename() {
        assertEquals(IgConventions.STANDARD, IgConventions.autoDetect(tempDir.resolve("nonFhirFilename")));
    }

    @Test
    void autoDetectWitCompartments() {
        var config = IgConventions.autoDetect(tempDir.resolve("compartment"));
        assertEquals(FilenameMode.ID_ONLY, config.filenameMode());
        assertEquals(CategoryLayout.DIRECTORY_PER_CATEGORY, config.categoryLayout());
        assertEquals(CompartmentLayout.DIRECTORY_PER_COMPARTMENT, config.compartmentLayout());
        assertEquals(FhirTypeLayout.DIRECTORY_PER_TYPE, config.typeLayout());
    }
}
