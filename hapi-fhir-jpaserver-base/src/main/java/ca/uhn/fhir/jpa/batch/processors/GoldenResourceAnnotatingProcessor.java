package ca.uhn.fhir.jpa.batch.processors;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

/**
 * Reusable Item Processor which converts ResourcePersistentIds to their IBaseResources
 */
public class GoldenResourceAnnotatingProcessor implements ItemProcessor<List<IBaseResource>, List<IBaseResource>> {
	 private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final String ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL = "https://hapifhir.org/associated-patient-golden-resource/";

	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	private RuntimeSearchParam myRuntimeSearchParam;
	private IFhirPath myFhirPath;




	@Override
	public List<IBaseResource> process(List<IBaseResource> theIBaseResources) throws Exception {
		if (myRuntimeSearchParam == null) {
			myRuntimeSearchParam = SearchParameterUtil.getPatientSearchParamForResourceType(myContext, myResourceType);
		}
		String path = runtimeSearchParamFhirPath();
		theIBaseResources
			.forEach(iBaseResource -> annotateResourceWithRelatedGoldenResourcePatient(path, iBaseResource));
		return theIBaseResources;
	}

	private void annotateResourceWithRelatedGoldenResourcePatient(String path, IBaseResource iBaseResource) {
		Optional<IBaseReference> evaluate = getFhirParser().evaluateFirst(iBaseResource, path, IBaseReference.class);
		if (evaluate.isPresent()) {
			String sourceResourceId = evaluate.get().getReferenceElement().getIdPart();
			ExtensionUtil.setExtension(myContext, iBaseResource, ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL, myMdmExpansionCacheSvc.getGoldenResourceId(sourceResourceId));
		} else {
			ourLog.warn("Failed to find the patient compartment information for resource {}", iBaseResource);
		}
	}

	private IFhirPath getFhirParser() {
		if (myFhirPath == null) {
			myFhirPath = myContext.newFhirPath();
		}
		return myFhirPath;
	}

	private String runtimeSearchParamFhirPath() {
		if (myRuntimeSearchParam == null) {
			myRuntimeSearchParam = SearchParameterUtil.getPatientSearchParamForResourceType(myContext, myResourceType);
		}
		return myRuntimeSearchParam.getPath();
	}
}
