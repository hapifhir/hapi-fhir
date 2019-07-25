package org.hl7.fhir.dstu3.elementmodel;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.exceptions.DefinitionException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by axemj on 14/07/2017.
 */
public class PropertyDstu3Test {

    private static final FhirContext ourCtx = FhirContext.forDstu3();
    private Property property;
    private StructureDefinition sd;
    private HapiWorkerContext workerContext;

    @Before
    public void setUp() throws IOException {
        final String sdString = IOUtils.toString(getClass().getResourceAsStream("/customPatientSd.xml"), StandardCharsets.UTF_8);
        final IParser parser = ourCtx.newXmlParser();
        sd = parser.parseResource(StructureDefinition.class, sdString);
        workerContext = new HapiWorkerContext(ourCtx, new DefaultProfileValidationSupport());
    }

    @Test
    public void getChildPropertiesPrimitiveTest() throws DefinitionException {
        final ElementDefinition ed = sd.getSnapshot().getElement().get(1);
        property = new Property(workerContext, ed, sd);
        final List<Property> result = property.getChildProperties("id", null);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertEquals("id.id", result.get(0).getDefinition().getPath());
    }

    @Test
    public void getChildPropertiesOnlyExtensionElementTest() throws DefinitionException {
        final ElementDefinition ed = sd.getSnapshot().getElement().get(23);
        property = new Property(workerContext, ed, sd);
        final List<Property> result = property.getChildProperties("birthdate", null);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertEquals("date.id", result.get(0).getDefinition().getPath());
    }

    @Test(expected = Error.class)
    public void getChildPropertiesErrorTest() throws DefinitionException {
        final ElementDefinition ed = sd.getSnapshot().getElement().get(7);
        property = new Property(workerContext, ed, sd);
        property.getChildProperties("birthdate", null);
    }
}
