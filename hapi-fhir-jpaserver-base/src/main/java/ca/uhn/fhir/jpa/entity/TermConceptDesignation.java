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

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_DESIG", uniqueConstraints = {
}, indexes = {
})
public class TermConceptDesignation implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name = "CONCEPT_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTDESIG_CONCEPT"))
	private TermConcept myConcept;
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_DESIG_PID", sequenceName = "SEQ_CONCEPT_DESIG_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_DESIG_PID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "LANG", length = 500, nullable = true)
	private String myLanguage;
	@Column(name = "USE_SYSTEM", length = 500, nullable = true)
	private String myUseSystem;
	@Column(name = "USE_CODE", length = 500, nullable = true)
	private String myUseCode;
	@Column(name = "USE_DISPLAY", length = 500, nullable = true)
	private String myUseDisplay;
	@Column(name = "VAL", length = 500, nullable = false)
	private String myValue;
	/**
	 * TODO: Make this non-null
	 *
	 * @since 3.5.0
	 */
	@ManyToOne
	@JoinColumn(name = "CS_VER_PID", nullable = true, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTDESIG_CSV"))
	private TermCodeSystemVersion myCodeSystemVersion;

	public String getLanguage() {
		return myLanguage;
	}

	public TermConceptDesignation setLanguage(String theLanguage) {
		myLanguage = theLanguage;
		return this;
	}

	public String getUseCode() {
		return myUseCode;
	}

	public TermConceptDesignation setUseCode(String theUseCode) {
		myUseCode = theUseCode;
		return this;
	}

	public String getUseDisplay() {
		return myUseDisplay;
	}

	public TermConceptDesignation setUseDisplay(String theUseDisplay) {
		myUseDisplay = theUseDisplay;
		return this;
	}

	public String getUseSystem() {
		return myUseSystem;
	}

	public TermConceptDesignation setUseSystem(String theUseSystem) {
		myUseSystem = theUseSystem;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public TermConceptDesignation setValue(String theValue) {
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
