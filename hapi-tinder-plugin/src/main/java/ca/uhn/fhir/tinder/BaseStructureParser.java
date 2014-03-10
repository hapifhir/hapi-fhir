package ca.uhn.fhir.tinder;

import static org.apache.commons.lang.StringUtils.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.Child;
import ca.uhn.fhir.tinder.model.Extension;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.SimpleSetter.Parameter;

public abstract class BaseStructureParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseStructureParser.class);
	private ArrayList<Extension> myExtensions;
	private List<Resource> myResources = new ArrayList<Resource>();

	public void bindValueSets(ValueSetGenerator theVsp) {
		for (Resource next : myResources) {
			bindValueSets(next, theVsp);
		}
	}

	public void addResource(Resource theResource) {
		myResources.add(theResource);
	}
	

	private void bindValueSets(BaseElement theResource, ValueSetGenerator theVsp) {
		if (isNotBlank(theResource.getBinding())) {
			String bindingClass = theVsp.getClassForValueSetIdAndMarkAsNeeded(theResource.getBinding());
			if (bindingClass!= null) {
				theResource.setBindingClass(bindingClass);
			}else {
				ourLog.info("No binding found for: {}", theResource.getBinding());
				ourLog.info(" * Valid: {}", new TreeSet<String>(theVsp.getValueSets().keySet()));
			}
		}
		for (BaseElement next : theResource.getChildren()) {
			bindValueSets(next, theVsp);
		}
	}


	protected void scanForSimpleSetters(Child theElem) {
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

			ca.uhn.fhir.tinder.model.SimpleSetter ss = new ca.uhn.fhir.tinder.model.SimpleSetter();
			ss.setDatatype(childDt.getSimpleName());
			ss.setSuffix(simpleSetter.suffix());
			theElem.getSimpleSetters().add(ss);

			Annotation[][] paramAnn = nextConstructor.getParameterAnnotations();
			Class<?>[] paramTypes = nextConstructor.getParameterTypes();
			for (int i = 0; i < paramTypes.length; i++) {
				Parameter p = new Parameter();
				if (paramTypes[i].getCanonicalName().startsWith("java.math")) {
					p.setDatatype(paramTypes[i].getCanonicalName());
				}else {
					p.setDatatype(paramTypes[i].getSimpleName());
				}
				p.setParameter(findAnnotation(childDt, paramAnn[i], SimpleSetter.Parameter.class).name());
				ss.getParameters().add(p);
			}
		}
	}

	public List<Resource> getResources() {
		return myResources;
	}

	private ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter findAnnotation(Class<?> theBase, Annotation[] theAnnotations, Class<ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter> theClass) {
		for (Annotation next : theAnnotations) {
			if (theClass.equals(next.annotationType())) {
				return (ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter) next;
			}
		}
		throw new IllegalArgumentException(theBase.getCanonicalName() + " has @" + SimpleSetter.class.getCanonicalName() + " constructor with no/invalid parameter annotation");
	}


	public void setExtensions(ArrayList<Extension> theExts) {
		myExtensions = theExts;
	}


	public void writeAll(File theOutputDirectory, String thePackageBase) throws MojoFailureException {
		if (!theOutputDirectory.exists()) {
			theOutputDirectory.mkdirs();
		}
		if (!theOutputDirectory.isDirectory()) {
			throw new MojoFailureException(theOutputDirectory + " is not a directory");
		}

		for (Resource next : myResources) {
			File f = new File(theOutputDirectory, next.getName() + getFilenameSuffix() + ".java");
			try {
				write(next, f, thePackageBase);
			} catch (IOException e) {
				throw new MojoFailureException("Failed to write structure", e);
			}
		}
	}

	protected abstract String getFilenameSuffix();
	
	private void write(Resource theResource, File theFile, String thePackageBase) throws IOException {
		FileWriter w = new FileWriter(theFile, false);

		ourLog.info("Writing file: {}", theFile.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		ctx.put("includeDescriptionAnnotations", true);
		ctx.put("packageBase", thePackageBase);
		ctx.put("profile", theResource.getProfile());
		ctx.put("className", theResource.getName());
		ctx.put("shortName", defaultString(theResource.getShortName()));
		ctx.put("definition", defaultString(theResource.getDefinition()));
		ctx.put("requirements", defaultString(theResource.getRequirement()));
		ctx.put("children", theResource.getChildren());
		ctx.put("resourceBlockChildren", theResource.getResourceBlockChildren());
		ctx.put("childExtensionTypes", ObjectUtils.defaultIfNull(myExtensions, new ArrayList<Extension>()));
		ctx.put("searchParams", (theResource.getSearchParameters()));

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream(getTemplate());
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	protected abstract String getTemplate();


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

	protected boolean isSpreadsheet(String theFileName) {
		return true;
	}

}
