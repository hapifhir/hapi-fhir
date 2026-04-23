/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(
		name = ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU,
		indexes = {
			@Index(name = "IDX_IDXCMBTOKNU_HASHC", columnList = "HASH_COMPLETE,RES_ID,PARTITION_ID", unique = false),
			@Index(
					name = "IDX_IDXCMBTOKNU_HD",
					columnList = "HASH_COMPLETE,DATE_ORDINAL,RES_ID,PARTITION_ID",
					unique = false),
			@Index(name = "IDX_IDXCMBTOKNU_RES", columnList = "RES_ID", unique = false)
		})
@IdClass(IdAndPartitionId.class)
public class ResourceIndexedComboTokenNonUnique extends BaseResourceIndexedCombo
		implements Comparable<ResourceIndexedComboTokenNonUnique>, IResourceIndexComboSearchParameter {

	public static final String HFJ_IDX_CMB_TOK_NU = "HFJ_IDX_CMB_TOK_NU";

	@GenericGenerator(
			name = "SEQ_IDXCMBTOKNU_ID",
			type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMBTOKNU_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(
			optional = false,
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						referencedColumnName = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = true),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = true)
			},
			foreignKey = @ForeignKey(name = "FK_IDXCMBTOKNU_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", updatable = false, nullable = true)
	private Long myResourceId;

	@Column(name = "HASH_COMPLETE", nullable = false)
	private Long myHashComplete;

	@Column(name = "DATE_ORDINAL", nullable = true)
	private Integer myDateOrdinal;

	@Column(name = "IDX_STRING", nullable = true, length = ResourceIndexedComboStringUnique.MAX_STRING_LENGTH)
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

	public Integer getDateOrdinal() {
		return myDateOrdinal;
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
		b.append(getDateOrdinal(), that.getDateOrdinal());
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
	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
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
		builder.append(getDateOrdinal());
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
		b.append(myDateOrdinal, theO.getDateOrdinal());
		b.append(myHashComplete, theO.getHashComplete());
		return b.toComparison();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", myId)
				.append("resourceId", myResourceId)
				.append("dateOrdinal", myDateOrdinal)
				.append("hashComplete", myHashComplete)
				.append("indexString", myIndexString)
				.toString();
	}

	public void applyRangedDate(
			StorageSettings theStorageSettings, @Nonnull ResourceIndexedSearchParamDate theDateParam) {
		String endOfTime = theStorageSettings.getPeriodIndexEndOfTime().getValueAsString();
		String endOfTimeDate = left(endOfTime, 10);
		int endOfTimeOrdinal = Integer.parseInt(endOfTimeDate.replace("-", ""));
		if (endOfTimeOrdinal == theDateParam.getValueHighDateOrdinal()) {
			myDateOrdinal = theDateParam.getValueLowDateOrdinal();
		} else {
			myDateOrdinal = theDateParam.getValueHighDateOrdinal();
		}
	}

	public static long calculateHashComplete(
			PartitionSettings partitionSettings, PartitionablePartitionId thePartitionId, String queryString) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(thePartitionId);
		return SearchParamHash.hashSearchParam(partitionSettings, requestPartitionId, queryString);
	}
}
