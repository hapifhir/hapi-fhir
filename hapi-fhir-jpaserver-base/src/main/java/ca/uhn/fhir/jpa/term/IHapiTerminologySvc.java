package ca.uhn.fhir.jpa.term;

import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.Set;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.method.RequestDetails;

public interface IHapiTerminologySvc {

	Set<TermConcept> findCodesAbove(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersionPid, String theCode);

	List<VersionIndependentConcept> findCodesBelow(String theSystem, String theCode);

	void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, TermCodeSystemVersion theCodeSytemVersion);

	public boolean supportsSystem(String theCodeSystem);

	List<VersionIndependentConcept> expandValueSet(String theValueSet);

	List<VersionIndependentConcept> findCodesAbove(String theSystem, String theCode);

	void storeNewCodeSystemVersion(String theSystem, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails);

	List<TermConcept> findCodes(String theSystem);

	void saveDeferred();

	/**
	 * This is mostly for unit tests - we can disable processing of deferred concepts
	 * by changing this flag
	 */
	void setProcessDeferred(boolean theProcessDeferred);

}
