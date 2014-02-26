package ca.uhn.fhir.starter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.ValueSet;

public class ValueSetParser {

	private String myDirectory;
	private String myValueSetName;
	private String myOutputDirectory;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		ValueSetParser p = new ValueSetParser();
		p.setDirectory("src/test/resources/vs/");
		p.setValueSetName("administrative-gender");
		p.setOutputDirectory("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/dstu/valueset/");
		p.parse();
		
	}

	private void parse() throws FileNotFoundException, IOException {
		String string = IOUtils.toString(new FileReader(myDirectory + "valueset-" + myValueSetName + ".xml"));
		ValueSet res = (ValueSet) new FhirContext(ValueSet.class).newXmlParser().parseResource(string);
	}

	private void setOutputDirectory(String theString) {
		myOutputDirectory=theString;
	}

	private void setValueSetName(String theString) {
		myValueSetName = theString;
	}

	public void setDirectory(String theString) {
		myDirectory = theString;
	}
	

}
