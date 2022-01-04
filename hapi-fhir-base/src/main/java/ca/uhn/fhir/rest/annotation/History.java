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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
//import ca.uhn.fhir.testmodel.Patient; // TODO: qualify this correctly

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#history">history</a> method.
 * 
 * <p>
 * History returns a feed containing all versions (or a selected range of versions) of 
 * a resource or a specific set of resources.
 * </p>
 * <p>
 * The history command supports three usage patterns, as described in the
 * <a href="http://hl7.org/implement/standards/fhir/http.html#history">FHIR history</a> documentation:
 * </p>
 * <ul>
 * <li>
 *   A search for the history of all resources on a server. In this case, {@link #type()} 
 *   should be set to {@link IResource} (as is the default) and the method should not have an ID parameter.
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/_history{?[parameters]&amp;_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * <li>
 *   A search for the history of all instances of a specific resource type on a server. In this case, {@link #type()} 
 *   should be set to the specific resource type (e.g. <code>Patient.class</code>) and the method should not have an ID parameter.
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/[type]/_history{?[parameters]&amp;_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * <li>
 *   A search for the history of a specific instances of a specific resource type on a server. In this case, {@link #type()} 
 *   should be set to the specific resource type (e.g. <code>Patient.class</code> and the method should 
 *   have one parameter of type {@link IdDt} annotated with the {@link IdParam} annotation. 
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/[type]/[id]/_history{?[parameters]&amp;_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * </ul>
 * 
 * @see Count
 * @see Since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface History {
	
	/**
	 * The resource type that this method applies to. See the {@link History History annotation type documentation}
	 * for information on usage patterns.  
	 */
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

}
