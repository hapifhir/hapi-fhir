// Created by Claude Code
package ca.uhn.fhir.jpa.provider.r4;

/*-
 * #%L
 * HAPI FHIR JPA Server Test - R4
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

import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Tests for edge cases in merge operations that don't fit the generic resource pattern.
 * These tests validate error handling for unusual scenarios like resource types without identifiers.
 */
public class MergeEdgeCaseR4Test extends BaseResourceProviderR4Test {

	@Autowired
	protected Batch2JobHelper myBatch2JobHelper;

	protected MergeOperationTestHelper myHelper;

	@BeforeEach
	public void beforeMergeEdgeCase() {
		myHelper = new MergeOperationTestHelper(myClient, myBatch2JobHelper, myFhirContext);
	}

	@Test
	void testMerge_resourceTypeWithoutIdentifierElement_failsWithUnprocessableEntityException() {
		// Setup: Build parameters for OperationOutcome (resource type without identifier)
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference("OperationOutcome/source-id"))
			.targetResource(new Reference("OperationOutcome/target-id"))
			.deleteSource(false)
			.preview(false);

		// Execute and validate error
		myHelper.callMergeAndValidateException(
			"OperationOutcome",
			params,
			UnprocessableEntityException.class,
			"Merge operation cannot be performed on resource type 'OperationOutcome'",
			"does not have an 'identifier' element");
	}
}
