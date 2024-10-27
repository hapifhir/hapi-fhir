/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.io.Serializable;
import java.util.Date;

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
	@Column(name = "REV", nullable = false)
	private long myRev;

	@RevisionTimestamp
	@Column(name = "REVTSTMP")
	private Date myRevtstmp;

	public long getRev() {
		return myRev;
	}

	public void setRev(long theRev) {
		myRev = theRev;
	}

	public Date getRevtstmp() {
		return myRevtstmp;
	}

	public void setRevtstmp(Date theRevtstmp) {
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
