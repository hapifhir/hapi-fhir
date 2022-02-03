package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.SearchParameter;
import ca.uhn.fhir.tinder.util.XMLUtils;

public class CompartmentParser {

	private String myVersion;
	private Resource myResourceDef;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompartmentParser.class);

	public CompartmentParser(String theVersion, Resource theResourceDef) {
		myVersion = theVersion;
		myResourceDef = theResourceDef;
	}
	
	public void parse() throws Exception {
		String resName = "/compartment/" + myVersion + "/compartments.xml";
		InputStream nextRes = getClass().getResourceAsStream(resName);
		if (nextRes == null) {
			throw new MojoFailureException(Msg.code(178) + "Unknown base resource name: " + resName);
		}

		ourLog.debug("Reading compartment file {}", resName);

		Document file;
		try {
			file = XMLUtils.parse(nextRes, false);
		} catch (Exception e) {
			throw new Exception(Msg.code(179) + "Failed during reading: " + resName, e);
		}

		Element resourcesSheet = null;
		for (int i = 0; i < file.getElementsByTagName("Worksheet").getLength() && resourcesSheet == null; i++) {
			resourcesSheet = (Element) file.getElementsByTagName("Worksheet").item(i);
			if (!"resources".equals(resourcesSheet.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Name"))) {
				resourcesSheet = null;
			}
		}

		if (resourcesSheet == null) {
			throw new Exception(Msg.code(180) + "Failed to find worksheet with name 'Data Elements' in spreadsheet: " + resName);
		}

		Element table = (Element) resourcesSheet.getElementsByTagName("Table").item(0);
		NodeList rows = table.getElementsByTagName("Row");
		
		Map<Integer, String> col2compartment = new HashMap<Integer, String>();
		Element headerRow = (Element) rows.item(0);
		for (int i = 1; i < headerRow.getElementsByTagName("Cell").getLength(); i++) {
			Element cellElement = (Element) headerRow.getElementsByTagName("Cell").item(i);
			Element dataElement = (Element) cellElement.getElementsByTagName("Data").item(0);
			col2compartment.put(i, dataElement.getTextContent());
		}
		
		Element row = null;
		for (int i = 1; i < rows.getLength(); i++) {
			Element nextRow = (Element) rows.item(i);
			
			NodeList cells = nextRow.getElementsByTagName("Cell");
			Element cellElement = (Element) cells.item(0);
			Element dataElement = (Element) cellElement.getElementsByTagName("Data").item(0);
			if (dataElement.getTextContent().equals(myResourceDef.getName())) {
				row = nextRow;
				break;
			}
		}
		
		if (row == null) {
			ourLog.debug("No compartments for resource {}", myResourceDef.getName());
			return;
		}
		
		NodeList cells = row.getElementsByTagName("Cell");
		for (int i = 1; i < cells.getLength(); i++) {
			Element cellElement = (Element) cells.item(i);
			int index = i;
			if (cellElement.hasAttribute("Index")) {
				index = Integer.parseInt(cellElement.getAttribute("Index"));
			}
			
			String compartment = col2compartment.get(index);
			
			Element dataElement = (Element) cellElement.getElementsByTagName("Data").item(0);
			String namesUnsplit = dataElement.getTextContent();
			String[] namesSplit = namesUnsplit.split("\\|");
			for (String nextName : namesSplit) {
				nextName = nextName.trim();
				if (isBlank(nextName)) {
					continue;
				}
				
				String[] parts = nextName.split("\\.");
				if (parts[0].equals("{def}")) {
					continue;
				}
				Resource element = myResourceDef;
				SearchParameter sp = element.getSearchParameterByName(parts[0]);
				if (sp == null) {
					throw new MojoFailureException(Msg.code(181) + "Can't find child named " + parts[0] + " - Valid names: " + element.getSearchParameterNames());
				}
				
				sp.addCompartment(compartment);
			}
		}
	}
	
}
