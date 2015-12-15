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

import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.server.IBundleProvider;

/**
 * @author Bill.Denton
 *
 */
public interface ISystemProvider<T, MT> extends IProvider {
	public IFhirSystemDao<T, MT> getDao ();

	@History
	public IBundleProvider historyServer (HttpServletRequest theRequest, @Since Date theDate);

	@GetTags
	public TagList getAllTagsOnServer (HttpServletRequest theRequest);

}
