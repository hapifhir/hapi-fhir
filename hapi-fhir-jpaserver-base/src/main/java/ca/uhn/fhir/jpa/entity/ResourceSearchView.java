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

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.Constants;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("SqlDialectInspection")
@Entity
@Immutable
@Subselect("SELECT h.pid               as pid,            "
		+ "               r.res_id            as res_id,         "
		+ "               h.res_type          as res_type,       "
		+ "               h.res_version       as res_version,    "
		// FHIR version
		+ "               h.res_ver           as res_ver,        "
		// resource version
		+ "               h.has_tags          as has_tags,       "
		+ "               h.res_deleted_at    as res_deleted_at, "
		+ "               h.res_published     as res_published,  "
		+ "               h.res_updated       as res_updated,    "
		+ "               h.res_text          as res_text,       "
		+ "               h.res_text_vc       as res_text_vc,    "
		+ "               h.res_encoding      as res_encoding,   "
		+ "               h.PARTITION_ID      as PARTITION_ID,   "
		+ "               p.SOURCE_URI        as PROV_SOURCE_URI,"
		+ "               p.REQUEST_ID        as PROV_REQUEST_ID,"
		+ "               r.fhir_id         as FHIR_ID      "
		+ "FROM HFJ_RESOURCE r "
		+ "    INNER JOIN HFJ_RES_VER h ON r.res_id = h.res_id and r.res_ver = h.res_ver"
		+ "    LEFT OUTER JOIN HFJ_RES_VER_PROV p ON p.res_ver_pid = h.pid ")
public class ResourceSearchView implements IBaseResourceEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "RES_ID")
	private Long myResourceId;

	@Column(name = "RES_TYPE", length = Constants.MAX_RESOURCE_NAME_LENGTH)
	private String myResourceType;

	@Column(name = "RES_VERSION")
	@Enumerated(EnumType.STRING)
	private FhirVersionEnum myFhirVersion;

	@Column(name = "RES_VER")
	private Long myResourceVersion;

	@Column(name = "PROV_REQUEST_ID", length = Constants.REQUEST_ID_LENGTH)
	private String myProvenanceRequestId;

	@Column(name = "PROV_SOURCE_URI", length = ResourceHistoryTable.SOURCE_URI_LENGTH)
	private String myProvenanceSourceUri;

	@Column(name = "HAS_TAGS")
	private boolean myHasTags;

	@Column(name = "RES_DELETED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date myDeleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_PUBLISHED")
	private Date myPublished;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_UPDATED")
	private Date myUpdated;

	@Column(name = "RES_TEXT")
	@Lob()
	private byte[] myResource;

	@Column(name = "RES_TEXT_VC")
	private String myResourceTextVc;

	@Column(name = "RES_ENCODING")
	@Enumerated(EnumType.STRING)
	private ResourceEncodingEnum myEncoding;

	@Column(name = "FHIR_ID", length = ResourceTable.MAX_FORCED_ID_LENGTH)
	private String myFhirId;

	@Column(name = "PARTITION_ID")
	private Integer myPartitionId;

	public ResourceSearchView() {
		// public constructor for Hibernate
	}

	public String getResourceTextVc() {
		return myResourceTextVc;
	}

	public String getProvenanceRequestId() {
		return myProvenanceRequestId;
	}

	public String getProvenanceSourceUri() {
		return myProvenanceSourceUri;
	}

	@Override
	public Date getDeleted() {
		return myDeleted;
	}

	public void setDeleted(Date theDate) {
		myDeleted = theDate;
	}

	@Override
	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public void setFhirVersion(FhirVersionEnum theFhirVersion) {
		myFhirVersion = theFhirVersion;
	}

	public String getFhirId() {
		return myFhirId;
	}

	@Override
	public Long getId() {
		return myResourceId;
	}

	@Override
	public IdDt getIdDt() {
		if (myFhirId == null) {
			Long id = myResourceId;
			return new IdDt(myResourceType + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		} else {
			return new IdDt(getResourceType() + '/' + getFhirId() + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
		}
	}

	@Override
	public InstantDt getPublished() {
		if (myPublished != null) {
			return new InstantDt(myPublished);
		} else {
			return null;
		}
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	@Override
	public Long getResourceId() {
		return myResourceId;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	@Override
	public InstantDt getUpdated() {
		return new InstantDt(myUpdated);
	}

	@Override
	public Date getUpdatedDate() {
		return myUpdated;
	}

	@Override
	public long getVersion() {
		return myResourceVersion;
	}

	@Override
	public boolean isHasTags() {
		return myHasTags;
	}

	@Override
	@Nullable
	public PartitionablePartitionId getPartitionId() {
		if (myPartitionId != null) {
			return new PartitionablePartitionId(myPartitionId, null);
		} else {
			return null;
		}
	}

	public byte[] getResource() {
		return myResource;
	}

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}
}
