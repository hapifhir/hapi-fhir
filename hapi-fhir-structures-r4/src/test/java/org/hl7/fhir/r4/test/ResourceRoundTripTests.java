package org.hl7.fhir.r4.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.utils.EOperationOutcome;
import org.hl7.fhir.r4.utils.NarrativeGenerator;
import org.hl7.fhir.utilities.Utilities;
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
      TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.home(), "publish", "definitions.xml.zip"));
    Resource res = new XmlParser().parse(new FileInputStream(Utilities.path(TestingUtilities.home(), "tests", "resources", "unicode.xml")));
    new NarrativeGenerator("", "", TestingUtilities.context).generate((DomainResource) res);
    new XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(Utilities.path(TestingUtilities.home(), "tests", "resources","unicode.out.xml")), res);
  }

}
