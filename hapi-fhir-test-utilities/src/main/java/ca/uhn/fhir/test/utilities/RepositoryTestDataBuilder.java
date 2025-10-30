/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.IRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class RepositoryTestDataBuilder implements ITestDataBuilder {

	private final IRepository myRepository;

	RepositoryTestDataBuilder(IRepository theIgRepository) {
		myRepository = theIgRepository;
	}

	public static RepositoryTestDataBuilder forRepository(IRepository theRepository) {
		return new RepositoryTestDataBuilder(theRepository);
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myRepository.create(theResource).getId();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myRepository.update(theResource).getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myRepository.fhirContext();
	}
}
