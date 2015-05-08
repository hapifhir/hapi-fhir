package org.hl7.fhir.instance.model.annotations;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
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

/**
 * Class annotation which indicates a resource definition class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.TYPE})
public @interface ResourceDef {

	/**
	 * The name of the resource (e.g. "Patient" or "DiagnosticReport"). If you are defining your
	 * own custom extension to a built-in FHIR resource definition type (e.g. you are extending
	 * the built-in Patient class) you do not need to supply a value for this property, as it 
	 * will be inferred from the parent class.
	 */
	String name() default "";

	/**
	 * if set, will be used as the id for any profiles generated for this resource. This property
	 * should be set for custom profile definition classes, and will be used as the resource ID
	 * for the generated profile exported by the server. For example, if you set this value to
	 * "hello" on a custom resource class, your server will automatically export a profile with the
	 * identity: <code>http://localhost:8080/fhir/Profile/hello</code> (the base URL will be whatever
	 * your server uses, not necessarily "http://localhost:8080/fhir")    
	 */
	String id() default "";
	
	/**
	 * The URL indicating the profile for this resource definition, if known. If this value is set
	 * on a custom profile definition class in a server, the profile is assumed to be external to
	 * the server, so the server will not export a profile for it.
	 */
	String profile() default "";
	
}
