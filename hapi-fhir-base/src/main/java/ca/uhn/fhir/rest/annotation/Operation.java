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

import org.hl7.fhir.instance.model.IBaseResource;

/**
 * RESTful method annotation used for a method which provides FHIR "operations".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Operation {

	/**
	 * The name of the operation
	 */
	String name();

	/**
	 * On a client, this value should be populated with the resource type that the operation applies to. If set to
	 * {@link IBaseResource} (which is the default) than the operation applies to the server and not to a specific
	 * resource type.
	 * <p>
	 * This value should not be populated on server implementations.
	 * </p>
	 */
	Class<? extends IBaseResource> type() default IBaseResource.class;

}
