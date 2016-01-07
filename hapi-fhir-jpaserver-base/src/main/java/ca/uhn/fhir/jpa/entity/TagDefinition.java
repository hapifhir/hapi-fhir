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
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uhn.fhir.model.api.Tag;

//@formatter:on
@Entity
@Table(name = "HFJ_TAG_DEF", uniqueConstraints = { @UniqueConstraint(columnNames = { "TAG_TYPE", "TAG_SYSTEM", "TAG_CODE" }) })
//@formatter:off
public class TagDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TAG_CODE", length = 200)
	private String myCode;

	@Column(name = "TAG_DISPLAY", length = 200)
	private String myDisplay;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TAG_ID")
	private Long myId;

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "myTag")
	private Collection<ResourceTag> myResources;

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "myTag")
	private Collection<ResourceHistoryTag> myResourceVersions;

	@Column(name = "TAG_SYSTEM", length = 200)
	private String mySystem;

	@Column(name="TAG_TYPE", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private TagTypeEnum myTagType;

	public TagDefinition() {
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

	public String getDisplay() {
		return myDisplay;
	}

	public Long getId() {
		return myId;
	}

	public String getSystem() {
		return mySystem;
	}

	public TagTypeEnum getTagType() {
		return myTagType;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public void setTagType(TagTypeEnum theTagType) {
		myTagType = theTagType;
	}

	public Tag toTag() {
		return new Tag(getSystem(), getCode(), getDisplay());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TagDefinition)) {
			return false;
		}
		TagDefinition other = (TagDefinition) obj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(myId, other.myId);
		return b.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myId);
		return b.toHashCode();
	}
	
}
