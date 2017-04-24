package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu3.model.StringType;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by Sébastien Rivière 12/04/2017
 */
public class ElementWithExtensionDstu3Test {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ca.uhn.fhir.parser.ElementWithExtensionDstu3Test.class);
  private static final FhirContext ctx = FhirContext.forDstu3();

  @AfterClass
  public static void afterClassClearContext() {
    TestUtil.clearAllStaticFieldsForUnitTest();
  }

  @Test
  @Ignore
  public void testNullFlavorPrimitiveExtensionJson() throws Exception {
    MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
    patient.setId("1");
    patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
    final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
    final String json = parser.encodeResourceToString(patient);

    ourLog.info(json);

    patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
    assertEquals(1, patient.getPetName().getExtension().size());
  }

  @Test
  @Ignore
  public void testNullFlavorPrimitiveExtensionXml() throws Exception {
    MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
    patient.setId("1");
    patient.getPetName().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
    final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
    final String xml = parser.encodeResourceToString(patient);

    ourLog.info(xml);

    patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
    assertEquals(1, patient.getPetName().getExtension().size());
  }

  @Test
  @Ignore
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
  @Ignore
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
  @Ignore
  public void testNullFlavorExtensionIDDatatypeJson() throws Exception {
    MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
    patient.setId("1");
    patient.getCustomId().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
    final IParser parser = ctx.newJsonParser().setPrettyPrint(true);
    final String json = parser.encodeResourceToString(patient);

    ourLog.info(json);

    patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, json);
    assertEquals(1, patient.getCustomId().getExtension().size());
  }

  @Test
  @Ignore
  public void testNullFlavorExtensionIDDatatypeXml() throws Exception {
    MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
    patient.setId("1");
    patient.getCustomId().addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-nullFlavor", new StringType("UNK"));
    final IParser parser = ctx.newXmlParser().setPrettyPrint(true);
    final String xml = parser.encodeResourceToString(patient);

    ourLog.info(xml);

    patient = parser.parseResource(MyPatientWithCustomUrlExtension.class, xml);
    assertEquals(1, patient.getCustomId().getExtension().size());
  }
}

