package ca.uhn.fhir.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.starter.model.BaseElement;
import ca.uhn.fhir.starter.model.Child;
import ca.uhn.fhir.starter.model.Resource;
import ca.uhn.fhir.starter.model.ResourceBlock;
import ca.uhn.fhir.starter.util.XMLUtils;

public abstract class BaseParser {

	private String myDirectory;
	private String myOutputFile;

	public void parse() throws Exception {
		File baseDir = new File(myDirectory);
		if (baseDir.exists() == false || baseDir.isDirectory() == false) {
			throw new Exception(myDirectory + " does not exist or is not a directory");
		}

		File resourceSpreadsheetFile = new File(baseDir, getFilename());
		if (resourceSpreadsheetFile.exists() == false) {
			throw new Exception(resourceSpreadsheetFile.getAbsolutePath() + " does not exist");
		}

		Document file = XMLUtils.parse(new FileInputStream(resourceSpreadsheetFile), false);
		Element dataElementsSheet = (Element) file.getElementsByTagName("Worksheet").item(0);
		NodeList tableList = dataElementsSheet.getElementsByTagName("Table");
		Element table = (Element) tableList.item(0);

		NodeList rows = table.getElementsByTagName("Row");

		Element resourceRow = (Element) rows.item(1);
		Resource resource = new Resource();
		parseBasicElements(resourceRow, resource);

		Map<String, BaseElement> elements = new HashMap<String, BaseElement>();
		elements.put(resource.getElementName(), resource);

		for (int i = 2; i < rows.getLength(); i++) {
			Element nextRow = (Element) rows.item(i);
			String name = cellValue(nextRow, 0);
			if (name == null || name.startsWith("!")) {
				continue;
			}

			String type = cellValue(nextRow, 5);

			Child elem;
			if (StringUtils.isBlank(type) || type.startsWith("=")) {
				elem = new ResourceBlock();
			} else {
				elem = new Child();
			}

			parseBasicElements(nextRow, elem);

			if (elem.isResourceRef()) {
				elem.setReferenceType(ResourceReference.class.getSimpleName());
			} else if (elem.getType().size() == 1) {
				String elemName = elem.getType().get(0);
				elemName = elemName.substring(0, 1).toUpperCase() + elemName.substring(1);
				if (elem instanceof ResourceBlock) {
					elem.setReferenceType(elemName);
				}else {
					elem.setReferenceType(elemName + "Dt");
				}
			} else {
				elem.setReferenceType(IDatatype.class.getSimpleName());
			}

			if (elem.isRepeatable()) {
				elem.setReferenceType("List<" + elem.getReferenceType() + ">");
			}

			elements.put(elem.getName(), elem);
			BaseElement parent = elements.get(elem.getElementParentName());
			if (parent == null) {
				throw new Exception("Can't find element " + elem.getElementParentName() + "  -  Valid values are: " + elements.keySet());
			}
			parent.getChildren().add(elem);

		}

		write(resource);

	}

	protected abstract void parseBasicElements(Element theResourceRow, BaseElement theElem);

	public void setDirectory(String theDirectory) {
		myDirectory = theDirectory;
	}

	public void setOutputFile(String theOutputFile) {
		myOutputFile = theOutputFile;
	}

	static String cellValue(Node theRowXml, int theCellIndex) {
		NodeList cells = ((Element) theRowXml).getElementsByTagName("Cell");

		for (int i = 0, currentCell = 0; i < cells.getLength(); i++) {
			Element nextCell = (Element) cells.item(i);
			String indexVal = nextCell.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Index");
			if (StringUtils.isNotBlank(indexVal)) {
				// 1-indexed for some reason...
				currentCell = Integer.parseInt(indexVal) - 1;
			}

			if (currentCell == theCellIndex) {
				NodeList dataElems = nextCell.getElementsByTagName("Data");
				Element dataElem = (Element) dataElems.item(0);
				if (dataElem == null) {
					return null;
				}
				String retVal = dataElem.getTextContent();
				return retVal;
			}

			currentCell++;
		}

		return null;
	}


	private void write(Resource theResource) throws IOException {
		File f = new File(myOutputFile);
		FileWriter w = new FileWriter(f, false);

		VelocityContext ctx = new VelocityContext();
		ctx.put("className", theResource.getName());
		ctx.put("shortName", theResource.getShortName());
		ctx.put("definition", theResource.getDefinition());
		ctx.put("requirements", theResource.getRequirement());
		ctx.put("children", theResource.getChildren());
		ctx.put("resourceBlockChildren", theResource.getResourceBlockChildren());

		VelocityEngine v = new VelocityEngine();
		InputStream templateIs = ResourceParser.class.getResourceAsStream(getTemplate());
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	protected abstract String getFilename();

	protected abstract String getTemplate();

}
