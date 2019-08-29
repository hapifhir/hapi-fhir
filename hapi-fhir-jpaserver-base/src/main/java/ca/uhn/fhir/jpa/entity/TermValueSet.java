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
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Table(name = "TRM_VALUESET", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_VALUESET_URL", columnNames = {"URL"})
})
@Entity()
public class TermValueSet implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_EXPANSION_STATUS_LENGTH = 50;
	public static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_URL_LENGTH = 200;

	@Id()
	@SequenceGenerator(name = "SEQ_VALUESET_PID", sequenceName = "SEQ_VALUESET_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESET_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "URL", nullable = false, length = MAX_URL_LENGTH)
	private String myUrl;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMVALUESET_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@Column(name = "VSNAME", nullable = true, length = MAX_NAME_LENGTH)
	private String myName;

	@OneToMany(mappedBy = "myValueSet")
	private List<TermValueSetConcept> myConcepts;

	@Enumerated(EnumType.STRING)
	@Column(name = "EXPANSION_STATUS", nullable = false, length = MAX_EXPANSION_STATUS_LENGTH)
	private TermValueSetPreExpansionStatusEnum myExpansionStatus;

	public TermValueSet() {
		super();
		myExpansionStatus = TermValueSetPreExpansionStatusEnum.NOT_EXPANDED;
	}

	public Long getId() {
		return myId;
	}

	public String getUrl() {
		return myUrl;
	}

	public TermValueSet setUrl(@Nonnull String theUrl) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theUrl, "theUrl must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUrl, MAX_URL_LENGTH,
			"URL exceeds maximum length (" + MAX_URL_LENGTH + "): " + length(theUrl));
		myUrl = theUrl;
		return this;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public TermValueSet setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public String getName() {
		return myName;
	}

	public TermValueSet setName(String theName) {
		myName = left(theName, MAX_NAME_LENGTH);
		return this;
	}

	public List<TermValueSetConcept> getConcepts() {
		if (myConcepts == null) {
			myConcepts = new ArrayList<>();
		}

		return myConcepts;
	}

	public TermValueSetPreExpansionStatusEnum getExpansionStatus() {
		return myExpansionStatus;
	}

	public void setExpansionStatus(TermValueSetPreExpansionStatusEnum theExpansionStatus) {
		myExpansionStatus = theExpansionStatus;
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
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append("myUrl", myUrl)
			.append(myResource != null ? ("myResource=" + myResource.toString()) : ("myResource=(null)"))
			.append("myResourcePid", myResourcePid)
			.append("myName", myName)
			.append(myConcepts != null ? ("myConcepts - size=" + myConcepts.size()) : ("myConcepts=(null)"))
			.append("myExpansionStatus", myExpansionStatus)
			.toString();
	}
}
