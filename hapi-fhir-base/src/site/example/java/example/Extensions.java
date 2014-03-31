package example;

import java.io.IOException;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;

public class Extensions {

public static void main(String[] args) throws DataFormatException, IOException {

	
// START SNIPPET: resourceExtension
// Create an example patient
Patient patient = new Patient();
patient.addIdentifier(IdentifierUseEnum.OFFICIAL, "urn:example", "7000135", null);

// Create an extension
ExtensionDt ext = new ExtensionDt();
ext.setModifier(false);
ext.setUrl("http://example.com/extensions#someext");
ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));

// Add the extension to the resource
patient.addUndeclaredExtension(ext);
//END SNIPPET: resourceExtension


//START SNIPPET: resourceStringExtension
HumanNameDt name = patient.addName();
name.addFamily().setValue("Shmoe");
StringDt given = name.addGiven();
given.setValue("Joe");
ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#moreext", new StringDt("Hello"));
given.addUndeclaredExtension(ext2);
//END SNIPPET: resourceStringExtension

//START SNIPPET: subExtension
ExtensionDt parent = new ExtensionDt(false, "http://example.com#parent");
patient.addUndeclaredExtension(parent);

ExtensionDt child1 = new ExtensionDt(false, "http://example.com#parent", new StringDt("value1"));
parent.addUndeclaredExtension(child1);

ExtensionDt child2 = new ExtensionDt(false, "http://example.com#parent", new StringDt("value1"));
parent.addUndeclaredExtension(child2);
//END SNIPPET: subExtension

String output = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
System.out.println(output);


//START SNIPPET: parseExtension
// Get all extensions (modifier or not) for a given URL
List<ExtensionDt> resourceExts = patient.getUndeclaredExtensionsByUrl("http://fooextensions.com#exts");

// Get all non-modifier extensions regardless of URL
List<ExtensionDt> nonModExts = patient.getUndeclaredExtensions();

//Get all non-modifier extensions regardless of URL
List<ExtensionDt> modExts = patient.getUndeclaredModifierExtensions();
//END SNIPPET: parseExtension

}
	
}
