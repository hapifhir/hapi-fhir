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

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

//START SNIPPET: servlet

/**
 * In this example, we are using Servlet 3.0 annotations to define
 * the URL pattern for this servlet, but we could also
 * define this in a web.xml file.
 */
@WebServlet(urlPatterns= {"/fhir/*"}, displayName="FHIR Server")
public class ExampleRestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	/**
	 * The initialize method is automatically called when the servlet is starting up, so it can
	 * be used to configure the servlet to define resource providers, or set up
	 * configuration, interceptors, etc.
	 */
   @Override
   protected void initialize() throws ServletException {
      /*
       * The servlet defines any number of resource providers, and
       * configures itself to use them by calling
       * setResourceProviders()
       */
      List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
      resourceProviders.add(new RestfulPatientResourceProvider());
      resourceProviders.add(new RestfulObservationResourceProvider());
      setResourceProviders(resourceProviders);
   }
	
}
//END SNIPPET: servlet



