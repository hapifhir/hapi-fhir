package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseTag extends BasePartitionable implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = {})
	@JoinColumn(name = "TAG_ID", nullable = false)
	private TagDefinition myTag;

	@Column(name = "TAG_ID", insertable = false, updatable = false)
	private Long myTagId;

	@Column(name = "TAG_USER_SELECTED")
	private Boolean myUserSelected;
	@Column(name = "TAG_VERSION", length = 30)
	private String myVersion;

	public Long getTagId() {
		return myTagId;
	}

	public TagDefinition getTag() {
		return myTag;
	}

	public void setTag(TagDefinition theTag) {
		myTag = theTag;
	}

	public Boolean getUserSelected() {
		return myUserSelected;
	}

	public void setUserSelected(Boolean theUserSelected) {
		myUserSelected = theUserSelected;
	}

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		myVersion = theVersion;
	}

	protected void setVersionAfterTrim(String theVersion) {
		if (theVersion != null) {
			setVersion(StringUtils.truncate(theVersion, 30));
		}
	}
}
