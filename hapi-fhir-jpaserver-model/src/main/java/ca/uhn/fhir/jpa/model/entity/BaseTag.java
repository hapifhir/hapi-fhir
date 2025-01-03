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

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;

@MappedSuperclass
public abstract class BaseTag extends BasePartitionable implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Every tag has a reference to the tag definition. Note that this field
	 * must not have a FK constraint! In this case, Postgres (and maybe others)
	 * are horribly slow writing to the table if there's an FK constraint.
	 * See https://pganalyze.com/blog/5mins-postgres-multiXact-ids-foreign-keys-performance
	 */
	@ManyToOne(cascade = {})
	@JoinColumn(name = "TAG_ID", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@NotFound(action = NotFoundAction.IGNORE)
	private TagDefinition myTag;

	@Column(name = "TAG_ID", insertable = false, updatable = false)
	private Long myTagId;

	public Long getTagId() {
		return myTagId;
	}

	/**
	 * This can be null if the tag definition has been deleted. This
	 * should never happen unless someone has manually messed with
	 * the database, but it could happen.
	 */
	@Nullable
	public TagDefinition getTag() {
		return myTag;
	}

	public void setTag(TagDefinition theTag) {
		myTag = theTag;
	}
}
