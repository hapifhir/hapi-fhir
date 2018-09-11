package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.left;

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

@Entity
@Table(name = "HFJ_SEARCH", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_SEARCH_UUID", columnNames = "SEARCH_UUID")
}, indexes = {
	@Index(name = "IDX_SEARCH_LASTRETURNED", columnList = "SEARCH_LAST_RETURNED"),
	@Index(name = "IDX_SEARCH_RESTYPE_HASHS", columnList = "RESOURCE_TYPE,SEARCH_QUERY_STRING_HASH,CREATED")
})
public class Search implements Serializable {

	private static final int MAX_SEARCH_QUERY_STRING = 10000;
	@SuppressWarnings("WeakerAccess")
	public static final int UUID_COLUMN_LENGTH = 36;
	private static final int FAILURE_MESSAGE_LENGTH = 500;
	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false, updatable = false)
	private Date myCreated;

	@Column(name = "FAILURE_CODE", nullable = true)
	private Integer myFailureCode;

	@Column(name = "FAILURE_MESSAGE", length = FAILURE_MESSAGE_LENGTH, nullable = true)
	private String myFailureMessage;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SEARCH")
	@SequenceGenerator(name = "SEQ_SEARCH", sequenceName = "SEQ_SEARCH")
	@Column(name = "PID")
	private Long myId;

	@OneToMany(mappedBy = "mySearch")
	private Collection<SearchInclude> myIncludes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_HIGH", nullable = true, insertable = true, updatable = false)
	private Date myLastUpdatedHigh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_UPDATED_LOW", nullable = true, insertable = true, updatable = false)
	private Date myLastUpdatedLow;

	@Column(name = "NUM_FOUND", nullable = false)
	private int myNumFound;

	@Column(name = "PREFERRED_PAGE_SIZE", nullable = true)
	private Integer myPreferredPageSize;

	@Column(name = "RESOURCE_ID", nullable = true)
	private Long myResourceId;

	@Column(name = "RESOURCE_TYPE", length = 200, nullable = true)
	private String myResourceType;

	@OneToMany(mappedBy = "mySearch")
	private Collection<SearchResult> myResults;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEARCH_LAST_RETURNED", nullable = false, updatable = false)
	private Date mySearchLastReturned;

	@Lob()
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "SEARCH_QUERY_STRING", nullable = true, updatable = false, length = MAX_SEARCH_QUERY_STRING)
	private String mySearchQueryString;

	@Column(name = "SEARCH_QUERY_STRING_HASH", nullable = true, updatable = false)
	private Integer mySearchQueryStringHash;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SEARCH_TYPE", nullable = false)
	private SearchTypeEnum mySearchType;

	@Enumerated(EnumType.STRING)
	@Column(name = "SEARCH_STATUS", nullable = false, length = 10)
	private SearchStatusEnum myStatus;

	@Column(name = "TOTAL_COUNT", nullable = true)
	private Integer myTotalCount;

	@Column(name = "SEARCH_UUID", length = UUID_COLUMN_LENGTH, nullable = false, updatable = false)
	private String myUuid;

	/**
	 * Constructor
	 */
	public Search() {
		super();
	}

	public Date getCreated() {
		return myCreated;
	}

	public void setCreated(Date theCreated) {
		myCreated = theCreated;
	}

	public Integer getFailureCode() {
		return myFailureCode;
	}

	public void setFailureCode(Integer theFailureCode) {
		myFailureCode = theFailureCode;
	}

	public String getFailureMessage() {
		return myFailureMessage;
	}

	public void setFailureMessage(String theFailureMessage) {
		myFailureMessage = left(theFailureMessage, FAILURE_MESSAGE_LENGTH);
	}

	public Long getId() {
		return myId;
	}

	public Collection<SearchInclude> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new ArrayList<>();
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

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		if (theLastUpdated == null) {
			myLastUpdatedLow = null;
			myLastUpdatedHigh = null;
		} else {
			myLastUpdatedLow = theLastUpdated.getLowerBoundAsInstant();
			myLastUpdatedHigh = theLastUpdated.getUpperBoundAsInstant();
		}
	}

	public Date getLastUpdatedHigh() {
		return myLastUpdatedHigh;
	}

	public Date getLastUpdatedLow() {
		return myLastUpdatedLow;
	}

	public int getNumFound() {
		return myNumFound;
	}

	public void setNumFound(int theNumFound) {
		myNumFound = theNumFound;
	}

	public Integer getPreferredPageSize() {
		return myPreferredPageSize;
	}

	public void setPreferredPageSize(Integer thePreferredPageSize) {
		myPreferredPageSize = thePreferredPageSize;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Date getSearchLastReturned() {
		return mySearchLastReturned;
	}

	public void setSearchLastReturned(Date theDate) {
		mySearchLastReturned = theDate;
	}

	public String getSearchQueryString() {
		return mySearchQueryString;
	}

	public void setSearchQueryString(String theSearchQueryString) {
		if (theSearchQueryString != null && theSearchQueryString.length() > MAX_SEARCH_QUERY_STRING) {
			mySearchQueryString = null;
		} else {
			mySearchQueryString = theSearchQueryString;
		}
	}

	public SearchTypeEnum getSearchType() {
		return mySearchType;
	}

	public void setSearchType(SearchTypeEnum theSearchType) {
		mySearchType = theSearchType;
	}

	public SearchStatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(SearchStatusEnum theStatus) {
		myStatus = theStatus;
	}

	public Integer getTotalCount() {
		return myTotalCount;
	}

	public void setTotalCount(Integer theTotalCount) {
		myTotalCount = theTotalCount;
	}

	public String getUuid() {
		return myUuid;
	}

	public void setUuid(String theUuid) {
		myUuid = theUuid;
	}

	public void setLastUpdated(Date theLowerBound, Date theUpperBound) {
		myLastUpdatedLow = theLowerBound;
		myLastUpdatedHigh = theUpperBound;
	}

	public void setSearchQueryStringHash(Integer theSearchQueryStringHash) {
		mySearchQueryStringHash = theSearchQueryStringHash;
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

	public void addInclude(SearchInclude theInclude) {
		getIncludes().add(theInclude);
	}
}
