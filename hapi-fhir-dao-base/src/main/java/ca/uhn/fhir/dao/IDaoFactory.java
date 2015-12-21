/*
 * #%L
 * HAPI FHIR JPA Server
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

package ca.uhn.fhir.dao;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author Bill.Denton
 *
 */
public interface IDaoFactory {
	public FhirContext getFhirContext ();
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDao (Class<T> theResourceClass);
	public IFhirSystemDao<?,?> getSystemDao ();
	public ISearchDao getSearchDao ();
	public String getValidResourceTypes ();
}
