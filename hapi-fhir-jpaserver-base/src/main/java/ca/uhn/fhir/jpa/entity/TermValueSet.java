package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.left;

@Table(name = "TRM_VALUESET", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_VALUESET_URL", columnNames = {"URL"})
})
@Entity()
public class TermValueSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int NAME_LENGTH = 200;

	@Id()
	@SequenceGenerator(name = "SEQ_VALUESET_PID", sequenceName = "SEQ_VALUESET_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESET_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "URL", nullable = false)
	private String myUrl;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMVALUESET_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@Column(name = "NAME")
	private String myName;

	@OneToMany(mappedBy = "myValueSet")
	private List<TermValueSetCode> myCodes;

	public Long getId() {
		return myId;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public String getName() {
		return myName;
	}

	public void setName(String theName) {
		myName = left(theName, NAME_LENGTH);
	}

	public List<TermValueSetCode> getCodes() {
		if (myCodes == null) {
			myCodes = new ArrayList<>();
		}

		return myCodes;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof TermValueSet)) return false;

		TermValueSet that = (TermValueSet) theO;

		return new EqualsBuilder()
			.append(getUrl(), that.getUrl())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getUrl())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("myId", myId)
			.append("myUrl", myUrl)
			.append(myResource != null ? ("myResource=" + myResource.toString()) : ("myResource=(null)"))
			.append("myResourcePid", myResourcePid)
			.append("myName", myName)
			.append(myCodes != null ? ("myCodes - size=" + myCodes.size()) : ("myCodes=(null)"))
			.toString();
	}
}
