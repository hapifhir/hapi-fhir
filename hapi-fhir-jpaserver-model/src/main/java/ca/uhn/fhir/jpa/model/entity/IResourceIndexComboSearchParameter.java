/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Provides a common interface used to extract Combo Unique ({@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique})
 * and Combo Non-Unique ({@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique}) SearchParameters
 */
public interface IResourceIndexComboSearchParameter {

	IIdType getSearchParameterId();

	void setSearchParameterId(IIdType theSearchParameterId);

	String getIndexString();

	ResourceTable getResource();

	void setResource(ResourceTable theResourceTable);
}
