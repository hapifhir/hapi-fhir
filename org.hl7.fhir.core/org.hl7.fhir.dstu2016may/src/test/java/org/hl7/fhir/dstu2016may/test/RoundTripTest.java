package org.hl7.fhir.dstu2016may.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.metamodel.Element;
import org.hl7.fhir.dstu2016may.metamodel.Manager;
import org.hl7.fhir.dstu2016may.metamodel.Manager.FhirFormat;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.utilities.Utilities;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RoundTripTest {
	 static String root = "C:\\work\\org.hl7.fhir.2016May\\build\\publish";

	 @Parameters
	 public static Collection<Object[]> getFiles() throws IOException {
		 Collection<Object[]> params = new ArrayList<Object[]>();
		 String examples = Utilities.path(root, "examples");
		 for (File f : new File(examples).listFiles()) {
			 if (f.getName().endsWith(".xml")) {
				 Object[] arr = new Object[] { f };
				 params.add(arr);
			 }
		 }
		 return params;
	 }
	 
	 private File file;

	 public RoundTripTest(File file) {
		 this.file = file;
	 }
		 
	@Test
	@SuppressWarnings("deprecation")
	public void test() throws Exception {
		System.out.println(file.getName());
		Resource r = new org.hl7.fhir.dstu2016may.formats.XmlParser().parse(new FileInputStream(file));
		String fn = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(fn), r);
    String msg = TestingUtilities.checkXMLIsSame(file.getAbsolutePath(), fn);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);
    String j1 = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(j1), r);

		if (TestingUtilities.context == null) {
      TestingUtilities.context = SimpleWorkerContext.fromPack(Utilities.path(root, "validation-min.xml.zip"));
    }
		
		Element re = Manager.parse(TestingUtilities.context, new FileInputStream(file), FhirFormat.XML);
    fn = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(fn), FhirFormat.XML, OutputStyle.PRETTY, null);
    msg = TestingUtilities.checkXMLIsSame(file.getAbsolutePath(), fn);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);
    String j2 = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(j2), FhirFormat.JSON, OutputStyle.PRETTY, null);

    msg = TestingUtilities.checkJsonIsSame(j1, j2);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);

	  // ok, we've produced equivalent JSON by both methods.
	  // now, we're going to reverse the process
		r = new org.hl7.fhir.dstu2016may.formats.JsonParser().parse(new FileInputStream(j2)); // crossover too
    fn = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.JsonParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(fn), r);
    msg = TestingUtilities.checkJsonIsSame(j2, fn);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);
    String x1 = makeTempFilename();
		new org.hl7.fhir.dstu2016may.formats.XmlParser().setOutputStyle(OutputStyle.PRETTY).compose(new FileOutputStream(x1), r);

		re = Manager.parse(TestingUtilities.context, new FileInputStream(j1), FhirFormat.JSON);
    fn = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(fn), FhirFormat.JSON, OutputStyle.PRETTY, null);
    msg = TestingUtilities.checkJsonIsSame(j1, fn);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);
    String x2 = makeTempFilename();
    Manager.compose(TestingUtilities.context, re, new FileOutputStream(x2), FhirFormat.XML, OutputStyle.PRETTY, null);

    msg = TestingUtilities.checkXMLIsSame(x1, x2);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);
    msg = TestingUtilities.checkXMLIsSame(file.getAbsolutePath(), x1);
    Assert.assertTrue(file.getName()+": "+msg, msg == null);

	}

	int i = 0;
	private String makeTempFilename() {
		i++;
  	return "c:\\temp\\fhirtests\\"+Integer.toString(i)+".tmp";
	}

}
