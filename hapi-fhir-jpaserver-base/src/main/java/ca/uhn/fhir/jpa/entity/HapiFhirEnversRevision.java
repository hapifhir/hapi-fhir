package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This class exists strictly to override the default names used to generate Hibernate Envers revision table.
 * <p>
 * It is not actually invoked by any hapi-fhir code or code that uses hapi-fhir.
 * <p>
 * Specificallyy (at this writing), the class overrides names for:
 *
 * <ol>
 *    <li>The table name itself</li>
 *    <li>The ID generator sequence</li>
 * </ol>
 *
 */
@Entity
@RevisionEntity
@Table(name = "HFJ_REVINFO")
public class HapiFhirEnversRevision implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_HFJ_REVINFO", sequenceName = "SEQ_HFJ_REVINFO")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_HFJ_REVINFO")
	@RevisionNumber
	@Column(name = "REV")
	private int myRev;

	@RevisionTimestamp
	@Column(name = "REVTSTMP")
	private long myRevtstmp;

	public int getRev() {
		return myRev;
	}

	public void setRev(int theRev) {
		myRev = theRev;
	}

	public long getRevtstmp() {
		return myRevtstmp;
	}

	public void setRevtstmp(long theRevtstmp) {
		myRevtstmp = theRevtstmp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myRev", myRev)
			.append("myRevtstmp", myRevtstmp)
			.toString();
	}
}
