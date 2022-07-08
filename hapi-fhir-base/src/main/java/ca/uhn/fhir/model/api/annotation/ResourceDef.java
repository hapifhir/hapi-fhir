package ca.uhn.fhir.model.api.annotation;

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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class annotation which indicates a resource definition class
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ResourceDef {

	/**
	 * The name of the resource (e.g. "Patient" or "DiagnosticReport"). If you are defining your
	 * own custom extension to a built-in FHIR resource definition type (e.g. you are extending
	 * the built-in Patient class) you do not need to supply a value for this property, as it 
	 * will be inferred from the parent class.
	 */
	String name() default "";

	/**
	 * if set, will be used as the id for any profiles generated for this resource. This property
	 * should be set for custom profile definition classes, and will be used as the resource ID
	 * for the generated profile exported by the server. For example, if you set this value to
	 * "hello" on a custom resource class, your server will automatically export a profile with the
	 * identity: <code>http://localhost:8080/fhir/Profile/hello</code> (the base URL will be whatever
	 * your server uses, not necessarily "http://localhost:8080/fhir")
	 */
	String id() default "";
	
	/**
	 * The URL indicating the profile for this resource definition. If specified, this URL will be
	 * automatically added to the meta tag when the resource is serialized.
	 * <p>
	 * This URL should be fully qualified to indicate the complete URL of
	 * the profile being used, e.g. <code>http://example.com/fhir/StructureDefiniton/some_profile</code>
	 * </p>
	 */
	String profile() default "";
	
}
