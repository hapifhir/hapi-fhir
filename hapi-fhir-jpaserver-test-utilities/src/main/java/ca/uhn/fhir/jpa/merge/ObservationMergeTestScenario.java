// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;

/**
 * Merge test scenario implementation for Observation resources.
 *
 * <p><b>Key difference from Practitioner</b>: Observation resources do NOT have
 * an "active" field, so merge operations should not attempt to set active=false
 * on the source resource.</p>
 */
public class ObservationMergeTestScenario extends AbstractMergeTestScenario<Observation> {

	/**
	 * Create a new Observation merge test scenario.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 * @param theHelper Helper for merge operations
	 */
	public ObservationMergeTestScenario(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull MergeOperationTestHelper theHelper) {

		super(theDaoRegistry, theFhirContext, theLinkServiceFactory, theRequestDetails, theHelper);
	}

	@Nonnull
	public String getResourceTypeName() {
		return "Observation";
	}

	@Nonnull
	public Class<Observation> getResourceClass() {
		return Observation.class;
	}

	@Nonnull
	public Observation createResource(@Nonnull List<Identifier> theIdentifiers) {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.getCode().addCoding().setSystem("http://loinc.org").setCode("test-code");
		obs.setIdentifier(theIdentifiers);
		return obs;
	}

	@Nonnull
	public IBaseResource createReferencingResource(@Nonnull String theResourceType, @Nonnull IIdType theReferencedId) {

		switch (theResourceType) {
			case "DiagnosticReport":
				DiagnosticReport report = new DiagnosticReport();
				report.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
				report.getCode().addCoding().setSystem("http://loinc.org").setCode("report-code");
				report.addResult(new Reference(theReferencedId));
				return report;

			default:
				throw new IllegalArgumentException(
						"Unsupported resource type for Observation references: " + theResourceType);
		}
	}

	@Nonnull
	@Override
	public IBaseResource createReferencingResource() {
		return createReferencingResource("DiagnosticReport", getVersionlessSourceId());
	}

	public AbstractMergeTestScenario<Observation> withOneReferencingResource() {
		return withReferences(List.of(ReferencingTestResourceType.of("DiagnosticReport", 1)));
	}

	public AbstractMergeTestScenario<Observation> withMultipleReferencingResources() {
		return withMultipleReferencingResources(3);
	}

	public AbstractMergeTestScenario<Observation> withMultipleReferencingResources(int theCount) {
		return withReferences(List.of(ReferencingTestResourceType.of("DiagnosticReport", theCount)));
	}

	@Override
	protected void assertActiveFieldIfSupported(@Nonnull Observation theResource, boolean theExpectedValue) {
		// Observation does NOT have an active field - no validation needed
	}
}
