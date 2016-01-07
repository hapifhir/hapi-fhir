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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

//@formatter:off
@Entity
@Table(name = "HFJ_SEARCH", uniqueConstraints= {
	@UniqueConstraint(name="IDX_SEARCH_UUID", columnNames="SEARCH_UUID")
}, indexes= {
	@Index(name="JDX_SEARCH_CREATED", columnList="CREATED")
})
//@formatter:on
public class Search implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED", nullable=false)
	private Date myCreated;

	@GeneratedValue(strategy = GenerationType.AUTO, generator="SEQ_SEARCH")
	@SequenceGenerator(name="SEQ_SEARCH", sequenceName="SEQ_SEARCH")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name="TOTAL_COUNT")
	private int myTotalCount;
	
	@Column(name="SEARCH_UUID", length=40, nullable=false)
	private String myUuid;

	public Date getCreated() {
		return myCreated;
	}

	public int getTotalCount() {
		return myTotalCount;
	}

	public String getUuid() {
		return myUuid;
	}

	public void setCreated(Date theCreated) {
		myCreated = theCreated;
	}

	public void setTotalCount(int theTotalCount) {
		myTotalCount = theTotalCount;
	}
	
	public void setUuid(String theUuid) {
		myUuid = theUuid;
	}
	
}
