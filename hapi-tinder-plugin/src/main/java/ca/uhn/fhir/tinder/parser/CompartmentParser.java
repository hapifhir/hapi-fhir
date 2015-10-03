package ca.uhn.fhir.tinder.parser;

import java.io.InputStream;

import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ca.uhn.fhir.tinder.util.XMLUtils;

public class CompartmentParser {

	private String myVersion;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompartmentParser.class);

	public CompartmentParser(String theVersion) {
		myVersion = theVersion;
	}
	
	public void parse() throws Exception {
		String resName = "/compartment/" + myVersion + "/compartments.xml";
		InputStream nextRes = getClass().getResourceAsStream(resName);
		if (nextRes == null) {
			throw new MojoFailureException("Unknown base resource name: " + resName);
		}

		ourLog.info("Reading spreadsheet file {}", resName);

		Document file;
		try {
			file = XMLUtils.parse(nextRes, false);
		} catch (Exception e) {
			throw new Exception("Failed during reading: " + resName, e);
		}

		Element resourcesSheet = null;
		for (int i = 0; i < file.getElementsByTagName("Worksheet").getLength() && resourcesSheet == null; i++) {
			resourcesSheet = (Element) file.getElementsByTagName("Worksheet").item(i);
			if (!"resources".equals(resourcesSheet.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Name"))) {
				resourcesSheet = null;
			}
		}

		if (resourcesSheet == null) {
			throw new Exception("Failed to find worksheet with name 'Data Elements' in spreadsheet: " + resName);
		}

	}
	
}
