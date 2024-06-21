package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
			assertThat(patient.getName().get(0).getFamilyFirstRep().getUndeclaredExtensions()).hasSize(1);
			assertThat(((StringDt) patient.getUndeclaredExtensionsByUrl("/myExt").get(0).getValue()).getUndeclaredExtensions()).hasSize(1);
			assertThat(patient.getPetName().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getPetName().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getName().get(0).getFamilyFirstRep().getUndeclaredExtensions()).hasSize(1);
			assertThat(((StringDt) patient.getUndeclaredExtensionsByUrl("/myExt").get(0).getValue()).getUndeclaredExtensions()).hasSize(1);
			assertThat(patient.getPetName().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getPetName().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getId().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getId().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getCustomId().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getCustomId().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getCustomId().getUndeclaredExtensions()).hasSize(1);
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
			assertThat(patient.getCustomId().getUndeclaredExtensions()).hasSize(1);
    }
}

