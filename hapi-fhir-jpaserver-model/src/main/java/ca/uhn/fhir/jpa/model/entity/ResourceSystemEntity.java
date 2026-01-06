/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.Validate;

import java.nio.charset.StandardCharsets;

/**
 * Used for Identifier system values and potentially other system URLs in FHIR
 */
@Entity
@Table(
		name = "HFJ_RES_SYSTEM",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_RESIDENT_SYS",
					columnNames = {"SYSTEM_URL"})
		})
public class ResourceSystemEntity {

	/**
	 * This column stores a hash of the system URL
	 */
	@Column(name = "PID")
	@Id
	private Long myPid;

	@Column(name = "SYSTEM_URL", length = 500, nullable = false)
	private String mySystem;

	public Long getPid() {
		return myPid;
	}

	@SuppressWarnings("UnstableApiUsage")
	public void setSystem(String theSystem) {
		Validate.notBlank(theSystem, "System URL cannot be blank");
		Validate.isTrue(theSystem.length() <= 500, "System URL cannot be longer than 500 characters: %s", theSystem);
		mySystem = theSystem;

		Hasher hasher = Hashing.murmur3_128(0).newHasher();
		hasher.putBytes(theSystem.getBytes(StandardCharsets.UTF_8));
		myPid = hasher.hash().asLong();
	}
}
