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

import ca.uhn.fhir.rest.api.SortSpec;

/**
 * For searches, a parameter may be annotated with the {@link Sort} annotation. The 
 * parameter should be of type {@link SortSpec}. 
 * 
 * <p>
 * Note that if you wish to chain
 * multiple sort parameters (i.e. a sub sort), you should use the {@link SortSpec#setChain(SortSpec)}
 * method. Multiple parameters should not be annotated with the Sort annotation.
 * </p>
 *   
 * @see Search
 */
@Target(value=ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sort {
	// nothing
}
