/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.api.ProvenanceAgent;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TerserUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseReference;

public class ProvenanceAgentJson {
	@JsonProperty("who")
	private String myWho;

	@JsonProperty("onBehalfOf")
	private String myOnBehalfOf;

	public String getWho() {
		return myWho;
	}

	public void setWho(String theWho) {
		this.myWho = theWho;
	}

	public String getOnBehalfOf() {
		return myOnBehalfOf;
	}

	public void setOnBehalfOf(String theOnBehalfOf) {
		this.myOnBehalfOf = theOnBehalfOf;
	}

	public static ProvenanceAgentJson from(IProvenanceAgent theProvenanceAgent, FhirContext theFhirContext) {
		if (theProvenanceAgent == null) {
			return null;
		}
		IParser parser = theFhirContext.newJsonParser();
		ProvenanceAgentJson retVal = new ProvenanceAgentJson();
		if (theProvenanceAgent.getWho() != null) {
			retVal.myWho = parser.encodeToString(theProvenanceAgent.getWho());
		}
		if (theProvenanceAgent.getOnBehalfOf() != null) {
			retVal.myOnBehalfOf = parser.encodeToString(theProvenanceAgent.getOnBehalfOf());
		}
		return retVal;
	}

	public static IProvenanceAgent toIProvenanceAgent(
			@Nullable ProvenanceAgentJson theProvenanceAgentJson, FhirContext theFhirContext) {
		if (theProvenanceAgentJson == null) {
			return null;
		}
		IParser parser = theFhirContext.newJsonParser();
		ProvenanceAgent provAgent = new ProvenanceAgent();
		if (theProvenanceAgentJson.myWho != null) {
			IBaseReference who = TerserUtil.newElement(theFhirContext, "Reference");
			parser.parseInto(theProvenanceAgentJson.myWho, who);
			provAgent.setWho(who);
		}
		if (theProvenanceAgentJson.myOnBehalfOf != null) {
			IBaseReference onBehalfOf = TerserUtil.newElement(theFhirContext, "Reference");
			parser.parseInto(theProvenanceAgentJson.myOnBehalfOf, onBehalfOf);
			provAgent.setOnBehalfOf(onBehalfOf);
		}
		return provAgent;
	}
}
