package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import java.util.Collections;

import static ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam.calculateHashIdentity;

@Entity
@Table(name = "HFJ_IDX_CMB_TOK_NU", indexes = {
	@Index(name = "IDX_IDXCMBTOKNU_HC", columnList = "HASH_COMPLETE", unique = false),
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

	@Transient
	private transient String myResourceType;
	@Transient
	private transient String myParamName;
	@Transient
	private transient String myQueryString;
	@Transient
	private transient PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public ResourceIndexedComboTokenNonUnique() {
		super();
	}

	public ResourceIndexedComboTokenNonUnique(PartitionSettings thePartitionSettings, ResourceTable theEntity, String theResourceType, String theParamName, String theQueryString) {
		myPartitionSettings = thePartitionSettings;
		myResource = theEntity;
		myResourceType = theResourceType;
		myParamName = theParamName;
		myQueryString = theQueryString;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public String getQueryString() {
		return myQueryString;
	}

	public void setQueryString(String theQueryString) {
		myQueryString = theQueryString;
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
		throw new IllegalStateException();
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
	public void calculateHashes() {
		String resourceType = getResourceType();
		String paramName = getParamName();
		PartitionSettings partitionSettings = getPartitionSettings();
		PartitionablePartitionId partitionId = getPartitionId();
		String queryString = myQueryString;
		setHashComplete(calculateHashComplete(resourceType, paramName, partitionSettings, partitionId, queryString));
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
			.append("resourceType", myResourceType)
			.append("paramName", myParamName)
			.append("hashComplete", myHashComplete)
			.toString();
	}

	public static long calculateHashComplete(String resourceType, String paramName, PartitionSettings partitionSettings, PartitionablePartitionId partitionId, String queryString) {
		return calculateHashIdentity(partitionSettings, partitionId, resourceType, paramName, Collections.singletonList(queryString));
	}

	public static long calculateHashComplete(String resourceType, String paramName, PartitionSettings partitionSettings, RequestPartitionId partitionId, String queryString) {
		return calculateHashIdentity(partitionSettings, partitionId, resourceType, paramName, Collections.singletonList(queryString));
	}

}
