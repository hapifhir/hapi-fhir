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

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;
import org.hl7.fhir.r4.model.DateTimeType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_DATE", indexes = {
	// We previously had an index called IDX_SP_DATE - Dont reuse
	@Index(name = "IDX_SP_DATE_HASH", columnList = "HASH_IDENTITY,SP_VALUE_LOW,SP_VALUE_HIGH"),
	@Index(name = "IDX_SP_DATE_HASH_LOW", columnList = "HASH_IDENTITY,SP_VALUE_LOW"),
	@Index(name = "IDX_SP_DATE_ORD_HASH", columnList = "HASH_IDENTITY,SP_VALUE_LOW_DATE_ORDINAL,SP_VALUE_HIGH_DATE_ORDINAL"),
	@Index(name = "IDX_SP_DATE_ORD_HASH_LOW", columnList = "HASH_IDENTITY,SP_VALUE_LOW_DATE_ORDINAL"),
	@Index(name = "IDX_SP_DATE_RESID", columnList = "RES_ID"),
	@Index(name = "IDX_SP_DATE_UPDATED", columnList = "SP_UPDATED"),
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

	/**
	 * Field which stores an integer representation of YYYYMDD as calculated by Calendar
	 * e.g. 2019-01-20 -> 20190120
	 */
	@Column(name = "SP_VALUE_LOW_DATE_ORDINAL")
	public Integer myValueLowDateOrdinal;
	@Column(name = "SP_VALUE_HIGH_DATE_ORDINAL")
	public Integer myValueHighDateOrdinal;

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
	public ResourceIndexedSearchParamDate(PartitionSettings thePartitionSettings, String theResourceType, String theParamName, Date theLow, String theLowString, Date theHigh, String theHighString, String theOriginalValue) {
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setValueLow(theLow);
		setValueHigh(theHigh);
		if (theHigh != null && theHighString == null) {
			theHighString = DateUtils.convertDateToIso8601String(theHigh);
		}
		if (theLow != null && theLowString == null) {
			theLowString = DateUtils.convertDateToIso8601String(theLow);
		}
		computeValueHighDateOrdinal(theHighString);
		computeValueLowDateOrdinal(theLowString);
		myOriginalValue = theOriginalValue;
		calculateHashes();
	}

	private void computeValueHighDateOrdinal(String theHigh) {
		if (!StringUtils.isBlank(theHigh)) {
			this.myValueHighDateOrdinal = generateOrdinalDateInteger(theHigh);
		}
	}

	private int generateOrdinalDateInteger(String theDateString) {
		if (theDateString.contains("T")) {
			theDateString = theDateString.substring(0, theDateString.indexOf("T"));
		}
		theDateString = theDateString.replace("-", "");
		return Integer.valueOf(theDateString);
	}

	private void computeValueLowDateOrdinal(String theLow) {
		if (StringUtils.isNotBlank(theLow)) {
			this.myValueLowDateOrdinal = generateOrdinalDateInteger(theLow);
		}
	}

	public Integer getValueLowDateOrdinal() {
		return myValueLowDateOrdinal;
	}

	public Integer getValueHighDateOrdinal() {
		return myValueHighDateOrdinal;
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamDate source = (ResourceIndexedSearchParamDate) theSource;
		myValueHigh = source.myValueHigh;
		myValueLow = source.myValueLow;
		myValueHighDateOrdinal = source.myValueHighDateOrdinal;
		myValueLowDateOrdinal = source.myValueLowDateOrdinal;
		myHashIdentity = source.myHashIdentity;
	}

	@Override
	public void calculateHashes() {
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
		myId = theId;
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
		b.append("partitionId", getPartitionId());
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("valueLow", new InstantDt(getValueLow()));
		b.append("valueHigh", new InstantDt(getValueHigh()));
		b.append("hashIdentity", myHashIdentity);
		b.append("missing", isMissing());
		return b.build();
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean matches(IQueryParameterType theParam, boolean theUseOrdinalDatesForDayComparison) {
		if (!(theParam instanceof DateParam)) {
			return false;
		}
		DateParam dateParam = (DateParam) theParam;
		DateRangeParam range = new DateRangeParam(dateParam);


		boolean result;
		if (theUseOrdinalDatesForDayComparison) {
			result = matchesOrdinalDateBounds(range);
			result = matchesDateBounds(range);
		} else {
			result = matchesDateBounds(range);
		}

		return result;
	}

	private boolean matchesDateBounds(DateRangeParam range) {
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

	private boolean matchesOrdinalDateBounds(DateRangeParam range) {
		boolean result = true;
		Integer lowerBoundAsDateInteger = range.getLowerBoundAsDateInteger();
		Integer upperBoundAsDateInteger = range.getUpperBoundAsDateInteger();
		if (upperBoundAsDateInteger == null && lowerBoundAsDateInteger == null) {
			return false;
		}
		if (lowerBoundAsDateInteger != null) {
			//TODO as we run into equality issues
			result &= (myValueLowDateOrdinal.equals(lowerBoundAsDateInteger) || myValueLowDateOrdinal > lowerBoundAsDateInteger);
			result &= (myValueHighDateOrdinal.equals(lowerBoundAsDateInteger) || myValueHighDateOrdinal > lowerBoundAsDateInteger);
		}
		if (upperBoundAsDateInteger != null) {
			result &= (myValueHighDateOrdinal.equals(upperBoundAsDateInteger) || myValueHighDateOrdinal < upperBoundAsDateInteger);
			result &= (myValueLowDateOrdinal.equals(upperBoundAsDateInteger) || myValueLowDateOrdinal < upperBoundAsDateInteger);
		}
		return result;
	}


	public static Long calculateOrdinalValue(Date theDate) {
		if (theDate == null) {
			return null;
		}
		return (long) DateUtils.convertDatetoDayInteger(theDate);
	}

}
