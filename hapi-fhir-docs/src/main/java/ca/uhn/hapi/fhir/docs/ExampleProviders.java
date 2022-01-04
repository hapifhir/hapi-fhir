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

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings(value= {"serial"})
public class ExampleProviders {

	
//START SNIPPET: plainProvider
public class MyPlainProvider {

  /**
   * This method is a Patient search, but HAPI can not automatically
   * determine the resource type so it must be explicitly stated.
   */
  @Search(type=Patient.class)
  public Bundle searchForPatients(@RequiredParam(name=Patient.SP_NAME) StringType theName) {
    Bundle retVal = new Bundle();
    // perform search
    return retVal;
  }	
	
}
//END SNIPPET: plainProvider


//START SNIPPET: plainProviderServer
public class ExampleServlet extends ca.uhn.fhir.rest.server.RestfulServer {

    /**
     * Constructor
     */
  public ExampleServlet() {
    /*
     * Plain providers are passed to the server in the same way
     * as resource providers. You may pass both resource providers
     * and plain providers to the same server if you like.
     */
    registerProvider(new MyPlainProvider());
    
    List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
    // ...add some resource providers...
	  registerProviders(resourceProviders);
  }
	
}
//END SNIPPET: plainProviderServer

    //START SNIPPET: addressStrategy
    public class MyServlet extends ca.uhn.fhir.rest.server.RestfulServer {

        /**
         * Constructor
         */
        public MyServlet() {

            String serverBaseUrl = "http://foo.com/fhir";
            setServerAddressStrategy(new HardcodedServerAddressStrategy(serverBaseUrl));

            // ...add some resource providers, etc...
            List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
            setResourceProviders(resourceProviders);
        }

    }
//END SNIPPET: addressStrategy



}
