package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

//@formatter:off
@Entity
@Table(name = "HFJ_SEARCH_RESULT", uniqueConstraints= {
	@UniqueConstraint(name="IDX_SEARCHRES_ORDER", columnNames= {"SEARCH_PID", "SEARCH_ORDER"})
})
//@formatter:on
public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO, generator="SEQ_SEARCH_RES")
	@SequenceGenerator(name="SEQ_SEARCH_RES", sequenceName="SEQ_SEARCH_RES")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name="SEARCH_ORDER", nullable=false)
	private int myOrder;
	
	@ManyToOne
	@JoinColumn(name="RESOURCE_PID", referencedColumnName="RES_ID", foreignKey=@ForeignKey(name="FK_SEARCHRES_RES"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myResource;

	@Column(name="RESOURCE_PID", insertable=true, updatable=false, nullable=false)
	private Long myResourcePid;

	@ManyToOne
	@JoinColumn(name="SEARCH_PID", referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_SEARCHRES_SEARCH"))
	private Search mySearch;

	@Column(name="SEARCH_PID", insertable=false, updatable=false, nullable=false)
	private Long mySearchPid;

	/**
	 * Constructor
	 */
	public SearchResult() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public SearchResult(Search theSearch) {
		mySearch = theSearch;
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof SearchResult)) {
			return false;
		}
		return myResourcePid.equals(((SearchResult)theObj).myResourcePid);
	}

	public int getOrder() {
		return myOrder;
	}
	
	public Long getResourcePid() {
		return myResourcePid;
	}
	
	@Override
	public int hashCode() {
		return myResourcePid.hashCode();
	}

	public void setOrder(int theOrder) {
		myOrder = theOrder;
	}

	public SearchResult setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}
}
