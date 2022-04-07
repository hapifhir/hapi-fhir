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
import ca.uhn.fhir.jaxrs.client.JaxRsRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@SuppressWarnings(value= {"serial"})
public class JaxRsClient {

public static void main(String[] args) {
//START SNIPPET: createClient
   
   // Create a client
   FhirContext ctx = FhirContext.forDstu2();
   
   // Create an instance of the JAX RS client factory and
   // set it on the context
   JaxRsRestfulClientFactory clientFactory = new JaxRsRestfulClientFactory(ctx);
   ctx.setRestfulClientFactory(clientFactory);
   
   // This client uses JAX-RS!
   IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
   
//END SNIPPET: createClient
}


}
