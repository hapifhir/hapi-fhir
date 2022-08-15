package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

@Entity
@Table(name = "TRM_CONCEPT_MAP_GRP_ELEMENT", indexes = {
	@Index(name = "IDX_CNCPT_MAP_GRP_CD", columnList = "SOURCE_CODE")
})
public class TermConceptMapGroupElement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_GRP_ELM_PID", sequenceName = "SEQ_CONCEPT_MAP_GRP_ELM_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_GRP_ELM_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_GROUP_PID", nullable = false, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_TCMGELEMENT_GROUP"))
	private TermConceptMapGroup myConceptMapGroup;

	@Column(name = "SOURCE_CODE", nullable = false, length = TermConcept.MAX_CODE_LENGTH)
	private String myCode;

	@Column(name = "SOURCE_DISPLAY", length = TermConcept.MAX_DISP_LENGTH)
	private String myDisplay;

	@OneToMany(mappedBy = "myConceptMapGroupElement")
	private List<TermConceptMapGroupElementTarget> myConceptMapGroupElementTargets;

	@Column(name = "CONCEPT_MAP_URL", nullable = true, length = TermConceptMap.MAX_URL_LENGTH)
	private String myConceptMapUrl;

	@Column(name = "SYSTEM_URL", nullable = true, length = TermCodeSystem.MAX_URL_LENGTH)
	private String mySystem;

	@Column(name = "SYSTEM_VERSION", nullable = true, length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
	private String mySystemVersion;

	@Column(name = "VALUESET_URL", nullable = true, length = TermValueSet.MAX_URL_LENGTH)
	private String myValueSet;

	public String getCode() {
		return myCode;
	}

	public TermConceptMapGroupElement setCode(@Nonnull String theCode) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCode, "theCode must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCode, TermConcept.MAX_CODE_LENGTH,
			"Code exceeds maximum length (" + TermConcept.MAX_CODE_LENGTH + "): " + length(theCode));
		myCode = theCode;
		return this;
	}

	public TermConceptMapGroup getConceptMapGroup() {
		return myConceptMapGroup;
	}

	public TermConceptMapGroupElement setConceptMapGroup(TermConceptMapGroup theTermConceptMapGroup) {
		myConceptMapGroup = theTermConceptMapGroup;
		return this;
	}

	public List<TermConceptMapGroupElementTarget> getConceptMapGroupElementTargets() {
		if (myConceptMapGroupElementTargets == null) {
			myConceptMapGroupElementTargets = new ArrayList<>();
		}

		return myConceptMapGroupElementTargets;
	}

	public String getConceptMapUrl() {
		if (myConceptMapUrl == null) {
			myConceptMapUrl = getConceptMapGroup().getConceptMap().getUrl();
		}
		return myConceptMapUrl;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public TermConceptMapGroupElement setDisplay(String theDisplay) {
		myDisplay = left(theDisplay, TermConcept.MAX_DISP_LENGTH);
		return this;
	}

	public Long getId() {
		return myId;
	}

	public String getSystem() {
		if (mySystem == null) {
			mySystem = getConceptMapGroup().getSource();
		}
		return mySystem;
	}

	public String getSystemVersion() {
		if (mySystemVersion == null) {
			mySystemVersion = getConceptMapGroup().getSourceVersion();
		}
		return mySystemVersion;
	}

	public String getValueSet() {
		if (myValueSet == null) {
			myValueSet = getConceptMapGroup().getSourceValueSet();
		}
		return myValueSet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof TermConceptMapGroupElement)) return false;

		TermConceptMapGroupElement that = (TermConceptMapGroupElement) o;

		return new EqualsBuilder()
			.append(getCode(), that.getCode())
			.append(getSystem(), that.getSystem())
			.append(getSystemVersion(), that.getSystemVersion())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getCode())
			.append(getSystem())
			.append(getSystemVersion())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append(myConceptMapGroup != null ? ("myConceptMapGroup - id=" + myConceptMapGroup.getId()) : ("myConceptMapGroup=(null)"))
			.append("myCode", myCode)
			.append("myDisplay", myDisplay)
			.append(myConceptMapGroupElementTargets != null ? ("myConceptMapGroupElementTargets - size=" + myConceptMapGroupElementTargets.size()) : ("myConceptMapGroupElementTargets=(null)"))
			.append("myConceptMapUrl", this.getConceptMapUrl())
			.append("mySystem", this.getSystem())
			.append("mySystemVersion", this.getSystemVersion())
			.append("myValueSet", this.getValueSet())
			.toString();
	}
}
