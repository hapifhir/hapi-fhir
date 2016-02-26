package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TRM_CONCEPT_PC_LINK")
public class TermConceptParentChildLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name="CHILD_PID", nullable=false, referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_TERM_CONCEPTPC_CHILD"))
	private TermConcept myChild;

	@ManyToOne()
	@JoinColumn(name="CODESYSTEM_PID", nullable=false, foreignKey=@ForeignKey(name="FK_TERM_CONCEPTPC_CS"))
	private TermCodeSystemVersion myCodeSystem;

	@ManyToOne()
	@JoinColumn(name="PARENT_PID", nullable=false, referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_TERM_CONCEPTPC_PARENT"))
	private TermConcept myParent;

	@Id()
	@SequenceGenerator(name="SEQ_CONCEPT_PC_PID", sequenceName="SEQ_CONCEPT_PC_PID")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_CONCEPT_PC_PID")
	@Column(name="PID")
	private Long myPid;

	public TermConcept getChild() {
		return myChild;
	}

	public TermCodeSystemVersion getCodeSystem() {
		return myCodeSystem;
	}
	
	public TermConcept getParent() {
		return myParent;
	}

	public void setChild(TermConcept theChild) {
		myChild = theChild;
	}
	
	public void setCodeSystem(TermCodeSystemVersion theCodeSystem) {
		myCodeSystem = theCodeSystem;
	}

	public void setParent(TermConcept theParent) {
		myParent = theParent;
	}
	
}
