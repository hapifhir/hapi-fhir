package org.hl7.fhir.dstu2016may.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.formats.XmlParser;
import org.hl7.fhir.dstu2016may.metamodel.Element;
import org.hl7.fhir.dstu2016may.metamodel.Manager;
import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.StructureMap;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.dstu2016may.utils.StructureMapUtilities;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.junit.Test;


public class StructureMapTests {

  private void testParse(String path) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir.2016May\\build\\publish\\validation-min.xml.zip");
    StructureMapUtilities scm = new StructureMapUtilities(TestingUtilities.context, null, null);
    StructureMap map = scm.parse(TextFile.fileToString(Utilities.path("C:\\work\\org.hl7.fhir.2016May\\build", path)));
    TextFile.stringToFile(scm.render(map), Utilities.path("C:\\work\\org.hl7.fhir.2016May\\build", path+".out"));
  }
  
  @Test
  public void testParseAny() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\any.map");
  }

  @Test
  public void testParseBL() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\bl.map");
  }

  @Test
  public void testParseED() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\ed.map");
  }

  @Test
  public void testParseCD() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\cd.map");
  }

  @Test
  public void testParseAD() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\ad.map");
  }

  @Test
  public void testParsePQ() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\pq.map");
  }

  @Test
  public void testParseIVLTS() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\ivl-ts.map");
  }

  @Test
  public void testParseCDA() throws FHIRException, IOException {
    testParse("guides\\ccda\\maps\\cda.map");
  }

  @Test
  public void testLoadCDA() throws FileNotFoundException, Exception {
    Map<String, StructureMap> maps = new HashMap<String, StructureMap>();

    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");

    StructureMapUtilities scu = new StructureMapUtilities(TestingUtilities.context, maps, null);
    
    for (String f : new File("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\CDA").list()) {
      try {
        StructureDefinition sd = (StructureDefinition) new XmlParser().parse(new FileInputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\CDA\\"+f));
        ((SimpleWorkerContext) TestingUtilities.context).seeResource(sd.getUrl(), sd);
      } catch (Exception e) {
      }
    }

    for (String f : new File("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\maps").list()) {
      try {
        StructureMap map = scu.parse(TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\maps\\"+ f));
        maps.put(map.getUrl(), map);
      } catch (Exception e) {
      }
    }
        
    Element cda = Manager.parse(TestingUtilities.context, new FileInputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\Example\\ccd.xml"), FhirFormat.XML);
    Manager.compose(TestingUtilities.context, cda, new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\Example\\ccd.out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
    Manager.compose(TestingUtilities.context, cda, new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\Example\\ccd.out.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
    Bundle bundle = new Bundle();
    scu.transform(null, cda, maps.get("http://hl7.org/fhir/StructureMap/cda"), bundle);
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\bundle.xml"), bundle);
  }

}
