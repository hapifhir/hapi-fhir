/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

import static ca.uhn.fhir.jpa.model.entity.ResourceTable.FHIR_ID;
import static ca.uhn.fhir.jpa.model.entity.ResourceTable.FHIR_ID_LENGTH;

@Entity
@Table(name = "HFJ_RES_IDENTIFIER_PT_UNIQ")
public class ResourceIdentifierPatientUniqueEntity {

	@EmbeddedId
	private PatientIdentifierPk myPk;

	@Column(name = FHIR_ID, length = FHIR_ID_LENGTH)
	private String myFhirId;

	public void setPk(PatientIdentifierPk thePk) {
		myPk = thePk;
	}

	public String getFhirId() {
		return myFhirId;
	}

	public void setFhirId(String theFhirId) {
		myFhirId = theFhirId;
	}

	public static class PatientIdentifierPk {

		@Column(name = "IDENT_SYSTEM_PID")
		private Long mySystemPid;

		@Column(name = "IDENT_VALUE")
		private String myValue;

		/**
		 * Constructor
		 */
		public PatientIdentifierPk() {
			// nothing
		}

		/**
		 * Constructor
		 */
		public PatientIdentifierPk(Long theSystemPid, String theValue) {
			mySystemPid = theSystemPid;
			myValue = theValue;
		}

		@Override
		public boolean equals(Object theO) {
			if (!(theO instanceof PatientIdentifierPk that)) {
				return false;
			}
			return Objects.equals(mySystemPid, that.mySystemPid) && Objects.equals(myValue, that.myValue);
		}

		@Override
		public int hashCode() {
			return Objects.hash(mySystemPid, myValue);
		}
	}
}
