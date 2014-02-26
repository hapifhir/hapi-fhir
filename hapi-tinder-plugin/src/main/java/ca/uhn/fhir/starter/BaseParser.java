package ca.uhn.fhir.starter;

import static org.apache.commons.lang.StringUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.starter.model.AnyChild;
import ca.uhn.fhir.starter.model.BaseElement;
import ca.uhn.fhir.starter.model.Child;
import ca.uhn.fhir.starter.model.Extension;
import ca.uhn.fhir.starter.model.Resource;
import ca.uhn.fhir.starter.model.ResourceBlock;
import ca.uhn.fhir.starter.model.ResourceBlockCopy;
import ca.uhn.fhir.starter.model.SimpleSetter.Parameter;
import ca.uhn.fhir.starter.util.XMLUtils;

public abstract class BaseParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParser.class);
	private int myColBinding;
	private int myColCard;
	private int myColDefinition;
	private int myColName;
	private int myColRequirements;
	private int myColShortName;
	private int myColType;
	private int myColV2Mapping;
	private String myDirectory;
	private ArrayList<Extension> myExtensions;
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

		Element defRow = (Element) rows.item(0);
		parseFirstRow(defRow);

		Element resourceRow = (Element) rows.item(1);
		Resource resource = new Resource();
		parseBasicElements(resourceRow, resource);

		Map<String, BaseElement> elements = new HashMap<String, BaseElement>();
		elements.put(resource.getElementName(), resource);

//		Map<String,String> blockFullNameToShortName = new HashMap<String,String>();
		
		
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
//				type = type.substring(type.lastIndexOf('.')+1);
				elem=new ResourceBlockCopy();
			} else if (type.equals("*")) {
				elem = new AnyChild();
			} else {
				elem = new Child();
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

		write(resource);

	}
	
	
	

	private void scanForSimpleSetters(Child theElem) {
		Class<?> childDt;
		if (theElem.getReferenceTypesForMultiple().size() == 1) {
			try {
				childDt = Class.forName("ca.uhn.fhir.model.primitive." + theElem.getReferenceTypesForMultiple().get(0));
			} catch (ClassNotFoundException e) {
				return;
			}
		} else {
			return;
		}

		for (Constructor<?> nextConstructor : childDt.getConstructors()) {
			SimpleSetter simpleSetter = nextConstructor.getAnnotation(SimpleSetter.class);
			if (simpleSetter == null) {
				continue;
			}

			ca.uhn.fhir.starter.model.SimpleSetter ss = new ca.uhn.fhir.starter.model.SimpleSetter();
			ss.setDatatype(childDt.getSimpleName());
			ss.setSuffix(simpleSetter.suffix());
			theElem.getSimpleSetters().add(ss);
			
			Annotation[][] paramAnn = nextConstructor.getParameterAnnotations();
			Class<?>[] paramTypes = nextConstructor.getParameterTypes();
			for (int i = 0; i < paramTypes.length; i++) {
				Parameter p = new Parameter();
				p.setDatatype(paramTypes[0].getSimpleName());
				p.setParameter(findAnnotation(childDt, paramAnn[i], SimpleSetter.Parameter.class).name());
				ss.getParameters().add(p);
			}
		}
	}

	private ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter findAnnotation(Class<?> theBase, Annotation[] theAnnotations, Class<ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter> theClass) {
		for (Annotation next : theAnnotations) {
			if (theClass.equals(next.annotationType())) {
				return (ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter) next;
			}
		}
		throw new IllegalArgumentException(theBase.getCanonicalName() + " has @" + SimpleSetter.class.getCanonicalName() + " constructor with no/invalid parameter annotation");
	}

	public void setDirectory(String theDirectory) {
		myDirectory = theDirectory;
	}

	public void setExtensions(ArrayList<Extension> theExts) {
		myExtensions = theExts;
	}

	public void setOutputFile(String theOutputFile) {
		myOutputFile = theOutputFile;
	}

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

	private void write(Resource theResource) throws IOException {
		File f = new File(myOutputFile);
		FileWriter w = new FileWriter(f, false);

		ourLog.info("Writing file: {}", f.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		ctx.put("className", theResource.getName());
		ctx.put("shortName", defaultString(theResource.getShortName()));
		ctx.put("definition", defaultString(theResource.getDefinition()));
		ctx.put("requirements", defaultString(theResource.getRequirement()));
		ctx.put("children", theResource.getChildren());
		ctx.put("resourceBlockChildren", theResource.getResourceBlockChildren());
		ctx.put("childExtensionTypes", ObjectUtils.defaultIfNull(myExtensions, new ArrayList<Extension>()));

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceParser.class.getResourceAsStream(getTemplate());
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	protected abstract String getFilename();

	protected abstract String getTemplate();

	protected void parseBasicElements(Element theRowXml, BaseElement theTarget) {
		String name = cellValue(theRowXml, myColName);
		theTarget.setName(name);

		theTarget.setElementName(name);
		

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

}
