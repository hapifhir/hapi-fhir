package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.hl7.fhir.utilities.cache.ToolsVersion;
import org.junit.Before;
import org.junit.Test;

public class CDARoundTripTests {

  private SimpleWorkerContext context;

  @Before
  public void setUp() throws Exception {
    context = new SimpleWorkerContext();
    PackageCacheManager pcm = new PackageCacheManager(true, ToolsVersion.TOOLS_VERSION);
    context.loadFromPackage(pcm.loadPackageCacheLatest("hl7.fhir.core"), null, "StructureDefinition");
    context.loadFromPackage(pcm.loadPackageCacheLatest("hl7.fhir.cda"), null, "StructureDefinition");
  }

  @Test
  public void testDCI() throws FHIRFormatError, DefinitionException, FileNotFoundException, IOException, FHIRException {
    try {
    Element e = Manager.parse(context, new FileInputStream("C:\\work\\org.hl7.fhir.us\\ccda-to-fhir-maps\\cda\\IAT2-Discharge_Summary-DCI.xml"), FhirFormat.XML);
    Manager.compose(context, e, new FileOutputStream("C:\\temp\\ccda.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
//    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge_Summary-DCI.out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
//    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge_Summary-DCI.out.ttl"), FhirFormat.TURTLE, OutputStyle.PRETTY, null);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  @Test
  public void testEpic() throws FHIRFormatError, DefinitionException, FileNotFoundException, IOException, FHIRException {
    Element e = Manager.parse(context, new FileInputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge-Homework-Epic.xml"), FhirFormat.XML);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge-Homework-Epic.out.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge-Homework-Epic.out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-Discharge-Homework-Epic.out.ttl"), FhirFormat.TURTLE, OutputStyle.PRETTY, null);
  }

  @Test
  public void testDHIT() throws FHIRFormatError, DefinitionException, FileNotFoundException, IOException, FHIRException {
    Element e = Manager.parse(context, new FileInputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-DS-Homework-DHIT.xml"), FhirFormat.XML);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-DS-Homework-DHIT.out.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-DS-Homework-DHIT.out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
    Manager.compose(context, e, new FileOutputStream("C:\\work\\org.hl7.fhir.test\\ccda-to-fhir-maps\\testdocuments\\IAT2-DS-Homework-DHIT.out.ttl"), FhirFormat.TURTLE, OutputStyle.PRETTY, null);
  }

}
