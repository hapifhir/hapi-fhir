package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;

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

public interface IDao {

	public static final ResourceMetadataKeyEnum<Long> RESOURCE_PID = new ResourceMetadataKeyEnum<Long>("RESOURCE_PID") {

		private static final long serialVersionUID = 1L;

		@Override
		public Long get(IResource theResource) {
			return (Long) theResource.getResourceMetadata().get(RESOURCE_PID);
		}

		@Override
		public void put(IResource theResource, Long theObject) {
			theResource.getResourceMetadata().put(RESOURCE_PID, theObject);
		}
	};

	void registerDaoListener(IDaoListener theListener);
	
}
