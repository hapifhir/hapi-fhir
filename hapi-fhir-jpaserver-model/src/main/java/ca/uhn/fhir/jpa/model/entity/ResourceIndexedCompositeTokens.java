package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_IDX_CMP_TOKS", indexes = {
	@Index(name = "IDX_IDXCMPTOKS_HI", columnList = "HASH_IDENTITY", unique = false),
	@Index(name = "IDX_IDXCMPTOKS_RES", columnList = "RES_ID", unique = false)
})
public class ResourceIndexedCompositeTokens extends BasePartitionable implements Comparable<ResourceIndexedCompositeTokens> {

	@SequenceGenerator(name = "SEQ_IDXCMPTOKS_ID", sequenceName = "SEQ_IDXCMPTOKS_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMPTOKS_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_IDXCMPTOKS_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "HASH_IDENTITY", nullable = false)
	private Long myHashIdentity;

	/**
	 * Constructor
	 */
	public ResourceIndexedCompositeTokens() {
		super();
	}


	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		ResourceIndexedCompositeTokens that = (ResourceIndexedCompositeTokens) theO;

		return new EqualsBuilder()
			.append(myResource, that.myResource)
			.append(myHashIdentity, that.myHashIdentity)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myResource)
			.append(myHashIdentity)
			.toHashCode();
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		myHashIdentity = theHashIdentity;
	}

	@Override
	public int compareTo(ResourceIndexedCompositeTokens theO) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(myHashIdentity, theO.getHashIdentity());
		return b.toComparison();
	}

}
