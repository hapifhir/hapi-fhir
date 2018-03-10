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

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "CONCEPT_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPTPROP_CONCEPT"))
	private TermConcept myConcept;
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_PROP_PID", sequenceName = "SEQ_CONCEPT_PROP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PROP_PID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "PROP_KEY", length = 200, nullable = false)
	@NotBlank
	private String myKey;
	@Column(name = "PROP_VAL", length = 200, nullable = true)
	private String myValue;

	public String getKey() {
		return myKey;
	}

	public void setKey(String theKey) {
		myKey = theKey;
	}

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) {
		myValue = theValue;
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
