package ca.uhn.fhir.tinder.parser;

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
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.tinder.TinderStructuresMojo;
import ca.uhn.fhir.tinder.ValueSetGenerator;
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
	private String myBaseDir;
	private ArrayList<Extension> myExtensions;
	private TreeSet<String> myImports = new TreeSet<String>();
	private boolean myImportsResolved;
	private Map<String, String> myLocallyDefinedClassNames = new HashMap<String, String>();
	private TreeMap<String, String> myNameToDatatypeClass = new TreeMap<String, String>();
	private TreeMap<String, String> myNameToResourceClass = new TreeMap<String, String>();
	private String myPackageBase;
	private List<BaseRootType> myResources = new ArrayList<BaseRootType>();
	private String myVersion;

	public BaseStructureParser(String theVersion, String theBaseDir) {
		myVersion = theVersion;
		myBaseDir = theBaseDir;
	}

	public String getVersion() {
		return myVersion;
	}

	private void addImport(String bindingClass) {
		myImports.add(bindingClass);
	}

	public void addResource(BaseRootType theResource) {
		myResources.add(theResource);
	}

	private void bindValueSets(BaseElement theResource, ValueSetGenerator theVsp) {
		if (isNotBlank(theResource.getBinding())) {
			String bindingClass = theVsp.getClassForValueSetIdAndMarkAsNeeded(theResource.getBinding());
			if (bindingClass != null) {
				ourLog.info("Adding binding ValueSet class: {}", bindingClass);
				theResource.setBindingClass(bindingClass);
				addImport(bindingClass);
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

	public void combineContentMaps(BaseStructureParser theStructureParser) {
		myNameToResourceClass.putAll(theStructureParser.myNameToResourceClass);
		myNameToDatatypeClass.putAll(theStructureParser.myNameToDatatypeClass);
		theStructureParser.myNameToResourceClass.putAll(myNameToResourceClass);
		theStructureParser.myNameToDatatypeClass.putAll(myNameToDatatypeClass);
	}

	private ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter findAnnotation(Class<?> theBase, Annotation[] theAnnotations,
			Class<ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter> theClass) {
		for (Annotation next : theAnnotations) {
			if (theClass.equals(next.annotationType())) {
				return (ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter) next;
			}
		}
		throw new IllegalArgumentException(theBase.getCanonicalName() + " has @" + SimpleSetter.class.getCanonicalName() + " constructor with no/invalid parameter annotation");
	}

	/**
	 * Example: Encounter has an internal block class named "Location", but it also has a reference to the Location resource type, so we need to use the fully qualified name for that resource
	 * reference
	 */
	private void fixResourceReferenceClassNames(BaseElement theNext, String thePackageBase) {
		for (BaseElement next : theNext.getChildren()) {
			fixResourceReferenceClassNames(next, thePackageBase);
		}

		if (theNext.isResourceRef()) {
			for (int i = 0; i < theNext.getType().size(); i++) {
				String nextTypeName = theNext.getType().get(i);
				if ("Any".equals(nextTypeName)) {
					continue;
				}
				// if ("Location".equals(nextTypeName)) {
				// ourLog.info("***** Checking for Location");
				// ourLog.info("***** Imports are: {}", new
				// TreeSet<String>(myImports));
				// }
				boolean found = false;
				for (String nextImport : myImports) {
					if (nextImport.endsWith(".resource." + nextTypeName)) {
						// ourLog.info("***** Found match " + nextImport);
						theNext.getType().set(i, nextImport);
						found = true;
					}
				}
				if (!found) {
					theNext.getType().set(i, thePackageBase + ".resource." + nextTypeName);
				}
			}
		}
	}

	protected abstract String getFilenameSuffix();

	public Map<String, String> getLocalImports() {
		return myLocallyDefinedClassNames;
	}

	public TreeMap<String, String> getNameToDatatypeClass() {
		return myNameToDatatypeClass;
	}

	public List<BaseRootType> getResources() {
		return myResources;
	}

	protected abstract String getTemplate();

	protected boolean isSpreadsheet(String theFileName) {
		return true;
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

	private void scanForCorrections(BaseRootType theNext) {
		if (theNext.getElementName().equals("ResourceReference")) {
			for (BaseElement next : theNext.getChildren()) {
				if (next.getElementName().equals("reference")) {
					next.clearTypes();
					next.setTypeFromString("id");
					scanForSimpleSetters((Child) next);
				}
			}
		}
	}

	private String scanForImportNamesAndReturnFqn(String theNextType) throws MojoFailureException {
		if ("Any".equals(theNextType)) {
			return (IResource.class.getCanonicalName());
		}
		if ("ExtensionDt".equals(theNextType)) {
			return (ExtensionDt.class.getCanonicalName());
		}
		if ("ResourceReferenceDt".equals(theNextType)) {
			return ResourceReferenceDt.class.getCanonicalName();
		}
		if ("Binary".equals(theNextType)) {
			return Binary.class.getCanonicalName();
		}
//		if ("BoundCodeableConceptDt".equals(theNextType)) {
//			return "ca.uhn.fhir.model." + myVersion + ".composite.BoundCodeableConceptDt";
//		}
		// QuantityCompararatorEnum
		// QuantityComparatorEnum

		if (myLocallyDefinedClassNames.containsKey(theNextType)) {
			return (theNextType);
		} else {
			try {
				String type = myPackageBase + ".composite." + theNextType;
				Class.forName(type);
				return (type);
			} catch (ClassNotFoundException e) {
				try {
					String type = "ca.uhn.fhir.model."+myVersion+ ".composite." + theNextType;
					Class.forName(type);
					return (type);
				} catch (ClassNotFoundException e5) {
					try {
						String type = "ca.uhn.fhir.model."+myVersion+".resource." + theNextType;
						Class.forName(type);
						return (type);
					} catch (ClassNotFoundException e1) {
						try {
							String type = "ca.uhn.fhir.model.primitive." + theNextType;
							Class.forName(type);
							return (type);
						} catch (ClassNotFoundException e2) {
							try {
								String type = myPackageBase + ".valueset." + theNextType;
								Class.forName(type);
								return (type);
							} catch (ClassNotFoundException e3) {
								try {
									String type = "ca.uhn.fhir.model.api." + theNextType;
									Class.forName(type);
									return (type);
								} catch (ClassNotFoundException e4) {
									try {
										String type = "ca.uhn.fhir.model."+myVersion+".valueset." + theNextType;
										Class.forName(type);
										return (type);
									} catch (ClassNotFoundException e6) {
										String fileName =  myBaseDir + "/src/main/java/" + myPackageBase.replace('.', '/') + "/composite/" + theNextType + ".java";
										File file = new File(fileName);
										if (file.exists()) {
											return myPackageBase + ".composite." + theNextType;
										}
										fileName =  myBaseDir + "/src/main/java/ca/uhn/fhir/model/primitive/" + theNextType + ".java";
										file = new File(fileName);
										if (file.exists()) {
											return "ca.uhn.fhir.model.primitive." + theNextType;
										}
										throw new MojoFailureException("Unknown type: " + theNextType + " - Have locally defined names: " + new TreeSet<String>(myLocallyDefinedClassNames.keySet()));
									}
								}
							}
						}
					}
				}
			}
		}
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

	private void scanForImportsNames(String theNextType) throws MojoFailureException {
		addImport(scanForImportNamesAndReturnFqn(theNextType));
	}

	protected void scanForSimpleSetters(Child theElem) {
		Class<?> childDt;
		if (theElem.getReferenceTypesForMultiple().size() == 1) {
			try {
				childDt = Class.forName("ca.uhn.fhir.model.primitive." + theElem.getReferenceTypesForMultiple().get(0));
			} catch (ClassNotFoundException e) {
				if (myVersion.equals("dstu")) {
					try {
						childDt = Class.forName("ca.uhn.fhir.model.dstu.composite." + theElem.getReferenceTypesForMultiple().get(0));
					} catch (ClassNotFoundException e2) {
						return;
					}
				} else {
					return;
				}
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
					if (paramTypes[i].getCanonicalName().startsWith("ca.uhn.fhir")) {
						addImport(paramTypes[i].getSimpleName());
					}
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
					for (BaseElement next : resourceBlock.getChildren()) {
						next.setDeclaringClassNameComplete(newClassName);
					}
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

	private String translateClassName(String theName) {
		if ("List".equals(theName)) {
			return "ListResource";
		}
		return theName;
	}

	private void write(BaseRootType theResource, File theFile, String thePackageBase) throws IOException, MojoFailureException {
		FileWriter w = new FileWriter(theFile, false);

		ourLog.info("Writing file: {}", theFile.getAbsolutePath());

		ArrayList<String> imports = new ArrayList<String>();
		for (String next : myImports) {
			if (next.contains(".")) {
				imports.add(next);
			} else {
				String string = myLocallyDefinedClassNames.get(next);
				if (string == null) {
					imports.add(scanForImportNamesAndReturnFqn(next));
				} else {
					imports.add(thePackageBase + "." + string + "." + next);
				}
			}
		}

		VelocityContext ctx = new VelocityContext();
		ctx.put("includeDescriptionAnnotations", true);
		ctx.put("packageBase", thePackageBase);
		ctx.put("hash", "#");
		ctx.put("imports", imports);
		ctx.put("profile", theResource.getProfile());
		ctx.put("version", myVersion);
		ctx.put("id", StringUtils.defaultString(theResource.getId()));
		if (theResource.getDeclaringClassNameComplete() != null) {
			ctx.put("className", theResource.getDeclaringClassNameComplete());
		} else {
			ctx.put("className", (theResource.getName()));
		} // HumanName}
		ctx.put("elementName", theResource.getElementName());
		ctx.put("classNameComplete", (theResource.getName()) + getFilenameSuffix()); // HumanNameDt
		ctx.put("shortName", defaultString(theResource.getShortName()));
		ctx.put("definition", defaultString(theResource.getDefinition()));
		ctx.put("requirements", defaultString(theResource.getRequirement()));
		ctx.put("children", theResource.getChildren());
		ctx.put("resourceBlockChildren", theResource.getResourceBlockChildren());
		ctx.put("childExtensionTypes", ObjectUtils.defaultIfNull(myExtensions, new ArrayList<Extension>()));
		ctx.put("searchParams", (theResource.getSearchParameters()));
		ctx.put("searchParamsReference", (theResource.getSearchParametersResource()));
		ctx.put("searchParamsWithoutComposite", (theResource.getSearchParametersWithoutComposite()));
		ctx.put("includes", (theResource.getIncludes()));

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream(getTemplate());
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	public void writeAll(File theOutputDirectory, File theResourceOutputDirectory, String thePackageBase) throws MojoFailureException {
		myPackageBase = thePackageBase;

		if (!theOutputDirectory.exists()) {
			theOutputDirectory.mkdirs();
		}
		if (!theOutputDirectory.isDirectory()) {
			throw new MojoFailureException(theOutputDirectory + " is not a directory");
		}
		if (theResourceOutputDirectory != null) {
			if (!theResourceOutputDirectory.exists()) {
				theResourceOutputDirectory.mkdirs();
			}
			if (!theResourceOutputDirectory.isDirectory()) {
				throw new MojoFailureException(theResourceOutputDirectory + " is not a directory");
			}
		}

		if (!myImportsResolved) {
			for (BaseRootType next : myResources) {
				ourLog.info("Scanning resource for imports {}", next.getName());
				scanForImportsNames(next);
			}
			myImportsResolved = true;
		}

		for (BaseRootType next : myResources) {
			ourLog.info("Writing Resource {}", next.getName());

			scanForCorrections(next);
			scanForTypeNameConflicts(next);
			fixResourceReferenceClassNames(next, thePackageBase);

			// File f = new File(theOutputDirectory, (next.getDeclaringClassNameComplete()) /*+ getFilenameSuffix()*/ +
			// ".java");
			String elementName = translateClassName(next.getElementName());
			File f = new File(theOutputDirectory, elementName + getFilenameSuffix() + ".java");
			try {
				write(next, f, thePackageBase);
			} catch (IOException e) {
				throw new MojoFailureException("Failed to write structure", e);
			}

			if (next instanceof Resource) {
				myNameToResourceClass.put(next.getElementName(), thePackageBase + ".resource." + elementName);
			} else if (next instanceof Composite) {
				myNameToDatatypeClass.put(next.getElementName(), thePackageBase + ".composite." + elementName + "Dt");
			} else {
				throw new IllegalStateException(next.getClass().toString());
			}
		}

		if (theResourceOutputDirectory != null) {
			try {
				File versionFile = new File(theResourceOutputDirectory, "fhirversion.properties");
				FileWriter w = new FileWriter(versionFile, false);

				ourLog.info("Writing file: {}", versionFile.getAbsolutePath());

				VelocityContext ctx = new VelocityContext();
				ctx.put("nameToResourceClass", myNameToResourceClass);
				ctx.put("nameToDatatypeClass", myNameToDatatypeClass);
				ctx.put("version", myVersion);

				VelocityEngine v = new VelocityEngine();
				v.setProperty("resource.loader", "cp");
				v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				v.setProperty("runtime.references.strict", Boolean.TRUE);

				InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/fhirversion_properties.vm");
				InputStreamReader templateReader = new InputStreamReader(templateIs);
				v.evaluate(ctx, w, "", templateReader);

				w.close();
			} catch (IOException e) {
				throw new MojoFailureException(e.getMessage(), e);
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
