package ca.uhn.fhir.tinder.parser;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.tinder.TinderResourceGeneratorMojo;
import ca.uhn.fhir.tinder.TinderStructuresMojo;
import ca.uhn.fhir.tinder.ValueSetGenerator;
import ca.uhn.fhir.tinder.VelocityHelper;
import ca.uhn.fhir.tinder.model.BaseElement;
import ca.uhn.fhir.tinder.model.BaseRootType;
import ca.uhn.fhir.tinder.model.Child;
import ca.uhn.fhir.tinder.model.Composite;
import ca.uhn.fhir.tinder.model.Extension;
import ca.uhn.fhir.tinder.model.Resource;
import ca.uhn.fhir.tinder.model.ResourceBlock;
import ca.uhn.fhir.tinder.model.SimpleChild;
import ca.uhn.fhir.tinder.model.SimpleSetter.Parameter;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class BaseStructureParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseStructureParser.class);
	protected List<BaseRootType> myResources = new ArrayList<BaseRootType>();
	private String myBaseDir;
	private ArrayList<Extension> myExtensions;
	private TreeSet<String> myImports = new TreeSet<String>();
	private boolean myImportsResolved;
	private Map<String, String> myLocallyDefinedClassNames = new HashMap<String, String>();
	private TreeMap<String, String> myNameToDatatypeClass = new TreeMap<String, String>();
	private TreeMap<String, String> myNameToResourceClass = new TreeMap<String, String>();
	private String myPackageBase;
	private String myVersion;
	private boolean myIsRi;
	private FhirContext myCtx;
	private String myFilenamePrefix = "";
	private String myFilenameSuffix = "";
	private String myTemplate = null;
	private File myTemplateFile = null;
	private String myVelocityPath = null;
	private String myVelocityProperties = null;

	public BaseStructureParser(String theVersion, String theBaseDir) throws MojoFailureException {
		myVersion = theVersion;
		myBaseDir = theBaseDir;

		if (myVersion.equals("r4")) {
			myCtx = FhirContext.forR4();
		} else if (myVersion.equals("dstu3")) {
			myCtx = FhirContext.forDstu3();
		} else if (myVersion.equals("dstu2")) {
			myCtx = FhirContext.forDstu2();
		} else if (myVersion.equals("r5")) {
			myCtx = FhirContext.forR5();
		} else {
			throw new MojoFailureException(Msg.code(151) + "Unknown version: " + myVersion);
		}

		myIsRi = myCtx.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3);
	}

	private void addImport(String bindingClass) {
		myImports.add(bindingClass);
	}

	public void addResource(BaseRootType theResource) {
		myResources.add(theResource);
	}

	private void bindValueSets(BaseElement theElement, ValueSetGenerator theVsp) {
		if (isNotBlank(theElement.getBinding())) {
			String bindingClass = theVsp.getClassForValueSetIdAndMarkAsNeeded(theElement.getBinding());
			if (bindingClass != null) {
				ourLog.debug("Adding binding ValueSet class: {}", bindingClass);
				theElement.setBindingClass(bindingClass);
				addImport(bindingClass);
				myLocallyDefinedClassNames.put(bindingClass, "valueset");
			} else {
				ourLog.debug("No binding found for: {}", theElement.getBinding());
				ourLog.debug(" * Valid: {}", new TreeSet<String>(theVsp.getValueSets().keySet()));
			}
		}
		for (BaseElement next : theElement.getChildren()) {
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

	protected FhirVersionEnum determineVersionEnum() throws MojoFailureException {
		return determineVersionEnum(myVersion);
	}

	private String doScanForImportNamesAndReturnFqn(String theNextType) throws MojoFailureException {
		String nextType = Resource.correctName(theNextType);

		if (myIsRi) {
			String unqualifiedTypeName = theNextType;
			if (theNextType.endsWith("Dt")) {
				unqualifiedTypeName = theNextType.substring(0, theNextType.length() - 2);
				try {
					return Class.forName("org.hl7.fhir.dstu3.model." + unqualifiedTypeName + "Type").getName();
				} catch (ClassNotFoundException e1) {
					// not found
				}
			}

			try {
				return Class.forName("org.hl7.fhir.dstu3.model." + unqualifiedTypeName).getName();
			} catch (ClassNotFoundException e) {
				// not found
			}
		}

		if ("Any".equals(nextType)) {
			return (IResource.class.getCanonicalName());
		}
		if ("ExtensionDt".equals(nextType)) {
			return (ExtensionDt.class.getCanonicalName());
		}
		// if ("ResourceReferenceDt".equals(theNextType)) {
		// return "ca.uhn.fhir.model." + myVersion + ".composite." + ResourceReferenceDt.class.getSimpleName();
		// }
		if ("ResourceDt".equals(nextType)) {
			return IResource.class.getCanonicalName();
		}
		if ("Binary".equals(nextType)) {
			return "ca.uhn.fhir.model." + myVersion + ".resource." + Binary.class.getSimpleName();
		}
		if ("ListResource".equals(nextType)) {
			return "ca.uhn.fhir.model." + myVersion + ".resource.ListResource";
		}
		// if ("BoundCodeableConceptDt".equals(theNextType)) {
		// return "ca.uhn.fhir.model." + myVersion + ".composite.BoundCodeableConceptDt";
		// }
		// QuantityCompararatorEnum
		// QuantityComparatorEnum

		if (myLocallyDefinedClassNames.containsKey(nextType)) {
			return nextType;
		} else {
			try {
				String type = myPackageBase + ".composite." + nextType;
				Class.forName(type);
				return (type);
			} catch (ClassNotFoundException e) {
				try {
					String type = "ca.uhn.fhir.model." + myVersion + ".composite." + nextType;
					Class.forName(type);
					return (type);
				} catch (ClassNotFoundException e5) {
					try {
						String type = "ca.uhn.fhir.model." + myVersion + ".resource." + nextType;
						Class.forName(type);
						return (type);
					} catch (ClassNotFoundException e1) {
						try {
							String type = "ca.uhn.fhir.model.primitive." + nextType;
							Class.forName(type);
							return (type);
						} catch (ClassNotFoundException e2) {
							try {
								String type = myPackageBase + ".valueset." + nextType;
								Class.forName(type);
								return (type);
							} catch (ClassNotFoundException e3) {
								try {
									String type = "ca.uhn.fhir.model.api." + nextType;
									Class.forName(type);
									return (type);
								} catch (ClassNotFoundException e4) {
									try {
										String type = "ca.uhn.fhir.model." + myVersion + ".valueset." + nextType;
										Class.forName(type);
										return (type);
									} catch (ClassNotFoundException e6) {
										String fileName = myBaseDir + "/src/main/java/" + myPackageBase.replace('.', '/') + "/composite/" + nextType + ".java";
										File file = new File(fileName);
										if (file.exists()) {
											return myPackageBase + ".composite." + nextType;
										}
										fileName = myBaseDir + "/src/main/java/ca/uhn/fhir/model/primitive/" + nextType + ".java";
										file = new File(fileName);
										if (file.exists()) {
											return "ca.uhn.fhir.model.primitive." + nextType;
										}
										throw new MojoFailureException(Msg.code(152) + "Unknown type: " + nextType + " - Have locally defined names: " + new TreeSet<String>(myLocallyDefinedClassNames.keySet()));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter findAnnotation(Class<?> theBase, Annotation[] theAnnotations, Class<ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter> theClass) {
		for (Annotation next : theAnnotations) {
			if (theClass.equals(next.annotationType())) {
				return (ca.uhn.fhir.model.api.annotation.SimpleSetter.Parameter) next;
			}
		}
		throw new IllegalArgumentException(Msg.code(153) + theBase.getCanonicalName() + " has @" + SimpleSetter.class.getCanonicalName() + " constructor with no/invalid parameter annotation");
	}

	/**
	 * Example: Encounter has an internal block class named "Location", but it also has a reference to the Location
	 * resource type, so we need to use the fully qualified name for that resource reference
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

	public FhirContext getCtx() {
		return myCtx;
	}

	protected String getFilenamePrefix() {
		return myFilenamePrefix != null ? myFilenamePrefix : "";
	}

	public void setFilenamePrefix(String theFilenamePrefix) {
		myFilenamePrefix = theFilenamePrefix;
	}

	protected String getFilenameSuffix() {
		return myFilenameSuffix != null ? myFilenameSuffix : "";
	}

	public void setFilenameSuffix(String theFilenameSuffix) {
		myFilenameSuffix = theFilenameSuffix;
	}

	public Map<String, String> getLocalImports() {
		return myLocallyDefinedClassNames;
	}

	public TreeMap<String, String> getNameToDatatypeClass() {
		return myNameToDatatypeClass;
	}

	public List<BaseRootType> getResources() {
		return myResources;
	}

	protected String getTemplate() {
		return myTemplate;
	}

	public void setTemplate(String theTemplate) {
		myTemplate = theTemplate;
	}

	protected File getTemplateFile() {
		return myTemplateFile;
	}

	public void setTemplateFile(File theTemplateFile) {
		myTemplateFile = theTemplateFile;
	}

	protected String getVelocityPath() {
		return myVelocityPath;
	}

	public void setVelocityPath(String theVelocityPath) {
		myVelocityPath = theVelocityPath;
	}

	public String getVersion() {
		return myVersion;
	}

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
				throw new IllegalStateException(Msg.code(154) + next.getClass() + "");
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
		String retVal = doScanForImportNamesAndReturnFqn(theNextType);
		if (myVersion.equals("dstu2")) {
			retVal = retVal.replace(".dev.", ".dstu2.");
		}

		return retVal;
	}

	private void scanForImportsNames(BaseElement theNext) throws MojoFailureException {
		for (BaseElement next : theNext.getChildren()) {
			ourLog.debug("Element Name: {}", next.getName());
			if (next instanceof SimpleChild) {
				for (String nextType : next.getType()) {
					ourLog.debug("* Element Type: {}", nextType);
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

	public void setExtensions(ArrayList<Extension> theExtensions) {
		myExtensions = theExtensions;
	}

	public void setVelocityProperties(String theVelocityProperties) {
		myVelocityProperties = theVelocityProperties;
	}

	private void write(BaseRootType theResource, File theFile, String thePackageBase) throws IOException, MojoFailureException {
		ArrayList<String> imports = new ArrayList<>();
		for (String next : myImports) {
			next = Resource.correctName(next);
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

		String packageSuffix = "";
		if (determineVersionEnum().isRi()) {
			packageSuffix = "." + myVersion;
		}

		VelocityContext ctx = new VelocityContext();
		ctx.put("includeDescriptionAnnotations", true);
		ctx.put("packageBase", thePackageBase);
		ctx.put("package_suffix", packageSuffix);
		ctx.put("hash", "#");
		ctx.put("imports", imports);
		ctx.put("profile", theResource.getProfile());
		ctx.put("version", myVersion.replace(".", ""));
		ctx.put("versionEnumName", determineVersionEnum().name());
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
		ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());
		ctx.put("isRi", determineVersionEnum().isRi());

		String capitalize = WordUtils.capitalize(myVersion);
		if ("Dstu".equals(capitalize)) {
			capitalize = "Dstu1";
		}
		ctx.put("versionCapitalized", capitalize);
		ctx.put("this", theResource);

		VelocityEngine v = VelocityHelper.configureVelocityEngine(getTemplateFile(), getVelocityPath(), myVelocityProperties);
		InputStream templateIs = null;
		if (getTemplateFile() != null) {
			templateIs = new FileInputStream(getTemplateFile());
		} else {
			templateIs = this.getClass().getResourceAsStream(getTemplate());
		}

		InputStreamReader templateReader = new InputStreamReader(templateIs);
		ByteArrayOutputStream byteArrayWriter = new ByteArrayOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayWriter, Charsets.UTF_8);
		v.evaluate(ctx, outputStreamWriter, "", templateReader);
		outputStreamWriter.flush();

		byte[] bytesToWrite = byteArrayWriter.toByteArray();

		boolean actuallyWrite = false;
		if (!theFile.exists()) {
			actuallyWrite = true;
		} else if (FileUtils.sizeOf(theFile) != bytesToWrite.length) {
			actuallyWrite = true;
		} else {
			byte[] existingBytes = IOUtils.toByteArray(new FileInputStream(theFile));
			if (!Arrays.equals(existingBytes, bytesToWrite)) {
				actuallyWrite = true;
			}
		}

		if (!actuallyWrite) {
			ourLog.info("Skipping writing already up-to-date file: {}", theFile.getAbsolutePath());
			return;
		}

		ourLog.debug("Writing file: {}", theFile.getAbsolutePath());

		try (FileOutputStream fos = new FileOutputStream(theFile, false)) {
			fos.write(bytesToWrite);
			fos.flush();
		}
	}

	public void writeAll(File theOutputDirectory, File theResourceOutputDirectory, String thePackageBase) throws MojoFailureException {
		writeAll(TargetType.SOURCE, theOutputDirectory, theResourceOutputDirectory, thePackageBase);
	}

	public void writeAll(TargetType theTarget, File theOutputDirectory, File theResourceOutputDirectory, String thePackageBase) throws MojoFailureException {
		myPackageBase = thePackageBase;

		if (!theOutputDirectory.exists()) {
			theOutputDirectory.mkdirs();
		}
		if (!theOutputDirectory.isDirectory()) {
			throw new MojoFailureException(Msg.code(155) + theOutputDirectory + " is not a directory");
		}
		if (theResourceOutputDirectory != null) {
			if (!theResourceOutputDirectory.exists()) {
				theResourceOutputDirectory.mkdirs();
			}
			if (!theResourceOutputDirectory.isDirectory()) {
				throw new MojoFailureException(Msg.code(156) + theResourceOutputDirectory + " is not a directory");
			}
		}

		if (!myImportsResolved) {
			ourLog.info("Scanning resources for imports...");
			for (BaseRootType next : myResources) {
				ourLog.debug("Scanning resource for imports {}", next.getName());
				scanForImportsNames(next);
			}
			myImportsResolved = true;
		}

		for (BaseRootType next : myResources) {
			ourLog.debug("Writing Resource {}", next.getName());

			scanForCorrections(next);
			scanForTypeNameConflicts(next);
			fixResourceReferenceClassNames(next, thePackageBase);

			// File f = new File(theOutputDirectory, (next.getDeclaringClassNameComplete()) /*+ getFilenameSuffix()*/ +
			// ".java");
			String elementName = Resource.correctName(next.getElementName());
			String prefix = getFilenamePrefix();
			String suffix = getFilenameSuffix();
			if (theTarget == TargetType.SOURCE) {
				if (!suffix.endsWith(".java")) {
					suffix += ".java";
				}
			}
			String fileName = prefix + elementName + suffix;
			int ix = fileName.lastIndexOf('.');
			String className = ix < 0 ? fileName : fileName.substring(0, ix);
			File f = new File(theOutputDirectory, fileName);
			try {
				write(next, f, thePackageBase);
			} catch (IOException e) {
				throw new MojoFailureException(Msg.code(157) + "Failed to write structure", e);
			}

			if (next instanceof Resource) {
				myNameToResourceClass.put(next.getElementName(), thePackageBase + ".resource." + className);
			} else if (next instanceof Composite) {
				myNameToDatatypeClass.put(next.getElementName(), thePackageBase + ".composite." + className);
			} else {
				throw new IllegalStateException(Msg.code(158) + next.getClass().toString());
			}
		}

		if (theResourceOutputDirectory != null) {

			// Binary is manually generated but should still go in the list
			myNameToResourceClass.put("Binary", thePackageBase + ".resource.Binary");
			myNameToDatatypeClass.put("Extension", ExtensionDt.class.getName());

			if (determineVersionEnum() == FhirVersionEnum.DSTU2) {
				myNameToDatatypeClass.put("boundCode", BoundCodeDt.class.getName());
				myNameToDatatypeClass.put("boundCodeableConcept", ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt.class.getName());
			}

			try {
				File versionFile = new File(theResourceOutputDirectory, "fhirversion.properties");
				OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(versionFile, false), "UTF-8");

				ourLog.debug("Writing file: {}", versionFile.getAbsolutePath());

				String packageSuffix = "";
				if (determineVersionEnum().isRi()) {
					packageSuffix = "." + myVersion;
				}

				VelocityContext ctx = new VelocityContext();
				ctx.put("nameToResourceClass", myNameToResourceClass);
				ctx.put("nameToDatatypeClass", myNameToDatatypeClass);
				ctx.put("version", myVersion.replace(".", ""));
				ctx.put("versionEnumName", determineVersionEnum().name());
				ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());
				ctx.put("isRi", determineVersionEnum().isRi());
				ctx.put("package_suffix", packageSuffix);
				String capitalize = WordUtils.capitalize(myVersion);
				if ("Dstu".equals(capitalize)) {
					capitalize = "Dstu1";
				}
				ctx.put("versionCapitalized", capitalize);

				VelocityEngine v = new VelocityEngine();
				v.setProperty(RuntimeConstants.RESOURCE_LOADERS, "cp");
				v.setProperty("resource.loader.cp.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				v.setProperty("runtime.strict_mode.enable", Boolean.TRUE);

				InputStream templateIs = ResourceGeneratorUsingSpreadsheet.class.getResourceAsStream("/vm/fhirversion_properties.vm");
				InputStreamReader templateReader = new InputStreamReader(templateIs);
				v.evaluate(ctx, w, "", templateReader);

				w.close();
			} catch (IOException e) {
				throw new MojoFailureException(Msg.code(159) + e.getMessage(), e);
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

	public static FhirVersionEnum determineVersionEnum(String version) {
		FhirVersionEnum versionEnum;
		if ("dstu2".equals(version)) {
			versionEnum = FhirVersionEnum.DSTU2;
		} else if ("dstu3".equals(version)) {
			versionEnum = FhirVersionEnum.DSTU3;
		} else if ("r4".equals(version)) {
			versionEnum = FhirVersionEnum.R4;
		} else if ("r5".equals(version)) {
			versionEnum = FhirVersionEnum.R5;
		} else {
			throw new IllegalArgumentException(Msg.code(160) + "Unknown version: " + version);
		}
		return versionEnum;
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
