package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;

/**
 * Parameter annotation which specifies a search parameter for a {@link Search} method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.PARAMETER)
public @interface RequiredParam {

	/**
	 * For reference parameters ({@link ReferenceParam}) this value may be used to indicate which chain values (if any) are <b>not</b> valid for the given parameter. Values here will supercede any
	 * values specified in {@link #chainWhitelist()}
	 * <p>
	 * If the parameter annotated with this annotation is not a {@link ReferenceParam}, this value must not be populated.
	 * </p>
	 */
	String[] chainBlacklist() default {};

	/**
	 * For reference parameters ({@link ReferenceParam}) this value may be
	 * used to indicate which chain values (if any) are valid for the given
	 * parameter. If the list contains the value {@link OptionalParam#ALLOW_CHAIN_ANY}, all values are valid. (this is the default)
	 * If the list contains the value {@link OptionalParam#ALLOW_CHAIN_NOTCHAINED}
	 * then the reference param only supports the empty chain (i.e. the resource
	 * ID).
	 * <p>
	 * Valid values for this parameter include:
	 * </p>
	 * <ul>
	 * <li><code>chainWhitelist={ OptionalParam.ALLOW_CHAIN_NOTCHAINED }</code> - Only allow resource reference (no chaining allowed for this parameter)</li>
	 * <li><code>chainWhitelist={ OptionalParam.ALLOW_CHAIN_ANY }</code> - Allow any chaining at all (including a non chained value, <b>this is the default</b>)</li>
	 * <li><code>chainWhitelist={ "foo", "bar" }</code> - Allow property.foo and property.bar</li>
	 * </ul>
	 * <p>
	 * Any values specified in
	 * {@link #chainBlacklist()} will supercede (have priority over) values
	 * here.
	 * </p>
	 * <p>
	 * If the parameter annotated with this annotation is not a {@link ReferenceParam},
	 * this value must not be populated.
	 * </p>
	 */
	String[] chainWhitelist() default { OptionalParam.ALLOW_CHAIN_ANY };

	/**
	 * For composite parameters ({@link CompositeParam}) this parameter may be used to indicate the parameter type(s) which may be referenced by this param.
	 * <p>
	 * If the parameter annotated with this annotation is not a {@link CompositeParam}, this value must not be populated.
	 * </p>
	 */
	Class<? extends IQueryParameterType>[] compositeTypes() default {};

	/**
	 * This is the name for the parameter. Generally this should be a simple string (e.g. "name", or "identifier") which will be the name of the URL parameter used to populate this method parameter.
	 * <p>
	 * Most resource model classes have constants which may be used to supply values for this field, e.g. Patient.SP_NAME or Observation.SP_DATE
	 * </p>
	 * <p>
	 * If you wish to specify a parameter for a resource reference which only accepts a specific chained value, it is also valid to supply a chained name here, such as "patient.name". It is
	 * recommended to supply this using constants where possible, e.g. <code>Observation.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER</code>
	 * </p>
	 */
	String name();

	/**
	 * For resource reference parameters ({@link ReferenceParam}) this parameter may be used to indicate the resource type(s) which may be referenced by this param.
	 * <p>
	 * If the parameter annotated with this annotation is not a {@link ReferenceParam}, this value must not be populated.
	 * </p>
	 */
	Class<? extends IBaseResource>[] targetTypes() default {};

}
