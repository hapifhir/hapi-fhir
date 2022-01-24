package ca.uhn.fhir.jpa.model.entity;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import static ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam.hash;

@Entity
@Table(name = "HFJ_IDX_CMB_TOK_NU", indexes = {
	@Index(name = "IDX_IDXCMBTOKNU_STR", columnList = "IDX_STRING", unique = false),
	@Index(name = "IDX_IDXCMBTOKNU_RES", columnList = "RES_ID", unique = false)
})
public class ResourceIndexedComboTokenNonUnique extends BaseResourceIndex implements Comparable<ResourceIndexedComboTokenNonUnique> {

	@SequenceGenerator(name = "SEQ_IDXCMBTOKNU_ID", sequenceName = "SEQ_IDXCMBTOKNU_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMBTOKNU_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_IDXCMBTOKNU_RES_ID"))
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

	public ResourceIndexedComboTokenNonUnique(PartitionSettings thePartitionSettings, ResourceTable theEntity, String theQueryString) {
		myPartitionSettings = thePartitionSettings;
		myResource = theEntity;
		myIndexString = theQueryString;
		calculateHashes();
	}

	public String getIndexString() {
		return myIndexString;
	}

	public void setIndexString(String theIndexString) {
		myIndexString = theIndexString;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		ResourceIndexedComboTokenNonUnique that = (ResourceIndexedComboTokenNonUnique) theO;

		return new EqualsBuilder()
			.append(myResource, that.myResource)
			.append(myHashComplete, that.myHashComplete)
			.isEquals();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		throw new IllegalStateException(Msg.code(1528));
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
		return new HashCodeBuilder(17, 37)
			.append(myResource)
			.append(myHashComplete)
			.toHashCode();
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public ResourceTable getResource() {
		return myResource;
	}

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

	public static long calculateHashComplete(PartitionSettings partitionSettings, PartitionablePartitionId thePartitionId, String queryString) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return hash(partitionSettings, requestPartitionId, queryString);
	}

	public static long calculateHashComplete(PartitionSettings partitionSettings, RequestPartitionId partitionId, String queryString) {
		return hash(partitionSettings, partitionId, queryString);
	}

}
