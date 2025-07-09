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
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

/**
 * See <a href="https://build.fhir.org/patient-operation-merge.html">Patient $merge spec</a>
 */
public abstract class BaseMergeOperationInputParameters extends BaseMergeOperationsCommonInputParameters {

	private boolean myPreview;
	private boolean myDeleteSource;
	private IBaseResource myResultResource;
	private final int myResourceLimit;
	private List<IProvenanceAgent> myProvenanceAgents;
	private boolean myCreateProvenance = true;

	public abstract String getResultResourceParameterName();

	protected BaseMergeOperationInputParameters(int theResourceLimit) {
		myResourceLimit = theResourceLimit;
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

	public int getResourceLimit() {
		return myResourceLimit;
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

	public MergeJobParameters asMergeJobParameters(
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings,
			Patient theSourceResource,
			Patient theTargetResource,
			RequestPartitionId thePartitionId) {
		MergeJobParameters retval = new MergeJobParameters();
		if (getResultResource() != null) {
			retval.setResultResource(theFhirContext.newJsonParser().encodeResourceToString(getResultResource()));
		}
		retval.setDeleteSource(getDeleteSource());
		retval.setBatchSize(theStorageSettings.getDefaultTransactionEntriesForWrite());
		retval.setSourceId(new FhirIdJson(theSourceResource.getIdElement().toVersionless()));
		retval.setTargetId(new FhirIdJson(theTargetResource.getIdElement().toVersionless()));
		retval.setPartitionId(thePartitionId);
		retval.setProvenanceAgents(ProvenanceAgentJson.from(myProvenanceAgents, theFhirContext));
		retval.setCreateProvenance(myCreateProvenance);
		return retval;
	}
}
