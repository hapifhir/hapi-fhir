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
import java.lang.annotation.*;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.ValidationModeEnum;

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#validate">validate</a> method.
 * 
 * <p>
 * Validate is used to accept a resource, and test whether it would be acceptable for
 * storing (e.g. using an update or create method)  
 * </p>
 * <p>
 * <b>FHIR Version Note:</b> The validate operation was defined as a type operation in DSTU1
 * using a URL syntax like <code>http://example.com/Patient/_validate</code>. In DSTU2, validation
 * has been switched to being an extended operation using a URL syntax like 
 * <code>http://example.com/Patient/$validate</code>, with a n 
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Validate {

	/**
	 * The return type for this method. This generally does not need
	 * to be populated for a server implementation (using an IResourceProvider, 
	 * since resource providers will return only one resource type per class, 
	 * but generally does need to be populated for client implementations. 
	 */
	// NB: Read, Search (maybe others) share this annotation, so update the javadocs everywhere
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
	 * Validation mode parameter annotation for the validation mode parameter (only supported
	 * in FHIR DSTU2+). Parameter must be of type {@link ValidationModeEnum}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.PARAMETER)
	@interface Mode {
		// nothing
	}
	
	/**
	 * Validation mode parameter annotation for the validation URI parameter (only supported
	 * in FHIR DSTU2+). Parameter must be of type {@link String}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.PARAMETER)
	@interface Profile {
		// nothing
	}

}
