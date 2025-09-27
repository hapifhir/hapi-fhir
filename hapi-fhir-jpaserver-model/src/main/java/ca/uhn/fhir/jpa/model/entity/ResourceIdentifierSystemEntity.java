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
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "HFJ_RES_IDENTIFIER_SYSTEM",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_RES_IDENT_SYS",
					columnNames = {"SYSTEM_URL"})
		})
public class ResourceIdentifierSystemEntity {

	@SequenceGenerator(name = "SEQ_RES_IDENTIFIER_SYSTEM", sequenceName = "SEQ_RES_IDENTIFIER_SYSTEM")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RES_IDENTIFIER_SYSTEM")
	@Column(name = "PID")
	@Id
	private Long myPid;

	@Column(name = "SYSTEM_URL", length = 500, nullable = false)
	private String mySystem;

	public Long getPid() {
		return myPid;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}
}
