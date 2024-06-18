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
	@Column(name = "HASH_IDENTITY")
	private Long myHashIdentity;

	@Column(name = "HASH_COMPLETE")
	private Long myHashComplete;

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
		b.append(myHashIdentity, that.myHashIdentity);
		b.append(myHashComplete, that.myHashComplete);
		return b.isEquals();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		ResourceIndexedComboStringUnique source = (ResourceIndexedComboStringUnique) theSource;
		myPartitionSettings = source.myPartitionSettings;
		myIndexString = source.myIndexString;
		myHashIdentity = source.myHashIdentity;
		myHashComplete = source.myHashComplete;
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

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	@Override
	public void calculateHashes() {
		if (myHashComplete == null) {
			PartitionSettings partitionSettings = getPartitionSettings();
			PartitionablePartitionId partitionId = getPartitionId();
			String queryString = myIndexString;

			setHashIdentity(calculateHashIdentity(partitionSettings, partitionId, getSearchParameterId()));
			setHashComplete(calculateHashComplete(partitionSettings, partitionId, getSearchParameterId(), queryString));
		}
	}

	/**
	 * @param theSearchParameterId Must be exactly in the form <code>[resourceType]/[id]</code>
	 */
	private static long calculateHashComplete(PartitionSettings thePartitionSettings, PartitionablePartitionId thePartitionId, String theSearchParameterId, String theQueryString) {
		assert BaseResourceIndexedCombo.SEARCH_PARAM_ID_PATTERN.matcher(theSearchParameterId).matches();
		return calculateHash(thePartitionSettings, thePartitionId, theSearchParameterId, theQueryString);
	}

	/**
	 * @param theSearchParameterId Must be exactly in the form <code>[resourceType]/[id]</code>
	 */
	private static long calculateHashIdentity(PartitionSettings thePartitionSettings, PartitionablePartitionId thePartitionId, String theSearchParameterId) {
		assert BaseResourceIndexedCombo.SEARCH_PARAM_ID_PATTERN.matcher(theSearchParameterId).matches();
		return calculateHash(thePartitionSettings, thePartitionId, theSearchParameterId);
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashComplete = null;
	}

	@Override
	public int hashCode() {
		calculateHashes();

        return new HashCodeBuilder(17, 37).append(myHashIdentity).append(myHashComplete).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("resourceId", myResourceId)
				.append("indexString", myIndexString)
				.append("partition", getPartitionId())
				.toString();
	}

	public static long calculateHash(
			PartitionSettings partitionSettings, PartitionablePartitionId thePartitionId, String... theValues) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return hash(partitionSettings, requestPartitionId, theValues);
	}
}
