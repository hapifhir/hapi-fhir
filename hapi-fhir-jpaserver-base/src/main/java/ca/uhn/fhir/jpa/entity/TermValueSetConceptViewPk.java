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
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TermValueSetConceptViewPk implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CONCEPT_PID")
	private Long myConceptPid;

	@Column(name = "DESIGNATION_PID")
	private Long myDesignationPid;

	public Long getConceptPid() {
		return myConceptPid;
	}

	public Long getDesignationPid() {
		return myDesignationPid;
	}
}
