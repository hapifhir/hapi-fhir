package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR Model
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

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_NUMBER", indexes = {
//	We used to have an index with name IDX_SP_NUMBER - Dont reuse
	@Index(name = "IDX_SP_NUMBER_HASH_VAL", columnList = "HASH_IDENTITY,SP_VALUE"),
	@Index(name = "IDX_SP_NUMBER_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_NUMBER_RESID", columnList = "RES_ID")
})
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;
	@Column(name = "SP_VALUE", nullable = true)
	@ScaledNumberField
	public BigDecimal myValue;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_NUMBER", sequenceName = "SEQ_SPIDX_NUMBER")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_NUMBER")
	@Column(name = "SP_ID")
	private Long myId;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	public ResourceIndexedSearchParamNumber() {
	}

	public ResourceIndexedSearchParamNumber(PartitionSettings thePartitionSettings, String theResourceType, String theParamName, BigDecimal theValue) {
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
	public void calculateHashes() {
		String resourceType = getResourceType();
		String paramName = getParamName();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
	}

	public Long getHashIdentity() {
		return myHashIdentity;
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
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
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
		b.append(getResourceType());
		b.append(getParamName());
		b.append(getValue());
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

}
