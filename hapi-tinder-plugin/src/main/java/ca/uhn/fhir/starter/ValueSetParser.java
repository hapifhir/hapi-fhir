package ca.uhn.fhir.starter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.resource.ValueSet.DefineConcept;

public class ValueSetParser {

	private String myDirectory;
	private String myValueSetName;
	private String myOutputDirectory;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		ValueSetParser p = new ValueSetParser();
		p.setDirectory("src/test/resources/vs/");
//		p.setBundle()
//		p.setValueSetName("administrative-gender");
		p.setOutputDirectory("../hapi-fhir-base/src/main/java/ca/uhn/fhir/model/dstu/valueset/");
		p.parse();
		
	}

	private void parse() throws FileNotFoundException, IOException {
		String string = IOUtils.toString(new FileReader(myDirectory + "valueset-" + myValueSetName + ".xml"));
		ValueSet input = (ValueSet) new FhirContext(ValueSet.class).newXmlParser().parseResource(string);
		ca.uhn.fhir.starter.model.ValueSet output = new ca.uhn.fhir.starter.model.ValueSet();
		
		for (DefineConcept next : input.getDefine().getConcept()) {
//			output.addConcept(next.getCode().getValue(), next.getDisplay().getValue(), next.getDefinition());
		}
		
	}

	private void setOutputDirectory(String theString) {
		myOutputDirectory=theString;
	}

//	private void setValueSetName(String theString) {
//		myValueSetName = theString;
//	}

	public void setDirectory(String theString) {
		myDirectory = theString;
	}
	

}
