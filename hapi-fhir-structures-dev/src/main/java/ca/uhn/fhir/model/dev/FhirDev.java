package ca.uhn.fhir.model.dev;

/*
 * #%L
 * HAPI FHIR Structures - DEV (FHIR Latest)
 * %%
 * Copyright (C) 2014 University Health Network
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

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dev.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.dev.ServerProfileProvider;

public class FhirDev implements IFhirVersion {

	private String myId;

	@Override
	public Object createServerConformanceProvider(RestfulServer theServer) {
		return new ServerConformanceProvider(theServer);
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition) {
		Profile retVal = new Profile();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(new IdDt(myId));
		return retVal;
	}

	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		return new ServerProfileProvider(theRestfulServer.getFhirContext());
	}

}
