package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.primitive.StringDt;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bill de Beaubien on 12/20/2015.
 */
public class EmptyElementWithExtensionTest {
    @Ignore
    @Test
    public void testNullFlavor() throws Exception {
        Observation observation = new Observation();
        observation.getCode().getCoding().add(new CodingDt().setSystem("http://loinc.org").setCode("3141-9"));
        observation.getStatusElement().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        FhirContext ctx = FhirContext.forDstu2();
        IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        String xml = parser.encodeResourceToString(observation);
        observation = (Observation) parser.parseResource(xml);
        assertEquals(1, observation.getStatusElement().getUndeclaredExtensions().size());
    }
}
