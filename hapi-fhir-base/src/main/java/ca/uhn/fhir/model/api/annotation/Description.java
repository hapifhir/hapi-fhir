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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which may be placed on a resource/datatype definition, or a field, or
 * a search parameter definition in order to provide documentation for that item.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD})
public @interface Description {

	/**
	 * A description of this method or parameter
	 *
	 * @since 5.4.0
	 */
	String value() default "";

	/**
	 * Optional short name for this child
	 */
	String shortDefinition() default "";

	/**
	 * Optional formal definition for this child
	 *
	 * @deprecated Use {@link #value()} instead. Deprecated in 5.4.0.
	 */
	@Deprecated
	String formalDefinition() default "";

	/**
	 * May be used to supply example values for this
	 *
	 * @since 5.4.0
	 */
	String[] example() default {};
}
