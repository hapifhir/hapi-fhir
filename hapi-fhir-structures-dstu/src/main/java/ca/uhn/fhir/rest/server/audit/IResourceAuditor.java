package ca.uhn.fhir.rest.server.audit;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;


public interface IResourceAuditor<T extends IResource> {
	
	/**
	 * @return the resource to be audited
	 */
	public T getResource();
	
	/**
	 * @param resource the resource to be audited by this auditor
	 */
	public void setResource(T resource);
	
	/**
	 * @return true if this resource is to be audited, false otherwise
	 */
	public boolean isAuditable();
	
	/**
	 * An instance-specific descriptor of the Participant Object ID audited, such as a person's name
	 * @return the descriptive name of the resource object
	 */
	public String getName();
	
	/**
	 * @return the identifier of the resource to be audited
	 */
	public BaseIdentifierDt getIdentifier();
	
	/**
	 * @return the SecurityEventObjectTypeEnum of this resource
	 */
	public SecurityEventObjectTypeEnum getType();	
	
	/**
	 * @return a text description of the resource
	 */
	public String getDescription();
	
	/**
	 * @return a map of additional details to be audited
	 */
	public Map<String, String> getDetail();
	
	/**
	 * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics
	 * @return the sensitivity of this resource
	 */
	public BaseCodingDt getSensitivity();
	

}
