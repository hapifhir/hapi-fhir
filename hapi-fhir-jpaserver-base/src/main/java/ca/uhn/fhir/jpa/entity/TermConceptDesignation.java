package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.util.ValidateUtil;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Entity
@Table(name = "TRM_CONCEPT_DESIG", uniqueConstraints = {
}, indexes = {
})
public class TermConceptDesignation implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH = 500;
	public static final int MAX_VAL_LENGTH = 2000;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONCEPT_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTDESIG_CONCEPT"))
	private TermConcept myConcept;
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_DESIG_PID", sequenceName = "SEQ_CONCEPT_DESIG_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_DESIG_PID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "LANG", nullable = true, length = MAX_LENGTH)
	private String myLanguage;
	@Column(name = "USE_SYSTEM", nullable = true, length = MAX_LENGTH)
	private String myUseSystem;
	@Column(name = "USE_CODE", nullable = true, length = MAX_LENGTH)
	private String myUseCode;
	@Column(name = "USE_DISPLAY", nullable = true, length = MAX_LENGTH)
	private String myUseDisplay;
	@Column(name = "VAL", nullable = false, length = MAX_VAL_LENGTH)
	private String myValue;
	/**
	 * TODO: Make this non-null
	 *
	 * @since 3.5.0
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CS_VER_PID", nullable = true, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTDESIG_CSV"))
	private TermCodeSystemVersion myCodeSystemVersion;

	public String getLanguage() {
		return myLanguage;
	}

	public TermConceptDesignation setLanguage(String theLanguage) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theLanguage, MAX_LENGTH,
			"Language exceeds maximum length (" + MAX_LENGTH + "): " + length(theLanguage));
		myLanguage = theLanguage;
		return this;
	}

	public String getUseCode() {
		return myUseCode;
	}

	public TermConceptDesignation setUseCode(String theUseCode) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUseCode, MAX_LENGTH,
			"Use code exceeds maximum length (" + MAX_LENGTH + "): " + length(theUseCode));
		myUseCode = theUseCode;
		return this;
	}

	public String getUseDisplay() {
		return myUseDisplay;
	}

	public TermConceptDesignation setUseDisplay(String theUseDisplay) {
		myUseDisplay = left(theUseDisplay, MAX_LENGTH);
		return this;
	}

	public String getUseSystem() {
		return myUseSystem;
	}

	public TermConceptDesignation setUseSystem(String theUseSystem) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUseSystem, MAX_LENGTH,
			"Use system exceeds maximum length (" + MAX_LENGTH + "): " + length(theUseSystem));
		myUseSystem = theUseSystem;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public TermConceptDesignation setValue(@Nonnull String theValue) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theValue, "theValue must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theValue, MAX_VAL_LENGTH,
			"Value exceeds maximum length (" + MAX_VAL_LENGTH + "): " + length(theValue));
		myValue = theValue;
		return this;
	}

	public TermConceptDesignation setCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
		return this;
	}

	public TermConceptDesignation setConcept(TermConcept theConcept) {
		myConcept = theConcept;
		return this;
	}


}
