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

public class IpsContext {

	private final IBaseResource mySubject;
	private final IIdType mySubjectId;

	/**
	 * Constructor
	 *
	 * @param theSubject   The subject Patient resource for the IPS being generated
	 * @param theSubjectId The original ID for {@literal theSubject}, which may not match the current ID if {@link IIpsGenerationStrategy#massageResourceId(IpsContext, IBaseResource)} has modified it
	 */
	public IpsContext(IBaseResource theSubject, IIdType theSubjectId) {
		mySubject = theSubject;
		mySubjectId = theSubjectId;
	}

	/**
	 * Returns the subject Patient resource for the IPS being generated. Note that
	 * the {@literal Resource.id} value may not match the ID of the resource stored in the
	 * repository if {@link IIpsGenerationStrategy#massageResourceId(IpsContext, IBaseResource)} has
	 * returned a different ID. Use {@link #getSubjectId()} if you want the originally stored ID.
	 *
	 * @see #getSubjectId() for the originally stored ID.
	 */
	public IBaseResource getSubject() {
		return mySubject;
	}

	/**
	 * Returns the ID of the subject for the given IPS. This value should match the
	 * ID which was originally fetched from the repository.
	 */
	public IIdType getSubjectId() {
		return mySubjectId;
	}

	public <T extends IBaseResource> IpsSectionContext<T> newSectionContext(
			Section theSection, Class<T> theResourceType) {
		return new IpsSectionContext<>(mySubject, mySubjectId, theSection, theResourceType);
	}
}
