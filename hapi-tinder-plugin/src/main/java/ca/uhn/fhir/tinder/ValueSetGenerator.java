package ca.uhn.fhir.tinder;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystem;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept;
import ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeIncludeConcept;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.tinder.TinderStructuresMojo.ValueSetFileDefinition;
import ca.uhn.fhir.tinder.model.ValueSetTm;
import ca.uhn.fhir.tinder.parser.TargetType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class ValueSetGenerator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetGenerator.class);
	private int myConceptCount;
	private Set<ValueSetTm> myMarkedValueSets = new HashSet<ValueSetTm>();
	private List<ValueSetFileDefinition> myResourceValueSetFiles;
	private int myValueSetCount;
	private Map<String, ValueSetTm> myValueSets = new HashMap<String, ValueSetTm>();
	private String myVersion;
	private String myFilenamePrefix = "";
	private String myFilenameSuffix = "";
	private String myTemplate = null;
	private File myTemplateFile = null;
	private String myVelocityPath = null;
	private String myVelocityProperties = null;

	public ValueSetGenerator(String theVersion) {
		myVersion = theVersion;
	}

	private void addDefinedConcept(ValueSetTm vs, String system, CodeSystemConcept nextConcept) {
		String nextCodeValue = nextConcept.getCode();
		String nextCodeDisplay = StringUtils.defaultString(nextConcept.getDisplay());
		String nextCodeDefinition = StringUtils.defaultString(nextConcept.getDefinition());
		vs.addConcept(system, nextCodeValue, nextCodeDisplay, nextCodeDefinition);
		for (CodeSystemConcept nextChild : nextConcept.getConcept()) {
			addDefinedConcept(vs, system, nextChild);
		}
	}


	public String getClassForValueSetIdAndMarkAsNeeded(String theId) {
		ValueSetTm vs = myValueSets.get(theId);
		if (vs == null) {
			return null;
		} else {
			myMarkedValueSets.add(vs);
			return vs.getClassName();
		}
	}

	public Map<String, ValueSetTm> getValueSets() {
		return myValueSets;
	}

	public void parse() throws FileNotFoundException, IOException {
		FhirContext ctx = FhirContext.forDstu2();
		IParser newXmlParser = ctx.newXmlParser();
		newXmlParser.setParserErrorHandler(new LenientErrorHandler(false));

		ourLog.info("Parsing built-in ValueSets");
		String version = myVersion;
		if (version.equals("dev")) {
			version = "dstu2";
		}

		String name = "/vs/" + version + "/all-valuesets-bundle.xml";
		if (version.equals("dstu2")) {
			name = "/org/hl7/fhir/instance/model/valueset/valuesets.xml";
		}
		if (version.equals("dstu3")) {
			name = "/org/hl7/fhir/dstu3/model/valueset/valuesets.xml";
		}
		ourLog.info("Loading valuesets from: {}", name);
		InputStream is = ValueSetGenerator.class.getResourceAsStream(name);
		if (null == is) {
			ourLog.error("Failed loading valuesets from: " + name);
			throw new FileNotFoundException(Msg.code(88) + name);
		}
		String vs = IOUtils.toString(is, Charset.defaultCharset());
		if ("dstu2".equals(myVersion)) {
			ca.uhn.fhir.model.dstu2.resource.Bundle bundle = newXmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, vs);
			for (Entry nextEntry : bundle.getEntry()) {
				ca.uhn.fhir.model.dstu2.resource.ValueSet nextVs = (ca.uhn.fhir.model.dstu2.resource.ValueSet) nextEntry.getResource();
				parseValueSet(nextVs);
			}
		} else {
			throw new IllegalStateException(Msg.code(89) + "Fhir version not supported");
		}

		if (myResourceValueSetFiles != null) {
			for (ValueSetFileDefinition next : myResourceValueSetFiles) {
				File file = new File(next.getValueSetFile());
				ourLog.info("Parsing ValueSet file: {}" + file.getName());
				vs = IOUtils.toString(new FileReader(file));
				ValueSetTm tm;
				if ("dstu".equals(myVersion)) {
					ValueSet nextVs = (ValueSet) newXmlParser.parseResource(ValueSet.class, vs);
					tm = parseValueSet(nextVs);
				} else {
					ca.uhn.fhir.model.dstu2.resource.ValueSet nextVs = (ca.uhn.fhir.model.dstu2.resource.ValueSet) newXmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.ValueSet.class, vs);
					tm = parseValueSet(nextVs);
				}
				if (tm != null) {
					myMarkedValueSets.add(tm);
				}
			}
		}
		
		/*
		 *	Purge empty valuesets 
		 */
		for (Iterator<java.util.Map.Entry<String, ValueSetTm>> iter = myValueSets.entrySet().iterator(); iter.hasNext(); ) {
			java.util.Map.Entry<String, ValueSetTm> next = iter.next();
			if (next.getValue().getCodes().isEmpty()) {
				iter.remove();
				continue;
			}
		}
		

		// File[] files = new
		// File(myResourceValueSetFiles).listFiles((FilenameFilter) new
		// WildcardFileFilter("*.xml"));
		// for (File file : files) {
		// ourLog.info("Parsing ValueSet file: {}" + file.getName());
		// vs = IOUtils.toString(new FileReader(file));
		// ValueSet nextVs = (ValueSet) newXmlParser.parseResource(vs);
		// parseValueSet(nextVs);
		// }

	}

	private ValueSetTm parseValueSet(ca.uhn.fhir.model.dstu2.resource.ValueSet nextVs) {
		myConceptCount += nextVs.getCodeSystem().getConcept().size();
		ourLog.debug("Parsing ValueSetTm #{} - {} - {} concepts total", myValueSetCount++, nextVs.getName(), myConceptCount);
		// output.addConcept(next.getCode().getValue(),
		// next.getDisplay().getValue(), next.getDefinition());

		ValueSetTm vs = new ValueSetTm();

		vs.setName(nextVs.getName());
		vs.setDescription(nextVs.getDescription());
		vs.setId(StringUtils.defaultString(nextVs.getIdentifier().getValue()));
		vs.setClassName(toClassName(nextVs.getName()));

		{
			CodeSystem define = nextVs.getCodeSystem();
			String system = define.getSystemElement().getValueAsString();
			for (CodeSystemConcept nextConcept : define.getConcept()) {
				addDefinedConcept(vs, system, nextConcept);
			}
		}

		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.ComposeInclude nextInclude : nextVs.getCompose().getInclude()) {
			String system = nextInclude.getSystemElement().getValueAsString();
			for (ComposeIncludeConcept nextConcept : nextInclude.getConcept()) {
				String nextCodeValue = nextConcept.getCode();
				vs.addConcept(system, nextCodeValue, null, null);
			}
		}

