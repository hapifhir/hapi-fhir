package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.util.BigDecimalNumericFieldBridge;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.QuantityParam;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.NumericField;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_QUANTITY", indexes = {
//	We used to have an index named IDX_SP_QUANTITY - Dont reuse
	@Index(name = "IDX_SP_QUANTITY_HASH", columnList = "HASH_IDENTITY,SP_VALUE"),
	@Index(name = "IDX_SP_QUANTITY_HASH_UN", columnList = "HASH_IDENTITY_AND_UNITS,SP_VALUE"),
	@Index(name = "IDX_SP_QUANTITY_HASH_SYSUN", columnList = "HASH_IDENTITY_SYS_UNITS,SP_VALUE"),
	@Index(name = "IDX_SP_QUANTITY_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_QUANTITY_RESID", columnList = "RES_ID")
})
public class ResourceIndexedSearchParamQuantity extends BaseResourceIndexedSearchParam {

	private static final int MAX_LENGTH = 200;

	private static final long serialVersionUID = 1L;
	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	@Field
	public String mySystem;
	@Column(name = "SP_UNITS", nullable = true, length = MAX_LENGTH)
	@Field
	public String myUnits;
	@Column(name = "SP_VALUE", nullable = true)
	@Field
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	public BigDecimal myValue;
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_QUANTITY", sequenceName = "SEQ_SPIDX_QUANTITY")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_QUANTITY")
	@Column(name = "SP_ID")
	private Long myId;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY_AND_UNITS", nullable = true)
	private Long myHashIdentityAndUnits;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY_SYS_UNITS", nullable = true)
	private Long myHashIdentitySystemAndUnits;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	public ResourceIndexedSearchParamQuantity() {
		super();
	}


	public ResourceIndexedSearchParamQuantity(String theResourceType, String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
		this();
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		setUnits(theUnits);
	}

	@Override
	@PrePersist
	public void calculateHashes() {
		if (myHashIdentity == null) {
			String resourceType = getResourceType();
			String paramName = getParamName();
			String units = getUnits();
			String system = getSystem();
			setHashIdentity(calculateHashIdentity(resourceType, paramName));
			setHashIdentityAndUnits(calculateHashUnits(resourceType, paramName, units));
			setHashIdentitySystemAndUnits(calculateHashSystemAndUnits(resourceType, paramName, system, units));
		}
	}

	@Override
	protected void clearHashes() {
		myHashIdentity = null;
		myHashIdentityAndUnits = null;
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
		b.append(getSystem(), obj.getSystem());
		b.append(getUnits(), obj.getUnits());
		b.append(getValue(), obj.getValue());
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	public Long getHashIdentity() {
		calculateHashes();
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	public Long getHashIdentityAndUnits() {
		calculateHashes();
		return myHashIdentityAndUnits;
	}

	public void setHashIdentityAndUnits(Long theHashIdentityAndUnits) {
		myHashIdentityAndUnits = theHashIdentityAndUnits;
	}

	private Long getHashIdentitySystemAndUnits() {
		calculateHashes();
		return myHashIdentitySystemAndUnits;
	}

	public void setHashIdentitySystemAndUnits(Long theHashIdentitySystemAndUnits) {
		myHashIdentitySystemAndUnits = theHashIdentitySystemAndUnits;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId =theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		clearHashes();
		mySystem = theSystem;
	}

	public String getUnits() {
		return myUnits;
	}

	public void setUnits(String theUnits) {
		clearHashes();
		myUnits = theUnits;
	}

	public BigDecimal getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamQuantity setValue(BigDecimal theValue) {
		clearHashes();
		myValue = theValue;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
		b.append(getSystem());
		b.append(getUnits());
		b.append(getValue());
		return b.toHashCode();
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
		b.append("hashIdentitySystemAndUnits", myHashIdentitySystemAndUnits);
		return b.build();
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
			if (Objects.equals(getValue(),quantity.getValue())) {
				retval = true;
			}
		} else {
			String unitsString = defaultString(getUnits());
			if (quantity.getSystem() == null) {
				if (unitsString.equalsIgnoreCase(quantityUnitsString) &&
					Objects.equals(getValue(),quantity.getValue())) {
					retval = true;
				}
			} else if (isBlank(quantityUnitsString)) {
				if (getSystem().equalsIgnoreCase(quantity.getSystem()) &&
					Objects.equals(getValue(),quantity.getValue())) {
					retval = true;
				}
			} else {
				if (getSystem().equalsIgnoreCase(quantity.getSystem()) &&
					unitsString.equalsIgnoreCase(quantityUnitsString) &&
					Objects.equals(getValue(),quantity.getValue())) {
					retval = true;
				}
			}
		}
		return retval;
	}

	public static long calculateHashSystemAndUnits(String theResourceType, String theParamName, String theSystem, String theUnits) {
		return hash(theResourceType, theParamName, theSystem, theUnits);
	}

	public static long calculateHashUnits(String theResourceType, String theParamName, String theUnits) {
		return hash(theResourceType, theParamName, theUnits);
	}


}
