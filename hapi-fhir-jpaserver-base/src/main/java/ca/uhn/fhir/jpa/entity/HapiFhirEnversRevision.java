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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

// TODO:  is this the best place for it?
@Entity
@RevisionEntity
@Table(name = "ENVERS_REVISION")
public class HapiFhirEnversRevision implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rev_id_generator")
	@SequenceGenerator(name = "rev_id_generator", sequenceName = "seq_revinfo", allocationSize = 1)
	@RevisionNumber
	private int id;

	@RevisionTimestamp
	private long timestamp;

	public int getId() {
		return id;
	}

	public void setId(int theId) {
		id = theId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long theTimestamp) {
		timestamp = theTimestamp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("timestamp", timestamp)
			.toString();
	}
}
