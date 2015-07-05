package org.hl7.fhir.instance;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
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
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.conf.ServerConformanceProvider;
import org.hl7.fhir.instance.conf.ServerProfileProvider;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.dstu2hl7org.Dstu2Hl7OrgBundleFactory;

public class FhirDstu2Hl7Org implements IFhirVersion {

	private String myId;

	@Override
	public ServerConformanceProvider createServerConformanceProvider(RestfulServer theServer) {
		return new ServerConformanceProvider(theServer);
	}

	@Override
	public StructureDefinition generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		StructureDefinition retVal = new StructureDefinition();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(myId);
		return retVal;
	}

	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		return new ServerProfileProvider(theRestfulServer);
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU2_HL7ORG;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		String path = "/org/hl7/fhir/instance/model/fhirversion.properties";
		InputStream str = FhirDstu2Hl7Org.class.getResourceAsStream(path);
		if (str == null) {
			str = FhirDstu2Hl7Org.class.getResourceAsStream(path.substring(1));
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + path);
		}
		return str;
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/instance/model/schema";
	}

	@Override
	public Class<Reference> getResourceReferenceType() {
		return Reference.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<ArrayList> getContainedType() {
		return ArrayList.class;
	}

	@Override
	public BaseCodingDt newCodingDt() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu2Hl7OrgBundleFactory(theContext);
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ((Resource)theResource).getMeta().getLastUpdatedElement();
	}

}
