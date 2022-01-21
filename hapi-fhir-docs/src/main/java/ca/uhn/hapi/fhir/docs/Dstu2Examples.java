package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

import javax.servlet.ServletException;
import java.util.Collection;

@SuppressWarnings("serial")
public class Dstu2Examples {
   private Collection<IResourceProvider> resourceProviderList;

   public static void main(String[] args) {
      new Dstu2Examples().getResourceTags();
   }

   @SuppressWarnings("unused")
   public void getResourceTags() {
      // START SNIPPET: context
      // Create a DSTU2 context, which will use DSTU2 semantics
      FhirContext ctx = FhirContext.forDstu2();
      
      // This parser supports DSTU2
      IParser parser = ctx.newJsonParser();
      
      // This client supports DSTU2
      IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
      // END SNIPPET: context
   }

   
   // START SNIPPET: server
   public class MyServer extends RestfulServer
   {

      @Override
      protected void initialize() throws ServletException {

         // In your initialize method, assign a DSTU2 FhirContext. This
         // is all that is required in order to put the server
         // into DSTU2 mode
         setFhirContext(FhirContext.forDstu2());
         
         // Then set resource providers as normal, and do any other
         // configuration you need to do.
         setResourceProviders(resourceProviderList);
         
      }
      
   }
   // END SNIPPET: server

   
   public void upgrade() {
      // START SNIPPET: client
      FhirContext ctxDstu2 = FhirContext.forDstu2();
      IGenericClient clientDstu2 = ctxDstu2.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
      
      // END SNIPPET: client
      
   }
   
}
