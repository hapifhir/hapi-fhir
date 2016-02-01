package org.hl7.fhir.dstu3.hapi.ctx;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.hapi.rest.server.Dstu3BundleFactory;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerConformanceProvider;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerProfileProvider;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirDstu3 implements IFhirVersion {

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
	public IBaseResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase) {
		StructureDefinition retVal = new StructureDefinition();

		RuntimeResourceDefinition def = theRuntimeResourceDefinition;

		myId = def.getId();
		if (StringUtils.isBlank(myId)) {
			myId = theRuntimeResourceDefinition.getName().toLowerCase();
		}

		retVal.setId(new IdDt(myId));
		return retVal;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<List> getContainedType() {
		return List.class;
	}

	@Override
	public InputStream getFhirVersionPropertiesFile() {
		InputStream str = FhirDstu3.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/fhirversion.properties");
		if (str == null) {
			str = FhirDstu3.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException("Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu3/fhirversion.properties");
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ((Resource) theResource).getMeta().getLastUpdatedElement();
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/instance/model/dstu3/schema";
	}

	@Override
	public Class<? extends IBaseReference> getResourceReferenceType() {
		return Reference.class;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU3;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu3BundleFactory(theContext);
	}

	@Override
	public IBaseCoding newCodingDt() {
		return new Coding();
	}

	@Override
	public IIdType newIdType() {
		return new IdType();
	}

}
