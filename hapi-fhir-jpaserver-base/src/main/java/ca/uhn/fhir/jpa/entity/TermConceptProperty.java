package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "TRM_CONCEPT_PROPERTY", uniqueConstraints = {
}, indexes = {
})
public class TermConceptProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "CONCEPT_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTPROP_CONCEPT"))
	private TermConcept myConcept;

	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_PROP_PID", sequenceName = "SEQ_CONCEPT_PROP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PROP_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name="PROP_KEY", length=200, nullable=false)
	private String myKey;
	
	@Column(name="PROP_VAL", length=200, nullable=true)
	private String myValue;

	public String getKey() {
		return myKey;
	}

	public String getValue() {
		return myValue;
	}

	public void setConcept(TermConcept theConcept) {
		myConcept = theConcept;
	}

	public void setKey(String theKey) {
		myKey = theKey;
	}
	
	public void setValue(String theValue) {
		myValue = theValue;
	}

}
