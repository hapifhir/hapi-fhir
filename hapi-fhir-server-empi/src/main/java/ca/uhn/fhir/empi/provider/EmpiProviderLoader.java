package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiBatchSvc;
import ca.uhn.fhir.empi.api.IEmpiResetSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.validation.IResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiProviderLoader {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ResourceProviderFactory myResourceProviderFactory;
	@Autowired
	private IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	@Autowired
	private IEmpiPersonMergerSvc myPersonMergerSvc;
	@Autowired
	private IEmpiLinkUpdaterSvc myEmpiLinkUpdaterSvc;
	@Autowired
	private IEmpiLinkQuerySvc myEmpiLinkQuerySvc;
	@Autowired
	private IResourceLoader myResourceLoader;
	@Autowired
	private IEmpiResetSvc myEmpiResetSvc;
	@Autowired
	private IEmpiBatchSvc myEmpiBatchSvc;

	public void loadProvider() {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myResourceProviderFactory.addSupplier(() -> new EmpiProviderDstu3(myFhirContext, myEmpiMatchFinderSvc, myPersonMergerSvc, myEmpiLinkUpdaterSvc, myEmpiLinkQuerySvc, myResourceLoader, myEmpiResetSvc, myEmpiBatchSvc));
				break;
			case R4:
				myResourceProviderFactory.addSupplier(() -> new EmpiProviderR4(myFhirContext, myEmpiMatchFinderSvc, myPersonMergerSvc, myEmpiLinkUpdaterSvc, myEmpiLinkQuerySvc, myResourceLoader, myEmpiResetSvc, myEmpiBatchSvc));
				break;
			default:
				throw new ConfigurationException("EMPI not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}
	}
}

