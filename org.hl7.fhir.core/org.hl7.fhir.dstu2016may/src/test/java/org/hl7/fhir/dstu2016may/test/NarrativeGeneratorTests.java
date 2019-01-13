package org.hl7.fhir.dstu2016may.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hl7.fhir.dstu2016may.formats.XmlParser;
import org.hl7.fhir.dstu2016may.model.DomainResource;
import org.hl7.fhir.dstu2016may.utils.EOperationOutcome;
import org.hl7.fhir.dstu2016may.utils.NarrativeGenerator;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

public class NarrativeGeneratorTests {

	private NarrativeGenerator gen;
	
	@Before
	public void setUp() throws FileNotFoundException, IOException, FHIRException {
		if (gen == null)
  		gen = new NarrativeGenerator("", null, SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation.zip"));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test() throws FileNotFoundException, IOException, XmlPullParserException, EOperationOutcome, FHIRException {
		process("C:\\work\\org.hl7.fhir\\build\\source\\questionnaireresponse\\questionnaireresponse-example-f201-lifelines.xml");
	}

	private void process(String path) throws FileNotFoundException, IOException, XmlPullParserException, EOperationOutcome, FHIRException {
	  XmlParser p = new XmlParser();
	  DomainResource r = (DomainResource) p.parse(new FileInputStream(path));
	  gen.generate(r);
	  FileOutputStream s = new FileOutputStream("c:\\temp\\gen.xml");
    new XmlParser().compose(s, r, true);
    s.close();
	  
  }

}
