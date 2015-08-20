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

import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.param.StringParam;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.PARAMETER)
public @interface OperationParam {

	/**
	 * Value for {@link OperationParam#max()} indicating no maximum
	 */
	final int MAX_UNLIMITED = -1;

	/**
	 * The name of the parameter
	 */
	String name();
	
	/**
	 * The type of the parameter. This will only have effect on <code>@OperationParam</code>
	 * annotations specified as values for {@link Operation#returnParameters()}, otherwise the
	 * value will be ignored. Value should be one of:
	 * <ul>
	 * <li>A resource type, e.g. <code>Patient.class</code></li>
	 * <li>A datatype, e.g. <code>{@link StringDt}.class</code> or </code>CodeableConceptDt.class</code>
	 * <li>A RESTful search parameter type, e.g. <code>{@link StringParam}.class</code>
	 * </ul>
	 */
	Class<? extends IBase> type() default IBase.class;
	
	/**
	 * The minimum number of repetitions allowed for this child (default is 0)
	 */
	int min() default 0;

	/**
	 * The maximum number of repetitions allowed for this child. Should be
	 * set to {@link #MAX_UNLIMITED} if there is no limit to the number of
	 * repetitions (default is 1)
	 */
	int max() default 1;

	
}
