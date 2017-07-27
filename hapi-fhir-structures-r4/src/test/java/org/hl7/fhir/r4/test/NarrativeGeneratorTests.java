package org.hl7.fhir.r4.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.test.support.TestingUtilities;
import org.hl7.fhir.r4.utils.EOperationOutcome;
import org.hl7.fhir.r4.utils.NarrativeGenerator;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

public class NarrativeGeneratorTests {

	private NarrativeGenerator gen;
	
	@Before
	public void setUp() throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
      TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(TestingUtilities.home(), "publish", "definitions.xml.zip"));
		if (gen == null)
  		gen = new NarrativeGenerator("", null, TestingUtilities.context);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test() throws FileNotFoundException, IOException, XmlPullParserException, EOperationOutcome, FHIRException {
		process(Utilities.path(TestingUtilities.home(), "source", "questionnaireresponse", "questionnaireresponse-example-f201-lifelines.xml"));
	}

	private void process(String path) throws FileNotFoundException, IOException, XmlPullParserException, EOperationOutcome, FHIRException {
	  XmlParser p = new XmlParser();
	  DomainResource r = (DomainResource) p.parse(new FileInputStream(path));
	  gen.generate(r);
	  FileOutputStream s = new FileOutputStream(Utilities.path(TestingUtilities.temp(), "gen.xml"));
    new XmlParser().compose(s, r, true);
    s.close();
	  
  }

}
