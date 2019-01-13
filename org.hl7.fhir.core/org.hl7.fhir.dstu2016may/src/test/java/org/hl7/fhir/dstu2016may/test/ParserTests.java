package org.hl7.fhir.dstu2016may.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.metamodel.Element;
import org.hl7.fhir.dstu2016may.metamodel.Manager;
import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.utilities.Utilities;
import org.junit.Test;

import junit.framework.Assert;

public class ParserTests {

	private String root = "C:\\work\\org.hl7.fhir.2016May\\build\\publish";

	@Test
	public void testSpecific() throws Exception {
		String examples = Utilities.path(root, "examples");
		String fn = "organization-example-f002-burgers-card(f002).xml";
		testRoundTrip(Utilities.path(examples, fn), fn);	  
	}

	@Test
	public void testAll() throws Exception {
		String examples = Utilities.path(root, "examples");
		for (String fn : new File(examples).list()) {
			if (fn.endsWith(".xml")) {
				testRoundTrip(Utilities.path(examples, fn), fn);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void testRoundTrip(String filename, String name) throws Exception {
		System.out.println(name);
		Resource r = new org.hl7.fhir.dstu2016may.formats.XmlParser().parse(new FileInputStream(filename));
		String fn = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(fn), r);
    String msg = TestingUtilities.checkXMLIsSame(filename, fn);
    Assert.assertTrue(name+": "+msg, msg == null);
    String j1 = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(j1), r);

		if (TestingUtilities.context == null) {
      TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(root, "validation-min.xml.zip"));
    }
		
		Element re = Manager.parse(TestingUtilities.context, new FileInputStream(filename), FhirFormat.XML);
    fn = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(fn), FhirFormat.XML, OutputStyle.PRETTY, null);
    msg = TestingUtilities.checkXMLIsSame(filename, fn);
    Assert.assertTrue(name+": "+msg, msg == null);
    String j2 = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(j2), FhirFormat.JSON, OutputStyle.PRETTY, null);

    msg = TestingUtilities.checkJsonIsSame(j1, j2);
    Assert.assertTrue(name+": "+msg, msg == null);

	  // ok, we've produced equivalent JSON by both methods.
	  // now, we're going to reverse the process
		r = new org.hl7.fhir.dstu2016may.formats.JsonParser().parse(new FileInputStream(j2)); // crossover too
    fn = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(fn), r);
    msg = TestingUtilities.checkJsonIsSame(j2, fn);
    Assert.assertTrue(name+": "+msg, msg == null);
    String x1 = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(x1), r);

		re = Manager.parse(TestingUtilities.context, new FileInputStream(j1), FhirFormat.JSON);
    fn = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(fn), FhirFormat.JSON, OutputStyle.PRETTY, null);
    msg = TestingUtilities.checkJsonIsSame(j1, fn);
    Assert.assertTrue(name+": "+msg, msg == null);
    String x2 = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(x2), FhirFormat.XML, OutputStyle.PRETTY, null);

    msg = TestingUtilities.checkXMLIsSame(x1, x2);
    Assert.assertTrue(name+": "+msg, msg == null);
    msg = TestingUtilities.checkXMLIsSame(filename, x1);
    Assert.assertTrue(name+": "+msg, msg == null);

	}

	int i = 0;
	private String makeTempFilename() {
		i++;
  	return "c:\\temp\\fhirtests\\"+Integer.toString(i)+".tmp";
	}

}
