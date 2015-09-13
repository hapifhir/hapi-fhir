package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@formatter:off
@Entity
@Table(name = "HFJ_SPIDX_COORDS" /* , indexes = { @Index(name = "IDX_SP_TOKEN", columnList = "SP_SYSTEM,SP_VALUE") } */)
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_COORDS", indexes = { 
	@org.hibernate.annotations.Index(name = "IDX_SP_COORDS", columnNames = { "RES_TYPE", "SP_NAME", "SP_LATITUDE" }) 
})
//@formatter:on
public class ResourceIndexedSearchParamCoords extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 100;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_LATITUDE")
	public double myLatitude;

	@Column(name = "SP_LONGITUDE")
	public double myLongitude;

	public ResourceIndexedSearchParamCoords() {
	}

	public ResourceIndexedSearchParamCoords(String theName, double theLatitude, double theLongitude) {
		setParamName(theName);
		setLatitude(theLatitude);
		setLongitude(theLongitude);
	}

	public double getLatitude() {
		return myLatitude;
	}

	public void setLatitude(double theLatitude) {
		myLatitude = theLatitude;
	}

	public double getLongitude() {
		return myLongitude;
	}

	public void setLongitude(double theLongitude) {
		myLongitude = theLongitude;
	}

}
