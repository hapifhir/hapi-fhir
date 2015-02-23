package ca.uhn.fhir.model.dev;

/*
 * #%L
 * HAPI FHIR Structures - DEV (FHIR Latest)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseExtension;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dev.composite.ContainedDt;
import ca.uhn.fhir.model.dev.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dev.resource.Profile;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirDev implements IFhirVersion {

	private String myId;


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
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dev/model.properties");
		}
		return str;
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "ca/uhn/fhir/model/dev/schema";
	}
	
	
	@Override
	public Class<? extends BaseResourceReferenceDt> getResourceReferenceType() {
		return ResourceReferenceDt.class;
	}

	@Override
	public Class<? extends BaseContainedDt> getContainedType() {
		return ContainedDt.class;
	}


	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}


	@Override
	public IServerConformanceProvider<? extends IBaseResource> createServerConformanceProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}


}
