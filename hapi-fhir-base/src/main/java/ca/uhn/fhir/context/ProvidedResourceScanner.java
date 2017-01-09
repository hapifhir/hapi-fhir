package ca.uhn.fhir.context;

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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.annotation.ProvidesResources;

/**
 * Scans a class tagged with {@code ProvidesResources} and adds any resources listed to its FhirContext's resource
 * definition list. This makes the profile generator find the classes.
 *
 * @see ca.uhn.fhir.model.api.annotation.ProvidesResources
 */
public class ProvidedResourceScanner {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ProvidedResourceScanner.class);
	private FhirContext myContext;

	/**
	 * Constructor
	 * 
	 * @param theContext
	 *           - context whose resource definition list is to be updated by the scanner
	 */
	public ProvidedResourceScanner(FhirContext theContext) {
		myContext = theContext;
	}

	/**
	 * If {@code theProvider} is tagged with the {@code ProvidesResources} annotation, this method will add every
	 * resource listed by the {@code resources} method.
	 * <p>
	 * Notes:
	 * </p>
	 * <ul>
	 * <li>if {@code theProvider} isn't annotated with {@code resources} nothing is done; it's expected that most
	 * RestfulServers and ResourceProviders won't be annotated.</li>
	 * <li>any object listed in {@code resources} that doesn't implement {@code IResource} will generate a warning in the
	 * log.</li>
	 * </ul>
	 *
	 * @param theProvider
	 *           - Normally, either a {@link ca.uhn.fhir.rest.server.RestfulServer} or a
	 *           {@link ca.uhn.fhir.rest.server.IResourceProvider} that might be annotated with
	 *           {@link ca.uhn.fhir.model.api.annotation.ProvidesResources}
	 */
	@SuppressWarnings("unchecked")
	public void scanForProvidedResources(Object theProvider) {
		ProvidesResources annotation = theProvider.getClass().getAnnotation(ProvidesResources.class);
		if (annotation == null)
			return;
		for (Class<?> clazz : annotation.resources()) {
			if (IBaseResource.class.isAssignableFrom(clazz)) {
				myContext.getResourceDefinition((Class<? extends IBaseResource>) clazz);
			} else {
				ourLog.warn(clazz.getSimpleName() + "is not assignable from IResource");
			}
		}
	}

	/**
	 * Remove any metadata that was added by any {@code ProvidesResources} annotation
	 * present in {@code theProvider}. This method is callled from {@code RestfulService}
	 * when it is unregistering a Resource Provider.
	 *  
	 * @param theProvider
	 *           - Normally a {@link ca.uhn.fhir.rest.server.IResourceProvider} that might 
	 *           be annotated with {@link ca.uhn.fhir.model.api.annotation.ProvidesResources}
	 */
	public void removeProvidedResources(Object theProvider) {
		ProvidesResources annotation = theProvider.getClass().getAnnotation(ProvidesResources.class);
		if (annotation == null)
			return;
		for (Class<?> clazz : annotation.resources()) {
			if (IBaseResource.class.isAssignableFrom(clazz)) {
				// TODO -- not currently used but should be finished for completeness
			} else {
				ourLog.warn(clazz.getSimpleName() + "is not assignable from IResource");
			}
		}
	}
}
