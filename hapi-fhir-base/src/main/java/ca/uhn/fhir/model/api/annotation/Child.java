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

import ca.uhn.fhir.model.api.IElement;

/**
 * Field annotation for fields within resource and datatype definitions, indicating 
 * a child of that type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface Child {

	/**
	 * Constant value to supply for {@link #order()} when the order is defined
	 * elsewhere
	 */
	int ORDER_UNKNOWN = -1;
	
	/**
	 * Constant value to supply for {@link #max()} to indicate '*' (no maximum)
	 */
	int MAX_UNLIMITED = -1;

	/**
	 * Constant value to supply for {@link #order()} to indicate that this child should replace the
	 * entry in the superclass with the same name (and take its {@link Child#order() order} value 
	 * in the process). This is useful if you wish to redefine an existing field in a resource/type
	 * definition in order to constrain/extend it.
	 */
	int REPLACE_PARENT = -2;

	/**
	 * The name of this field, as it will appear in serialized versions of the message
	 */
	String name();
	
	/**
	 * The order in which this field comes within its parent. The first field should have a 
	 * value of 0, the second a value of 1, etc.
	 */
	int order() default ORDER_UNKNOWN;

	/**
	 * The minimum number of repetitions allowed for this child
	 */
	int min() default 0;

	/**
	 * The maximum number of repetitions allowed for this child. Should be
	 * set to {@link #MAX_UNLIMITED} if there is no limit to the number of
	 * repetitions.
	 */
	int max() default 1;

	/**
	 * Lists the allowable types for this field, if the field supports multiple
	 * types (otherwise does not need to be populated).
	 * <p>
	 * For example, if this field supports either DateTimeDt or BooleanDt types,
	 * those two classes should be supplied here.
	 * </p>
	 */
	Class<? extends IElement>[] type() default {};

	// Not implemented
//	/**
//	 * This value is used when extending a built-in model class and defining a
//	 * field to replace a field within the built-in class. For example, the {@link Patient} 
//	 * resource has a {@link Patient#getName() name} field, but if you wanted to extend Patient and
//	 * provide your own implementation of {@link HumanNameDt} (most likely your own subclass of 
//	 * HumanNameDt which adds extensions of your choosing) you could do that using a replacement field. 
//	 */
//	String replaces() default "";

	/**
	 * Is this element a modifier?
	 */
	boolean modifier() default false;	

	/**
	 * Should this element be included in the summary view
	 */
	boolean summary() default false;
	
}
