package ca.uhn.fhir.rest.annotation;

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
	 * to be populated for IResourceProvider instances in a server implementation, 
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
	 * This method allows the return type for this method to be specified in a
	 * non-type-specific way, using the text name of the resource, e.g. "Patient".
	 *
	 * This attribute should be populate, or {@link #type()} should be, but not both.
	 *
	 * @since 5.4.0
	 */
	String typeName() default "";

	/**
	 * In a REST server, should this method be invoked even if it does not have method parameters 
	 * which correspond to all of the URL parameters passed in by the client (default is <code>false</code>).
	 * <p>
	 * Use this method with caution: Methods marked with a value of <code>true</code> will
	 * be greedy, meaning they may handle invocations you had intended to be handled by other
	 * search methods. Such a method may be invoked as long as any method parameters
	 * marked as {@link RequiredParam required} have been satisfied. If there are other methods
	 * which have parameters marked as {@link OptionalParam optional} which would technically be
	 * a better match, either the this method or the other method might be called.
	 * </p>
	 */
	boolean allowUnknownParams() default false;

}
