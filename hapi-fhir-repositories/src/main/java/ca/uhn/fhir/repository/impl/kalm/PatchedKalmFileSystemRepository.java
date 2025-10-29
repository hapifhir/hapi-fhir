/*-
 * #%L
 * HAPI FHIR - Repository implementations and utilities
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
package ca.uhn.fhir.repository.impl.kalm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.NaiveRepositoryTransactionProcessor;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.fhir.utility.repository.ig.IgRepository;

import java.nio.file.Path;
import java.util.Map;

/**
 * A patched version of the IgRepository that adds support for transaction bundle processing.
 * The current 3.23 version from cqf-fhir-utility has a nop implementation of transaction().
 * TODO Delete this once the cfq implementation adds transaction support
 */
public class PatchedKalmFileSystemRepository extends IgRepository {
	public PatchedKalmFileSystemRepository(FhirContext theFhirContext, Path theRoot) {
		super(theFhirContext, theRoot);
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theTransactionBundle, Map<String, String> theHeaders) {
		return new NaiveRepositoryTransactionProcessor(this).processTransaction(theTransactionBundle);
	}
}
