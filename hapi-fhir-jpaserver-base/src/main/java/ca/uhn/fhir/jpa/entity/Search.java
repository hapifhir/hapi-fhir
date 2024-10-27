/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HistorySearchStyleEnum;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.system.HapiSystemProperties;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.Length;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Entity
@Table(
		name = Search.HFJ_SEARCH,
		uniqueConstraints = {@UniqueConstraint(name = "IDX_SEARCH_UUID", columnNames = "SEARCH_UUID")},
		indexes = {
			@Index(name = "IDX_SEARCH_RESTYPE_HASHS", columnList = "RESOURCE_TYPE,SEARCH_QUERY_STRING_HASH,CREATED"),
			@Index(name = "IDX_SEARCH_CREATED", columnList = "CREATED")
		})
public class Search implements ICachedSearchDetails, Serializable {

	/**
	 * Long enough to accommodate a full UUID (36) with an additional prefix
	 * used by megascale (12)
	 */
	@SuppressWarnings("WeakerAccess")
	public static final int SEARCH_UUID_COLUMN_LENGTH = 48;

	public static final String HFJ_SEARCH = "HFJ_SEARCH";
	private static final int MAX_SEARCH_QUERY_STRING = 10000;
	private static final int FAILURE_MESSAGE_LENGTH = 500;
	private static final long serialVersionUID = 1L;
	private static final Logger ourLog = LoggerFactory.getLogger(Search.class);
	public static final String SEARCH_UUID = "SEARCH_UUID";

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

	@OneToMany(mappedBy = "mySearch", cascade = CascadeType.ALL)
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
	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "SEARCH_QUERY_STRING", nullable = true, updatable = false, length = MAX_SEARCH_QUERY_STRING)
	private String mySearchQueryString;

	/**
	 * Note that this field may have the request partition IDs prepended to it
	 */
	@Column(name = "SEARCH_QUERY_STRING_VC", nullable = true, length = Length.LONG32)
	private String mySearchQueryStringVc;

	@Column(name = "SEARCH_QUERY_STRING_HASH", nullable = true, updatable = false)
	private Integer mySearchQueryStringHash;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "SEARCH_TYPE", nullable = false)
	@JdbcTypeCode(SqlTypes.INTEGER)
	private SearchTypeEnum mySearchType;

	@Enumerated(EnumType.STRING)
	@Column(name = "SEARCH_STATUS", nullable = false, length = 10)
	private SearchStatusEnum myStatus;

	@Column(name = "TOTAL_COUNT", nullable = true)
	private Integer myTotalCount;

	@Column(name = SEARCH_UUID, length = SEARCH_UUID_COLUMN_LENGTH, nullable = false, updatable = false)
	private String myUuid;

	@SuppressWarnings("unused")
	@Version
	@Column(name = "OPTLOCK_VERSION", nullable = true)
	private Integer myVersion;

	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Column(name = "SEARCH_PARAM_MAP", nullable = true)
	private byte[] mySearchParameterMap;

	@Column(name = "SEARCH_PARAM_MAP_BIN", nullable = true, length = Length.LONG32)
	private byte[] mySearchParameterMapBin;

	@Transient
	private transient SearchParameterMap mySearchParameterMapTransient;

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
	 * This isn't currently persisted in the DB. When there is search criteria defined in the
	 * search parameter, this is used to keep the search criteria type.
	 */
	@Transient
	private HistorySearchStyleEnum myHistorySearchStyle;

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
		if (HapiSystemProperties.isUnitTestCaptureStackEnabled()) {
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
		return mySearchQueryStringVc != null ? mySearchQueryStringVc : mySearchQueryString;
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
			mySearchQueryStringVc = UUID.randomUUID().toString();
		} else {
			mySearchQueryStringVc = searchQueryString;
		}

		mySearchQueryString = null;
		mySearchQueryStringHash = mySearchQueryStringVc.hashCode();
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

	@Override
	public String getUuid() {
		return myUuid;
	}

	@Override
	public void setUuid(String theUuid) {
		myUuid = theUuid;
	}

	public void setLastUpdated(Date theLowerBound, Date theUpperBound) {
		myLastUpdatedLow = theLowerBound;
		myLastUpdatedHigh = theUpperBound;
	}

	private Set<Include> toIncList(boolean theWantReverse, boolean theIncludeAll, boolean theWantIterate) {
		HashSet<Include> retVal = new HashSet<>();
		for (SearchInclude next : getIncludes()) {
			if (theWantReverse == next.isReverse()) {
				if (theIncludeAll) {
					retVal.add(new Include(next.getInclude(), next.isRecurse()));
				} else {
					if (theWantIterate == next.isRecurse()) {
						retVal.add(new Include(next.getInclude(), next.isRecurse()));
					}
				}
			}
		}
		return Collections.unmodifiableSet(retVal);
	}

	private Set<Include> toIncList(boolean theWantReverse) {
		return toIncList(theWantReverse, true, true);
	}

	public Set<Include> toIncludesList() {
		return toIncList(false);
	}

	public Set<Include> toRevIncludesList() {
		return toIncList(true);
	}

	public Set<Include> toIncludesList(boolean iterate) {
		return toIncList(false, false, iterate);
	}

	public Set<Include> toRevIncludesList(boolean iterate) {
		return toIncList(true, false, iterate);
	}

	public void addInclude(SearchInclude theInclude) {
		getIncludes().add(theInclude);
	}

	public Integer getVersion() {
		return myVersion;
	}

	/**
	 * Note that this is not always set! We set this if we're storing a
	 * Search in {@link SearchStatusEnum#PASSCMPLET} status since we'll need
	 * the map in order to restart, but otherwise we save space and time by
	 * not storing it.
	 */
	public Optional<SearchParameterMap> getSearchParameterMap() {
		if (mySearchParameterMapTransient != null) {
			return Optional.of(mySearchParameterMapTransient);
		}
		SearchParameterMap searchParameterMap = null;
		byte[] searchParameterMapSerialized = mySearchParameterMapBin;
		if (searchParameterMapSerialized == null) {
			searchParameterMapSerialized = mySearchParameterMap;
		}
		if (searchParameterMapSerialized != null) {
			searchParameterMap = SerializationUtils.deserialize(searchParameterMapSerialized);
			mySearchParameterMapTransient = searchParameterMap;
		}
		return Optional.ofNullable(searchParameterMap);
	}

	public void setSearchParameterMap(SearchParameterMap theSearchParameterMap) {
		mySearchParameterMapTransient = theSearchParameterMap;
		mySearchParameterMapBin = SerializationUtils.serialize(theSearchParameterMap);
		mySearchParameterMap = null;
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

	public HistorySearchStyleEnum getHistorySearchStyle() {
		return myHistorySearchStyle;
	}

	public void setHistorySearchStyle(HistorySearchStyleEnum theHistorySearchStyle) {
		this.myHistorySearchStyle = theHistorySearchStyle;
	}

	@Nonnull
	public static String createSearchQueryStringForStorage(
			@Nonnull String theSearchQueryString, @Nonnull RequestPartitionId theRequestPartitionId) {
		String searchQueryString = theSearchQueryString;
		if (!theRequestPartitionId.isAllPartitions()) {
			searchQueryString = RequestPartitionId.stringifyForKey(theRequestPartitionId) + " " + searchQueryString;
		}
		return searchQueryString;
	}
}
