/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.term.models;

public class CodeSystemConceptsDeleteResult {

	private int myDeletedLinks;

	private int myDeletedProperties;

	private int myDeletedDesignations;

	private int myCodeSystemConceptDelete;

	public int getDeletedLinks() {
		return myDeletedLinks;
	}

	public void setDeletedLinks(int theDeletedLinks) {
		myDeletedLinks = theDeletedLinks;
	}

	public int getDeletedProperties() {
		return myDeletedProperties;
	}

	public void setDeletedProperties(int theDeletedProperties) {
		myDeletedProperties = theDeletedProperties;
	}

	public int getDeletedDesignations() {
		return myDeletedDesignations;
	}

	public void setDeletedDesignations(int theDeletedDesignations) {
		myDeletedDesignations = theDeletedDesignations;
	}

	public int getCodeSystemConceptDelete() {
		return myCodeSystemConceptDelete;
	}

	public void setCodeSystemConceptDelete(int theCodeSystemConceptDelete) {
		myCodeSystemConceptDelete = theCodeSystemConceptDelete;
	}
}
