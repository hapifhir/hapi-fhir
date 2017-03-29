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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;

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
	@Column(name="CREATED", nullable=false, updatable=false)
	private Date myCreated;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="SEQ_SEARCH")
	@SequenceGenerator(name="SEQ_SEARCH", sequenceName="SEQ_SEARCH")
	@Column(name = "PID")
	private Long myId;

	@OneToMany(mappedBy="mySearch")
	private Collection<SearchInclude> myIncludes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATED_HIGH", nullable=true, insertable=true, updatable=false)
	private Date myLastUpdatedHigh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATED_LOW", nullable=true, insertable=true, updatable=false)
	private Date myLastUpdatedLow;

	@Column(name="PREFERRED_PAGE_SIZE", nullable=true)
	private Integer myPreferredPageSize;
	
	@Column(name="RESOURCE_ID", nullable=true)
	private Long myResourceId;
	
	@Column(name="RESOURCE_TYPE", length=200, nullable=true)
	private String myResourceType;

	@OneToMany(mappedBy="mySearch")
	private Collection<SearchResult> myResults;

	@Lob
	@Column(name="SEARCH_PARAM_MAP", nullable=true)
	private byte[] mySearchParamMap;

	@Enumerated(EnumType.ORDINAL)
	@Column(name="SEARCH_TYPE", nullable=false)
	private SearchTypeEnum mySearchType;

	@Column(name="TOTAL_COUNT", nullable=false)
	private Integer myTotalCount;

	@Column(name="SEARCH_UUID", length=40, nullable=false, updatable=false)
	private String myUuid;

	public Date getCreated() {
		return myCreated;
	}

	public Long getId() {
		return myId;
	}

	public Collection<SearchInclude> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new ArrayList<SearchInclude>();
		}
		return myIncludes;
	}

	public DateRangeParam getLastUpdated() {
		if (myLastUpdatedLow == null && myLastUpdatedHigh == null) {
			return null;
		} else {
			return new DateRangeParam(myLastUpdatedLow, myLastUpdatedHigh);
		}
	}

	public Date getLastUpdatedHigh() {
		return myLastUpdatedHigh;
	}
	
	public Date getLastUpdatedLow() {
		return myLastUpdatedLow;
	}

	public Integer getPreferredPageSize() {
		return myPreferredPageSize;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public byte[] getSearchParamMap() {
		return mySearchParamMap;
	}
	
	public SearchTypeEnum getSearchType() {
		return mySearchType;
	}

	public Integer getTotalCount() {
		return myTotalCount;
	}


	public String getUuid() {
		return myUuid;
	}

	public void setCreated(Date theCreated) {
		myCreated = theCreated;
	}

	public void setLastUpdated(Date theLowerBound, Date theUpperBound) {
		myLastUpdatedLow = theLowerBound;
		myLastUpdatedHigh = theUpperBound;
	}
	public void setLastUpdated(DateRangeParam theLastUpdated) {
		if (theLastUpdated == null) {
			myLastUpdatedLow = null;
			myLastUpdatedHigh = null;
		} else {
			myLastUpdatedLow = theLastUpdated.getLowerBoundAsInstant();
			myLastUpdatedHigh = theLastUpdated.getUpperBoundAsInstant();
		}
	}
	
	public void setPreferredPageSize(Integer thePreferredPageSize) {
		myPreferredPageSize = thePreferredPageSize;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}
	
	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}


	public void setSearchParamMap(byte[] theSearchParamMap) {
		mySearchParamMap = theSearchParamMap;
	}

	public void setSearchType(SearchTypeEnum theSearchType) {
		mySearchType = theSearchType;
	}

	public void setTotalCount(Integer theTotalCount) {
		myTotalCount = theTotalCount;
	}

	public void setUuid(String theUuid) {
		myUuid = theUuid;
	}

	private Set<Include> toIncList(boolean theWantReverse) {
		HashSet<Include> retVal = new HashSet<Include>();
		for (SearchInclude next : getIncludes()) {
			if (theWantReverse == next.isReverse()) {
				retVal.add(new Include(next.getInclude(), next.isRecurse()));
			}
		}
		return Collections.unmodifiableSet(retVal);
	}

	public Set<Include> toIncludesList() {
		return toIncList(false);
	}

	public Set<Include> toRevIncludesList() {
		return toIncList(true);
	}
	
}
