/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class JpaResourceLookup implements IResourceLookup<JpaPid> {

	private final String myResourceType;
	private final JpaPid myResourcePid;
	private final Date myDeletedAt;
	private final PartitionablePartitionId myPartitionablePartitionId;
	private final String myFhirId;

	public JpaResourceLookup(
			String theResourceType,
			String theFhirId,
			Long theResourcePid,
			Date theDeletedAt,
			PartitionablePartitionId thePartitionablePartitionId) {
		myResourceType = theResourceType;
		myFhirId = theFhirId;
		myDeletedAt = theDeletedAt;
		myPartitionablePartitionId = thePartitionablePartitionId;

		myResourcePid = JpaPid.fromId(theResourcePid, myPartitionablePartitionId);
	}

	public JpaResourceLookup(
			String theResourceType,
			String theFhirId,
			JpaPid theResourcePid,
			Date theDeletedAt,
			PartitionablePartitionId thePartitionablePartitionId) {
		myResourceType = theResourceType;
		myFhirId = theFhirId;
		myResourcePid = theResourcePid;
		myDeletedAt = theDeletedAt;
		myPartitionablePartitionId = thePartitionablePartitionId;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	@Override
	public String getFhirId() {
		return myFhirId;
	}

	@Override
	public Date getDeleted() {
		return myDeletedAt;
	}

	@Override
	public JpaPid getPersistentId() {
		return myResourcePid;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("resType", myResourceType)
				.append("resPid", myResourcePid)
				.append("deletedAt", myDeletedAt)
				.append("partId", myPartitionablePartitionId)
				.toString();
	}

	@Override
	public PartitionablePartitionId getPartitionId() {
		return myPartitionablePartitionId;
	}
}
