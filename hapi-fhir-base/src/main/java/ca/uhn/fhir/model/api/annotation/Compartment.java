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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This may only be populated on a reference search paramater field. On such a field, places the containing
 * resource in a compartment with the name(s) specified by the given strings, where the compartment
 * belongs to the target resource. For example, this field could be populated with <code>Patient</code> on 
 * the <code>Observation.subject</code> field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {})
public @interface Compartment {

	String name();
	
}
