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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_DATE" /*, indexes= {@Index(name="IDX_SP_DATE", columnList= "SP_VALUE_LOW,SP_VALUE_HIGH")}*/)
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_DATE", indexes= {
	@org.hibernate.annotations.Index(name="IDX_SP_DATE", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE_LOW","SP_VALUE_HIGH"})
})
//@formatter:on
public class ResourceIndexedSearchParamDate extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE_HIGH", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Field
	public Date myValueHigh;

	@Column(name = "SP_VALUE_LOW", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Field
	public Date myValueLow;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamDate() {
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamDate(String theName, Date theLow, Date theHigh) {
		setParamName(theName);
		setValueLow(theLow);
		setValueHigh(theHigh);
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamDate)) {
			return false;
		}
		ResourceIndexedSearchParamDate obj = (ResourceIndexedSearchParamDate) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getValueHigh(), obj.getValueHigh());
		b.append(getValueLow(), obj.getValueLow());
		return b.isEquals();
	}

	public Date getValueHigh() {
		return myValueHigh;
	}

	public Date getValueLow() {
		return myValueLow;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getValueHigh());
		b.append(getValueLow());
		return b.toHashCode();
	}

	public void setValueHigh(Date theValueHigh) {
		myValueHigh = theValueHigh;
	}

	public void setValueLow(Date theValueLow) {
		myValueLow = theValueLow;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResource().getId()); // TODO: add a field so we don't need to resolve this
		b.append("valueLow", getValueLow());
		b.append("valueHigh", getValueHigh());
		return b.build();
	}
}
