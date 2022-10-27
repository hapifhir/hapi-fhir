package ca.uhn.fhir.jpa.model.cross;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.Date;

public class ResourceLookup implements IResourceLookup {
    private final String myResourceType;
    private final Long myResourcePid;
    private final Date myDeletedAt;

	public ResourceLookup(String theResourceType, Long theResourcePid, Date theDeletedAt) {
        myResourceType = theResourceType;
        myResourcePid = theResourcePid;
        myDeletedAt = theDeletedAt;
    }

    @Override
    public String getResourceType() {
        return myResourceType;
    }

    @Override
    public Date getDeleted() {
        return myDeletedAt;
    }

	@Override
	public ResourcePersistentId getPersistentId() {
		return new ResourcePersistentId(myResourcePid);
	}
}
