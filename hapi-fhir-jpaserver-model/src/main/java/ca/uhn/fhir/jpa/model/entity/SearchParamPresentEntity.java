/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import static ca.uhn.fhir.jpa.model.util.SearchParamHash.hashSearchParam;

@Entity
@Table(
		name = "HFJ_RES_PARAM_PRESENT",
		indexes = {
			// We used to have a constraint named IDX_RESPARMPRESENT_SPID_RESID - Don't reuse
			@Index(name = "IDX_RESPARMPRESENT_RESID", columnList = "RES_ID"),
			@Index(name = "IDX_RESPARMPRESENT_HASHPRES", columnList = "HASH_PRESENCE")
		})
public class SearchParamPresentEntity extends BasePartitionable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_RESPARMPRESENT_ID", sequenceName = "SEQ_RESPARMPRESENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESPARMPRESENT_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SP_PRESENT", nullable = false)
	private boolean myPresent;

	@ManyToOne()
	@JoinColumn(
			name = "RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_RESPARMPRES_RESID"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", nullable = false, insertable = false, updatable = false)
	private Long myResourcePid;

	@Transient
	private transient String myParamName;

	@Column(name = "HASH_PRESENCE")
	private Long myHashPresence;

	@Transient
	private transient PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public SearchParamPresentEntity() {
		super();
	}

	/**
	 * Constructor
	 */
	public SearchParamPresentEntity(String theParamName, boolean thePresent) {
		myParamName = theParamName;
		myPresent = thePresent;
	}

	@SuppressWarnings("unused")
	@PrePersist
	public void calculateHashes() {
		if (myHashPresence == null && getParamName() != null) {
			String resourceType = getResource().getResourceType();
			String paramName = getParamName();
			boolean present = myPresent;
			setHashPresence(
					calculateHashPresence(getPartitionSettings(), getPartitionId(), resourceType, paramName, present));
		}
	}

	public Long getHashPresence() {
		Validate.notNull(myHashPresence);
		return myHashPresence;
	}

	public void setHashPresence(Long theHashPresence) {
		myHashPresence = theHashPresence;
	}

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResourceTable) {
		myResource = theResourceTable;
	}

	public boolean isPresent() {
		return myPresent;
	}

	public void setPresent(boolean thePresent) {
		myPresent = thePresent;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		SearchParamPresentEntity that = (SearchParamPresentEntity) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getHashPresence(), that.getHashPresence());
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(getHashPresence());
		return b.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);

		b.append("resPid", myResource.getIdDt().toUnqualifiedVersionless().getValue());
		b.append("paramName", myParamName);
		b.append("present", myPresent);
		b.append("partition", getPartitionId());
		return b.build();
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public void setPartitionSettings(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	/**
	 * Copy all mutable values from the given source
	 */
	public void updateValues(SearchParamPresentEntity theSource) {
		super.setPartitionId(theSource.getPartitionId());
		setResource(theSource.getResource());
		setPartitionSettings(theSource.getPartitionSettings());
		setHashPresence(theSource.getHashPresence());
		setParamName(theSource.getParamName());
		setPresent(theSource.isPresent());
	}

	public static long calculateHashPresence(
			PartitionSettings thePartitionSettings,
			PartitionablePartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			Boolean thePresent) {
		RequestPartitionId requestPartitionId = PartitionablePartitionId.toRequestPartitionId(theRequestPartitionId);
		return calculateHashPresence(
				thePartitionSettings, requestPartitionId, theResourceType, theParamName, thePresent);
	}

	public static long calculateHashPresence(
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theParamName,
			Boolean thePresent) {
		String string = thePresent != null ? Boolean.toString(thePresent) : Boolean.toString(false);
		return hashSearchParam(thePartitionSettings, theRequestPartitionId, theResourceType, theParamName, string);
	}
}
