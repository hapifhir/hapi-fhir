package ca.uhn.fhir.tinder.parser;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.tinder.model.AnyChild;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Child;
import ca.uhn.fhir.tinder.model.ResourceBlock;
import ca.uhn.fhir.tinder.model.ResourceBlockCopy;
import ca.uhn.fhir.tinder.model.SearchParameter;
import ca.uhn.fhir.tinder.model.SimpleChild;
import ca.uhn.fhir.tinder.util.XMLUtils;

public abstract class BaseStructureSpreadsheetParser extends BaseStructureParser {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseStructureSpreadsheetParser.class);
	private int myColBinding;
	private int myColCard;
	private int myColDefinition;
	private int myColName;
	private int myColRequirements;
	private int myColShortName;
	private int myColType;
	private int myColV2Mapping;

	public void parse() throws Exception {
		int index = 0;
		for (InputStream nextInputStream : getInputStreams()) {

			ourLog.info("Reading spreadsheet file {}", getInputStreamNames().get(index));
			
			Document file;
			try {
				file = XMLUtils.parse(nextInputStream, false);
			} catch (Exception e) {
				throw new Exception("Failed during reading: " + getInputStreamNames().get(index), e);
			}

			Element dataElementsSheet = (Element) file.getElementsByTagName("Worksheet").item(0);
			NodeList tableList = dataElementsSheet.getElementsByTagName("Table");
			Element table = (Element) tableList.item(0);

			NodeList rows = table.getElementsByTagName("Row");

			Element defRow = (Element) rows.item(0);
			parseFirstRow(defRow);

			Element resourceRow = (Element) rows.item(1);

			BaseRootType resource = createRootType();
			addResource(resource);

			parseParameters(file, resource);

			parseBasicElements(resourceRow, resource);

			resource.setId(resource.getName().toLowerCase());

			if (this instanceof ResourceGeneratorUsingSpreadsheet) {
				resource.setProfile("http://hl7.org/fhir/profiles/" + resource.getElementName());
			}

			Map<String, BaseElement> elements = new HashMap<String, BaseElement>();
			elements.put(resource.getElementName(), resource);

			// Map<String,String> blockFullNameToShortName = new
			// HashMap<String,String>();

			for (int i = 2; i < rows.getLength(); i++) {
				Element nextRow = (Element) rows.item(i);
				String name = cellValue(nextRow, 0);
				if (name == null || name.startsWith("!")) {
					continue;
				}

				String type = cellValue(nextRow, myColType);

				Child elem;
				if (StringUtils.isBlank(type) || type.startsWith("=")) {
					elem = new ResourceBlock();
				} else if (type.startsWith("@")) {
					// type = type.substring(type.lastIndexOf('.')+1);
					elem = new ResourceBlockCopy();
				} else if (type.equals("*")) {
					elem = new AnyChild();
				} else {
					elem = new SimpleChild();
				}

				parseBasicElements(nextRow, elem);

				elements.put(elem.getName(), elem);
				BaseElement parent = elements.get(elem.getElementParentName());
				if (parent == null) {
					throw new Exception("Can't find element " + elem.getElementParentName() + "  -  Valid values are: " + elements.keySet());
				}
				parent.addChild(elem);

				/*
				 * Find simple setters
				 */
				if (elem instanceof Child) {
					scanForSimpleSetters(elem);
				}

			}

			index++;
		}

		ourLog.info("Parsed {} spreadsheet structures", getResources().size());

	}

	protected abstract BaseRootType createRootType();

	private void parseParameters(Document theFile, BaseRootType theResource) {
		NodeList sheets = theFile.getElementsByTagName("Worksheet");
		for (int i = 0; i < sheets.getLength(); i++) {
			Element sheet = (Element) sheets.item(i);
			String name = sheet.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Name");
			if ("Search".equals(name)) {

				NodeList tableList = sheet.getElementsByTagName("Table");
				Element table = (Element) tableList.item(0);
				NodeList rows = table.getElementsByTagName("Row");
				Element defRow = (Element) rows.item(0);

				int colName = 0;
				int colDesc = 0;
				int colType = 0;
				int colPath = 0;
				for (int j = 0; j < 20; j++) {
					String nextName = cellValue(defRow, j);
					if (nextName == null) {
						continue;
					}
					nextName = nextName.toLowerCase().trim().replace(".", "");
					if ("name".equals(nextName)) {
						colName = j;
					} else if ("description".equals(nextName)) {
						colDesc = j;
					} else if ("type".equals(nextName)) {
						colType = j;
					} else if ("path".equals(nextName)) {
						colPath = j;
					}
				}

				for (int j = 1; j < rows.getLength(); j++) {
					Element nextRow = (Element) rows.item(j);
					SearchParameter sp = new SearchParameter();

					sp.setName(cellValue(nextRow, colName));
					sp.setDescription(cellValue(nextRow, colDesc));
					sp.setType(cellValue(nextRow, colType));
					sp.setPath(cellValue(nextRow, colPath));

					if (StringUtils.isNotBlank(sp.getName()) && !sp.getName().startsWith("!")) {
						theResource.getSearchParameters().add(sp);
					}
				}

			}
		}
	}

	protected abstract Collection<InputStream> getInputStreams();

	protected abstract List<String> getInputStreamNames();

	private void parseFirstRow(Element theDefRow) {
		for (int i = 0; i < 20; i++) {
			String nextName = cellValue(theDefRow, i);
			if (nextName == null) {
				continue;
			}
			nextName = nextName.toLowerCase().trim().replace(".", "");
			if ("element".equals(nextName)) {
				myColName = i;
			} else if ("card".equals(nextName)) {
				myColCard = i;
			} else if ("type".equals(nextName)) {
				myColType = i;
			} else if ("binding".equals(nextName)) {
				myColBinding = i;
			} else if ("short name".equals(nextName)) {
				myColShortName = i;
			} else if ("definition".equals(nextName)) {
				myColDefinition = i;
			} else if ("requirements".equals(nextName)) {
				myColRequirements = i;
			} else if ("v2 mapping".equals(nextName)) {
				myColV2Mapping = i;
			}
		}
	}

	protected void parseBasicElements(Element theRowXml, BaseElement theTarget) {
		String name = cellValue(theRowXml, myColName);
		theTarget.setName(name);

		theTarget.setElementNameAndDeriveParentElementName(name);

		String cardValue = cellValue(theRowXml, myColCard);
		if (cardValue != null && cardValue.contains("..")) {
			String[] split = cardValue.split("\\.\\.");
			theTarget.setCardMin(split[0]);
			theTarget.setCardMax(split[1]);
		}

		String type = cellValue(theRowXml, myColType);
		theTarget.setTypeFromString(type);

		theTarget.setBinding(cellValue(theRowXml, myColBinding));
		theTarget.setShortName(cellValue(theRowXml, myColShortName));
		theTarget.setDefinition(cellValue(theRowXml, myColDefinition));
		theTarget.setRequirement(cellValue(theRowXml, myColRequirements));
		theTarget.setV2Mapping(cellValue(theRowXml, myColV2Mapping));
	}

}
