package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;

import java.io.IOException;

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
