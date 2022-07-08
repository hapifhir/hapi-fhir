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

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

public class Multitenancy {

//START SNIPPET: enableUrlBaseTenantIdentificationStrategy
	public class MyServer extends RestfulServer {

	@Override
	protected void initialize() {

		setTenantIdentificationStrategy(new UrlBaseTenantIdentificationStrategy());

		// ... do other initialization ...
	}
}
//END SNIPPET: enableUrlBaseTenantIdentificationStrategy

//START SNIPPET: resourceProvider
	public class MyPatientResourceProvider implements IResourceProvider {

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Patient.class;
	}

	@Read
	public Patient read(RequestDetails theRequestDetails, @IdParam IdType theId) {

		String tenantId = theRequestDetails.getTenantId();
		String resourceId = theId.getIdPart();

		// Use these two values to fetch the patient

		return new Patient();
	}
}

//END SNIPPET: resourceProvider

}
