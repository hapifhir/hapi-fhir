package ca.uhn.fhir.jpa.entity;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;

import javax.persistence.*;

@MappedSuperclass
public class BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = {})
	@JoinColumn(name = "TAG_ID", nullable = false)
	private TagDefinition myTag;

	@Column(name = "TAG_ID", insertable = false, updatable = false)
	private Long myTagId;

	public Long getTagId() {
		return myTagId;
	}

	public TagDefinition getTag() {
		return myTag;
	}

	public void setTag(TagDefinition theTag) {
		myTag = theTag;
	}

}
