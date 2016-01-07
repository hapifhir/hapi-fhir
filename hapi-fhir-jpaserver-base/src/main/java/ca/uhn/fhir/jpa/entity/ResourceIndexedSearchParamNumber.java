package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.NumericField;

import ca.uhn.fhir.jpa.util.BigDecimalNumericFieldBridge;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_NUMBER" /*, indexes= {@Index(name="IDX_SP_NUMBER", columnList="SP_VALUE")}*/ )
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_NUMBER", indexes= {
		@org.hibernate.annotations.Index(name="IDX_SP_NUMBER", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE"}
	)})
//@formatter:on
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE", nullable = true)
	@Field
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	public BigDecimal myValue;

	public ResourceIndexedSearchParamNumber() {
	}

	public ResourceIndexedSearchParamNumber(String theParamName, BigDecimal theValue) {
		setParamName(theParamName);
		setValue(theValue);
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
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getValue(), obj.getValue());
		return b.isEquals();
	}

	public BigDecimal getValue() {
		return myValue;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getValue());
		return b.toHashCode();
	}

	public void setValue(BigDecimal theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResource().getId()); // TODO: add a field so we don't need to resolve this
		b.append("value", getValue());
		return b.build();
	}
}
