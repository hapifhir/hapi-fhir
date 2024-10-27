/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(
		name = "HFJ_TAG_DEF",
		indexes = {
			@Index(
					name = "IDX_TAG_DEF_TP_CD_SYS",
					columnList = "TAG_TYPE, TAG_CODE, TAG_SYSTEM, TAG_ID, TAG_VERSION, TAG_USER_SELECTED"),
		})
public class TagDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TAG_CODE", length = 200)
	private String myCode;

	@Column(name = "TAG_DISPLAY", length = 200)
	private String myDisplay;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_TAGDEF_ID")
	@SequenceGenerator(name = "SEQ_TAGDEF_ID", sequenceName = "SEQ_TAGDEF_ID")
	@Column(name = "TAG_ID")
	private Long myId;

	// one tag definition -> many resource tags
	@OneToMany(
			cascade = {},
			fetch = FetchType.LAZY,
			mappedBy = "myTag")
	private Collection<ResourceTag> myResources;

	// one tag definition -> many history
	@OneToMany(
			cascade = {},
			fetch = FetchType.LAZY,
			mappedBy = "myTag")
	private Collection<ResourceHistoryTag> myResourceVersions;

	@Column(name = "TAG_SYSTEM", length = 200)
	private String mySystem;

	@Column(name = "TAG_TYPE", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@JdbcTypeCode(SqlTypes.INTEGER)
	private TagTypeEnum myTagType;

	@Column(name = "TAG_VERSION", length = 30)
	private String myVersion;

	@Column(name = "TAG_USER_SELECTED")
	private Boolean myUserSelected;

	@Transient
	private transient Integer myHashCode;

	/**
	 * Constructor
	 */
	public TagDefinition() {
		super();
	}

	public TagDefinition(TagTypeEnum theTagType, String theSystem, String theCode, String theDisplay) {
		setTagType(theTagType);
		setCode(theCode);
		setSystem(theSystem);
		setDisplay(theDisplay);
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
		myHashCode = null;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
		myHashCode = null;
	}

	public TagTypeEnum getTagType() {
		return myTagType;
	}

	public void setTagType(TagTypeEnum theTagType) {
		myTagType = theTagType;
		myHashCode = null;
	}

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		setVersionAfterTrim(theVersion);
	}

	private void setVersionAfterTrim(String theVersion) {
		if (theVersion != null) {
			myVersion = StringUtils.truncate(theVersion, 30);
		}
	}

	/**
	 * Warning - this is nullable, while IBaseCoding getUserSelected isn't.
	 * todo maybe rename?
	 */
	public Boolean getUserSelected() {
		return myUserSelected;
	}

	public void setUserSelected(Boolean theUserSelected) {
		myUserSelected = theUserSelected;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TagDefinition)) {
			return false;
		}
		TagDefinition other = (TagDefinition) obj;

		EqualsBuilder b = new EqualsBuilder();

		if (myId != null && other.myId != null) {
			b.append(myId, other.myId);
		} else {
			b.append(myTagType, other.myTagType);
			b.append(mySystem, other.mySystem);
			b.append(myCode, other.myCode);
			b.append(myVersion, other.myVersion);
			b.append(myUserSelected, other.myUserSelected);
		}

		return b.isEquals();
	}

	@Override
	public int hashCode() {
		if (myHashCode == null) {
			HashCodeBuilder b = new HashCodeBuilder();
			b.append(myTagType);
			b.append(mySystem);
			b.append(myCode);
			b.append(myVersion);
			b.append(myUserSelected);
			myHashCode = b.toHashCode();
		}
		return myHashCode;
	}

	@Override
	public String toString() {
		ToStringBuilder retVal = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		retVal.append("id", myId);
		retVal.append("system", mySystem);
		retVal.append("code", myCode);
		retVal.append("display", myDisplay);
		retVal.append("version", myVersion);
		retVal.append("userSelected", myUserSelected);
		return retVal.build();
	}
}
