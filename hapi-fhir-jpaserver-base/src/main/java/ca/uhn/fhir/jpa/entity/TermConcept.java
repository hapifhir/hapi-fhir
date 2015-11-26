package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

public class TermConcept implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id()
	@SequenceGenerator(name="SEQ_CONCEPT_PID", sequenceName="SEQ_CONCEPT_PID")
	@GeneratedValue()
	@Column(name="PID")
	private Long myPid;

	@ManyToOne()
	@JoinColumn(name="VS_PID", referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_CONCEPT_PID_VS_PID"))
	private TermValueSet myValueSet;
	
}
