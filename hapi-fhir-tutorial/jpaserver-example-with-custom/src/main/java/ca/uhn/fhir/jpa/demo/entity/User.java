package ca.uhn.fhir.jpa.demo.entity;

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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@formatter:off
@Entity()
@Table(name = "CUST_USER")
//@formatter:on
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "PASSWORD", length = 200)
	private String myPassword;

	@Column(name = "USERNAME", length = 200)
	private String myUsername;

	public Long getId() {
		return myId;
	}

	public String getPassword() {
		return myPassword;
	}

	public String getUsername() {
		return myUsername;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

}
