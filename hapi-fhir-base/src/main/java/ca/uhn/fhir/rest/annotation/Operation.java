package ca.uhn.fhir.rest.annotation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

/**
 * RESTful method annotation used for a method which provides FHIR "operations".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Operation {

	/**
	 * The name of the operation, e.g. "<code>$everything</code>" 
	 * 
	 * <p>
	 * This may be specified with or without a leading 
	 * '$'. (If the leading '$' is omitted, it will be added internally by the API).
	 * </p>
	 */
	String name();

	/**
	 * On a client, this value should be populated with the resource type that the operation applies to. If set to
	 * {@link IBaseResource} (which is the default) than the operation applies to the server and not to a specific
	 * resource type.
	 * <p>
	 * This value has no effect when used on server implementations.
	 * </p>
	 */
	Class<? extends IBaseResource> type() default IBaseResource.class;

	/**
	 * If a given operation method is <b><a href="http://en.wikipedia.org/wiki/Idempotence">idempotent</a></b>
	 * (meaning roughly that it does not modify any data or state on the server)
	 * then this flag should be set to <code>true</code> (default is <code>false</code>).
	 * <p>
	 * One the server, setting this to <code>true</code> means that the 
	 * server will allow the operation to be invoked using an <code>HTTP GET</code>
	 * (on top of the standard <code>HTTP POST</code>)
	 * </p> 
	 */
	boolean idempotent() default false;

	/**
	 * This parameter may be used to specify the parts which will be found in the
	 * response to this operation.
	 */
	OperationParam[] returnParameters() default {};
}
