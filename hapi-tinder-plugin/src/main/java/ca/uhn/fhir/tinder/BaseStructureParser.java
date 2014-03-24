package ca.uhn.fhir.tinder;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Child;
import ca.uhn.fhir.tinder.model.Composite;
import ca.uhn.fhir.tinder.model.Extension;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.ResourceBlock;
import ca.uhn.fhir.tinder.model.SimpleChild;
import ca.uhn.fhir.tinder.model.SimpleSetter.Parameter;

public abstract class BaseStructureParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseStructureParser.class);
	private ArrayList<Extension> myExtensions;
	private TreeSet<String> myImports = new TreeSet<String>();
	private Map<String, String> myLocallyDefinedClassNames = new HashMap<String, String>();
	private List<BaseRootType> myResources = new ArrayList<BaseRootType>();

	public void addResource(BaseRootType theResource) {
		myResources.add(theResource);
	}

	public Map<String, String> getLocalImports() {
		return myLocallyDefinedClassNames;
	}

	private void bindValueSets(BaseElement theResource, ValueSetGenerator theVsp) {
		if (isNotBlank(theResource.getBinding())) {
			String bindingClass = theVsp.getClassForValueSetIdAndMarkAsNeeded(theResource.getBinding());
			if (bindingClass != null) {
				ourLog.info("Adding binding ValueSet class: {}", bindingClass);
				theResource.setBindingClass(bindingClass);
				myImports.add(bindingClass);
				myLocallyDefinedClassNames.put(bindingClass, "valueset");
			} else {
				ourLog.info("No binding found for: {}", theResource.getBinding());
				ourLog.info(" * Valid: {}", new TreeSet<String>(theVsp.getValueSets().keySet()));
			}
		}
		for (BaseElement next : theResource.getChildren()) {
			bindValueSets(next, theVsp);
		}
	}

	public void bindValueSets(ValueSetGenerator theVsp) {
		for (BaseRootType next : myResources) {
			bindValueSets(next, theVsp);
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

	protected abstract String getFilenameSuffix();

	public List<BaseRootType> getResources() {
		return myResources;
	}

	protected abstract String getTemplate();

	protected boolean isSpreadsheet(String theFileName) {
		return true;
	}

	private void scanForImportsNames(BaseElement theNext) throws MojoFailureException {
		for (BaseElement next : theNext.getChildren()) {
			ourLog.debug("Element Name: {}", next.getName());
			if (next instanceof SimpleChild) {
				for (String nextType : next.getType()) {
					if (((SimpleChild) next).isBoundCode()) {
						scanForImportsNames(((SimpleChild) next).getBoundDatatype());
					}
					if (next.isResourceRef()) {
						scanForImportsNames(ResourceReferenceDt.class.getSimpleName());
					}
					scanForImportsNames(nextType);
				}
			}
			scanForImportsNames(next);
		}
	}

	private void scanForImportsNames(String nextType) throws MojoFailureException {
		if ("Any".equals(nextType)) {
			myImports.add(IResource.class.getCanonicalName());
			return;
		}
		if ("ExtensionDt".equals(nextType)) {
			myImports.add(UndeclaredExtension.class.getCanonicalName());
			return;
		}

		if (myLocallyDefinedClassNames.containsKey(nextType)) {
			myImports.add(nextType);
		} else {
			try {
				String type = "ca.uhn.fhir.model.dstu.composite." + nextType;
				Class.forName(type);
				myImports.add(type);
			} catch (ClassNotFoundException e) {
				try {
					String type = "ca.uhn.fhir.model.dstu.resource." + nextType;
					Class.forName(type);
					myImports.add(type);
				} catch (ClassNotFoundException e1) {
					String type = "ca.uhn.fhir.model.primitive." + nextType;
					try {
						Class.forName(type);
						myImports.add(type);
					} catch (ClassNotFoundException e2) {
						throw new MojoFailureException("Unknown type: " + nextType);
					}
				}
			}
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
				} else {
					p.setDatatype(paramTypes[i].getSimpleName());
				}
				p.setParameter(findAnnotation(childDt, paramAnn[i], SimpleSetter.Parameter.class).name());
				ss.getParameters().add(p);
			}
		}
	}

	private void scanForTypeNameConflicts(BaseElement theResourceBlock, Set<String> theTypeNames) {
		for (BaseElement nextChild : theResourceBlock.getChildren()) {
			if (nextChild instanceof ResourceBlock) {
				ResourceBlock resourceBlock = (ResourceBlock) nextChild;
				String className = resourceBlock.getClassName();
				String newClassName = className;
				int index = 2;
				while (theTypeNames.contains(newClassName)) {
					newClassName = className + (index++);
					resourceBlock.setForcedClassName(newClassName);
				}
				theTypeNames.add(newClassName);

				scanForTypeNameConflicts(resourceBlock, theTypeNames);
			}
		}
	}

	private void scanForTypeNameConflicts(BaseRootType theNext) {
		Set<String> typeNames = new HashSet<String>();
		typeNames.add(theNext.getName());
		scanForTypeNameConflicts(theNext, typeNames);
	}

	public void setExtensions(ArrayList<Extension> theExts) {
		myExtensions = theExts;
	}

	private void write(BaseRootType theResource, File theFile, String thePackageBase) throws IOException {
		FileWriter w = new FileWriter(theFile, false);

		ourLog.info("Writing file: {}", theFile.getAbsolutePath());

		ArrayList<String> imports = new ArrayList<String>();
		for (String next : myImports) {
			if (next.contains(".")) {
				imports.add(next);
			} else {
				imports.add(thePackageBase + "." + myLocallyDefinedClassNames.get(next) + "." + next);
			}
		}

		VelocityContext ctx = new VelocityContext();
		ctx.put("includeDescriptionAnnotations", true);
		ctx.put("packageBase", thePackageBase);
		ctx.put("hash", "#");
		ctx.put("imports", imports);
		ctx.put("profile", theResource.getProfile());
		ctx.put("id", StringUtils.defaultString(theResource.getId()));
		ctx.put("className", theResource.getName()); // HumanName
		ctx.put("classNameComplete", theResource.getName() + getFilenameSuffix()); // HumanNameDt
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

	public void markResourcesForImports() {
		for (BaseRootType next : myResources) {
			if (next instanceof Resource) {
				myLocallyDefinedClassNames.put(next.getName(), "resource");
			} else if (next instanceof Composite) {
				myLocallyDefinedClassNames.put(next.getName() + "Dt", "composite");
			} else {
				throw new IllegalStateException(next.getClass() + "");
			}
		}
	}

	public void writeAll(File theOutputDirectory, String thePackageBase) throws MojoFailureException {
		if (!theOutputDirectory.exists()) {
			theOutputDirectory.mkdirs();
		}
		if (!theOutputDirectory.isDirectory()) {
			throw new MojoFailureException(theOutputDirectory + " is not a directory");
		}

		for (BaseRootType next : myResources) {
			scanForImportsNames(next);
		}

		for (BaseRootType next : myResources) {
			scanForTypeNameConflicts(next);
			fixResourceReferenceClassNames(next, thePackageBase);

			File f = new File(theOutputDirectory, next.getName() + getFilenameSuffix() + ".java");
			try {
				write(next, f, thePackageBase);
			} catch (IOException e) {
				throw new MojoFailureException("Failed to write structure", e);
			}
		}
	}

	/**
	 * Example: Encounter has an internal block class named "Location", but it
	 * also has a reference to the Location resource type, so we need to use the
	 * fully qualified name for that resource reference
	 */
	private void fixResourceReferenceClassNames(BaseElement theNext, String thePackageBase) {
		for (BaseElement next : theNext.getChildren()) {
			fixResourceReferenceClassNames(next, thePackageBase);
		}

		if (theNext.isResourceRef()) {
			for (int i = 0; i < theNext.getType().size(); i++) {
				String nextTypeName= theNext.getType().get(i);
				if ("Any".equals(nextTypeName)) {
					continue;
				}
//				if ("Location".equals(nextTypeName)) {
//					ourLog.info("***** Checking for Location");
//					ourLog.info("***** Imports are: {}", new TreeSet<String>(myImports));
//				}
				boolean found = false;
				for (String nextImport : myImports) {
					if (nextImport.endsWith(".resource."+nextTypeName)) {
//						ourLog.info("***** Found match " + nextImport);
						theNext.getType().set(i, nextImport);
						found=true;
					}
				}
				if (!found) {
					theNext.getType().set(i, thePackageBase+".resource." + nextTypeName);
				}
			}
		}
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

	public static void main(String[] args) throws Exception {
		String base = "/home/t3903uhn/workspace/uhn-fhir-service/";
		TinderStructuresMojo m = new TinderStructuresMojo();
		m.setPackageName("ca.uhn.sailfhirmodel");
		// m.setResourceProfileFiles(new ArrayList<String>());
		// m.getResourceProfileFiles().add(base +
		// "src/main/resources/profile/patient.xml");
		// m.getResourceProfileFiles().add(base +
		// "src/main/resources/profile/organization.xml");
		// m.setResourceValueSetFiles(new ArrayList<String>());
		// m.getResourceValueSetFiles().add(base +
		// "src/main/resources/valueset/valueset-cgta-patientidpool.xml");
		// m.getResourceValueSetFiles().add(base +
		// "src/main/resources/valueset/valueset-cgta-provideridpool.xml");
		m.setTargetDirectory(base + "target/generated-sources/tinder");
		m.setBuildDatatypes(true);
		m.execute();
	}

}
