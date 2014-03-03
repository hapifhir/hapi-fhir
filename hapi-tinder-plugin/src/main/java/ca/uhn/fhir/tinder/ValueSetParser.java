package ca.uhn.fhir.tinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.resource.ValueSet.DefineConcept;
import ca.uhn.fhir.tinder.model.ValueSetTm;
import ca.uhn.fhir.tinder.model.ValueSetTm.Code;

public class ValueSetParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetParser.class);
	private String myDirectory;
	private Set<ValueSetTm> myMarkedValueSets = new HashSet<ValueSetTm>();

	private Map<String, ValueSetTm> myValueSets = new HashMap<String, ValueSetTm>();

	public String getClassForValueSetIdAndMarkAsNeeded(String theId) {
		ValueSetTm vs = myValueSets.get(theId);
		if (vs == null) {
			return null;
		} else {
			myMarkedValueSets.add(vs);
			return vs.getClassName();
		}
	}

	public void parse() throws FileNotFoundException, IOException {
		String string = IOUtils.toString(new FileReader(myDirectory + "valuesets.xml"));
		Bundle bundle = new FhirContext(ValueSet.class).newXmlParser().parseBundle(string);

		int vsCount = 0;
		int conceptCount = 0;
		for (BundleEntry next : bundle.getEntries()) {
			ValueSet nextVs = (ValueSet) next.getResource();
			conceptCount += nextVs.getDefine().getConcept().size();
			ourLog.info("Parsing ValueSetTm #{} - {} - {} concepts total", vsCount++, nextVs.getName().getValue(), conceptCount);
			// output.addConcept(next.getCode().getValue(),
			// next.getDisplay().getValue(), next.getDefinition());

			ValueSetTm vs = new ValueSetTm();

			vs.setName(nextVs.getName().getValue());
			vs.setDescription(nextVs.getDescription().getValue());
			vs.setId(nextVs.getIdentifier().getValue());
			vs.setClassName(toClassName(nextVs.getName().getValue()));

			for (DefineConcept nextConcept : nextVs.getDefine().getConcept()) {
				String nextCodeValue = nextConcept.getCode().getValue();
				String nextCodeDisplay = StringUtils.defaultString(nextConcept.getDisplay().getValue());
				String nextCodeDefinition = StringUtils.defaultString(nextConcept.getDefinition().getValue());

				vs.getCodes().add(new Code(nextCodeValue, nextCodeDisplay, nextCodeDefinition));
			}

			if (myValueSets.containsKey(vs.getName())) {
				ourLog.warn("Duplicate Name: " + vs.getName());
				continue;
			}
			myValueSets.put(vs.getName(), vs);
			
			// if (vsCount > 5) {
			// break;
			// }
		}

	}

	public void setDirectory(String theString) {
		myDirectory = theString;
	}

	public void write(Collection<ValueSetTm> theValueSets, String theOutputDirectory) throws IOException {
		for (ValueSetTm nextValueSetTm : theValueSets) {
			write(nextValueSetTm, theOutputDirectory);
		}
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

	// private void setValueSetName(String theString) {
	// myValueSetName = theString;
	// }

	private void write(ValueSetTm theValueSetTm, String theOutputDirectory) throws IOException {
		File targetDir = new File(theOutputDirectory);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		if (!targetDir.isDirectory()) {
			throw new IOException(theOutputDirectory + " is not a directory");
		}

		File f = new File(theOutputDirectory, theValueSetTm.getClassName() + ".java");
		FileWriter w = new FileWriter(f, false);

		ourLog.info("Writing file: {}", f.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		ctx.put("valueSet", theValueSetTm);

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceSpreadsheetParser.class.getResourceAsStream("/valueset.vm");
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// p.setOutputDirectory("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/dstu/valueset/");
		// p.setOutputDirectory("target/generated/valuesets/ca/uhn/fhir/model/dstu/valueset");
		// p.parse();

	}

	public void writeMarkedValueSets(String theOutputDirectory) throws IOException {
		write(myMarkedValueSets, theOutputDirectory);
	}

}
