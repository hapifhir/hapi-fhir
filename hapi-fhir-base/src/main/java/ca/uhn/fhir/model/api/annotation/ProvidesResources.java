package ca.uhn.fhir.model.api.annotation;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * IResourceProvider and RestfulServer subclasses can use this annotation to designate which custom resources they can provide.
 * These resources will automatically be added to the resource list used for profile generation.
 * <pre>
 * Examples:
 * {@literal @}ProvidesResources(resource=CustomObservation.class)
 * class CustomObservationResourceProvider implements IResourceProvider{...}
 *
 * {@literal @}ProvidesResources(resource={CustomPatient.class,CustomObservation.class}){...}
 * class FhirServer extends RestfulServer
 * }
 * </pre>
 * Note that you needn't annotate both the IResourceProvider and the RestfulServer for a given resource; either one will suffice.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvidesResources {
    Class[] resources();
}
