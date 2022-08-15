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

import ca.uhn.fhir.model.api.IResource;

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#update">update</a> method.
 * 
 * <p>
 * Update is used to save an update to an existing resource (using its ID and optionally
 * a version ID). It also may allow a client to save a new resource using an ID of its choosing. 
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Update {

	/**
	 * The return type for this search method. This generally does not need
	 * to be populated for a server implementation, since servers will return
	 * only one resource per class, but generally does need to be populated
	 * for client implementations.
	 */
	// NB: Read, Search (maybe others) share this annotation, so update the javadocs everywhere
	Class<? extends IResource> type() default IResource.class;

	/**
	 * This method allows the return type for this method to be specified in a
	 * non-type-specific way, using the text name of the resource, e.g. "Patient".
	 * <p>
	 * This attribute should be populate, or {@link #type()} should be, but not both.
	 *
	 * @since 5.4.0
	 */
	String typeName() default "";

}
