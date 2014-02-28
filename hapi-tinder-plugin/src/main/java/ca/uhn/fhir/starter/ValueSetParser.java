package ca.uhn.fhir.starter;

import static org.apache.commons.lang.StringUtils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import ca.uhn.fhir.starter.model.ValueSetTm;
import ca.uhn.fhir.starter.model.ValueSetTm.Code;

public class ValueSetParser {

	private String myDirectory;
	private String myValueSetName;
	private String myOutputDirectory;

	public static void main(String[] args) throws FileNotFoundException, IOException {

		ValueSetParser p = new ValueSetParser();
		p.setDirectory("src/test/resources/vs/");
//		p.setOutputDirectory("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/dstu/valueset/");
		p.setOutputDirectory("target/generated/valuesets/ca/uhn/fhir/model/dstu/valueset");
		p.parse();

	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValueSetParser.class);

	private void parse() throws FileNotFoundException, IOException {
		String string = IOUtils.toString(new FileReader(myDirectory + "valuesets.xml"));
		Bundle bundle = new FhirContext(ValueSet.class).newXmlParser().parseBundle(string);

		int vsCount = 0;
		int conceptCount = 0;
		List<ca.uhn.fhir.starter.model.ValueSetTm> valueSets = new ArrayList<>();
		for (BundleEntry next : bundle.getEntries()) {
			ValueSet nextVs = (ValueSet) next.getResource();
			conceptCount += nextVs.getDefine().getConcept().size();
			ourLog.info("Parsing ValueSetTm #{} - {} - {} concepts total", vsCount++, nextVs.getName().getValue(), conceptCount);
			// output.addConcept(next.getCode().getValue(),
			// next.getDisplay().getValue(), next.getDefinition());

			ValueSetTm vs = new ValueSetTm();
			valueSets.add(vs);
			
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

			if (vsCount > 5) {
				break;
			}
		}

		write(valueSets);

	}

	private void write(List<ValueSetTm> theValueSets) throws IOException {
		for (ValueSetTm nextValueSetTm : theValueSets) {
			write(nextValueSetTm);
		}
	}

	private void write(ValueSetTm theValueSetTm) throws IOException {
		File targetDir = new File(myOutputDirectory);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		if (!targetDir.isDirectory()) {
			throw new IOException(myOutputDirectory + " is not a directory");
		}

		File f = new File(myOutputDirectory, theValueSetTm.getClassName() + ".java");
		FileWriter w = new FileWriter(f, false);

		ourLog.info("Writing file: {}", f.getAbsolutePath());

		VelocityContext ctx = new VelocityContext();
		ctx.put("valueSet", theValueSetTm);

		VelocityEngine v = new VelocityEngine();
		v.setProperty("resource.loader", "cp");
		v.setProperty("cp.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		v.setProperty("runtime.references.strict", Boolean.TRUE);

		InputStream templateIs = ResourceParser.class.getResourceAsStream("/valueset.vm");
		InputStreamReader templateReader = new InputStreamReader(templateIs);
		v.evaluate(ctx, w, "", templateReader);

		w.close();
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
			if (StringUtils.isAllUpperCase(next)) {
				next = WordUtils.capitalize(next);
			}
			b.append(WordUtils.capitalize(next));
		}
		return b.toString();
	}

	private void setOutputDirectory(String theString) {
		myOutputDirectory = theString;
	}

	// private void setValueSetName(String theString) {
	// myValueSetName = theString;
	// }

	public void setDirectory(String theString) {
		myDirectory = theString;
	}

}
