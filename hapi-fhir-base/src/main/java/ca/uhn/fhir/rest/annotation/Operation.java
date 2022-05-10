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

import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RESTful method annotation used for a method which provides FHIR "operations".
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface Operation {

	/**
	 * This constant is a special return value for {@link #name()}. If this name is
	 * used, the given operation method will match all operation calls. This is
	 * generally not desirable, but can be useful if you have a server that should
	 * dynamically match any FHIR operations that are requested.
	 */
	String NAME_MATCH_ALL = "*";

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
	 * This value may be populated with the resource type that the operation applies to. If set to
	 * {@link IBaseResource} (which is the default) than the operation applies to the server and not to a specific
	 * resource type.
	 * <p>
	 * This attribute should not be used a resource provider implementing
	 * <code>IResourceProvider</code> since the type can be inferred from the
	 * resource provider type.
	 * </p>
	 * @see #typeName() may also be used to specify a value as a String
	 */
	Class<? extends IBaseResource> type() default IBaseResource.class;

	/**
	 * This value may be populated with the resource type that the operation applies to. If set to
	 * {@link IBaseResource} (which is the default) than the operation applies to the server and not to a specific
	 * resource type.
	 * <p>
	 * This attribute should not be used a resource provider implementing
	 * <code>IResourceProvider</code> since the type can be inferred from the
	 * resource provider type.
	 * </p>
	 * @see #type() may also be used to specify a value for this setting as a class type
	 */
	String typeName() default "";

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

	/**
	 * If this operation returns a bundle, this parameter can be used to specify the
	 * bundle type to set in the bundle.
	 */
	BundleTypeEnum bundleType() default BundleTypeEnum.COLLECTION;

	/**
	 * If this is set to <code>true</code> (default is <code>false</code> and this is almost
	 * always the right choice), the framework will not attempt to generate a response to
	 * this method.
	 * <p>
	 * This is useful if you want to include an {@link javax.servlet.http.HttpServletResponse}
	 * in your method parameters and create a response yourself directly from your
	 * <code>@Operation</code> method.
	 * </p>
	 * <p>
	 * Note that this will mean that interceptor methods will not get fired for the
	 * response, so there are security implications to using this flag.
	 * </p>
	 */
	boolean manualResponse() default false;

	/**
	 * If this is set to <code>true</code> (default is <code>false</code> and this is almost
	 * always the right choice), the framework will not attempt to parse the request body,
	 * but will instead delegate it to the <code>@Operation</code> method.
	 * <p>
	 * This is useful if you want to include an {@link javax.servlet.http.HttpServletRequest}
	 * in your method parameters and parse the request yourself.
	 * </p>
	 */
	boolean manualRequest() default false;

	/**
	 * If this is set to <code>true</code>, this method will be a <b>global operation</b>
	 * meaning that it applies to all resource types. Operations with this flag set should be
	 * placed in Plain Providers (i.e. they don't need to be placed in a resource-type-specific
	 * <code>IResourceProvider</code> instance) and should have a parameter annotated with
	 * {@link IdParam}.
	 */
	boolean global() default false;

}
