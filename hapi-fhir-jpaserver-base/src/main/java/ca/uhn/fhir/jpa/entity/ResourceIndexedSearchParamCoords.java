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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

//@formatter:off
@Embeddable
@Entity
@Table(name = "HFJ_SPIDX_COORDS", indexes = { 
	@Index(name = "IDX_SP_COORDS", columnList = "RES_TYPE,SP_NAME,SP_LATITUDE,SP_LONGITUDE"), 
	@Index(name = "IDX_SP_COORDS_UPDATED", columnList = "SP_UPDATED"), 
	@Index(name = "IDX_SP_COORDS_RESID", columnList = "RES_ID") 
})
//@formatter:on
public class ResourceIndexedSearchParamCoords extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 100;

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_COORDS", sequenceName = "SEQ_SPIDX_COORDS")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_COORDS")
	@Column(name = "SP_ID")
	private Long myId;

	@Column(name = "SP_LATITUDE")
	@Field
	public double myLatitude;

	@Column(name = "SP_LONGITUDE")
	@Field
	public double myLongitude;

	public ResourceIndexedSearchParamCoords() {
	}

	public ResourceIndexedSearchParamCoords(String theName, double theLatitude, double theLongitude) {
		setParamName(theName);
		setLatitude(theLatitude);
		setLongitude(theLongitude);
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamCoords)) {
			return false;
		}
		ResourceIndexedSearchParamCoords obj = (ResourceIndexedSearchParamCoords) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getParamName(), obj.getParamName());
		b.append(getResource(), obj.getResource());
		b.append(getLatitude(), obj.getLatitude());
		b.append(getLongitude(), obj.getLongitude());
		return b.isEquals();
	}

	@Override
	protected Long getId() {
		return myId;
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return null;
	}

	public double getLatitude() {
		return myLatitude;
	}

	public double getLongitude() {
		return myLongitude;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getParamName());
		b.append(getResource());
		b.append(getLatitude());
		b.append(getLongitude());
		return b.toHashCode();
	}

	public void setLatitude(double theLatitude) {
		myLatitude = theLatitude;
	}

	public void setLongitude(double theLongitude) {
		myLongitude = theLongitude;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("paramName", getParamName());
		b.append("resourceId", getResourcePid());
		b.append("lat", getLatitude());
		b.append("lon", getLongitude());
		return b.build();
	}
}
