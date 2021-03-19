package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_MAP_PRESENCE", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_CNCPT_MAP_PRES_ST", columnNames = {"SOURCE_CS_URL", "TARGET_CS_URL"})
})
public class TermConceptMapMappingPresence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_PRES_PID", sequenceName = "SEQ_CONCEPT_MAP_PRES_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_PRES_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SOURCE_CS_URL", nullable = false, length = TermCodeSystem.MAX_URL_LENGTH)
	private String mySourceCodeSystemUrl;
	@Column(name = "TARGET_CS_URL", nullable = false, length = TermCodeSystem.MAX_URL_LENGTH)
	private String myTargetCodeSystemUrl;

	public String getSourceCodeSystemUrl() {
		return mySourceCodeSystemUrl;
	}

	public void setSourceCodeSystemUrl(String theSourceCodeSystemUrl) {
		mySourceCodeSystemUrl = theSourceCodeSystemUrl;
	}

	public String getTargetCodeSystemUrl() {
		return myTargetCodeSystemUrl;
	}

	public void setTargetCodeSystemUrl(String theTargetCodeSystemUrl) {
		myTargetCodeSystemUrl = theTargetCodeSystemUrl;
	}

}
