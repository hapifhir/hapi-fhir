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
import ca.uhn.fhir.jpa.bulk.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
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

	@Value("#{jobParameters['" + BulkExportJobConfig.EXPAND_MDM_PARAMETER+ "'] ?: false}")
	private boolean myMdmEnabled;

	private RuntimeSearchParam myRuntimeSearchParam;
	private String myPatientFhirPath;
	private IFhirPath myFhirPath;

	@Override
	public List<IBaseResource> process(List<IBaseResource> theIBaseResources) throws Exception {
		if (myMdmEnabled) {
			if (myRuntimeSearchParam == null) {
				myRuntimeSearchParam = SearchParameterUtil.getPatientSearchParamForResourceType(myContext, myResourceType);
			}
			String path = runtimeSearchParamFhirPath();
			theIBaseResources.forEach(iBaseResource -> annotateClinicalResourceWithRelatedGoldenResourcePatient(path, iBaseResource));
		}

		return theIBaseResources;
	}

	private void annotateClinicalResourceWithRelatedGoldenResourcePatient(String path, IBaseResource iBaseResource) {
		Optional<String> patientReference = getPatientReference(path, iBaseResource);
		if (patientReference.isPresent()) {
			addGoldenResourceExtension(iBaseResource, patientReference.get());
		} else {
			ourLog.warn("Failed to find the patient reference information for resource {}", iBaseResource);
		}
	}

	private Optional<String> getPatientReference(String path, IBaseResource iBaseResource) {
		//In the case of patient, we will just use the raw ID.
		if (myResourceType.equalsIgnoreCase("Patient")) {
			return Optional.of(iBaseResource.getIdElement().getIdPart());
		//Otherwise, we will perform evaluation of the fhirPath.
		} else {
			Optional<IBaseReference> optionalReference = getFhirParser().evaluateFirst(iBaseResource, path, IBaseReference.class);
			return optionalReference.map(theIBaseReference -> theIBaseReference.getReferenceElement().getIdPart());
		}
	}

	private void addGoldenResourceExtension(IBaseResource iBaseResource, String sourceResourceId) {
		String goldenResourceId = myMdmExpansionCacheSvc.getGoldenResourceId(sourceResourceId);
		if (!StringUtils.isBlank(goldenResourceId)) {
			IBaseExtension<?, ?> extension = ExtensionUtil.getOrCreateExtension(iBaseResource, ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
			ExtensionUtil.setExtension(myContext, extension, "reference", prefixPatient(goldenResourceId));
		}
	}

	private String prefixPatient(String theResourceId) {
		return "Patient/" + theResourceId;
	}

	private IFhirPath getFhirParser() {
		if (myFhirPath == null) {
			myFhirPath = myContext.newFhirPath();
		}
		return myFhirPath;
	}

	private String runtimeSearchParamFhirPath() {
		if (myPatientFhirPath == null) {
			myRuntimeSearchParam = SearchParameterUtil.getPatientSearchParamForResourceType(myContext, myResourceType);
			myPatientFhirPath = myRuntimeSearchParam.getPath();
			//Yes this is a stupid hack, but by default this runtime search param will return stuff like
			// Observation.subject.where(resolve() is Patient) which unfortunately our FHIRpath evaluator doesn't play nicely with.
			if (myPatientFhirPath.contains(".where")) {
				myPatientFhirPath = myPatientFhirPath.substring(0, myPatientFhirPath.indexOf(".where"));
			}
		}
		return myPatientFhirPath;
	}
}
