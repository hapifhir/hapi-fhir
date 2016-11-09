package example;

import java.io.IOException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

public class Parser {

public static void main(String[] args) throws DataFormatException, IOException {
   {
//START SNIPPET: disableStripVersions
FhirContext ctx = FhirContext.forDstu2();
IParser parser = ctx.newJsonParser();

// Disable the automatic stripping of versions from references on the parser
parser.setStripVersionsFromReferences(false);
//END SNIPPET: disableStripVersions

//START SNIPPET: disableStripVersionsCtx
ctx.getParserOptions().setStripVersionsFromReferences(false);
//END SNIPPET: disableStripVersionsCtx

   }
   
   {
//START SNIPPET: disableStripVersionsField
FhirContext ctx = FhirContext.forDstu2();
IParser parser = ctx.newJsonParser();

// Preserve versions only on these two fields (for the given parser)
parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference", "Patient.managingOrganization");

// You can also apply this setting to the context so that it will 
// flow to all parsers
ctx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference", "Patient.managingOrganization");
//END SNIPPET: disableStripVersionsField

   }
}	
}
