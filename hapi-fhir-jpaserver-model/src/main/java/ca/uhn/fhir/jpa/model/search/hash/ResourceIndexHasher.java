package ca.uhn.fhir.jpa.model.search.hash;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StringUtil;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceIndexHasher {
	private static final int HASH_PREFIX_LENGTH = 1;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private StorageSettings myStorageSettings;

	private static class HashIdentity implements IdentityHasher.IHashIdentity {
		private Integer myPartitionId;
		private boolean myIsContained;
		private final String[] myValues;

		public HashIdentity(String[] theValues) {
			myValues = theValues;
		}

		public HashIdentity setPartitionId(Integer thePartitionId) {
			myPartitionId = thePartitionId;
			return this;
		}

		public HashIdentity setContained(boolean theContained) {
			myIsContained = theContained;
			return this;
		}

		public Integer getPartitionId() {
			return myPartitionId;
		}

		public boolean isContained() {
			return myIsContained;
		}

		public String[] getValues() {
			return myValues;
		}
	}

	public ResourceIndexHasher(PartitionSettings thePartitionSettings, StorageSettings theStorageSettings) {
		this.myPartitionSettings = thePartitionSettings;
		this.myStorageSettings = theStorageSettings;
	}

	/**
	 * Compute a normalized hash value for the provided arguments.
	 * @param thePartitionId the partition id to be included in the hash
	 * @param theIsContained true if the hash is computed for a contained resource
	 * @param theValues the list of values to be included in the hash
	 * @return the hash value
	 */
	public long hashNormalized(
			@Nullable final PartitionablePartitionId thePartitionId,
			boolean theIsContained,
			@NotNull final String... theValues) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return hashNormalized(requestPartitionId, theIsContained, theValues);
	}

	/**
	 * Compute a normalized hash value for the provided arguments.
	 * @param theRequestPartitionId the partition id to be included in the hash
	 * @param theValues the list of values to be included in the hash
	 * @return the hash value
	 */
	public long hashNormalized(
			@Nullable final RequestPartitionId theRequestPartitionId, @NotNull final String... theValues) {
		return hashNormalized(theRequestPartitionId, false, theValues);
	}

	/**
	 * Compute a normalized hash value for the provided arguments.
	 * @param theRequestPartitionId the partition id to be included in the hash
	 * @param theIsContained true if the hash is computed for a contained resource
	 * @param theValues the list of values to be included in the hash
	 * @return the hash value
	 */
	public long hashNormalized(
			@Nullable final RequestPartitionId theRequestPartitionId,
			boolean theIsContained,
			@NotNull final String... theValues) {
		/*
		 * If we're not allowing contained searches, we'll add the first
		 * bit of the normalized value to the hash. This helps to
		 * make the hash even more unique, which will be good for
		 * performance.
		 */
		int hashPrefixLength = HASH_PREFIX_LENGTH;
		if (myStorageSettings.isAllowContainsSearches()) {
			hashPrefixLength = 0;
		}

		if (theValues.length > 2) {
			theValues[2] = StringUtil.left(theValues[2], hashPrefixLength);
		}
		return hash(theRequestPartitionId, theIsContained, theValues);
	}

	/**
	 * Compute the hash value for a set of provided arguments.
	 * @param thePartitionId the partition id assigned to the object being hashed
	 * @param theIsContained true if the hash is computed for a contained resource
	 * @param theValues the values which need to be hashed
	 * @return the hash value
	 */
	public long hash(
			@Nullable PartitionablePartitionId thePartitionId, boolean theIsContained, @NotNull String... theValues) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return hash(requestPartitionId, theIsContained, theValues);
	}

	/**
	 * Compute the hash value for a set of provided arguments.
	 * @param theRequestPartitionId the partition id assigned to the object being hashed
	 * @param theValues the values which need to be hashed
	 * @return the hash value
	 */
	public long hash(@Nullable RequestPartitionId theRequestPartitionId, @NotNull String... theValues) {
		return hash(theRequestPartitionId, false, theValues);
	}

	/**
	 * Compute the hash value for a set of provided arguments.
	 * @param theRequestPartitionId the partition id assigned to the object being hashed
	 * @param theIsContained true if the hash is computed for a contained resource
	 * @param theValues the values which need to be hashed
	 * @return the hash value
	 */
	public long hash(
			@Nullable RequestPartitionId theRequestPartitionId, boolean theIsContained, @NotNull String... theValues) {
		HashIdentity identity = new HashIdentity(theValues);

		if (myPartitionSettings.isPartitioningEnabled()
				&& myPartitionSettings.isIncludePartitionInSearchHashes()
				&& theRequestPartitionId != null
				&& theRequestPartitionId.hasPartitionIds()) {
			if (theRequestPartitionId.getPartitionIds().size() > 1) {
				throw new InternalErrorException(Msg.code(1527)
						+ "Can not search multiple partitions when partitions are included in search hashes");
			}
			Integer partitionId = theRequestPartitionId.getFirstPartitionIdOrNull();
			identity.setPartitionId(partitionId);
		}

		// TODO: ignore this value for now, since the search functionality is not updated yet to use this parameter
		// identity.setContained(theIsContained);
		identity.setContained(false);

		return IdentityHasher.hash(identity);
	}
}
