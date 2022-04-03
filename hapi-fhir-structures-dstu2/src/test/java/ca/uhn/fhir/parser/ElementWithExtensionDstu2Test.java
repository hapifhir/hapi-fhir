package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Sébastien Rivière 12/04/2017
 */
public class ElementWithExtensionDstu2Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ca.uhn.fhir.parser.ElementWithExtensionDstu2Test.class);
    private final FhirContext ctx = FhirContext.forDstu2();

    @AfterAll
    public static void afterClassClearContext() {
        TestUtil.randomizeLocaleAndTimezone();
    }

    @Test
    public void testExtensionOnPrimitiveExtensionJson() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        final HumanNameDt name = patient.getNameFirstRep();
        name.addFamily(new StringDt("family"));
        name.getFamilyFirstRep().addUndeclaredExtension(new ExtensionDt(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK")));

        final StringDt stringExt = new StringDt();
        stringExt.setValue("myStringExt");
        stringExt.addUndeclaredExtension(new ExtensionDt(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK")));
        final ExtensionDt ext = new ExtensionDt();
        ext.setValue(stringExt);
        ext.setUrl("/myExt");
        patient.addUndeclaredExtension(ext);


        patient.setPetName(new StringDt("myPet"));
        patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));

        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getName().get(0).getFamilyFirstRep().getUndeclaredExtensions().size());
        assertEquals(1, ((StringDt) patient.getUndeclaredExtensionsByUrl("/myExt").get(0).getValue()).getUndeclaredExtensions().size());
        assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnPrimitiveExtensionWithNullValueJson() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
    }


    @Test
    public void testExtensionOnPrimitiveExtensionXml() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");

        final HumanNameDt name = patient.getNameFirstRep();
        name.addFamily(new StringDt("family"));
        name.getFamilyFirstRep().addUndeclaredExtension(new ExtensionDt(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK")));

        final StringDt stringExt = new StringDt();
        stringExt.setValue("myStringExt");
        stringExt.addUndeclaredExtension(new ExtensionDt(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK")));
        final ExtensionDt ext = new ExtensionDt();
        ext.setValue(stringExt);
        ext.setUrl("/myExt");
        patient.addUndeclaredExtension(ext);


        patient.setPetName(new StringDt("myPet"));
        patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getName().get(0).getFamilyFirstRep().getUndeclaredExtensions().size());
        assertEquals(1, ((StringDt) patient.getUndeclaredExtensionsByUrl("/myExt").get(0).getValue()).getUndeclaredExtensions().size());
        assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnPrimitiveExtensionWithNullValueXml() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getPetName().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getPetName().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnIDDatatypeJson() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getId().getUndeclaredExtensions().size());
    }



    @Test
    public void testExtensionOnIDDatatypeXml() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getId().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnIDDatatypeExtensionJson() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.setCustomId(new IdDt("3"));
        patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnIDDatatypeExtensionNullValueJson() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnIDDatatypeExtensionXml() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.setCustomId(new IdDt("4"));
        patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
    }

    @Test
    public void testExtensionOnIDDatatypeExtensionNullValueXml() {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getCustomId().addUndeclaredExtension(false, "http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringDt("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getCustomId().getUndeclaredExtensions().size());
    }
}

