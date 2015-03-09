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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//@formatter:off
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
	public Date myValueHigh;

	@Column(name = "SP_VALUE_LOW", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValueLow;

	
	
	public ResourceIndexedSearchParamDate() {
	}
	
	public ResourceIndexedSearchParamDate(String theName, Date theLow, Date theHigh) {
		setParamName(theName);
		setValueLow(theLow);
		setValueHigh(theHigh);
	}

	

	public Date getValueHigh() {
		return myValueHigh;
	}

	public Date getValueLow() {
		return myValueLow;
	}

	public void setValueHigh(Date theValueHigh) {
		myValueHigh = theValueHigh;
	}

	public void setValueLow(Date theValueLow) {
		myValueLow = theValueLow;
	}

	

}
