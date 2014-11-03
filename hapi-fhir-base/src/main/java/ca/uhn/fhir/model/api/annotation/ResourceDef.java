package ca.uhn.fhir.model.api.annotation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

/**
 * Class annotation which indicates a resource definition class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ResourceDef {

	/**
	 * The name of the resource (e.g. "Patient" or "DiagnosticReport")
	 */
	String name() default "";

	/**
	 * if set, will be used as the id for any profiles generated for this resource
	 */
	String id() default "";
	
	/**
	 * The URL indicating the profile for this resource definition, if known
	 */
	String profile() default "";
	
}
