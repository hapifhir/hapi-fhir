package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_PROPERTY", uniqueConstraints = {
}, indexes = {
})
public class TermConceptProperty implements Serializable {

	static final int MAX_PROPTYPE_ENUM_LENGTH = 6;
	private static final long serialVersionUID = 1L;
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
	@Column(name = "PROP_KEY", length = 500, nullable = false)
	@NotBlank
	private String myKey;
	@Column(name = "PROP_VAL", length = 500, nullable = true)
	private String myValue;
	@Column(name = "PROP_TYPE", length = MAX_PROPTYPE_ENUM_LENGTH, nullable = false)
	private TermConceptPropertyTypeEnum myType;
	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	@Column(name = "PROP_CODESYSTEM", length = 500, nullable = true)
	private String myCodeSystem;
	/**
	 * Relevant only for properties of type {@link TermConceptPropertyTypeEnum#CODING}
	 */
	@Column(name = "PROP_DISPLAY", length = 500, nullable = true)
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
		myDisplay = theDisplay;
		return this;
	}

	public String getKey() {
		return myKey;
	}

	public void setKey(String theKey) {
		myKey = theKey;
	}

	public TermConceptPropertyTypeEnum getType() {
		return myType;
	}

	public TermConceptProperty setType(TermConceptPropertyTypeEnum theType) {
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
	public void setValue(String theValue) {
		myValue = theValue;
	}

	public TermConceptProperty setCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
		return this;
	}

	public void setConcept(TermConcept theConcept) {
		myConcept = theConcept;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("key", myKey)
			.append("value", myValue)
			.toString();
	}

}
