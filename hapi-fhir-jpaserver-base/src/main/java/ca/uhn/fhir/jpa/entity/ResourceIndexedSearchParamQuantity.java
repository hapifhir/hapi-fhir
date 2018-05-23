package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.util.BigDecimalNumericFieldBridge;
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
import java.math.RoundingMode;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_QUANTITY", indexes = {
	@Index(name = "IDX_SP_QUANTITY", columnList = "RES_TYPE,SP_NAME,SP_SYSTEM,SP_UNITS,SP_VALUE"),
	@Index(name = "IDX_SP_QUANTITY_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_QUANTITY_RESID", columnList = "RES_ID")
})
//@formatter:on
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
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_UNITS_AND_VALPREFIX", nullable = true)
	private Long myHashUnitsAndValPrefix;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_VALPREFIX", nullable = true)
	private Long myHashValPrefix;

	public ResourceIndexedSearchParamQuantity() {
		// nothing
	}

	public ResourceIndexedSearchParamQuantity(String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		setUnits(theUnits);
	}

	@PrePersist
	public void calculateHashes() {
		if (myHashUnitsAndValPrefix == null) {
			setHashUnitsAndValPrefix(hash(getResourceType(), getParamName(), getSystem(), getUnits(), toTruncatedString(getValue())));
			setHashValPrefix(hash(getResourceType(), getParamName(), toTruncatedString(getValue())));
		}
	}

	@Override
	protected void clearHashes() {
		myHashUnitsAndValPrefix = null;
		myHashValPrefix = null;
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
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getSystem(), obj.getSystem());
		b.append(getUnits(), obj.getUnits());
		b.append(getValue(), obj.getValue());
		b.append(getHashUnitsAndValPrefix(), obj.getHashUnitsAndValPrefix());
		b.append(getHashValPrefix(), obj.getHashValPrefix());
		return b.isEquals();
	}

	public Long getHashUnitsAndValPrefix() {
		calculateHashes();
		return myHashUnitsAndValPrefix;
	}

	public void setHashUnitsAndValPrefix(Long theHashUnitsAndValPrefix) {
		myHashUnitsAndValPrefix = theHashUnitsAndValPrefix;
	}

	public Long getHashValPrefix() {
		calculateHashes();
		return myHashValPrefix;
	}

	public void setHashValPrefix(Long theHashValPrefix) {
		myHashValPrefix = theHashValPrefix;
	}

	@Override
	protected Long getId() {
		return myId;
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

	public void setValue(BigDecimal theValue) {
		clearHashes();
		myValue = theValue;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getSystem());
		b.append(getUnits());
		b.append(getValue());
		b.append(getHashUnitsAndValPrefix());
		b.append(getHashValPrefix());
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
		return b.build();
	}

	private static String toTruncatedString(BigDecimal theValue) {
		if (theValue == null) {
			return null;
		}
		return theValue.setScale(0, RoundingMode.FLOOR).toPlainString();
	}

}
