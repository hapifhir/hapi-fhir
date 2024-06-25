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
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
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
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(
		name = "HFJ_IDX_CMB_TOK_NU",
		indexes = {
			// TODO: The hash index was added in 7.4.0 - In 7.6.0 we should drop the string index
			@Index(name = "IDX_IDXCMBTOKNU_STR", columnList = "IDX_STRING", unique = false),
			@Index(name = "IDX_IDXCMBTOKNU_HASHC", columnList = "HASH_COMPLETE,RES_ID,PARTITION_ID", unique = false),
			@Index(name = "IDX_IDXCMBTOKNU_RES", columnList = "RES_ID", unique = false)
		})
public class ResourceIndexedComboTokenNonUnique extends BaseResourceIndexedCombo
		implements Comparable<ResourceIndexedComboTokenNonUnique>, IResourceIndexComboSearchParameter {

	@SequenceGenerator(name = "SEQ_IDXCMBTOKNU_ID", sequenceName = "SEQ_IDXCMBTOKNU_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMBTOKNU_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			foreignKey = @ForeignKey(name = "FK_IDXCMBTOKNU_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourceId;

	@Column(name = "HASH_COMPLETE", nullable = false)
	private Long myHashComplete;

	@Column(name = "IDX_STRING", nullable = false, length = ResourceIndexedComboStringUnique.MAX_STRING_LENGTH)
	private String myIndexString;

	@Transient
	private transient PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public ResourceIndexedComboTokenNonUnique() {
		super();
	}

	public ResourceIndexedComboTokenNonUnique(
			PartitionSettings thePartitionSettings, ResourceTable theEntity, String theQueryString) {
		myPartitionSettings = thePartitionSettings;
		myResource = theEntity;
		myIndexString = theQueryString;
		calculateHashes();
	}

	@Override
	public String getIndexString() {
		return myIndexString;
	}

	public void setIndexString(String theIndexString) {
		myIndexString = theIndexString;
	}

	@Override
	public boolean equals(Object theO) {
		calculateHashes();

		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		ResourceIndexedComboTokenNonUnique that = (ResourceIndexedComboTokenNonUnique) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getHashComplete(), that.getHashComplete());
		return b.isEquals();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		ResourceIndexedComboTokenNonUnique source = (ResourceIndexedComboTokenNonUnique) theSource;
		myPartitionSettings = source.myPartitionSettings;
		myHashComplete = source.myHashComplete;
		myIndexString = source.myIndexString;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	@Override
	public void clearHashes() {
		myHashComplete = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashComplete != null) {
			return;
		}

		PartitionSettings partitionSettings = getPartitionSettings();
		PartitionablePartitionId partitionId = getPartitionId();
		String queryString = myIndexString;
		setHashComplete(calculateHashComplete(partitionSettings, partitionId, queryString));
	}

	@Override
	public int hashCode() {
		calculateHashes();

		HashCodeBuilder builder = new HashCodeBuilder(17, 37);
		builder.append(getHashComplete());
		return builder.toHashCode();
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Override
	public ResourceTable getResource() {
		return myResource;
	}

	@Override
	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public Long getHashComplete() {
		return myHashComplete;
	}

	public void setHashComplete(Long theHashComplete) {
		myHashComplete = theHashComplete;
	}

	@Override
	public int compareTo(ResourceIndexedComboTokenNonUnique theO) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(myHashComplete, theO.getHashComplete());
		return b.toComparison();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", myId)
				.append("resourceId", myResourceId)
				.append("hashComplete", myHashComplete)
				.append("indexString", myIndexString)
				.toString();
	}

	public static long calculateHashComplete(
			PartitionSettings partitionSettings, PartitionablePartitionId thePartitionId, String queryString) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return SearchParamHash.hashSearchParam(partitionSettings, requestPartitionId, queryString);
	}
}
