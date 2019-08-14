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
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Entity
@Table(name = "TRM_CONCEPT_PROPERTY", uniqueConstraints = {
}, indexes = {
})
public class TermConceptProperty implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MAX_LENGTH = 500;
	static final int MAX_PROPTYPE_ENUM_LENGTH = 6;

	@ManyToOne
	@JoinColumn(name = "CONCEPT_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTPROP_CONCEPT"))
	private TermConcept myConcept;
	/**
	 * TODO: Make this non-null
	 *
	 * @since 3.5.0
	 */
	@ManyToOne
	@JoinColumn(name = "CS_VER_PID", nullable = true, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTPROP_CSV"))
	private TermCodeSystemVersion myCodeSystemVersion;
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_PROP_PID", sequenceName = "SEQ_CONCEPT_PROP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PROP_PID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "PROP_KEY", nullable = false, length = MAX_LENGTH)
	@NotBlank
	private String myKey;
	@Column(name = "PROP_VAL", nullable = true, length = MAX_LENGTH)
	private String myValue;
	@Column(name = "PROP_TYPE", nullable = false, length = MAX_PROPTYPE_ENUM_LENGTH)
	private TermConceptPropertyTypeEnum myType;

	/**
	 * Constructor
	 */
	public TermConceptProperty() {
		super();
	}

	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	@Column(name = "PROP_CODESYSTEM", length = MAX_LENGTH, nullable = true)
	private String myCodeSystem;
	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	@Column(name = "PROP_DISPLAY", length = MAX_LENGTH, nullable = true)
	private String myDisplay;

	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	public String getCodeSystem() {
		return myCodeSystem;
	}

	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	public TermConceptProperty setCodeSystem(String theCodeSystem) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCodeSystem, MAX_LENGTH,
			"Property code system exceeds maximum length (" + MAX_LENGTH + "): " + length(theCodeSystem));
		myCodeSystem = theCodeSystem;
		return this;
	}

	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	public String getDisplay() {
		return myDisplay;
	}

	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	public TermConceptProperty setDisplay(String theDisplay) {
		myDisplay = left(theDisplay, MAX_LENGTH);
		return this;
	}

	public String getKey() {
		return myKey;
	}

	public TermConceptProperty setKey(@Nonnull String theKey) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theKey, "theKey must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theKey, MAX_LENGTH,
			"Code exceeds maximum length (" + MAX_LENGTH + "): " + length(theKey));
		myKey = theKey;
		return this;
	}

	public TermConceptPropertyTypeEnum getType() {
		return myType;
	}

	public TermConceptProperty setType(@Nonnull TermConceptPropertyTypeEnum theType) {
		Validate.notNull(theType);
		myType = theType;
		return this;
	}

	/**
	 * This will contain the value for a {@link TermConceptPropertyTypeEnum#STRING string}
	 * property, and the code for a {@link TermConceptPropertyTypeEnum#CODING coding} property.
	 */
	public String getValue() {
		return myValue;
	}

	/**
	 * This will contain the value for a {@link TermConceptPropertyTypeEnum#STRING string}
	 * property, and the code for a {@link TermConceptPropertyTypeEnum#CODING coding} property.
	 */
	public TermConceptProperty setValue(String theValue) {
		myValue = left(theValue, MAX_LENGTH);
		return this;
	}

	public TermConceptProperty setCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
		return this;
	}

	public TermConceptProperty setConcept(TermConcept theConcept) {
		myConcept = theConcept;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("key", myKey)
			.append("value", myValue)
			.toString();
	}

}
