// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import ca.uhn.fhir.merge.GenericMergeOperationInputParameterNames;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test parameter builder for generic merge operations.
 *
 * This class provides a fluent API for building merge operation input parameters
 * for test scenarios. It supports both Patient-specific and generic merge operations.
 *
 * <p>Example usage:</p>
 * <pre>
 * MergeTestParameters params = new MergeTestParameters()
 *     .sourceResource(new Reference("Practitioner/123"))
 *     .targetResource(new Reference("Practitioner/456"))
 *     .deleteSource(true)
 *     .preview(false);
 *
 * Parameters fhirParams = params.asParametersResource("Practitioner");
 * </pre>
 */
public class MergeTestParameters {

	private static final GenericMergeOperationInputParameterNames ourParameterNames =
			new GenericMergeOperationInputParameterNames();

	private Reference mySourceResource;
	private List<Identifier> mySourceResourceIdentifiers;
	private Reference myTargetResource;
	private List<Identifier> myTargetResourceIdentifiers;
	private IBaseResource myResultResource;
	private Boolean myPreview;
	private Boolean myDeleteSource;
	private Integer myBatchSize;

	/**
	 * Set the source resource reference (e.g., "Practitioner/123").
	 *
	 * @param theSourceResource The source resource reference
	 * @return This builder for chaining
	 */
	public MergeTestParameters sourceResource(Reference theSourceResource) {
		mySourceResource = theSourceResource;
		return this;
	}

	/**
	 * Set source resource identifiers for identifier-based resolution.
	 *
	 * @param theIdentifiers One or more identifiers to match the source resource
	 * @return This builder for chaining
	 */
	public MergeTestParameters sourceIdentifiers(Identifier... theIdentifiers) {
		mySourceResourceIdentifiers = Arrays.asList(theIdentifiers);
		return this;
	}

	/**
	 * Set source resource identifiers for identifier-based resolution.
	 *
	 * @param theIdentifiers List of identifiers to match the source resource
	 * @return This builder for chaining
	 */
	public MergeTestParameters sourceIdentifiers(List<Identifier> theIdentifiers) {
		mySourceResourceIdentifiers = new ArrayList<>(theIdentifiers);
		return this;
	}

	/**
	 * Set the target resource reference (e.g., "Practitioner/456").
	 *
	 * @param theTargetResource The target resource reference
	 * @return This builder for chaining
	 */
	public MergeTestParameters targetResource(Reference theTargetResource) {
		myTargetResource = theTargetResource;
		return this;
	}

	/**
	 * Set target resource identifiers for identifier-based resolution.
	 *
	 * @param theIdentifiers One or more identifiers to match the target resource
	 * @return This builder for chaining
	 */
	public MergeTestParameters targetIdentifiers(Identifier... theIdentifiers) {
		myTargetResourceIdentifiers = Arrays.asList(theIdentifiers);
		return this;
	}

	/**
	 * Set target resource identifiers for identifier-based resolution.
	 *
	 * @param theIdentifiers List of identifiers to match the target resource
	 * @return This builder for chaining
	 */
	public MergeTestParameters targetIdentifiers(List<Identifier> theIdentifiers) {
		myTargetResourceIdentifiers = new ArrayList<>(theIdentifiers);
		return this;
	}

	/**
	 * Set the result resource to use instead of merging source identifiers into target.
	 *
	 * @param theResultResource The result resource (optional)
	 * @return This builder for chaining
	 */
	public MergeTestParameters resultResource(IBaseResource theResultResource) {
		myResultResource = theResultResource;
		return this;
	}

	/**
	 * Set whether this is a preview-only merge (no actual changes made).
	 *
	 * @param thePreview true for preview mode, false for actual merge
	 * @return This builder for chaining
	 */
	public MergeTestParameters preview(boolean thePreview) {
		myPreview = thePreview;
		return this;
	}

	/**
	 * Set whether to delete the source resource after merge.
	 *
	 * @param theDeleteSource true to delete source, false to keep it
	 * @return This builder for chaining
	 */
	public MergeTestParameters deleteSource(boolean theDeleteSource) {
		myDeleteSource = theDeleteSource;
		return this;
	}

	/**
	 * Set the batch size for async processing.
	 *
	 * @param theBatchSize The batch size (optional)
	 * @return This builder for chaining
	 */
	public MergeTestParameters batchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
		return this;
	}

	/**
	 * Convert this test parameters object to a FHIR Parameters resource
	 * suitable for invoking the merge operation.
	 *
	 * @return A Parameters resource with all configured values
	 */
	public Parameters asParametersResource() {
		Parameters params = new Parameters();

		// Source resource
		if (mySourceResource != null) {
			params.addParameter()
					.setName(ourParameterNames.getSourceResourceParameterName())
					.setValue(mySourceResource);
		}

		// Source identifiers
		if (mySourceResourceIdentifiers != null && !mySourceResourceIdentifiers.isEmpty()) {
			for (Identifier identifier : mySourceResourceIdentifiers) {
				params.addParameter()
						.setName(ourParameterNames.getSourceIdentifiersParameterName())
						.setValue(identifier);
			}
		}

		// Target resource
		if (myTargetResource != null) {
			params.addParameter()
					.setName(ourParameterNames.getTargetResourceParameterName())
					.setValue(myTargetResource);
		}

		// Target identifiers
		if (myTargetResourceIdentifiers != null && !myTargetResourceIdentifiers.isEmpty()) {
			for (Identifier identifier : myTargetResourceIdentifiers) {
				params.addParameter()
						.setName(ourParameterNames.getTargetIdentifiersParameterName())
						.setValue(identifier);
			}
		}

		// Result resource
		if (myResultResource != null) {
			params.addParameter()
					.setName(ourParameterNames.getResultResourceParameterName())
					.setResource((org.hl7.fhir.r4.model.Resource) myResultResource);
		}

		// Preview
		if (myPreview != null) {
			params.addParameter().setName("preview").setValue(new org.hl7.fhir.r4.model.BooleanType(myPreview));
		}

		// Delete source
		if (myDeleteSource != null) {
			params.addParameter()
					.setName("delete-source")
					.setValue(new org.hl7.fhir.r4.model.BooleanType(myDeleteSource));
		}

		// Batch size
		if (myBatchSize != null) {
			params.addParameter().setName("batch-size").setValue(new org.hl7.fhir.r4.model.IntegerType(myBatchSize));
		}

		return params;
	}

	// Getters for test validation

	@Nullable
	public Reference getSourceResource() {
		return mySourceResource;
	}

	@Nullable
	public List<Identifier> getSourceResourceIdentifiers() {
		return mySourceResourceIdentifiers;
	}

	@Nullable
	public Reference getTargetResource() {
		return myTargetResource;
	}

	@Nullable
	public List<Identifier> getTargetResourceIdentifiers() {
		return myTargetResourceIdentifiers;
	}

	@Nullable
	public IBaseResource getResultResource() {
		return myResultResource;
	}

	@Nullable
	public Boolean getPreview() {
		return myPreview;
	}

	@Nullable
	public Boolean getDeleteSource() {
		return myDeleteSource;
	}

	@Nullable
	public Integer getBatchSize() {
		return myBatchSize;
	}
}
