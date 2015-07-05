package ca.uhn.fhir.rest.annotation;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.server.IDynamicSearchResourceProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;


/**
 * RESTful method annotation used for a method which provides
 * the FHIR "search" method.
 * 
 * See the <a href="http://hl7.org/implement/standards/fhir/http.html#search">FHIR Search</a> definition
 * for more information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Search {
	
	/**
	 * If specified, this the name for the Named Query
	 * 
	 * <p>
	 * See the FHIR specification section on 
	 * 	<a href="http://www.hl7.org/implement/standards/fhir/search.html#advanced">named queries</a>
	 * </p>
	 */
	String queryName() default "";

	/**
	 * If specified, this the name for the compartment
	 * 
	 * <p>
	 * See the FHIR specification section on 
	 * 	<a href="http://hl7-fhir.github.io/extras.html#compartments">compartments</a>
	 * </p>
	 */
	String compartmentName() default "";

	/**
	 * The return type for this method. This generally does not need
	 * to be populated for {@link IResourceProvider resource providers} in a server implementation, 
	 * but often does need to be populated in client implementations using {@link IBasicClient} or
	 * {@link IRestfulClient}, or in plain providers on a server.
	 * <p>
	 * This value also does not need to be populated if the return type for a method annotated with 
	 * this annotation is sufficient to determine the type of resource provided. E.g. if the 
	 * method returns <code>Patient</code> or <code>List&lt;Patient&gt;</code>, the server/client 
	 * will automatically determine that the Patient resource is the return type, and this value
	 * may be left blank. 
	 * </p>
	 */
	// NB: Read, Search (maybe others) share this annotation method, so update the javadocs everywhere
	Class<? extends IBaseResource> type() default IBaseResource.class;

	/**
	 * This is an experimental option - Use with caution
	 * 
	 * @see IDynamicSearchResourceProvider
	 */
	boolean dynamic() default false;
	
}
