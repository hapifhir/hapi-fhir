/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IIdType;

import static ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam.hash;

@Entity()
@Table(
		name = "HFJ_IDX_CMP_STRING_UNIQ",
		indexes = {
			@Index(
					name = ResourceIndexedComboStringUnique.IDX_IDXCMPSTRUNIQ_STRING,
					columnList = "IDX_STRING",
					unique = true),
			@Index(
					name = ResourceIndexedComboStringUnique.IDX_IDXCMPSTRUNIQ_RESOURCE,
					columnList = "RES_ID",
					unique = false)
		})
public class ResourceIndexedComboStringUnique extends BaseResourceIndexedCombo
		implements Comparable<ResourceIndexedComboStringUnique>, IResourceIndexComboSearchParameter {

	public static final int MAX_STRING_LENGTH = 500;
	public static final String IDX_IDXCMPSTRUNIQ_STRING = "IDX_IDXCMPSTRUNIQ_STRING";
	public static final String IDX_IDXCMPSTRUNIQ_RESOURCE = "IDX_IDXCMPSTRUNIQ_RESOURCE";

	@SequenceGenerator(name = "SEQ_IDXCMPSTRUNIQ_ID", sequenceName = "SEQ_IDXCMPSTRUNIQ_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMPSTRUNIQ_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			foreignKey = @ForeignKey(name = "FK_IDXCMPSTRUNIQ_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourceId;

	// TODO: These hashes were added in 7.4.0 - They aren't used or indexed yet, but
	// eventually we should replace the string index with a hash index in order to
	// reduce the space usage.
	@Column(name = "HASH_COMPLETE")
	private Long myHashComplete;

	/**
	 * Because we'll be using these hashes to enforce uniqueness, the risk of
	 * collisions is bad, since it would be plain impossible to insert a row
	 * with a false collision here. So in order to reduce that risk, we
	 * double the number of bits we hash by having two hashes.
	 *
	 * @see #calculateHashComplete2(PartitionSettings, PartitionablePartitionId, String) to see how this is calculated
	 */
	@Column(name = "HASH_COMPLETE_2")
	private Long myHashComplete2;

	@Column(name = "IDX_STRING", nullable = false, length = MAX_STRING_LENGTH)
	private String myIndexString;

	/**
	 * This is here to support queries only, do not set this field directly
	 */
	@SuppressWarnings("unused")
	@Column(name = PartitionablePartitionId.PARTITION_ID, insertable = false, updatable = false, nullable = true)
	private Integer myPartitionIdValue;

	@Transient
	private PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public ResourceIndexedComboStringUnique() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedComboStringUnique(
			ResourceTable theResource,
			PartitionSettings thePartitionSettings,
			String theIndexString,
			IIdType theSearchParameterId) {
		setResource(theResource);
		setPartitionSettings(thePartitionSettings);
		setIndexString(theIndexString);
		setPartitionId(theResource.getPartitionId());
		setSearchParameterId(theSearchParameterId);
	}

	@Override
	public int compareTo(ResourceIndexedComboStringUnique theO) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(myIndexString, theO.getIndexString());
		return b.toComparison();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof ResourceIndexedComboStringUnique)) {
			return false;
		}

		calculateHashes();

		ResourceIndexedComboStringUnique that = (ResourceIndexedComboStringUnique) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myHashComplete, that.myHashComplete);
		b.append(myHashComplete2, that.myHashComplete2);
		return b.isEquals();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		ResourceIndexedComboStringUnique source = (ResourceIndexedComboStringUnique) theSource;
		myPartitionSettings = source.myPartitionSettings;
		myIndexString = source.myIndexString;
		myHashComplete = source.myHashComplete;
		myHashComplete2 = source.myHashComplete2;
	}

	@Override
	public String getIndexString() {
		return myIndexString;
	}

	public void setIndexString(String theIndexString) {
		myIndexString = theIndexString;
	}

	@Override
	public ResourceTable getResource() {
		return myResource;
	}

	@Override
	public void setResource(ResourceTable theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		myResource = theResource;
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	public Long getHashComplete() {
		return myHashComplete;
	}

	public void setHashComplete(Long theHashComplete) {
		myHashComplete = theHashComplete;
	}

	public Long getHashComplete2() {
		return myHashComplete2;
	}

	public void setHashComplete2(Long theHashComplete2) {
		myHashComplete2 = theHashComplete2;
	}

	@Override
	public void setPlaceholderHashesIfMissing() {
		super.setPlaceholderHashesIfMissing();
		if (myHashComplete == null) {
			myHashComplete = 0L;
		}
		if (myHashComplete2 == null) {
			myHashComplete2 = 0L;
		}
	}

	@Override
	public void calculateHashes() {
		if (myHashComplete == null) {
			PartitionSettings partitionSettings = getPartitionSettings();
			PartitionablePartitionId partitionId = getPartitionId();
			String queryString = myIndexString;

			setHashComplete(calculateHashComplete(partitionSettings, partitionId, queryString));
			setHashComplete2(calculateHashComplete2(partitionSettings, partitionId, queryString));
		}
	}

	private static long calculateHashComplete(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId thePartitionId,
			String theQueryString) {
		return calculateHash(thePartitionSettings, thePartitionId, theQueryString);
	}

	private static long calculateHashComplete2(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId thePartitionId,
			String theQueryString) {
		// Just add a constant salt to the query string in order to hopefully
		// further avoid collisions
		String newQueryString = theQueryString + "ABC123";
		return calculateHashComplete(thePartitionSettings, thePartitionId, newQueryString);
	}

	@Override
	public void clearHashes() {
		myHashComplete = null;
		myHashComplete2 = null;
	}

	@Override
	public int hashCode() {
		calculateHashes();

		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(myHashComplete);
		b.append(myHashComplete2);
		return b.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("resourceId", myResourceId)
				.append("indexString", myIndexString)
				.append("hashComplete", myHashComplete)
				.append("hashComplete2", myHashComplete2)
				.append("partition", getPartitionId())
				.toString();
	}

	public static long calculateHash(
			PartitionSettings partitionSettings, PartitionablePartitionId thePartitionId, String... theValues) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return hash(partitionSettings, requestPartitionId, theValues);
	}
}
