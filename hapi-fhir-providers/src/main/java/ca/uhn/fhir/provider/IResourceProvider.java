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

package ca.uhn.fhir.provider;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.server.IBundleProvider;

/**
 * @author Bill.Denton
 *
 */
public interface IResourceProvider<T extends IResource> extends IProvider, ca.uhn.fhir.rest.server.IResourceProvider {
	
	public IFhirResourceDao<T> getDao();
	
	@Override
	public Class<? extends IBaseResource> getResourceType();

	@History
	public IBundleProvider getHistoryForResourceInstance (HttpServletRequest theRequest, @IdParam IdDt theId, @Since Date theDate);

	@History
	public IBundleProvider getHistoryForResourceType (HttpServletRequest theRequest, @Since Date theDate);
	
	@GetTags
	public TagList getTagsForResourceInstance (HttpServletRequest theRequest, @IdParam IdDt theResourceId);

	@GetTags
	public TagList getTagsForResourceType (HttpServletRequest theRequest);

	@Read(version = true)
	public T read (HttpServletRequest theRequest, @IdParam IdDt theId);


}
