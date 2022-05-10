package ca.uhn.fhir.jpa.model.entity;

/*
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

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_QUANTITY", indexes = {
//	We used to have an index named IDX_SP_QUANTITY - Dont reuse
	@Index(name = "IDX_SP_QUANTITY_HASH_V2", columnList = "HASH_IDENTITY,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QUANTITY_HASH_UN_V2", columnList = "HASH_IDENTITY_AND_UNITS,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QUANTITY_HASH_SYSUN_V2", columnList = "HASH_IDENTITY_SYS_UNITS,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QUANTITY_RESID_V2", columnList = "RES_ID,HASH_IDENTITY,HASH_IDENTITY_SYS_UNITS,HASH_IDENTITY_AND_UNITS,SP_VALUE,PARTITION_ID")
})
public class ResourceIndexedSearchParamQuantity extends ResourceIndexedSearchParamBaseQuantity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_QUANTITY", sequenceName = "SEQ_SPIDX_QUANTITY")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_QUANTITY")
	@Column(name = "SP_ID")
	private Long myId;
	
	@Column(name = "SP_VALUE", nullable = true)
	@ScaledNumberField
	public Double myValue;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_SP_QUANTITY_RES"),
		name = "RES_ID", referencedColumnName = "RES_ID", nullable = false)
	private ResourceTable myResource;

	public ResourceIndexedSearchParamQuantity() {
		super();
	}

	public ResourceIndexedSearchParamQuantity(PartitionSettings thePartitionSettings, String theResourceType, String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
		this();
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		setUnits(theUnits);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamQuantity source = (ResourceIndexedSearchParamQuantity) theSource;
		mySystem = source.mySystem;
		myUnits = source.myUnits;
		myValue = source.myValue;
		setHashIdentity(source.getHashIdentity());
		setHashIdentityAndUnits(source.getHashIdentityAndUnits());
		setHashIdentitySystemAndUnits(source.getHashIdentitySystemAndUnits());
	}
	
	public BigDecimal getValue() {
		return myValue != null ? new BigDecimal(myValue) : null;
	}

	public ResourceIndexedSearchParamQuantity setValue(BigDecimal theValue) {
		myValue = theValue != null ? theValue.doubleValue() : null;
		return this;
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
	public IQueryParameterType toQueryParameterType() {
		return new QuantityParam(null, getValue(), getSystem(), getUnits());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("system", getSystem());
		b.append("units", getUnits());
		b.append("value", getValue());
		b.append("missing", isMissing());
		b.append("hashIdentitySystemAndUnits", getHashIdentitySystemAndUnits());
		return b.build();
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamQuantity)) {
			return false;
		}
		ResourceIndexedSearchParamQuantity obj = (ResourceIndexedSearchParamQuantity) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(getHashIdentityAndUnits(), obj.getHashIdentityAndUnits());
		b.append(getHashIdentitySystemAndUnits(), obj.getHashIdentitySystemAndUnits());
		b.append(isMissing(), obj.isMissing());
		b.append(getValue(), obj.getValue());
		return b.isEquals();
	}
	
	@Override
	public boolean matches(IQueryParameterType theParam) {
		
		if (!(theParam instanceof QuantityParam)) {
			return false;
		}
		QuantityParam quantity = (QuantityParam) theParam;
		boolean retval = false;

		// Only match on system if it wasn't specified
		String quantityUnitsString = defaultString(quantity.getUnits());
		if (quantity.getSystem() == null && isBlank(quantityUnitsString)) {
			if (Objects.equals(getValue(), quantity.getValue())) {
				retval = true;
			}
		} else {
			String unitsString = defaultString(getUnits());
			if (quantity.getSystem() == null) {
				if (unitsString.equalsIgnoreCase(quantityUnitsString) &&
					Objects.equals(getValue(), quantity.getValue())) {
					retval = true;
				}
			} else if (isBlank(quantityUnitsString)) {
				if (getSystem().equalsIgnoreCase(quantity.getSystem()) &&
					Objects.equals(getValue(), quantity.getValue())) {
					retval = true;
				}
			} else {
				if (getSystem().equalsIgnoreCase(quantity.getSystem()) &&
					unitsString.equalsIgnoreCase(quantityUnitsString) &&
					Objects.equals(getValue(), quantity.getValue())) {
					retval = true;
				}
			}
		}
		
		return retval;
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
