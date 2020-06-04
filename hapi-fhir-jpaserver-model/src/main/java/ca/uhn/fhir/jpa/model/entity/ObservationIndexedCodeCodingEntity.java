package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Entity
@Embeddable
@Table(name = "HFJ_SPIDX_LASTN_CODING")
public class ObservationIndexedCodeCodingEntity {

	public static final int MAX_LENGTH = 200;

	@Id
	@Column(name = "CODEABLE_CONCEPT_ID", length = MAX_LENGTH)
	private String myCodeableConceptId;

	@Field(name = "code", analyze = Analyze.NO)
	private String myCode;

	@Field(name = "system", analyze = Analyze.NO)
	private String mySystem;

	@Field(name = "code_system_hash", analyze = Analyze.NO)
	private String myCodeSystemHash;

	@Field(name = "display")
	private String myDisplay;

	public ObservationIndexedCodeCodingEntity() {
	}

	public ObservationIndexedCodeCodingEntity(String theSystem, String theCode, String theDisplay, String theCodeableConceptId) {
		myCode = theCode;
		mySystem = theSystem;
		myCodeSystemHash = String.valueOf(CodeSystemHash.hashCodeSystem(theSystem, theCode));
		myDisplay = theDisplay;
		myCodeableConceptId = theCodeableConceptId;
	}

}
