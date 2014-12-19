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

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dev.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.dev.ServerProfileProvider;

import javax.servlet.http.HttpServletRequest;

public class FhirDev implements IFhirVersion {

	private String myId;

	@Override
	public Object createServerConformanceProvider(RestfulServer theServer) {
		return new ServerConformanceProvider(theServer);
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
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
		return new ServerProfileProvider(theRestfulServer);
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DEV;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDev.class.getResourceAsStream("/ca/uhn/fhir/model/dev/fhirversion.properties");
		if (str == null) {
			str = FhirDev.class.getResourceAsStream("ca/uhn/fhir/model/dev/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dev/fhirversion.properties");
		}
		return str;
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "ca/uhn/fhir/model/dev/schema";
	}


}
