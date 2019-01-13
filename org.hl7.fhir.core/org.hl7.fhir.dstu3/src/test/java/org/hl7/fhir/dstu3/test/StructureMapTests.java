package org.hl7.fhir.dstu3.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.dstu3.context.SimpleWorkerContext;
import org.hl7.fhir.dstu3.elementmodel.Element;
import org.hl7.fhir.dstu3.elementmodel.Manager;
import org.hl7.fhir.dstu3.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureMap;
import org.hl7.fhir.dstu3.test.support.TestingUtilities;
import org.hl7.fhir.dstu3.utils.StructureMapUtilities;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.junit.Test;


public class StructureMapTests {

  private void testParse(String path) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.home(), "publish", "definitions.xml.zip"));
    
    StructureMapUtilities scm = new StructureMapUtilities(TestingUtilities.context, null, null);
    StructureMap map = scm.parse(TextFile.fileToString(Utilities.path(TestingUtilities.home(), path)));
    TextFile.stringToFile(scm.render(map), Utilities.path(TestingUtilities.home(), path+".out"));
  }
  
//  @Test
//  public void testParseAny() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\any.map");
//  }
//
//  @Test
//  public void testParseBL() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\bl.map");
//  }
//
//  @Test
//  public void testParseED() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\ed.map");
//  }
//
//  @Test
//  public void testParseCD() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\cd.map");
//  }
//
//  @Test
//  public void testParseAD() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\ad.map");
//  }
//
//  @Test
//  public void testParsePQ() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\pq.map");
//  }
//
//  @Test
//  public void testParseIVLTS() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\ivl-ts.map");
//  }
//
//  @Test
//  public void testParseCDA() throws FHIRException, IOException {
//    testParse("guides\\ccda2\\mapping\\map\\cda.map");
//  }

//  @Test
//  public void testTransformCDA() throws FileNotFoundException, Exception {
//    Map<String, StructureMap> maps = new HashMap<String, StructureMap>();
//
//    if (TestingUtilities.context == null)
//    	TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.home(), "publish", "definitions.xml.zip"));
//
//    StructureMapUtilities scu = new StructureMapUtilities(TestingUtilities.context, maps, null);
//    
//    for (String f : new File(Utilities.path(TestingUtilities.home(), "guides", "ccda2", "mapping", "logical")).list()) {
//      try {
//        StructureDefinition sd = (StructureDefinition) new XmlParser().parse(new FileInputStream(Utilities.path(TestingUtilities.home(), "guides", "ccda2", "mapping", "logical", f)));
//        ((SimpleWorkerContext) TestingUtilities.context).seeResource(sd.getUrl(), sd);
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
//   
//    for (String f : new File(Utilities.path(TestingUtilities.home(), "guides", "ccda2", "mapping", "map")).list()) {
//      try {
//        StructureMap map = scu.parse(TextFile.fileToString(Utilities.path(TestingUtilities.home(), "guides", "ccda2", "mapping", "map", f)));
//        maps.put(map.getUrl(), map);
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
//        
//    Element cda = Manager.parse(TestingUtilities.context, new FileInputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda2\\mapping\\example\\ccd.xml"), FhirFormat.XML);
//    Manager.compose(TestingUtilities.context, cda, new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda2\\mapping\\example\\ccd.out.json"), FhirFormat.JSON, OutputStyle.PRETTY, null);
//    Manager.compose(TestingUtilities.context, cda, new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda2\\mapping\\example\\ccd.out.xml"), FhirFormat.XML, OutputStyle.PRETTY, null);
//    Bundle bundle = new Bundle();
//    scu.transform(null, cda, maps.get("http://hl7.org/fhir/StructureMap/cda"), bundle);
//    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\bundle.xml"), bundle);
//  }
//
//  @Test
//  public void testTransformProfilesCDA() throws FileNotFoundException, Exception {
//    Map<String, StructureMap> maps = new HashMap<String, StructureMap>();
//
//    if (TestingUtilities.context == null)
//      TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");
//
//    StructureMapUtilities scu = new StructureMapUtilities(TestingUtilities.context, maps, null);
//    
//    for (String f : new File("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\CDA").list()) {
//      try {
//        StructureDefinition sd = (StructureDefinition) new XmlParser().parse(new FileInputStream("C:\\work\\org.hl7.fhir\\build\\guides\\ccda\\CDA\\"+f));
//        ((SimpleWorkerContext) TestingUtilities.context).seeResource(sd.getUrl(), sd);
//      } catch (Exception e) {
//      }
//    }
//
//    for (String f : new File("C:\\work\\org.hl7.fhir\\build\\guides\\ccda2\\mapping\\map").list()) {
//      try {
//        StructureMap map = scu.parse(TextFile.fileToString("C:\\work\\org.hl7.fhir\\build\\guides\\ccda2\\mapping\\map\\"+ f));
//        maps.put(map.getUrl(), map);
//      } catch (Exception e) {
//      }
//    }
//        
//    List<StructureDefinition> result = scu.analyse(null, maps.get("http://hl7.org/fhir/StructureMap/cda")).getProfiles();
//    for (StructureDefinition sd : result)
//      new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("c:\\temp\\res-"+sd.getId()+".xml"), sd);
//  }
//
}
