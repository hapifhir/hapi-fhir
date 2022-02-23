package org.hl7.fhir.dstu2016may.hapi.ctx;

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
import org.hl7.fhir.dstu2016may.hapi.rest.server.Dstu2_1BundleFactory;
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ReflectionUtil;

public class FhirDstu2_1 implements IFhirVersion {

	private String myId;

	@Override
	public IFhirPath createFhirPathExecutor(FhirContext theFhirContext) {
		throw new UnsupportedOperationException(Msg.code(466) + "FluentPath is not supported in DSTU2 contexts");
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
		InputStream str = FhirDstu2_1.class.getResourceAsStream("/org/hl7/fhir/dstu2016may/model/fhirversion.properties");
		if (str == null) {
			str = FhirDstu2_1.class.getResourceAsStream("/org/hl7/fhir/dstu2016may/model/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException(Msg.code(467) + "Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu2016may/fhirversion.properties");
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ((Resource) theResource).getMeta().getLastUpdatedElement();
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/dstu2016may/model/schema";
	}

	@Override
	public Class<? extends IBaseReference> getResourceReferenceType() {
		return Reference.class;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU2_1;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu2_1BundleFactory(theContext);
	}

	@Override
	public IBaseCoding newCodingDt() {
		return new Coding();
	}

	@Override
	public IIdType newIdType() {
		return new IdType();
	}

	@Override
	public Object getServerVersion() {
		return ReflectionUtil.newInstanceOfFhirServerType("org.hl7.fhir.dstu2016may.hapi.ctx.FhirServerDstu2_1");
	}

}
