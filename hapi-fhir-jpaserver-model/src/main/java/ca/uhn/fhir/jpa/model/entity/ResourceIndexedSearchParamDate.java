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

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
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
	// We previously had an index called IDX_SP_DATE - Dont reuse
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
	public ResourceIndexedSearchParamDate(String theResourceType, String theParamName, Date theLow, Date theHigh, String theOriginalValue) {
		setResourceType(theResourceType);
		setParamName(theParamName);
		setValueLow(theLow);
		setValueHigh(theHigh);
		myOriginalValue = theOriginalValue;
	}

	@Override
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
		b.append(getResourceType(), obj.getResourceType());
		b.append(getParamName(), obj.getParamName());
		b.append(getTimeFromDate(getValueHigh()), getTimeFromDate(obj.getValueHigh()));
		b.append(getTimeFromDate(getValueLow()), getTimeFromDate(obj.getValueLow()));
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
		myId =theId;
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

	public ResourceIndexedSearchParamDate setValueHigh(Date theValueHigh) {
		myValueHigh = theValueHigh;
		return this;
	}

	public Date getValueLow() {
		return myValueLow;
	}

	public ResourceIndexedSearchParamDate setValueLow(Date theValueLow) {
		myValueLow = theValueLow;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getResourceType());
		b.append(getParamName());
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

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof DateParam)) {
			return false;
		}
		DateParam date = (DateParam) theParam;
		DateRangeParam range = new DateRangeParam(date);
		Date lowerBound = range.getLowerBoundAsInstant();
		Date upperBound = range.getUpperBoundAsInstant();

		if (lowerBound == null && upperBound == null) {
			// should never happen
			return false;
		}

		boolean result = true;
		if (lowerBound != null) {
			result &= (myValueLow.after(lowerBound) || myValueLow.equals(lowerBound));
			result &= (myValueHigh.after(lowerBound) || myValueHigh.equals(lowerBound));
		}
		if (upperBound != null) {
			result &= (myValueLow.before(upperBound) || myValueLow.equals(upperBound));
			result &= (myValueHigh.before(upperBound) || myValueHigh.equals(upperBound));
		}
		return result;
	}

}
