/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.merge.MergeJobParameters;
import ca.uhn.fhir.batch2.jobs.replacereferences.ProvenanceAgentJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

/**
 * See <a href="https://build.fhir.org/patient-operation-merge.html">Patient $merge spec</a>
 */
public class MergeOperationInputParameters extends MergeOperationsCommonInputParameters {

	private boolean myPreview;
	private boolean myDeleteSource;
	private IBaseResource myResultResource;
	private List<IProvenanceAgent> myProvenanceAgents;
	private boolean myCreateProvenance = true;
	private IBaseResource myOriginalInputParameters;

	protected MergeOperationInputParameters(int theResourceLimit) {
		super(theResourceLimit);
	}

	public boolean getPreview() {
		return myPreview;
	}

	public void setPreview(boolean thePreview) {
		this.myPreview = thePreview;
	}

	public boolean getDeleteSource() {
		return myDeleteSource;
	}

	public void setDeleteSource(boolean theDeleteSource) {
		this.myDeleteSource = theDeleteSource;
	}

	public IBaseResource getResultResource() {
		return myResultResource;
	}

	public void setResultResource(IBaseResource theResultResource) {
		this.myResultResource = theResultResource;
	}

	public boolean getCreateProvenance() {
		return myCreateProvenance;
	}

	public void setCreateProvenance(boolean theCreateProvenance) {
		this.myCreateProvenance = theCreateProvenance;
	}

	public List<IProvenanceAgent> getProvenanceAgents() {
		return myProvenanceAgents;
	}

	public void setProvenanceAgents(List<IProvenanceAgent> theProvenanceAgents) {
		this.myProvenanceAgents = theProvenanceAgents;
	}

	public IBaseResource getOriginalInputParameters() {
		return myOriginalInputParameters;
	}

	public void setOriginalInputParameters(IBaseResource theOriginalInputParameters) {
		myOriginalInputParameters = theOriginalInputParameters;
	}

	public MergeJobParameters asMergeJobParameters(
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings,
			Patient theSourceResource,
			Patient theTargetResource,
			RequestPartitionId thePartitionId) {
		MergeJobParameters retval = new MergeJobParameters();
		retval.setOriginalInputParameters(
				theFhirContext.newJsonParser().encodeResourceToString(myOriginalInputParameters));
		retval.setBatchSize(theStorageSettings.getDefaultTransactionEntriesForWrite());
		retval.setSourceId(new FhirIdJson(theSourceResource.getIdElement().toVersionless()));
		retval.setTargetId(new FhirIdJson(theTargetResource.getIdElement().toVersionless()));
		retval.setPartitionId(thePartitionId);
		retval.setProvenanceAgents(ProvenanceAgentJson.from(myProvenanceAgents, theFhirContext));
		retval.setCreateProvenance(myCreateProvenance);
		return retval;
	}

	/**
	 * Static factory method to create MergeOperationInputParameters from operation parameters.
	 * Version-agnostic - uses IBase for identifiers to support all FHIR versions.
	 *
	 * @param theSourceIdentifiers List of source resource identifiers (IBase to support all FHIR versions)
	 * @param theTargetIdentifiers List of target resource identifiers (IBase to support all FHIR versions)
	 * @param theSourceResource Reference to the source resource
	 * @param theTargetResource Reference to the target resource
	 * @param thePreview Preview mode flag
	 * @param theDeleteSource Delete source flag
	 * @param theResultResource Result resource template
	 * @param theResourceLimit Resource processing limit
	 * @param theProvenanceAgents Provenance agents for tracking
	 * @param theOriginalInputParameters Original input parameters for audit trail
	 * @return Populated MergeOperationInputParameters object
	 */
	public static MergeOperationInputParameters from(
			List<IBase> theSourceIdentifiers,
			List<IBase> theTargetIdentifiers,
			IBaseReference theSourceResource,
			IBaseReference theTargetResource,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultResource,
			int theResourceLimit,
			List<IProvenanceAgent> theProvenanceAgents,
			IBaseResource theOriginalInputParameters) {

		MergeOperationInputParameters parameters = new MergeOperationInputParameters(theResourceLimit);

		// Set common parameters (identifiers and resource references)
		MergeOperationsCommonInputParameters.setParameters(
				parameters, theSourceIdentifiers, theTargetIdentifiers, theSourceResource, theTargetResource);

		// Set merge-specific parameters
		parameters.setPreview(thePreview != null && thePreview.getValue());
		parameters.setDeleteSource(theDeleteSource != null && theDeleteSource.getValue());

		if (theResultResource != null) {
			// Pass in a copy of the result resource as we don't want it to be modified.
			// It will be returned back to the client as part of the response.
			parameters.setResultResource(theResultResource);
		}

		parameters.setProvenanceAgents(theProvenanceAgents);
		parameters.setOriginalInputParameters(theOriginalInputParameters);

		return parameters;
	}
}
