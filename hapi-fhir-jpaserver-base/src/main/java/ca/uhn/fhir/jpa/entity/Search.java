package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.OptimisticLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.left;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
@Table(name = Search.HFJ_SEARCH, uniqueConstraints = {
	@UniqueConstraint(name = "IDX_SEARCH_UUID", columnNames = "SEARCH_UUID")
}, indexes = {
	@Index(name = "IDX_SEARCH_RESTYPE_HASHS", columnList = "RESOURCE_TYPE,SEARCH_QUERY_STRING_HASH,CREATED"),
	@Index(name = "IDX_SEARCH_CREATED", columnList = "CREATED")
})
public class Search implements ICachedSearchDetails, Serializable {

	@SuppressWarnings("WeakerAccess")
	public static final int UUID_COLUMN_LENGTH = 36;
	public static final String HFJ_SEARCH = "HFJ_SEARCH";
	private static final int MAX_SEARCH_QUERY_STRING = 10000;
	private static final int FAILURE_MESSAGE_LENGTH = 500;
	private static final long serialVersionUID = 1L;
	private static final Logger ourLog = LoggerFactory.getLogger(Search.class);
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false, updatable = false)
	private Date myCreated;
	@OptimisticLock(excluded = true)
	@Column(name = "SEARCH_DELETED", nullable = true)
	private Boolean myDeleted;
	@Column(name = "FAILURE_CODE", nullable = true)
	private Integer myFailureCode;
	@Column(name = "FAILURE_MESSAGE", length = FAILURE_MESSAGE_LENGTH, nullable = true)
	private String myFailureMessage;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRY_OR_NULL", nullable = true)
	private Date myExpiryOrNull;
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
	@Column(name = "NUM_BLOCKED", nullable = true)
	private Integer myNumBlocked;
	@Column(name = "PREFERRED_PAGE_SIZE", nullable = true)
	private Integer myPreferredPageSize;
	@Column(name = "RESOURCE_ID", nullable = true)
	private Long myResourceId;
	@Column(name = "RESOURCE_TYPE", length = 200, nullable = true)
	private String myResourceType;
	/**
	 * Note that this field may have the request partition IDs prepended to it
	 */
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
	@SuppressWarnings("unused")
	@Version
	@Column(name = "OPTLOCK_VERSION", nullable = true)
	private Integer myVersion;
	@Lob
	@Column(name = "SEARCH_PARAM_MAP", nullable = true)
	private byte[] mySearchParameterMap;

	/**
	 * This isn't currently persisted in the DB as it's only used for offset mode. We could
	 * change this if needed in the future.
	 */
	@Transient
	private Integer myOffset;
	/**
	 * This isn't currently persisted in the DB as it's only used for offset mode. We could
	 * change this if needed in the future.
	 */
	@Transient
	private Integer mySizeModeSize;

	/**
	 * Constructor
	 */
	public Search() {
		super();
	}

	public Integer getSizeModeSize() {
		return mySizeModeSize;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myLastUpdatedHigh", myLastUpdatedHigh)
			.append("myLastUpdatedLow", myLastUpdatedLow)
			.append("myNumFound", myNumFound)
			.append("myNumBlocked", myNumBlocked)
			.append("myStatus", myStatus)
			.append("myTotalCount", myTotalCount)
			.append("myUuid", myUuid)
			.append("myVersion", myVersion)
			.toString();
	}

	public int getNumBlocked() {
		return myNumBlocked != null ? myNumBlocked : 0;
	}

	public void setNumBlocked(int theNumBlocked) {
		myNumBlocked = theNumBlocked;
	}

	public Date getExpiryOrNull() {
		return myExpiryOrNull;
	}

	public void setExpiryOrNull(Date theExpiryOrNull) {
		myExpiryOrNull = theExpiryOrNull;
	}

	public Boolean getDeleted() {
		return myDeleted;
	}

	public void setDeleted(Boolean theDeleted) {
		myDeleted = theDeleted;
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
		if (System.getProperty(SearchCoordinatorSvcImpl.UNIT_TEST_CAPTURE_STACK) != null) {
			myFailureMessage = theFailureMessage;
		}
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
		ourLog.trace("getNumFound {}", myNumFound);
		return myNumFound;
	}

	public void setNumFound(int theNumFound) {
		ourLog.trace("setNumFound {}", theNumFound);
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

	/**
	 * Note that this field may have the request partition IDs prepended to it
	 */
	public String getSearchQueryString() {
		return mySearchQueryString;
	}

	public void setSearchQueryString(String theSearchQueryString, RequestPartitionId theRequestPartitionId) {
		String searchQueryString = null;
		if (theSearchQueryString != null) {
			searchQueryString = createSearchQueryStringForStorage(theSearchQueryString, theRequestPartitionId);
		}
		if (searchQueryString == null || searchQueryString.length() > MAX_SEARCH_QUERY_STRING) {
			// We want this field to always have a wide distribution of values in order
			// to avoid optimizers avoiding using it if it has lots of nulls, so in the
			// case of null, just put a value that will never be hit
			mySearchQueryString = UUID.randomUUID().toString();
		} else {
			mySearchQueryString = searchQueryString;
		}

		mySearchQueryStringHash = mySearchQueryString.hashCode();
	}

	public SearchTypeEnum getSearchType() {
		return mySearchType;
	}

	public void setSearchType(SearchTypeEnum theSearchType) {
		mySearchType = theSearchType;
	}

	public SearchStatusEnum getStatus() {
		ourLog.trace("getStatus {}", myStatus);
		return myStatus;
	}

	public void setStatus(SearchStatusEnum theStatus) {
		ourLog.trace("setStatus {}", theStatus);
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

	private Set<Include> toIncList(boolean theWantReverse) {
		HashSet<Include> retVal = new HashSet<>();
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

	public Integer getVersion() {
		return myVersion;
	}

	public Optional<SearchParameterMap> getSearchParameterMap() {
		return Optional.ofNullable(mySearchParameterMap).map(t -> SerializationUtils.deserialize(mySearchParameterMap));
	}

	public void setSearchParameterMap(SearchParameterMap theSearchParameterMap) {
		mySearchParameterMap = SerializationUtils.serialize(theSearchParameterMap);
	}

	@Override
	public void setCannotBeReused() {
		mySearchQueryStringHash = null;
	}

	public Integer getOffset() {
		return myOffset;
	}

	public void setOffset(Integer theOffset) {
		myOffset = theOffset;
	}

	@Nonnull
	public static String createSearchQueryStringForStorage(@Nonnull String theSearchQueryString, @Nonnull RequestPartitionId theRequestPartitionId) {
		String searchQueryString = theSearchQueryString;
		if (!theRequestPartitionId.isAllPartitions()) {
			searchQueryString = RequestPartitionId.stringifyForKey(theRequestPartitionId) + " " + searchQueryString;
		}
		return searchQueryString;
	}
}
