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

import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

@SuppressWarnings({ "serial" })
//START SNIPPET: provider
public class PagingServer extends RestfulServer {

	public PagingServer() {
		
		/*
		 * Set the resource providers as always. Here we are using the paging
		 * provider from the example below, but it is not strictly necessary
		 * to use a paging resource provider as well. If a normal resource 
		 * provider is used (one which returns List<?> instead of IBundleProvider)
		 * then the loaded resources will be stored by the IPagingProvider.
		 */
		setResourceProviders(new PagingPatientProvider());
		
		/*
		 * Set a paging provider. Here a simple in-memory implementation
		 * is used, but you may create your own. 
		 */
		FifoMemoryPagingProvider pp = new FifoMemoryPagingProvider(10);
		pp.setDefaultPageSize(10);
		pp.setMaximumPageSize(100);
		setPagingProvider(pp);
				
	}

}
//END SNIPPET: provider
