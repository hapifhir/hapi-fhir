package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Sébastien Rivière 12/04/2017
 */
public class ElementWithExtensionDstu3Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ca.uhn.fhir.parser.ElementWithExtensionDstu3Test.class);
    private final FhirContext ctx = FhirContext.forDstu3();

    @AfterAll
    public static void afterClassClearContext() {
        TestUtil.randomizeLocaleAndTimezone();
    }

    @Test
    public void testNullFlavorPrimitiveExtensionJson() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        final HumanName name = patient.getNameFirstRep();
        name.setFamily("family");
        name.getFamilyElement().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        patient.setPetName(new StringType("myPet"));
        patient.getExtensionsByUrl("/petname");
        patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        final StringType stringExt = new StringType();
        stringExt.setValue("myStringExt");
        stringExt.addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
        final Extension ext = new Extension();
        ext.setValue(stringExt);
        ext.setUrl("/myExt");
        patient.addExtension(ext);

        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getName().get(0).getFamilyElement().getExtension().size());
        assertEquals(1, patient.getExtensionsByUrl("/myExt").get(0).getValue().getExtension().size());
        assertEquals(1, patient.getPetName().getExtension().size());
    }

    @Test
    public void testNullFlavorPrimitiveExtensionNullValueJson() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getExtensionsByUrl("/petname");
        patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getPetName().getExtension().size());
    }

    @Test
    public void testNullFlavorPrimitiveExtensionXml() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        final HumanName name = patient.getNameFirstRep();
        name.setFamily("family");
        name.getFamilyElement().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        patient.setPetName(new StringType("myPet"));
        patient.getExtensionsByUrl("/petname");
        patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        final StringType stringExt = new StringType();
        stringExt.setValue("myStringExt");
        stringExt.addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
        final Extension ext = new Extension();
        ext.setValue(stringExt);
        ext.setUrl("/myExt");
        patient.addExtension(ext);

        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getName().get(0).getFamilyElement().getExtension().size());
        assertEquals(1, patient.getExtensionsByUrl("/myExt").get(0).getValue().getExtension().size());
        assertEquals(1, patient.getPetName().getExtension().size());
    }


    @Test
    public void testNullFlavorPrimitiveExtensionNullValueXml() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");

        patient.getExtensionsByUrl("/petname");
        patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getPetName().getExtension().size());
    }


    @Test
    public void testNullFlavorIDDatatypeJson() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getIdElement().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getIdElement().getExtension().size());
    }

    @Test
    public void testNullFlavorIDDatatypeXml() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.getIdElement().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getIdElement().getExtension().size());
    }

    @Test
    public void testNullFlavorExtensionIDDatatypeJson() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        patient.setCustomId(new IdType(("4")));
        patient.getCustomId().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
        final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
        final String json = parser.encodeResourceToString(patient);

        ourLog.info(json);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
        assertEquals(1, patient.getCustomId().getExtension().size());
    }

    @Test
    public void testNullFlavorExtensionIDDatatypeXml() throws Exception {
        MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
        patient.setId("1");
        final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
        patient.setCustomId(new IdType(("4")));
        patient.getCustomId().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));

        final String xml = parser.encodeResourceToString(patient);

        ourLog.info(xml);

        patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
        assertEquals(1, patient.getCustomId().getExtension().size());
    }

	@Test
    public void testExtensionOnResourceIdXml(){
		Patient p = new Patient();
		p.setActive(true);
		p.getIdElement().setValue("123");
		p.getIdElement().addExtension().setUrl("http://foo").setValue(new StringType("FOO"));

		IParser parser = ctx.newXmlParser();
		String encoded = parser.encodeResourceToString(p);
		assertThat(encoded, containsString("http://foo"));
		assertThat(encoded, containsString("FOO"));

		p = (Patient) parser.parseResource(encoded);
		assertEquals("Patient/123", p.getId());
		Extension ex = p.getIdElement().getExtension().get(0);
		assertEquals("http://foo", ex.getUrl());
		assertEquals("FOO", ex.getValueAsPrimitive().getValueAsString());


	 }

	@Test
	public void testExtensionOnResourceIdJson(){
		Patient p = new Patient();
		p.setActive(true);
		p.getIdElement().setValue("123");
		p.getIdElement().addExtension().setUrl("http://foo").setValue(new StringType("FOO"));

		IParser parser = ctx.newJsonParser();
		String encoded = parser.encodeResourceToString(p);
		assertThat(encoded, containsString("http://foo"));
		assertThat(encoded, containsString("FOO"));

		p = (Patient) parser.parseResource(encoded);
		assertEquals("Patient/123", p.getId());
		Extension ex = p.getIdElement().getExtension().get(0);
		assertEquals("http://foo", ex.getUrl());
		assertEquals("FOO", ex.getValueAsPrimitive().getValueAsString());


	}

}

