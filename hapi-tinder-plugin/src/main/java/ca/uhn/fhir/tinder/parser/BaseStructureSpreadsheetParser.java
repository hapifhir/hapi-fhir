package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.tinder.model.*;
import ca.uhn.fhir.tinder.util.XMLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStructureSpreadsheetParser extends BaseStructureParser {

	public BaseStructureSpreadsheetParser(String theVersion, String theBaseDir) throws MojoFailureException {
		super(theVersion, theBaseDir);

		myBindingStrengths = new HashMap<>();
		myBindingRefs = new HashMap<>();
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseStructureSpreadsheetParser.class);
	private int myColBinding = -1;
	private int myColModifier = -1;
	private int myColSummary = -1;
	private int myColCard = -1;
	private int myColDefinition = -1;
	private int myColName = -1;
	private int myColRequirements = -1;
	private int myColShortName = -1;
	private int myColType = -1;
	private int myColV2Mapping = -1;
	private HashMap<String, String> myBindingStrengths;
	private HashMap<String, String> myBindingRefs;
	private HashMap<String, String> myNameToValueSetUrl;

	public void parse() throws Exception {

		myNameToValueSetUrl = new HashMap<String, String>();
		if (getVersion().equals("dstu2")) {
			ourLog.info("Loading ValueSets...");
			FhirContext ctx = FhirContext.forDstu2();
			String path = ctx.getVersion().getPathToSchemaDefinitions().replace("/schema", "/valueset") + "/valuesets.xml";

			InputStream valuesetText = BaseStructureSpreadsheetParser.class.getResourceAsStream(path);
			Bundle bundle = ctx.newXmlParser().parseResource(Bundle.class, new InputStreamReader(valuesetText));
			for (Entry next : bundle.getEntry()) {
				ValueSet nextVs = (ValueSet) next.getResource();
				myNameToValueSetUrl.put(nextVs.getName(), nextVs.getUrl());
			}
			ourLog.info("Done Loading ValueSets");
		}

		int index = 0;
		for (InputStream nextInputStream : getInputStreams()) {

			String spreadsheetName = getInputStreamNames().get(index);
			ourLog.debug("Reading spreadsheet file {}", spreadsheetName);

			Document file;
			try {
				file = XMLUtils.parse(nextInputStream, false);
			} catch (Exception e) {
				throw new Exception(Msg.code(164) + "Failed during reading: " + spreadsheetName, e);
			}

			Element bindingsSheet = findSheetByName(spreadsheetName, "Bindings", file, false);
			if (bindingsSheet != null) {
				processBindingsSheet(bindingsSheet);
			}

			Element dataElementsSheet = findSheetByName(spreadsheetName, "Data Elements", file, true);
			NodeList tableList = dataElementsSheet.getElementsByTagName("Table");
			Element table = (Element) tableList.item(0);

			NodeList rows = table.getElementsByTagName("Row");

			Element defRow = (Element) rows.item(0);
			parseFirstRow(defRow);

			Element resourceRow = (Element) rows.item(1);

			BaseRootType resource = createRootType();
			addResource(resource);

			parseBasicElements(resourceRow, resource, null);

			parseParameters(file, resource);

			resource.setId(resource.getName().toLowerCase());

			if (this instanceof ResourceGeneratorUsingSpreadsheet) {
				resource.setProfile("http://hl7.org/fhir/profiles/" + resource.getElementName());
			}

			Map<String, BaseElement> elements = new HashMap<String, BaseElement>();
			elements.put(resource.getElementName(), resource);

			// Map<String,String> blockFullNameToShortName = new
			// HashMap<String,String>();

			List<Child> blockCopies = new ArrayList<Child>();
			for (int i = 2; i < rows.getLength(); i++) {
				Element nextRow = (Element) rows.item(i);
				String name = cellValue(nextRow, 0);
				if (name == null || name.startsWith("!")) {
					continue;
				}

				String type = cellValue(nextRow, myColType);

				if (i < rows.getLength() - 1) {
					Element followingRow = (Element) rows.item(i + 1);
					if (followingRow != null) {
						String followingName = cellValue(followingRow, 0);
						if (followingName != null && followingName.startsWith(name + ".")) {
							type = "";
						}
					}
				}

				Child elem;
				if (StringUtils.isBlank(type) || type.startsWith("=")) {
					elem = new ResourceBlock();
				} else if (type.startsWith("@")) {
					// type = type.substring(type.lastIndexOf('.')+1);
					elem = new ResourceBlockCopy();
					blockCopies.add(elem);
				} else if (type.equals("*")) {
					elem = new AnyChild();
				} else {
					elem = new SimpleChild();
				}

				parseBasicElements(nextRow, elem, type);
				postProcess(elem);

				elements.put(elem.getName(), elem);
				BaseElement parent = elements.get(elem.getElementParentName());
				if (parent == null) {
					throw new Exception(Msg.code(165) + "Can't find element " + elem.getElementParentName() + "  -  Valid values are: " + elements.keySet());
				}
				parent.addChild(elem);

				/*
				 * Find simple setters
				 */
				if (elem instanceof Child) {
					scanForSimpleSetters(elem);
				}

			}

			postProcess(resource);

			//			for (SearchParameter nextParam : resource.getSearchParameters()) {
			//				if (nextParam.getType().equals("reference")) {
			//					String path = nextParam.getPath();
			//					List<String> targetTypes = pathToResourceTypes.get(path);
			//					if (targetTypes != null) {
			//						targetTypes = new ArrayList<String>(targetTypes);
			//						for (Iterator<String> iter = targetTypes.iterator(); iter.hasNext();) {
			//							String next = iter.next();
			//							if (next.equals("Any") || next.endsWith("Dt")) {
			//								iter.remove();
			//							}
			//						}
			//					}
			//					nextParam.setTargetTypes(targetTypes);
			//				}
			//			}

			// resolve BlockCopy elements so they can access
			// the children of the referenced ResourceBlock
			// from Velocity templates.
			for (Child blockCopy : blockCopies) {
				BaseElement element = blockCopy;
				refLoop:
				while (element.getElementParentName() != null) {
					BaseElement parent = elements.get(element.getElementParentName());
					List<BaseElement> children = parent.getChildren();
					for (BaseElement child : children) {
						if (!child.equals(blockCopy) && child instanceof ResourceBlock 
						&& child.getElementName().equals(blockCopy.getElementName())) {
							((ResourceBlockCopy)blockCopy).setReferencedBlock((ResourceBlock)child);
							break refLoop;
						}
					}
					element = parent;
				}
			}
			
			index++;
		}

		ourLog.info("Parsed {} spreadsheet structures", getResources().size());

	}

	private Element findSheetByName(String spreadsheetName, String wantedName, Document file, boolean theFailIfNotFound) throws Exception {
		Element retVal = null;
		for (int i = 0; i < file.getElementsByTagName("Worksheet").getLength() && retVal == null; i++) {
			retVal = (Element) file.getElementsByTagName("Worksheet").item(i);
			if (!wantedName.equals(retVal.getAttributeNS("urn:schemas-microsoft-com:office:spreadsheet", "Name"))) {
				retVal = null;
			}
		}

		if (retVal == null && theFailIfNotFound) {
			throw new Exception(Msg.code(166) + "Failed to find worksheet with name '" + wantedName + "' in spreadsheet: " + spreadsheetName);
		}
		return retVal;
	}

	private void processBindingsSheet(Element theBindingsSheet) {
		NodeList tableList = theBindingsSheet.getElementsByTagName("Table");
		Element table = (Element) tableList.item(0);
		NodeList rows = table.getElementsByTagName("Row");
		Element defRow = (Element) rows.item(0);

		int colName = 0;
		int colStrength = 0;
		int colRef = 0;
		for (int j = 0; j < 20; j++) {
			String nextName = cellValue(defRow, j);
			if (nextName == null) {
				continue;
			}
			nextName = nextName.toLowerCase().trim().replace(".", "");
			switch (nextName) {
				case "name":
				case "binding name":
					colName = j;
					break;
				case "reference":
					colRef = j;
					break;
				case "conformance":
					colStrength = j;
					break;
			}
		}

		for (int j = 1; j < rows.getLength(); j++) {
			Element nextRow = (Element) rows.item(j);

			String name = cellValue(nextRow, colName);
			String strength = cellValue(nextRow, colStrength);
			String ref = cellValue(nextRow, colRef);
			if (isNotBlank(name) && isNotBlank(strength)) {
				myBindingStrengths.put(name, strength);
			}
			if (isNotBlank(name) && isNotBlank(ref)) {
				if (ref.startsWith("#")) {
					ref = "http://hl7.org/fhir/ValueSet/" + ref.substring(1);
				} else if (!ref.startsWith("http")) {
					ref = "http://hl7.org/fhir/ValueSet/" + ref;
				}
				myBindingRefs.put(name, ref);
			}
		}
	}

	protected abstract BaseRootType createRootType();

	private void parseParameters(Document theFile, BaseRootType theResource) throws MojoExecutionException {
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
				int colTargetTypes = 0;
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
					} else if ("target types".equals(nextName)) {
						colTargetTypes = j;
					}
				}

				List<SearchParameter> compositeParams = new ArrayList<SearchParameter>();

				for (int j = 1; j < rows.getLength(); j++) {
					Element nextRow = (Element) rows.item(j);
					SearchParameter sp = new SearchParameter(getVersion(), theResource.getName());

					sp.setName(cellValue(nextRow, colName));
					sp.setDescription(cellValue(nextRow, colDesc));
					sp.setType(cellValue(nextRow, colType));
					sp.setPath(cellValue(nextRow, colPath));

					if (StringUtils.isNotBlank(sp.getName()) && !sp.getName().startsWith("!")) {
						if (sp.getType().equals("composite")) {
							compositeParams.add(sp);
						} else {
							theResource.addSearchParameter(sp);
						}
					}

					String targetTypes = cellValue(nextRow, colTargetTypes);
					if (isNotBlank(targetTypes)) {
						for (String next : targetTypes.trim().split("\\s*,\\s*")) {
							if (isNotBlank(next)) {
								if (Character.isUpperCase(next.charAt(0))) {
									sp.addTargetType(next);
								}
							}
						}
					}
				}

				for (SearchParameter nextCompositeParam : compositeParams) {
					// if(true)continue;

					if (isBlank(nextCompositeParam.getPath())) {
						throw new MojoExecutionException(Msg.code(167) + "Composite param " + nextCompositeParam.getName() + " has no path");
					}

					if (nextCompositeParam.getPath().indexOf('&') == -1) {
						throw new MojoExecutionException(Msg.code(168) + "Composite param " + nextCompositeParam.getName() + " has path with no '&': " + nextCompositeParam.getPath());
					}

					String[] parts = nextCompositeParam.getPath().split("\\&");
					List<List<SearchParameter>> compositeOf = new ArrayList<List<SearchParameter>>();

					for (String nextPart : parts) {
						nextPart = nextPart.trim();
						if (isBlank(nextPart)) {
							continue;
						}

						List<SearchParameter> part = new ArrayList<SearchParameter>();
						compositeOf.add(part);

						Set<String> possibleMatches = new HashSet<String>();
						possibleMatches.add(nextPart);
						possibleMatches.add(theResource.getName() + "." + nextPart);
						possibleMatches.add(nextPart.replace("[x]", "-[x]"));
						possibleMatches.add(theResource.getName() + "." + nextPart.replace("[x]", "-[x]"));
						possibleMatches.add(nextPart.replace("-[x]", "[x]"));
						possibleMatches.add(theResource.getName() + "." + nextPart.replace("-[x]", "[x]"));

						for (SearchParameter nextParam : theResource.getSearchParameters()) {
							if (possibleMatches.contains(nextParam.getPath()) || possibleMatches.contains(nextParam.getName())) {
								part.add(nextParam);
							}
						}

						/*
						 * Paths have changed in DSTU2
						 */
						for (SearchParameter nextParam : theResource.getSearchParameters()) {
							if (nextPart.equals("value[x]") && (nextParam.getName().startsWith("value-"))) {
								part.add(nextParam);
							}
							if (nextPart.equals("component-value[x]") && (nextParam.getName().startsWith("component-value-"))) {
								part.add(nextParam);
							}
						}

						if (part.isEmpty()) {
							throw new MojoExecutionException(Msg.code(169) + "Composite param " + nextCompositeParam.getName() + " has path that doesn't seem to correspond to any other params: " + nextPart);
						}

					}

					if (compositeOf.size() > 2) {
						// TODO: change back to exception maybe? Grahame says these aren't allowed..
						ourLog.warn("Composite param " + nextCompositeParam.getName() + " has >2 parts, this isn't supported yet");
						continue;
					}

					for (SearchParameter part1 : compositeOf.get(0)) {
						for (SearchParameter part2 : compositeOf.get(1)) {
							SearchParameter composite = new SearchParameter(getVersion(), theResource.getName());
							theResource.addSearchParameter(composite);
							composite.setName(part1.getName() + "-" + part2.getName());
							composite.setDescription(nextCompositeParam.getDescription());
							composite.setPath(nextCompositeParam.getPath());
							composite.setType("composite");
							composite.setCompositeOf(Arrays.asList(part1.getName(), part2.getName()));
							composite.setCompositeTypes(Arrays.asList(WordUtils.capitalize(part1.getType()), WordUtils.capitalize(part2.getType())));
						}
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
			nextName = nextName.toLowerCase().trim().replace(".", "").replace(" ", "");
			if ("element".equals(nextName)) {
				myColName = i;
			} else if ("ismodifier".equals(nextName)) {
				myColModifier = i;
			} else if ("summary".equals(nextName)) {
				myColSummary = i;
			} else if ("card".equals(nextName)) {
				myColCard = i;
			} else if ("type".equals(nextName)) {
				myColType = i;
			} else if ("binding".equals(nextName)) {
				myColBinding = i;
			} else if ("shortname".equals(nextName)) {
				myColShortName = i;
			} else if ("definition".equals(nextName)) {
				myColDefinition = i;
			} else if ("requirements".equals(nextName)) {
				myColRequirements = i;
			} else if ("v2mapping".equals(nextName)) {
				myColV2Mapping = i;
			}
		}

		if (myColName == -1) {
			throw new IllegalArgumentException(Msg.code(170) + "Unable to determine column: name");
		}
		if (myColModifier == -1) {
			throw new IllegalArgumentException(Msg.code(171) + "Unable to determine column: modifier");
		}
		if (myColCard == -1) {
			throw new IllegalArgumentException(Msg.code(172) + "Unable to determine column: card");
		}
		if (myColType == -1) {
			throw new IllegalArgumentException(Msg.code(173) + "Unable to determine column: type");
		}
		if (myColBinding == -1) {
			throw new IllegalArgumentException(Msg.code(174) + "Unable to determine column: binding");
		}
		if (myColDefinition == -1) {
			throw new IllegalArgumentException(Msg.code(175) + "Unable to determine column: definition");
		}
		if (myColRequirements == -1) {
			throw new IllegalArgumentException(Msg.code(176) + "Unable to determine column: requirements");
		}
		if (myColV2Mapping == -1) {
			throw new IllegalArgumentException(Msg.code(177) + "Unable to determine column: v2 mapping");
		}
	}

	private void parseBasicElements(Element theRowXml, BaseElement theTarget, String theTypeOrNull) {
		String name = cellValue(theRowXml, myColName);
		theTarget.setName(name);

		theTarget.setElementNameAndDeriveParentElementName(name);

		String cardValue = cellValue(theRowXml, myColCard);
		if (cardValue != null && cardValue.contains("..")) {
			String[] split = cardValue.split("\\.\\.");
			theTarget.setCardMin(split[0]);
			theTarget.setCardMax(split[1]);
		}

		String type = theTypeOrNull != null ? theTypeOrNull : cellValue(theRowXml, myColType);
		theTarget.setTypeFromString(type);
		theTarget.setBinding(cellValue(theRowXml, myColBinding));
		theTarget.setShortName(cellValue(theRowXml, myColShortName));
		theTarget.setDefinition(cellValue(theRowXml, myColDefinition));
		theTarget.setRequirement(cellValue(theRowXml, myColRequirements));
		theTarget.setV2Mapping(cellValue(theRowXml, myColV2Mapping));
		theTarget.setSummary(cellValue(theRowXml, myColSummary));
		theTarget.setModifier(cellValue(theRowXml, myColModifier));

		// Per #320
		if ("example".equals(myBindingStrengths.get(theTarget.getBinding()))) {
			theTarget.setBinding(null);
		}

		if (isNotBlank(theTarget.getBinding())) {
			String bindingUrl = myBindingRefs.get(theTarget.getBinding());
			if (isNotBlank(bindingUrl)) {
				theTarget.setBindingUrl(bindingUrl);
			} else {
				bindingUrl = myNameToValueSetUrl.get(theTarget.getBinding());
				if (isNotBlank(bindingUrl)) {
					theTarget.setBindingUrl(bindingUrl);
				}
			}
		}

	}

	/**
	 * Subclasses may override
	 */
	protected void postProcess(BaseElement theTarget) throws MojoFailureException {
		// nothing
	}

}
