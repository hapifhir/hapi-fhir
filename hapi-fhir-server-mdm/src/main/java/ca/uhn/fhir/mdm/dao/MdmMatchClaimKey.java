/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.util.Comparator;

/**
 * Identifies something an MDM unit of work claims before it searches for candidates, so that two
 * concurrent units of work whose decisions could affect each other are forced into a serial order.
 * See {@link IMdmMatchClaimSvc}.
 *
 * @param type         what kind of thing is claimed
 * @param canonicalKey the canonical text of the claim, unique for what it identifies
 * @param partitionId  the partition scope of the claim, or {@link #ALL_PARTITIONS} when the claim spans partitions
 */
// Created by claude-opus-5-5
public record MdmMatchClaimKey(@Nonnull ClaimTypeEnum type, @Nonnull String canonicalKey, int partitionId)
		implements Comparable<MdmMatchClaimKey> {

	/**
	 * Partition scope used when a claim isn't confined to a single partition.
	 */
	public static final int ALL_PARTITIONS = -1;

	private static final Comparator<MdmMatchClaimKey> ourComparator =
			Comparator.comparingInt(MdmMatchClaimKey::partitionId).thenComparing(MdmMatchClaimKey::canonicalKey);

	public enum ClaimTypeEnum {
		/**
		 * A source resource, identified by its persistent id.
		 */
		PID,
		/**
		 * An external enterprise identifier carried by a source resource.
		 */
		EID
	}

	public MdmMatchClaimKey {
		Validate.notNull(type, "type must not be null");
		Validate.notBlank(canonicalKey, "canonicalKey must not be blank");
	}

	/**
	 * Builds the claim on a source resource.
	 *
	 * @param theResourceType the resource type of the source
	 * @param thePid          the persistent id of the source
	 * @param thePartitionId  the partition the source lives in, or {@code null} for the default partition
	 */
	@Nonnull
	public static MdmMatchClaimKey forSourcePid(
			@Nonnull String theResourceType,
			@Nonnull IResourcePersistentId<?> thePid,
			@Nullable Integer thePartitionId) {
		Validate.notNull(thePid.getId(), "thePid must have an id");
		return new MdmMatchClaimKey(
				ClaimTypeEnum.PID,
				"PID|" + theResourceType + "|" + thePid.getId(),
				thePartitionId == null ? ALL_PARTITIONS : thePartitionId);
	}

	/**
	 * Builds the claim on an external EID. The key uses the same exact {@code system|value} equality
	 * that the EID candidate searches use.
	 *
	 * @param theResourceType the resource type of the source carrying the EID
	 * @param theEid          the EID, which must have a value
	 * @param thePartitionId  the partition the EID search is confined to, or {@code null} when it spans partitions
	 */
	@Nonnull
	public static MdmMatchClaimKey forEid(
			@Nonnull String theResourceType, @Nonnull CanonicalEID theEid, @Nullable Integer thePartitionId) {
		Validate.notBlank(theEid.getValue(), "theEid must have a value");
		String system = theEid.getSystem() == null ? "" : theEid.getSystem();
		return new MdmMatchClaimKey(
				ClaimTypeEnum.EID,
				"EID|" + theResourceType + "|" + system + "|" + theEid.getValue(),
				thePartitionId == null ? ALL_PARTITIONS : thePartitionId);
	}

	/**
	 * Claims are taken in this order, so that two units of work claiming overlapping sets can't deadlock.
	 */
	@Override
	public int compareTo(@Nonnull MdmMatchClaimKey theOther) {
		return ourComparator.compare(this, theOther);
	}
}
