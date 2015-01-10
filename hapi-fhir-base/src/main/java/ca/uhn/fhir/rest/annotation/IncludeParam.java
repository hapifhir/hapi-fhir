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

/**
 * Method parameter which is used to indicate a parameter that will
 * be populated with the "_include" values for a search param.  
 * <p>
 * Only one parameter may be annotated with this annotation, and that
 * parameter should be one of the following:
 * </p> 
 * <ul>
 * <li><code>Collection&lt;PathSpecification&gt;</code></li> 
 * <li><code>List&lt;PathSpecification&gt;</code></li> 
 * <li><code>Set&lt;PathSpecification&gt;</code></li> 
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.PARAMETER})
public @interface IncludeParam {

	/**
	 * Optional parameter, if provided the server will only allow the values
	 * within the given set. If an _include parameter is passed to the server
	 * which does not match any allowed values the server will return an error.
	 * <p>
	 * Values for this parameter takew the form that the FHIR specification
	 * defines for <code>_include</code> values, namely <code>[Resource Name].[path]</code>.
	 * For example: <code>"Patient.link.other"</code>
	 * or <code>"Encounter.partOf"</code> 
	 * </p>
	 * <p>
	 * You may also pass in a value of "*" which indicates means that the
	 * client may request <code>_include=*</code>. This is a request to 
	 * include all referenced resources as well as any resources referenced
	 * by those resources, etc.
	 * </p>
	 * <p>
	 * Leave this parameter empty if you do not want the server to declare or
	 * restrict which includes are allowable. In this case, the client may add
	 * any _include value they want, and that value will be accepted by the server
	 * and passed to the handling method. Note that this means that the server 
	 * will not declare which _include values it supports in its conformance
	 * statement.
	 * </p> 
	 */
	String[] allow() default {};
	
}
