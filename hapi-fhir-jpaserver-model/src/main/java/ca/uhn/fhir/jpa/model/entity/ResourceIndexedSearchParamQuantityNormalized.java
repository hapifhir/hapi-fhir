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
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.fhir.ucum.Pair;
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
@Table(name = "HFJ_SPIDX_QUANTITY_NRML", indexes = {
	@Index(name = "IDX_SP_QNTY_NRML_HASH_V2", columnList = "HASH_IDENTITY,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QNTY_NRML_HASH_UN_V2", columnList = "HASH_IDENTITY_AND_UNITS,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QNTY_NRML_HASH_SYSUN_V2", columnList = "HASH_IDENTITY_SYS_UNITS,SP_VALUE,RES_ID,PARTITION_ID"),
	@Index(name = "IDX_SP_QNTY_NRML_RESID_V2", columnList = "RES_ID,HASH_IDENTITY,HASH_IDENTITY_SYS_UNITS,HASH_IDENTITY_AND_UNITS,SP_VALUE,PARTITION_ID")
})
/**
 * Support UCUM service
 * @since 5.3.0 
 *
 */
public class ResourceIndexedSearchParamQuantityNormalized extends ResourceIndexedSearchParamBaseQuantity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_QUANTITY_NRML", sequenceName = "SEQ_SPIDX_QUANTITY_NRML")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_QUANTITY_NRML")
	@Column(name = "SP_ID")
	private Long myId;

	// Changed to double here for storing the value after converted to the CanonicalForm due to BigDecimal maps NUMBER(19,2) 
	// The precision may lost even to store 1.2cm which is 0.012m in the CanonicalForm
	@Column(name = "SP_VALUE", nullable = true)
	@ScaledNumberField
	public Double myValue;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_SP_QUANTITYNM_RES"),
		name = "RES_ID", referencedColumnName = "RES_ID", nullable = false)
	private ResourceTable myResource;

	public ResourceIndexedSearchParamQuantityNormalized() {
		super();
	}

	public ResourceIndexedSearchParamQuantityNormalized(PartitionSettings thePartitionSettings, String theResourceType, String theParamName, double theValue, String theSystem, String theUnits) {
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
		ResourceIndexedSearchParamQuantityNormalized source = (ResourceIndexedSearchParamQuantityNormalized) theSource;
		mySystem = source.mySystem;
		myUnits = source.myUnits;
		myValue = source.myValue;
		setHashIdentity(source.getHashIdentity());
		setHashIdentityAndUnits(source.getHashIdentityAndUnits());
		setHashIdentitySystemAndUnits(source.getHashIdentitySystemAndUnits());
	}
	
	//- myValue
	public Double getValue() {
		return myValue;
	}
	public ResourceIndexedSearchParamQuantityNormalized setValue(Double theValue) {
		myValue = theValue;
		return this;
	}

	public ResourceIndexedSearchParamQuantityNormalized setValue(double theValue) {
		myValue = theValue;
		return this;
	}

	//-- myId
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
		if (!(theObj instanceof ResourceIndexedSearchParamQuantityNormalized)) {
			return false;
		}
		ResourceIndexedSearchParamQuantityNormalized obj = (ResourceIndexedSearchParamQuantityNormalized) theObj;
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

		String quantitySystem = quantity.getSystem();
		BigDecimal quantityValue = quantity.getValue();
		Double quantityDoubleValue = null;
		if (quantityValue != null)
			quantityDoubleValue = quantityValue.doubleValue();
		String quantityUnits = defaultString(quantity.getUnits());
		
		//-- convert the value/unit to the canonical form if any, otherwise store the original value/units pair
		Pair canonicalForm = UcumServiceUtil.getCanonicalForm(quantitySystem, quantityValue, quantityUnits);
		if (canonicalForm != null) {
			quantityDoubleValue = Double.parseDouble(canonicalForm.getValue().asDecimal());
			quantityUnits = canonicalForm.getCode();
		}  
		
		// Only match on system if it wasn't specified
		if (quantitySystem == null && isBlank(quantityUnits)) {
			if (Objects.equals(getValue(), quantityDoubleValue)) {
				retval = true;
			}
		} else {
			String unitsString = defaultString(getUnits());
			if (quantitySystem == null) {
				if (unitsString.equalsIgnoreCase(quantityUnits) &&
					Objects.equals(getValue(), quantityDoubleValue)) {
					retval = true;
				}
			} else if (isBlank(quantityUnits)) {
				if (getSystem().equalsIgnoreCase(quantitySystem) &&
					Objects.equals(getValue(), quantityDoubleValue)) {
					retval = true;
				}
			} else {
				if (getSystem().equalsIgnoreCase(quantitySystem) &&
					unitsString.equalsIgnoreCase(quantityUnits) &&
					Objects.equals(getValue(), quantityDoubleValue)) {
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