//		if (vs.getCodes().isEmpty()) {
//			ourLog.info("ValueSet " + nextVs.getName() + " has no codes, not going to generate any code for it");
//			return null;
//		}
		
		if (myValueSets.containsKey(vs.getName())) {
			ourLog.warn("Duplicate Name: " + vs.getName());
		} else {
			myValueSets.put(vs.getName(), vs);
		}

		// This is hackish, but deals with "Administrative Gender Codes" vs "AdministrativeGender"
		if (vs.getName().endsWith(" Codes")) {
			myValueSets.put(vs.getName().substring(0, vs.getName().length() - 6).replace(" ", ""), vs);
		}
		myValueSets.put(vs.getName().replace(" ", ""), vs);

		return vs;
	}

	public void setFilenamePrefix(String theFilenamePrefix) {
		myFilenamePrefix = theFilenamePrefix;
	}

	public void setFilenameSuffix(String theFilenameSuffix) {
		myFilenameSuffix = theFilenameSuffix;
	}

	public void setResourceValueSetFiles(List<ValueSetFileDefinition> theResourceValueSetFiles) {
		myResourceValueSetFiles = theResourceValueSetFiles;
	}

	public void setTemplate(String theTemplate) {
		myTemplate = theTemplate;
	}

	public void setTemplateFile (File theTemplateFile) {
		myTemplateFile = theTemplateFile;
	}

	public void setVelocityPath(String theVelocityPath) {
		myVelocityPath = theVelocityPath;
	}

	public void setVelocityProperties(String theVelocityProperties) {
		myVelocityProperties = theVelocityProperties;
	}

	private String toClassName(String theValue) {
		StringBuilder b = new StringBuilder();
		for (String next : theValue.split("\\s+")) {
			next = next.trim();
			if (StringUtils.isBlank(next)) {
				continue;
			}
			if (next.startsWith("(") && next.endsWith(")")) {
				continue;
			}
			next = next.replace("/", "");
			next = next.replace("-", "");
			next = next.replace(',', '_');
			next = next.replace('.', '_');
			if (next.contains(" ")) {
				next = WordUtils.capitalizeFully(next);
			}
			b.append(next);
		}

		b.append("Enum");
		return b.toString();
	}

	public void write(Collection<ValueSetTm> theValueSets, File theOutputDirectory, String thePackageBase) throws IOException {
		write(TargetType.SOURCE, theValueSets, theOutputDirectory, thePackageBase);
	}

	public void write(TargetType theTarget, Collection<ValueSetTm> theValueSets, File theOutputDirectory, String thePackageBase) throws IOException {
		for (ValueSetTm nextValueSetTm : theValueSets) {
			write(theTarget, nextValueSetTm, theOutputDirectory, thePackageBase);
		}
	}

	// private void setValueSetName(String theString) {
	// myValueSetName = theString;
	// }

	private void write(TargetType theTarget, ValueSetTm theValueSetTm, File theOutputDirectory, String thePackageBase) throws IOException {
		if (!theOutputDirectory.exists()) {
			theOutputDirectory.mkdirs();
		}
		if (!theOutputDirectory.isDirectory()) {
			throw new IOException(Msg.code(90) + theOutputDirectory + " is not a directory");
		}

		String valueSetName = theValueSetTm.getClassName();
		String prefix = myFilenamePrefix;
		String suffix = myFilenameSuffix;
		if (theTarget == TargetType.SOURCE) {
			if (!suffix.endsWith(".java")) {
				suffix += ".java";
			}
		}
		String fileName = prefix + valueSetName + suffix;
		File f = new File(theOutputDirectory, fileName);
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8");

		ourLog.debug("Writing file: {}", f.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		InputStream templateIs = null;
		ctx.put("valueSet", theValueSetTm);
		ctx.put("packageBase", thePackageBase);
		ctx.put("esc", new TinderResourceGeneratorMojo.EscapeTool());

		VelocityEngine v = VelocityHelper.configureVelocityEngine(myTemplateFile, myVelocityPath, myVelocityProperties);
		if (myTemplateFile != null) {
			templateIs = new FileInputStream(myTemplateFile);
		} else {
			String templateName = myTemplate;
			if (null == templateName) {
				templateName = "/vm/valueset.vm";
			}
			templateIs = this.getClass().getResourceAsStream(templateName);
		}

		InputStreamReader templateReader = new InputStreamReader(templateIs, "UTF-8");
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	public void writeMarkedValueSets(File theOutputDirectory, String thePackageBase) throws MojoFailureException {
		writeMarkedValueSets(TargetType.SOURCE, theOutputDirectory, thePackageBase);
	}

	public void writeMarkedValueSets(TargetType theTarget, File theOutputDirectory, String thePackageBase) throws MojoFailureException {
		try {
			write(theTarget, myMarkedValueSets, theOutputDirectory, thePackageBase);
		} catch (IOException e) {
			throw new MojoFailureException(Msg.code(91) + "Failed to write valueset", e);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		ValueSetGenerator p = new ValueSetGenerator("dstu1");
		p.parse();

	}

}
