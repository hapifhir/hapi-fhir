package ca.uhn.fhir.model.dstu2;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v0.4.0)
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

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.rest.server.Dstu1BundleFactory;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirDstu2 implements IFhirVersion {

	private String myId;



	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}


	@Override
	public IServerConformanceProvider<? extends IBaseResource> createServerConformanceProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU2;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDstu2.class.getResourceAsStream("/ca/uhn/fhir/model/dstu2/fhirversion.properties");
		if (str == null) {
			str = FhirDstu2.class.getResourceAsStream("ca/uhn/fhir/model/dstu2/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu2/model.properties");
		}
		return str;
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "ca/uhn/fhir/model/dstu2/schema";
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
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public BaseCodingDt newCodingDt() {
		return new ca.uhn.fhir.model.dstu2.composite.CodingDt();
	}


	@Override
	public IVersionSpecificBundleFactory newBundleFactory() {
		throw new UnsupportedOperationException();
	}


}
