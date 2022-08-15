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

import org.hl7.fhir.instance.model.api.IBaseDatatype;

import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;

/**
 * Class annotation to note a class which defines a datatype
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface DatatypeDef {

	/**
	 * The defined name of this datatype
	 */
	String name();
	
	/**
	 * Set this to true (default is false) for any types that are
	 * really only a specialization of another type. For example,
	 * {@link BoundCodeDt} is really just a specific type of 
	 * {@link CodeDt} and not a separate datatype, so it should
	 * have this set to true.
	 */
	boolean isSpecialization() default false;
	
	/**
	 * Indicates that this datatype is a profile of the given datatype, which
	 * implies certain parsing/encoding rules (e.g. a choice element named
	 * foo[x] which allows a Markdown value will still be encoded as
	 * fooString because Markdown is a profile of string.
	 */
	Class<? extends IBaseDatatype> profileOf() default IBaseDatatype.class;

}
