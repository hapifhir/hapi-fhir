/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.listener.IndexStorageOptimizationListener;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import static ca.uhn.fhir.jpa.model.util.SearchParamHash.hashSearchParam;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

@EntityListeners(IndexStorageOptimizationListener.class)
@Entity
@Table(
		name = ResourceIndexedSearchParamToken.HFJ_SPIDX_TOKEN,
		indexes = {
			/*
			 * Note: We previously had indexes with the following names,
			 * do not reuse these names:
			 * IDX_SP_TOKEN
			 * IDX_SP_TOKEN_UNQUAL
			 */

			@Index(name = "IDX_SP_TOKEN_HASH_V2", columnList = "HASH_IDENTITY,SP_SYSTEM,SP_VALUE,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_S_V2", columnList = "HASH_SYS,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_SV_V2", columnList = "HASH_SYS_AND_VALUE,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_HASH_V_V2", columnList = "HASH_VALUE,RES_ID,PARTITION_ID"),
			@Index(
					name = "IDX_SP_TOKEN_RESID_V2",
					columnList = "RES_ID,HASH_SYS_AND_VALUE,HASH_VALUE,HASH_SYS,HASH_IDENTITY,PARTITION_ID")
		})
@IdClass(IdAndPartitionId.class)
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 200;

	private static final long serialVersionUID = 1L;
	public static final String HFJ_SPIDX_TOKEN = "HFJ_SPIDX_TOKEN";

	@FullTextField
	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	public String mySystem;

	@FullTextField
	@Column(name = "SP_VALUE", nullable = true, length = MAX_LENGTH)
	private String myValue;

	@SuppressWarnings("unused")
	@Id
	@GenericGenerator(name = "SEQ_SPIDX_TOKEN", type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_TOKEN")
	@Column(name = "SP_ID")
	private Long myId;

	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_SYS", nullable = true)
	private Long myHashSystem;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_SYS_AND_VALUE", nullable = true)
	private Long myHashSystemAndValue;
	/**
	 * @since 3.4.0 - At some point this should be made not-null
	 */
	@Column(name = "HASH_VALUE", nullable = true)
	private Long myHashValue;

	@ManyToOne(
			optional = false,
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						referencedColumnName = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_SP_TOKEN_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", nullable = false)
	private Long myResourceId;

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(
			PartitionSettings thePartitionSettings,
			String theResourceType,
			String theParamName,
			String theSystem,
			String theValue) {
		super();
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		calculateHashes();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedSearchParamToken(
			PartitionSettings thePartitionSettings, String theResourceType, String theParamName, boolean theMissing) {
		super();
		setPartitionSettings(thePartitionSettings);
		setResourceType(theResourceType);
		setParamName(theParamName);
		setMissing(theMissing);
		calculateHashes();
	}

	@Override
	public <T extends BaseResourceIndex> void copyMutableValuesFrom(T theSource) {
		super.copyMutableValuesFrom(theSource);
		ResourceIndexedSearchParamToken source = (ResourceIndexedSearchParamToken) theSource;

		mySystem = source.mySystem;
		myValue = source.myValue;
		myHashSystem = source.myHashSystem;
		myHashSystemAndValue = source.getHashSystemAndValue();
		myHashValue = source.myHashValue;
		myHashIdentity = source.myHashIdentity;
	}

	@Override
	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	@Override
	public void clearHashes() {
		myHashIdentity = null;
		myHashSystem = null;
		myHashSystemAndValue = null;
		myHashValue = null;
	}

	@Override
	public void calculateHashes() {
		if (myHashIdentity != null || myHashSystem != null || myHashValue != null || myHashSystemAndValue != null) {
			return;
		}

		String resourceType = getResourceType();
		String paramName = getParamName();
		String system = getSystem();
		String value = getValue();
		setHashIdentity(calculateHashIdentity(getPartitionSettings(), getPartitionId(), resourceType, paramName));
		setHashSystemAndValue(calculateHashSystemAndValue(
				getPartitionSettings(), getPartitionId(), resourceType, paramName, system, value));

		// Searches using the :of-type modifier can never be partial (system-only or value-only) so don't
		// bother saving these
		boolean calculatePartialHashes = !StringUtils.endsWith(paramName, Constants.PARAMQUALIFIER_TOKEN_OF_TYPE);
		if (calculatePartialHashes) {
			setHashSystem(
					calculateHashSystem(getPartitionSettings(), getPartitionId(), resourceType, paramName, system));
			setHashValue(calculateHashValue(getPartitionSettings(), getPartitionId(), resourceType, paramName, value));
		}
	}

	@Override
	public boolean equals(Object theObj) {
		if (this == theObj) {
			return true;
		}
		if (theObj == null) {
			return false;
		}
		if (!(theObj instanceof ResourceIndexedSearchParamToken)) {
			return false;
		}
		ResourceIndexedSearchParamToken obj = (ResourceIndexedSearchParamToken) theObj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getHashIdentity(), obj.getHashIdentity());
		b.append(getHashSystem(), obj.getHashSystem());
		b.append(getHashValue(), obj.getHashValue());
		b.append(getHashSystemAndValue(), obj.getHashSystemAndValue());
		b.append(isMissing(), obj.isMissing());
		return b.isEquals();
	}

	public Long getHashSystem() {
		return myHashSystem;
	}

	private void setHashSystem(Long theHashSystem) {
		myHashSystem = theHashSystem;
	}

	public Long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	private void setHashSystemAndValue(Long theHashSystemAndValue) {
		myHashSystemAndValue = theHashSystemAndValue;
	}

	public Long getHashValue() {
		return myHashValue;
	}

	private void setHashValue(Long theHashValue) {
		myHashValue = theHashValue;
	}

	@Override
	public Long getId() {
		return myId;
	}

	@Override
	public void setId(Long theId) {
		myId = theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = StringUtils.defaultIfBlank(theSystem, null);
		myHashSystemAndValue = null;
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamToken setValue(String theValue) {
		myValue = StringUtils.defaultIfBlank(theValue, null);
		myHashSystemAndValue = null;
		return this;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getHashIdentity());
		b.append(getHashValue());
		b.append(getHashSystem());
		b.append(getHashSystemAndValue());
		b.append(isMissing());
		return b.toHashCode();
	}

	@Override
	public IQueryParameterType toQueryParameterType() {
		return new TokenParam(getSystem(), getValue());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("id", getId());
		if (getPartitionId() != null) {
			b.append("partitionId", getPartitionId().getPartitionId());
		}
		b.append("resourceType", getResourceType());
		b.append("paramName", getParamName());
		if (isMissing()) {
			b.append("missing", true);
		} else {
			b.append("system", getSystem());
			b.append("value", getValue());
		}
		b.append("hashIdentity", myHashIdentity);
		b.append("hashSystem", myHashSystem);
		b.append("hashValue", myHashValue);
		b.append("hashSysAndValue", myHashSystemAndValue);
		b.append("partition", getPartitionId());
		return b.build();
	}

	@Override
	public boolean matches(IQueryParameterType theParam) {
		if (!(theParam instanceof TokenParam)) {
			return false;
		}
		TokenParam token = (TokenParam) theParam;
		boolean retVal = false;
		String valueString = defaultString(getValue());
		String tokenValueString = defaultString(token.getValue());

		// Only match on system if it wasn't specified
		if (token.getSystem() == null || token.getSystem().isEmpty()) {
			if (valueString.equals(tokenValueString)) {
				retVal = true;
			}
		} else if (tokenValueString == null || tokenValueString.isEmpty()) {
			if (token.getSystem().equals(getSystem())) {
				retVal = true;
			}
		} else {
			if (token.getSystem().equals(getSystem()) && valueString.equals(tokenValueString)) {
				retVal = true;
			}
		}
		return retVal;
	}

	public static long calculateHashSystem(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theSystem) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashSystem(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theSystem);
	}

	public static long calculateHashSystem(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theSystem) {
		return hashSearchParam(
				thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, trim(theSystem));
	}

	public static long calculateHashSystemAndValue(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theSystem,
			String theValue) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashSystemAndValue(
				thePartitionSettings, requestPartitionId, theResourceType, theParamName, theSystem, theValue);
	}

	public static long calculateHashSystemAndValue(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theSystem,
			String theValue) {
		return hashSearchParam(
				thePartitionSettings,
				theRequestPartitionId,
				theResourceType,
				theParamName,
				defaultString(trim(theSystem)),
				trim(theValue));
	}

	public static long calculateHashValue(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theValue) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashValue(thePartitionSettings, requestPartitionId, theResourceType, theParamName, theValue);
	}

	public static long calculateHashValue(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			String theValue) {
		String value = trim(theValue);
		return hashSearchParam(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, value);
	}

	@Override
	public ResourceTable getResource() {
		return myResource;
	}

	@Override
	public BaseResourceIndexedSearchParam setResource(ResourceTable theResource) {
		setResourceType(theResource.getResourceType());
		return this;
	}

	/**
	 * We truncate the fields at the last moment because the tables have limited size.
	 * We don't truncate earlier in the flow because the index hashes MUST be calculated on the full string.
	 */
	@PrePersist
	@PreUpdate
	public void truncateFieldsForDB() {
		mySystem = StringUtils.truncate(mySystem, MAX_LENGTH);
		myValue = StringUtils.truncate(myValue, MAX_LENGTH);
	}
}
