package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionDstu3Test {

    @Test
    public void testVersion() {
        assertEquals("3.0.2", FhirVersionEnum.DSTU3.getFhirVersionString());
    }
}
