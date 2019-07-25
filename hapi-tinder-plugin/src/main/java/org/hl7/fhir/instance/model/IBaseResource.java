package org.hl7.fhir.instance.model;


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

/**
 * For now, this is a simple marker interface indicating that a class is a resource type. 
 * There are two concrete types of implementations of this interrface. The first are
 * HL7.org's Resource structures (e.g. 
 * <code>org.hl7.fhir.instance.model.Patient</code>) and
 * the second are HAPI's Resource structures, e.g. 
 * <code>ca.uhn.fhir.model.dstu.resource.Patient</code>)
 */
public interface IBaseResource extends IBase {

	IIdType getIdElement();
	
	IBaseResource setId(String theId);

	IBaseResource setId(IIdType theId);

}
