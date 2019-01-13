package org.hl7.fhir.dstu3.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.context.SimpleWorkerContext;
import org.hl7.fhir.dstu3.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.test.support.TestingUtilities;
import org.hl7.fhir.dstu3.utils.EOperationOutcome;
import org.hl7.fhir.dstu3.utils.NarrativeGenerator;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.Before;
import org.junit.Test;

public class ResourceRoundTripTests {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void test() throws FileNotFoundException, IOException, FHIRException, EOperationOutcome {
    if (TestingUtilities.context == null)
      TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\definitions.xml.zip");
    Resource res = new XmlParser().parse(new FileInputStream("C:\\work\\org.hl7.fhir\\build\\tests\\resources\\unicode.xml"));
    new NarrativeGenerator("", "", TestingUtilities.context).generate((DomainResource) res);
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream("C:\\work\\org.hl7.fhir\\build\\tests\\resources\\unicode.out.xml"), res);
  }

}
