/*
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

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.listener.IndexStorageOptimizationListener;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@EntityListeners(IndexStorageOptimizationListener.class)
@Entity
@Table(
		name = "HFJ_SPIDX_NUMBER",
		indexes = {
			//	We used to have an index with name IDX_SP_NUMBER - Dont reuse
			@Index(name = "IDX_SP_NUMBER_HASH_VAL_V2", columnList = "HASH_IDENTITY,SP_VALUE,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_NUMBER_RESID_V2", columnList = "RES_ID, HASH_IDENTITY, SP_VALUE, PARTITION_ID")
		})
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE", nullable = true, precision = 19, scale = 2)
	@ScaledNumberField
	@JdbcTypeCode(SqlTypes.DECIMAL)
	public BigDecimal myValue;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_NUMBER", sequenceName = "SEQ_SPIDX_NUMBER")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_NUMBER")
	@Column(name = "SP_ID")
	private Long myId;

	@ManyToOne(
			optional = false,
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumn(
			foreignKey = @ForeignKey(name = "FK_SP_NUMBER_RES"),
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false)
	private ResourceTable myResource;

	public ResourceIndexedSearchParamNumber() {}

	public ResourceIndexedSearchParamNumber(
			PartitionSettings thePartitionSettings, String theResourceType, String theParamName, BigDecimal theValue) {
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setValue(theValue);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamNumber source = (ResourceIndexedSearchParamNumber) theSource;
		myValue = source.myValue;
		myHashIdentity = source.myHashIdentity;
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashIdentity != null) {
			return;
		}
		String resourceType = getResourceType();
		String paramName = getParamName();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamNumber)) {
			return false;
		}
		ResourceIndexedSearchParamNumber obj = (ResourceIndexedSearchParamNumber) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(normalizeForEqualityComparison(getValue()), normalizeForEqualityComparison(obj.getValue()));
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	private Double normalizeForEqualityComparison(BigDecimal theValue) {
		if (theValue == null) {
			return null;
		}
		return theValue.doubleValue();
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	public BigDecimal getValue() {
		return myValue;
	}

	public void setValue(BigDecimal theValue) {
		myValue = theValue;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getHashIdentity());
		b.append(normalizeForEqualityComparison(getValue()));
		b.append(isMissing());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new NumberParam(myValue.toPlainString());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResource().getId()); // TODO: add a field so we don't need to resolve this
		b.append("value", getValue());
		return b.build();
	}

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof NumberParam)) {
			return false;
		}
		NumberParam number = (NumberParam) theParam;
		return Objects.equals(getValue(), number.getValue());
	}

	@Override
	public ResourceTable getResource() {
		return myResource;
	}

	@Override
	public BaseResourceIndexedSearchParam setResource(ResourceTable theResource) {
		myResource = theResource;
		setResourceType(theResource.getResourceType());
		return this;
	}
}
