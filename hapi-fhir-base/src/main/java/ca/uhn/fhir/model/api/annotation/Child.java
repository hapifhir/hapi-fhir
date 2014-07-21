package ca.uhn.fhir.model.api.annotation;

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

import ca.uhn.fhir.model.api.IElement;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface Child {

	/**
	 * Constant value to supply for {@link #order()} when the order is defined
	 * elsewhere
	 */
	int ORDER_UNKNOWN = -1;
	
	/**
	 * COnstant value to supply for {@link #max()} to indicate '*' (no maximum)
	 */
	int MAX_UNLIMITED = -1;

	String name();
	
	int order() default ORDER_UNKNOWN;

	int min() default 0;

	int max() default 1;

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
		
}
