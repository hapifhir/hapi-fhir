package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.*;

import javax.persistence.*;

@Entity()
@Table(name = "HFJ_IDX_CMP_STRING_UNIQ", indexes = {
	@Index(name = ResourceIndexedCompositeStringUnique.IDX_IDXCMPSTRUNIQ_STRING, columnList = "IDX_STRING", unique = true),
	@Index(name = ResourceIndexedCompositeStringUnique.IDX_IDXCMPSTRUNIQ_RESOURCE, columnList = "RES_ID", unique = false)
})
public class ResourceIndexedCompositeStringUnique implements Comparable<ResourceIndexedCompositeStringUnique> {

	public static final int MAX_STRING_LENGTH = 200;
	public static final String IDX_IDXCMPSTRUNIQ_STRING = "IDX_IDXCMPSTRUNIQ_STRING";
	public static final String IDX_IDXCMPSTRUNIQ_RESOURCE = "IDX_IDXCMPSTRUNIQ_RESOURCE";

	@SequenceGenerator(name = "SEQ_IDXCMPSTRUNIQ_ID", sequenceName = "SEQ_IDXCMPSTRUNIQ_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_IDXCMPSTRUNIQ_ID")
	@Id
	@Column(name = "PID")
	private Long myId;
	@ManyToOne
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_IDXCMPSTRUNIQ_RES_ID"))
	private ResourceTable myResource;
	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourceId;
	@Column(name = "IDX_STRING", nullable = false, length = MAX_STRING_LENGTH)
	private String myIndexString;

	/**
	 * Constructor
	 */
	public ResourceIndexedCompositeStringUnique() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceIndexedCompositeStringUnique(ResourceTable theResource, String theIndexString) {
		setResource(theResource);
		setIndexString(theIndexString);
	}

	@Override
	public int compareTo(ResourceIndexedCompositeStringUnique theO) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(myIndexString, theO.getIndexString());
		return b.toComparison();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof ResourceIndexedCompositeStringUnique)) {
			return false;
		}

		ResourceIndexedCompositeStringUnique that = (ResourceIndexedCompositeStringUnique) theO;

		return new EqualsBuilder()
			.append(myIndexString, that.myIndexString)
			.isEquals();
	}

	public String getIndexString() {
		return myIndexString;
	}

	public void setIndexString(String theIndexString) {
		myIndexString = theIndexString;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		Validate.notNull(theResource);
		myResource = theResource;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myIndexString)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", myId)
			.append("resourceId", myResourceId)
			.append("indexString", myIndexString)
			.toString();
	}
}
