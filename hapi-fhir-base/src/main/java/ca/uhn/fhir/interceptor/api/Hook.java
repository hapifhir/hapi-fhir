package ca.uhn.fhir.interceptor.api;

/*-
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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be placed on interceptor methods. The
 * {@link Pointcut value=Pointcut} property determines which event
 * is actually being invoked. See the Pointcut JavaDoc for information
 * on available method parameters for a given hook.
 *
 * @see Interceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hook {

	/**
	 * Provides the specific point where this method should be invoked
	 */
	Pointcut value();

	/**
	 * The order that interceptors should be called in. Lower numbers happen before higher numbers. Default is 0
	 * and allowable values can be positive or negative or 0.
	 * <p>
	 * If no order is specified, or the order is set to <code>0</code> (the default order),
	 * the order specified at the interceptor type level will take precedence.
	 * </p>
	 */
	int order() default Interceptor.DEFAULT_ORDER;
}
