/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.cross;

import ca.uhn.fhir.jpa.model.dao.JpaPid;

import java.util.Date;

public class JpaResourceLookup implements IResourceLookup<JpaPid> {
	private final String myResourceType;
	private final Long myResourcePid;
	private final Date myDeletedAt;

	public JpaResourceLookup(String theResourceType, Long theResourcePid, Date theDeletedAt) {
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
	public JpaPid getPersistentId() {
		return JpaPid.fromId(myResourcePid);
	}
}
