/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Builds the {@link MdmMatchClaimKey match claims} an MDM unit of work takes. The EID claims use the same
 * external EIDs as the EID candidate searches, so that any two sources those searches could connect claim
 * the same key.
 * <p>
 * Every claim is scoped across all partitions. The same source or EID can reach this class with its
 * partition expressed differently (by id, by name, or not at all), and a narrower scope could then miss a
 * conflict. A wider scope can only add waiting between unrelated resources that happen to share an EID.
 */
// Created by claude-opus-5-5
public class MdmMatchClaimKeySvc {

	private final EIDHelper myEIDHelper;

	public MdmMatchClaimKeySvc(EIDHelper theEIDHelper) {
		myEIDHelper = theEIDHelper;
	}

	/**
	 * The claims taken before any candidate search: the source itself, and each external EID it carries.
	 * HAPI-generated EIDs are random, so they are never claimed.
	 *
	 * @param theSource    the source resource being processed
	 * @param theSourcePid its persistent id
	 */
	@Nonnull
	public List<MdmMatchClaimKey> buildInitialClaims(
			@Nonnull IAnyResource theSource, @Nonnull IResourcePersistentId<?> theSourcePid) {
		String resourceType = theSource.getIdElement().getResourceType();
		List<MdmMatchClaimKey> retVal = new ArrayList<>();
		retVal.add(MdmMatchClaimKey.forSourcePid(resourceType, theSourcePid));
		for (CanonicalEID eid : myEIDHelper.getExternalEid(theSource)) {
			if (isNotBlank(eid.getValue())) {
				retVal.add(MdmMatchClaimKey.forEid(resourceType, eid));
			}
		}
		return retVal;
	}

	/**
	 * The claims on matching source resources that have no MATCH link yet.
	 */
	@Nonnull
	public List<MdmMatchClaimKey> buildSourceClaims(
			@Nonnull String theResourceType, @Nonnull Collection<IResourcePersistentId<?>> thePids) {
		return thePids.stream()
				.map(pid -> MdmMatchClaimKey.forSourcePid(theResourceType, pid))
				.toList();
	}
}
