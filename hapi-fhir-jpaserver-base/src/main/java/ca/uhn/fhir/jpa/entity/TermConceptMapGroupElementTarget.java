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
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.length;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GRP_ELM_TGT", indexes = {
	@Index(name = "IDX_CNCPT_MP_GRP_ELM_TGT_CD", columnList = "TARGET_CODE")
})
public class TermConceptMapGroupElementTarget implements Serializable {
	private static final long serialVersionUID = 1L;

	static final int MAX_EQUIVALENCE_LENGTH = 50;

	@Id()
	@SequenceGenerator(name = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID", sequenceName = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_GRP_ELM_PID", nullable = false, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_TCMGETARGET_ELEMENT"))
	private TermConceptMapGroupElement myConceptMapGroupElement;

	@Column(name = "TARGET_CODE", nullable = false, length = TermConcept.MAX_CODE_LENGTH)
	private String myCode;

	@Column(name = "TARGET_DISPLAY", nullable = true, length = TermConcept.MAX_DISP_LENGTH)
	private String myDisplay;

	@Enumerated(EnumType.STRING)
	@Column(name = "TARGET_EQUIVALENCE", nullable = true, length = MAX_EQUIVALENCE_LENGTH)
	private ConceptMapEquivalence myEquivalence;

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

	public TermConceptMapGroupElementTarget setCode(@Nonnull String theCode) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCode, "theCode must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCode, TermConcept.MAX_CODE_LENGTH,
			"Code exceeds maximum length (" + TermConcept.MAX_CODE_LENGTH + "): " + length(theCode));
		myCode = theCode;
		return this;
	}

	public TermConceptMapGroupElement getConceptMapGroupElement() {
		return myConceptMapGroupElement;
	}

	public void setConceptMapGroupElement(TermConceptMapGroupElement theTermConceptMapGroupElement) {
		myConceptMapGroupElement = theTermConceptMapGroupElement;
	}

	public String getConceptMapUrl() {
		if (myConceptMapUrl == null) {
			myConceptMapUrl = getConceptMapGroupElement().getConceptMapGroup().getConceptMap().getUrl();
		}
		return myConceptMapUrl;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public TermConceptMapGroupElementTarget setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		return this;
	}

	public ConceptMapEquivalence getEquivalence() {
		return myEquivalence;
	}

	public TermConceptMapGroupElementTarget setEquivalence(ConceptMapEquivalence theEquivalence) {
		myEquivalence = theEquivalence;
		return this;
	}

	public Long getId() {
		return myId;
	}

	public String getSystem() {
		if (mySystem == null) {
			mySystem = getConceptMapGroupElement().getConceptMapGroup().getTarget();
		}
		return mySystem;
	}

	public String getSystemVersion() {
		if (mySystemVersion == null) {
			mySystemVersion = getConceptMapGroupElement().getConceptMapGroup().getTargetVersion();
		}
		return mySystemVersion;
	}

	public String getValueSet() {
		if (myValueSet == null) {
			myValueSet = getConceptMapGroupElement().getConceptMapGroup().getTargetValueSet();
		}
		return myValueSet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof TermConceptMapGroupElementTarget)) return false;

		TermConceptMapGroupElementTarget that = (TermConceptMapGroupElementTarget) o;

		return new EqualsBuilder()
			.append(getCode(), that.getCode())
			.append(getEquivalence(), that.getEquivalence())
			.append(getSystem(), that.getSystem())
			.append(getSystemVersion(), that.getSystemVersion())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getCode())
			.append(getEquivalence())
			.append(getSystem())
			.append(getSystemVersion())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append(myConceptMapGroupElement != null ? ("myConceptMapGroupElement - id=" + myConceptMapGroupElement.getId()) : ("myConceptMapGroupElement=(null)"))
			.append("myCode", myCode)
			.append("myDisplay", myDisplay)
			.append("myEquivalence", myEquivalence.toCode())
			.append("myConceptMapUrl", this.getConceptMapUrl())
			.append("mySystem", this.getSystem())
			.append("mySystemVersion", this.getSystemVersion())
			.append("myValueSet", this.getValueSet())
			.toString();
	}
}
