/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
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
package ca.uhn.fhir.jpa.ips.api;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class IpsSectionContext extends IpsContext {

	private final Section mySection;
	private final String myResourceType;

	IpsSectionContext(IBaseResource theSubject, IIdType theSubjectId, Section theSection, String theResourceType) {
		super(theSubject, theSubjectId);
		mySection = theSection;
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Section getSection() {
		return mySection;
	}
}
