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

import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

/**
 * RESTful method annotation to be used for the FHIR <a href="http://hl7.org/implement/standards/fhir/http.html#read">read</a> and <a
 * href="http://hl7.org/implement/standards/fhir/http.html#vread">vread</a> method.
 * 
 * <p>
 * If this method has a parameter annotated with the {@link IdParam} annotation and a parameter annotated with the {@link VersionIdParam} annotation, the method will be treated as a vread method. If
 * the method has only a parameter annotated with the {@link IdParam} annotation, it will be treated as a read operation.
 * the
 * </p>
 * <p>
 * If you wish for your server to support both read and vread operations, you will need
 * two methods annotated with this annotation.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Read {

	/**
	 * The return type for this method. This generally does not need to be populated for IResourceProvider in a server implementation, but often does need to be populated in
	 * client implementations using {@link IBasicClient} or {@link IRestfulClient}, or in plain providers on a server.
	 * <p>
	 * This value also does not need to be populated if the return type for a method annotated with this annotation is sufficient to determine the type of resource provided. E.g. if the method returns
	 * <code>Patient</code> or <code>List&lt;Patient&gt;</code>, the server/client will automatically determine that the Patient resource is the return type, and this value may be left blank.
	 * </p>
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
	 * If set to true (default is false), this method supports vread operation as well as read
	 */
	boolean version() default false;
}
