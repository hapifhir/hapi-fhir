package ca.uhn.fhir.rest.annotation;

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
	 * You may also pass in a value of "*" which indicates to the server
	 * that any value is allowable and will be passed to this parameter. This is
	 * helpful if you want to explicitly declare support for some includes, but also
	 * allow others implicitly (e.g. imports from other resources)
	 * </p> 
	 */
	String[] allow() default {};
	
}
