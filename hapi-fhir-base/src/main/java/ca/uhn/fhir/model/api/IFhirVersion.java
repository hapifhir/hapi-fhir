package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.io.InputStream;
import java.util.Date;

import ca.uhn.fhir.fhirpath.IFhirPath;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;

/**
 * Each structure version JAR will have an implementation of this interface.
 * This is used internally by HAPI and subject to change. Do not use this interface
 * directly in user code.
 * 
 * See also IFhirVersionServer for the hapi-fhir-server equivalent.
 */
public interface IFhirVersion {

	IFhirPath createFhirPathExecutor(FhirContext theFhirContext);

	IBaseResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition, String theServerBase);

	Class<?> getContainedType();

	InputStream getFhirVersionPropertiesFile();

	IPrimitiveType<Date> getLastUpdated(IBaseResource theResource);

	String getPathToSchemaDefinitions();

	Class<? extends IBase> getResourceReferenceType();

	FhirVersionEnum getVersion();

	IVersionSpecificBundleFactory newBundleFactory(FhirContext theContext);

	IBase newCodingDt();

	IIdType newIdType();

	/**
	 * Returns an instance of <code>IFhirVersionServer<code> for this version.
	 * Note that this method may only be called if the <code>hapi-fhir-server</code>
	 * JAR is on the classpath. Otherwise it will result in a {@link ClassNotFoundException}
	 */
	Object getServerVersion();

}
