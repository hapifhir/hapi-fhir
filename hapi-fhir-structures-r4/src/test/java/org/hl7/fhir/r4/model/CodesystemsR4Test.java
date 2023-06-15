package org.hl7.fhir.r4.model;

import java.util.*;

import org.junit.jupiter.api.*;

public class CodesystemsR4Test {

    @Test
    public void testCodesystemsPresent() throws ClassNotFoundException {
        Class.forName(org.hl7.fhir.r4.model.codesystems.W3cProvenanceActivityType.class.getName());
    }
}
