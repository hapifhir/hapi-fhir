package org.hl7.fhir.r4.hapi.ctx;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.hapi.fluentpath.FhirPathR4;
import org.hl7.fhir.r4.hapi.rest.server.R4BundleFactory;
import org.hl7.fhir.r4.model.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ReflectionUtil;

public class FhirR4 implements IFhirVersion {

	private String myId;

	@Override
	public IFhirPath createFhirPathExecutor(FhirContext theFhirContext) {
		return new FhirPathR4(theFhirContext);
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
		String path = "org/hl7/fhir/r4/model/fhirversion.properties";
		InputStream str = FhirR4.class.getResourceAsStream("/" + path);
		if (str == null) {
			str = FhirR4.class.getResourceAsStream(path);
		}
		if (str == null) {
			throw new ConfigurationException(Msg.code(257) + "Can not find model property file on classpath: " + path);
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ((Resource) theResource).getMeta().getLastUpdatedElement();
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/r4/model/schema";
	}

	@Override
	public Class<? extends IBaseReference> getResourceReferenceType() {
		return Reference.class;
	}

	@Override
	public Object getServerVersion() {
		return ReflectionUtil.newInstanceOfFhirServerType("org.hl7.fhir.r4.hapi.ctx.FhirServerR4");
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.R4;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new R4BundleFactory(theContext);
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
