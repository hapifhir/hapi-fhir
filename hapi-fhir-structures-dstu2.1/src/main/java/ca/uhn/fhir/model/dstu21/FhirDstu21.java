package ca.uhn.fhir.model.dstu21;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2.1 (FHIR v1.1.x)
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
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu21.composite.CodingDt;
import ca.uhn.fhir.model.dstu21.composite.ContainedDt;
import ca.uhn.fhir.model.dstu21.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu21.resource.StructureDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu21.Dstu21BundleFactory;
import ca.uhn.fhir.rest.server.provider.dstu21.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.provider.dstu21.ServerProfileProvider;

public class FhirDstu21 implements IFhirVersion {

	private String myId;

	@Override
	public ServerConformanceProvider createServerConformanceProvider(RestfulServer theServer) {
		return new ServerConformanceProvider(theServer);
	}

	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		return new ServerProfileProvider(theRestfulServer);
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		StructureDefinition retVal = new StructureDefinition();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(new IdDt(myId));
		return retVal;
	}

	@Override
	public Class<? extends BaseContainedDt> getContainedType() {
		return ContainedDt.class;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDstu21.class.getResourceAsStream("/ca/uhn/fhir/model/dstu21/fhirversion.properties");
		if (str == null) {
			str = FhirDstu21.class.getResourceAsStream("ca/uhn/fhir/model/dstu21/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu21/fhirversion.properties");
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/instance/model/dstu21/schema";
	}

	@Override
	public Class<? extends BaseResourceReferenceDt> getResourceReferenceType() {
		return ResourceReferenceDt.class;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU2_1;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu21BundleFactory(theContext);
	}

	@Override
	public BaseCodingDt newCodingDt() {
		return new CodingDt();
	}

}
