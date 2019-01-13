package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.Observation;
import org.junit.Before;
import org.junit.Test;

public class JsonDirectTests {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void test() throws FHIRFormatError, FileNotFoundException, IOException {
    File src = new File("C:\\temp\\obs.xml");
    File xml = new File("C:\\temp\\xml.xml");
    File json = new File("C:\\temp\\json.json");
    File json2 = new File("C:\\temp\\json2.json");
    FileUtils.copyFile(new File("C:\\work\\org.hl7.fhir\\build\\publish\\observation-decimal.xml"), src);
    Observation obs = (Observation) new XmlParser().parse(new FileInputStream(src));
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(json), obs);
    obs = (Observation) new JsonParser().parse(new FileInputStream(json));
    new JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(json2), obs);
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(xml), obs);
  }

}
