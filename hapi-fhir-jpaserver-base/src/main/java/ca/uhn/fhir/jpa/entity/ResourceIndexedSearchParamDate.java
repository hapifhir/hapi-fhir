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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.param.DateParam;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;
import org.hl7.fhir.r4.model.DateTimeType;

import javax.persistence.*;
import java.util.Date;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_DATE", indexes = {
//	@Index(name = "IDX_SP_DATE", columnList = "RES_TYPE,SP_NAME,SP_VALUE_LOW,SP_VALUE_HIGH"),
	@Index(name = "IDX_SP_DATE_HASH", columnList = "HASH_IDENTITY,SP_VALUE_LOW,SP_VALUE_HIGH"),
	@Index(name = "IDX_SP_DATE_UPDATED", columnList = "SP_UPDATED"),
	@Index(name = "IDX_SP_DATE_RESID", columnList = "RES_ID")
})
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
	@Transient
	private transient String myOriginalValue;
	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_DATE", sequenceName = "SEQ_SPIDX_DATE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_DATE")
	@Column(name = "SP_ID")
	private Long myId;
	/**
	 * @since 3.5.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_IDENTITY", nullable = true)
	private Long myHashIdentity;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamDate() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamDate(String theName, Date theLow, Date theHigh, String theOriginalValue) {
		setParamName(theName);
		setValueLow(theLow);
		setValueHigh(theHigh);
		myOriginalValue = theOriginalValue;
	}

	@PrePersist
	public void calculateHashes() {
		if (myHashIdentity == null) {
			String resourceType = getResourceType();
			String paramName = getParamName();
			setHashIdentity(calculateHashIdentity(resourceType, paramName));
		}
	}

	@Override
	protected void clearHashes() {
		myHashIdentity = null;
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
		b.append(getTimeFromDate(getValueHigh()), getTimeFromDate(obj.getValueHigh()));
		b.append(getTimeFromDate(getValueLow()), getTimeFromDate(obj.getValueLow()));
		b.append(getHashIdentity(), obj.getHashIdentity());
		return b.isEquals();
	}

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	@Override
	protected Long getId() {
		return myId;
	}

	protected Long getTimeFromDate(Date date) {
		if (date != null) {
			return date.getTime();
		}
		return null;
	}

	public Date getValueHigh() {
		return myValueHigh;
	}

	public void setValueHigh(Date theValueHigh) {
		myValueHigh = theValueHigh;
	}

	public Date getValueLow() {
		return myValueLow;
	}

	public void setValueLow(Date theValueLow) {
		myValueLow = theValueLow;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getTimeFromDate(getValueHigh()));
		b.append(getTimeFromDate(getValueLow()));
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		DateTimeType value = new DateTimeType(myOriginalValue);
		if (value.getPrecision().ordinal() > TemporalPrecisionEnum.DAY.ordinal()) {
			value.setTimeZoneZulu(true);
		}
		return new DateParam(value.getValueAsString());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResource().getId()); // TODO: add a field so we don't need to resolve this
		b.append("valueLow", new InstantDt(getValueLow()));
		b.append("valueHigh", new InstantDt(getValueHigh()));
		return b.build();
	}
}
