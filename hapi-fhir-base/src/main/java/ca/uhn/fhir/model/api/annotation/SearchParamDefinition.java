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

import org.hl7.fhir.instance.model.api.IBaseResource;

@Target(value=ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchParamDefinition {

	/**
	 * The name for this parameter
	 */
	String name();
	
	/**
	 * The path for this parameter
	 */
	String path();
	
	/**
	 * A description of this parameter
	 */
	String description() default "";
	
	/**
	 * The type for this parameter, e.g. "string", or "token"
	 */
	String type() default "string";
	
	/**
	 * If the parameter is of type "composite", this parameter lists the names of the parameters 
	 * which this parameter is a composite of. E.g. "name-value-token" is a composite of "name" and "value-token".
	 * <p>
	 * If the parameter is not a composite, this parameter must be empty
	 * </p>
	 */
	String[] compositeOf() default {};

	/**
	 * For search params of type "reference", this can optionally be used to
	 * specify the resource type(s) that this parameter applies to.
	 */
	Class<? extends IBaseResource>[] target() default {};
	
	/**
	 * Indicates that this field indicates that resources linked to by this parameter
	 * (must be a reference parameter) place the resource in the given compartment.
	 */
	Compartment[] providesMembershipIn() default {};

}
