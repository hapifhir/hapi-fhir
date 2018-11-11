package org.hl7.fhir.r4.model.annotations;

/*
Copyright (c) 2011+, HL7, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hl7.fhir.r4.model.Resource;

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
	Class<? extends Resource>[] target() default {};
	
	
}
