/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.annotation;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by any method parameter within an class passed to an {@link Operation} method to be converted into an
 * OperationEmbeddedParameter.
 * <p/>
 * The class itself doesn't have an annotation, only its fields.
 * <p/>
 * The method parameter in the operation also is explicitly not annotated
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
public @interface OperationEmbeddedParam {
	// LUKETODO:  after writing the conformance code get rid of a bunch of cruft like MAX_UNLIMITED

	/**
	 * Value for {@link OperationEmbeddedParam#max()} indicating no maximum
	 */
	int MAX_UNLIMITED = -1;

	/**
	 * Value for {@link OperationEmbeddedParam#max()} indicating that the maximum will be inferred
	 * from the type. If the type is a single parameter type (e.g. <code>StringDt</code>,
	 * <code>TokenParam</code>, <code>IBaseResource</code>) the maximum will be
	 * <code>1</code>.
	 * <p>
	 * If the type is a collection, e.g.
	 * <code>List&lt;StringDt&gt;</code> or <code>List&lt;TokenOrListParam&gt;</code>
	 * the maximum will be set to <code>*</code>. If the param is a search parameter
	 * "and" type, such as <code>TokenAndListParam</code> the maximum will also be
	 * set to <code>*</code>
	 * </p>
	 *
	 * @since 1.5
	 */
	int MAX_DEFAULT = -2;

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
	 * The source type of the parameters if we're expecting to do a type conversion, such as String to ZonedDateTime.
	 * Void indicates that we don't want to do a type conversion.
	 *
	 * @return the source type of the parameter
	 */
	Class<?> sourceType() default Void.class;

	/**
	 * @return The range type associated with any type conversion.  For instance, if we expect a start and end date.
	 * NOT_APPLICABLE is the default and indicates range conversion is not applicable.
	 */
	EmbeddedParameterRangeType rangeType() default EmbeddedParameterRangeType.NOT_APPLICABLE;

	/**
	 * Optionally specifies the type of the parameter as a string, such as <code>Coding</code> or
	 * <code>base64Binary</code>. This can be useful if you want to use a generic interface type
	 * on the actual method,such as {@link org.hl7.fhir.instance.model.api.IPrimitiveType} or
	 * {@link @org.hl7.fhir.instance.model.api.ICompositeType}.
	 */
	String typeName() default "";

	/**
	 * The minimum number of repetitions allowed for this child (default is 0)
	 */
	int min() default 0;

	/**
	 * The maximum number of repetitions allowed for this child. Should be
	 * set to {@link #MAX_UNLIMITED} if there is no limit to the number of
	 * repetitions. See {@link #MAX_DEFAULT} for a description of the default
	 * behaviour.
	 */
	int max() default MAX_DEFAULT;
}
