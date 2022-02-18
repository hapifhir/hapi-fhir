package ca.uhn.fhir.model.dstu2;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
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

import ca.uhn.fhir.i18n.Msg;
import java.io.InputStream;
import java.util.Date;

import ca.uhn.fhir.fhirpath.IFhirPath;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.*;
import ca.uhn.fhir.model.dstu2.composite.*;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.provider.dstu2.Dstu2BundleFactory;
import ca.uhn.fhir.util.ReflectionUtil;

public class FhirDstu2 implements IFhirVersion {

	private String myId;

	@Override
	public IFhirPath createFhirPathExecutor(FhirContext theFhirContext) {
		throw new UnsupportedOperationException(Msg.code(578) + "FluentPath is not supported in DSTU2 contexts");
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
		InputStream str = FhirDstu2.class.getResourceAsStream("/ca/uhn/fhir/model/dstu2/fhirversion.properties");
		if (str == null) {
			str = FhirDstu2.class.getResourceAsStream("ca/uhn/fhir/model/dstu2/fhirversion.properties");
		}
		if (str == null) {
			throw new ConfigurationException(Msg.code(579) + "Can not find model property file on classpath: " + "/ca/uhn/fhir/model/dstu2/fhirversion.properties");
		}
		return str;
	}

	@Override
	public IPrimitiveType<Date> getLastUpdated(IBaseResource theResource) {
		return ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
	}

	@Override
	public String getPathToSchemaDefinitions() {
		return "/org/hl7/fhir/instance/model/schema";
	}

	@Override
	public Class<? extends BaseResourceReferenceDt> getResourceReferenceType() {
		return ResourceReferenceDt.class;
	}

	@Override
	public FhirVersionEnum getVersion() {
		return FhirVersionEnum.DSTU2;
	}

	@Override
	public IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext) {
		return new Dstu2BundleFactory(theContext);
	}

	@Override
	public BaseCodingDt newCodingDt() {
		return new CodingDt();
	}

	@Override
	public IIdType newIdType() {
		return new IdDt();
	}


	

	@Override
	public Object getServerVersion() {
		return ReflectionUtil.newInstanceOfFhirServerType("ca.uhn.fhir.model.dstu2.FhirServerDstu2");
	}


}
