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

/**
 * On the {@link Update}, {@link Create} and {@link Delete} operation methods, this annotation
 * can be used to mark a {@link String} parameter which will be populated with the 
 * conditional "search" URL for the operation, if an incoming client invocation is
 * a conditional operation. For non-conditional invocations, the value will be set to
 * <code>null</code> so it is important to handle <code>null</code>.
 * <p>
 * Parameters annotated with this annotation <b>must be of type {@link String}</b>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ConditionalUrlParam {

	/**
	 * Does this param support operating over multiple objects without throwing an error? This
	 * should be set to <code>true</code> only for conditional delete operations if the server
	 * supports multiple deletes via a conditional URL.
	 * <p>
	 * Note that this flag is only a hint to the Conformance statement generator,
	 * it does not actually affect how the server itself works.
	 * </p>  
	 */
	boolean supportsMultiple() default false;
	
}
