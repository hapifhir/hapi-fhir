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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.length;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GROUP")
public class TermConceptMapGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_GROUP_PID", sequenceName = "SEQ_CONCEPT_MAP_GROUP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_GROUP_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_PID", nullable = false, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_TCMGROUP_CONCEPTMAP"))
	private TermConceptMap myConceptMap;

	@Column(name = "SOURCE_URL", nullable = false, length = TermCodeSystem.MAX_URL_LENGTH)
	private String mySource;

	@Column(name = "SOURCE_VERSION", nullable = true, length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
	private String mySourceVersion;

	@Column(name = "TARGET_URL", nullable = false, length = TermCodeSystem.MAX_URL_LENGTH)
	private String myTarget;

	@Column(name = "TARGET_VERSION", nullable = true, length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
	private String myTargetVersion;

	@OneToMany(mappedBy = "myConceptMapGroup")
	private List<TermConceptMapGroupElement> myConceptMapGroupElements;

	@Column(name= "CONCEPT_MAP_URL", nullable = true, length = TermConceptMap.MAX_URL_LENGTH)
	private String myConceptMapUrl;

	@Column(name= "SOURCE_VS", nullable = true, length = TermValueSet.MAX_URL_LENGTH)
	private String mySourceValueSet;

	@Column(name= "TARGET_VS", nullable = true, length = TermValueSet.MAX_URL_LENGTH)
	private String myTargetValueSet;

	public TermConceptMap getConceptMap() {
		return myConceptMap;
	}

	public TermConceptMapGroup setConceptMap(TermConceptMap theTermConceptMap) {
		myConceptMap = theTermConceptMap;
		return this;
	}

	public List<TermConceptMapGroupElement> getConceptMapGroupElements() {
		if (myConceptMapGroupElements == null) {
			myConceptMapGroupElements = new ArrayList<>();
		}

		return myConceptMapGroupElements;
	}

	public String getConceptMapUrl() {
		if (myConceptMapUrl == null) {
			myConceptMapUrl = getConceptMap().getUrl();
		}
		return myConceptMapUrl;
	}

	public Long getId() {
		return myId;
	}

	public String getSource() {
		return mySource;
	}

	public TermConceptMapGroup setSource(@Nonnull String theSource) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theSource, "theSource must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theSource, TermCodeSystem.MAX_URL_LENGTH,
			"Source exceeds maximum length (" + TermCodeSystem.MAX_URL_LENGTH + "): " + length(theSource));
		this.mySource = theSource;
		return this;
	}

	public String getSourceValueSet() {
		if (mySourceValueSet == null) {
			mySourceValueSet = getConceptMap().getSource();
		}
		return mySourceValueSet;
	}

	public String getSourceVersion() {
		return mySourceVersion;
	}

	public TermConceptMapGroup setSourceVersion(String theSourceVersion) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theSourceVersion, TermCodeSystemVersion.MAX_VERSION_LENGTH,
			"Source version ID exceeds maximum length (" + TermCodeSystemVersion.MAX_VERSION_LENGTH + "): " + length(theSourceVersion));
		mySourceVersion = theSourceVersion;
		return this;
	}

	public String getTarget() {
		return myTarget;
	}

	public TermConceptMapGroup setTarget(@Nonnull String theTarget) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theTarget, "theTarget must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theTarget, TermCodeSystem.MAX_URL_LENGTH,
			"Target exceeds maximum length (" + TermCodeSystem.MAX_URL_LENGTH + "): " + length(theTarget));
		this.myTarget = theTarget;
		return this;
	}

	public String getTargetValueSet() {
		if (myTargetValueSet == null) {
			myTargetValueSet = getConceptMap().getTarget();
		}
		return myTargetValueSet;
	}

	public String getTargetVersion() {
		return myTargetVersion;
	}

	public TermConceptMapGroup setTargetVersion(String theTargetVersion) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theTargetVersion, TermCodeSystemVersion.MAX_VERSION_LENGTH,
			"Target version ID exceeds maximum length (" + TermCodeSystemVersion.MAX_VERSION_LENGTH + "): " + length(theTargetVersion));
		myTargetVersion = theTargetVersion;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append(myConceptMap != null ? ("myConceptMap - id=" + myConceptMap.getId()) : ("myConceptMap=(null)"))
			.append("mySource", mySource)
			.append("mySourceVersion", mySourceVersion)
			.append("myTarget", myTarget)
			.append("myTargetVersion", myTargetVersion)
			.append(myConceptMapGroupElements != null ? ("myConceptMapGroupElements - size=" + myConceptMapGroupElements.size()) : ("myConceptMapGroupElements=(null)"))
			.append("myConceptMapUrl", this.getConceptMapUrl())
			.append("mySourceValueSet", this.getSourceValueSet())
			.append("myTargetValueSet", this.getTargetValueSet())
			.toString();
	}
}
