package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.context.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.fluentpath.IFluentPath;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public interface IFhirVersion {

	IFluentPath createFluentPathExecutor(FhirContext theFhirContext);
	
	IServerConformanceProvider<? extends IBaseResource> createServerConformanceProvider(RestfulServer theRestfulServer); 
	
	IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer);
	
	IContextValidationSupport<?, ?, ?, ?, ?, ?> createValidationSupport();

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

}
