package ca.uhn.fhir.jpa.batch.processor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES;

/**
 * Reusable Item Processor which attaches an extension to any outgoing resource. This extension will contain a resource
 * reference to the golden resource patient of the given resources' patient. (e.g. Observation.subject, Immunization.patient, etc)
 */
public class GoldenResourceAnnotatingProcessor implements ItemProcessor<List<IBaseResource>, List<IBaseResource>> {
	 private static final Logger ourLog = Logs.getBatchTroubleshootingLog();


	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	@Value("#{jobParameters['" + BatchConstants.EXPAND_MDM_PARAMETER + "'] ?: false}")
	private boolean myMdmEnabled;


	private RuntimeSearchParam myRuntimeSearchParam;

	private String myPatientFhirPath;

	private IFhirPath myFhirPath;

	private void populateRuntimeSearchParam() {
		Optional<RuntimeSearchParam> oPatientSearchParam= SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, myResourceType);
		if (!oPatientSearchParam.isPresent()) {
			String errorMessage = String.format("[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!", myResourceType);
			throw new IllegalArgumentException(Msg.code(1279) + errorMessage);
		} else {
			myRuntimeSearchParam = oPatientSearchParam.get();
		}
	}

	@Override
	public List<IBaseResource> process(@NonNull List<IBaseResource> theIBaseResources) throws Exception {
		if (shouldAnnotateResource()) {
			lazyLoadSearchParamsAndFhirPath();
			theIBaseResources.forEach(this::annotateBackwardsReferences);
		}
		return theIBaseResources;
	}

	private void lazyLoadSearchParamsAndFhirPath() {
		if (myRuntimeSearchParam == null) {
			populateRuntimeSearchParam();
		}
		if (myPatientFhirPath == null) {
			populatePatientFhirPath();
		}
	}

	/**
	 * If the resource is added via a forward-reference from a patient, e.g. Patient.managingOrganization, we have no way to fetch the patient at this point in time. 
	 * This is a shortcoming of including the forward reference types in a Group/Patient bulk export.
	 * 
	 * @return true if the resource should be annotated with the golden resource patient reference
	 */
	private boolean shouldAnnotateResource() {
		return myMdmEnabled && !PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(myResourceType);
	}

	private void annotateBackwardsReferences(IBaseResource iBaseResource) {
		Optional<String> patientReference = getPatientReference(iBaseResource);
		if (patientReference.isPresent()) {
			addGoldenResourceExtension(iBaseResource, patientReference.get());
		} else {
			ourLog.error("Failed to find the patient reference information for resource {}. This is a bug, " +
				"as all resources which can be exported via Group Bulk Export must reference a patient.", iBaseResource);
		}
	}

	private Optional<String> getPatientReference(IBaseResource iBaseResource) {
		if (myResourceType.equalsIgnoreCase("Patient")) {
			return Optional.of(iBaseResource.getIdElement().getIdPart());
		} else {
			Optional<IBaseReference> optionalReference = getFhirParser().evaluateFirst(iBaseResource, myPatientFhirPath, IBaseReference.class);
			if (optionalReference.isPresent()) {
				return optionalReference.map(theIBaseReference -> theIBaseReference.getReferenceElement().getIdPart());
			} else {
				return Optional.empty();
			}
		}
	}

	private void addGoldenResourceExtension(IBaseResource iBaseResource, String sourceResourceId) {
		String goldenResourceId = myMdmExpansionCacheSvc.getGoldenResourceId(sourceResourceId);
		IBaseExtension<?, ?> extension = ExtensionUtil.getOrCreateExtension(iBaseResource, HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
		if (!StringUtils.isBlank(goldenResourceId)) {
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

	private String populatePatientFhirPath() {
		if (myPatientFhirPath == null) {
			myPatientFhirPath = myRuntimeSearchParam.getPath();
			// GGG: Yes this is a stupid hack, but by default this runtime search param will return stuff like
			// Observation.subject.where(resolve() is Patient) which unfortunately our FHIRpath evaluator doesn't play nicely with
			// our FHIRPath evaluator.
			if (myPatientFhirPath.contains(".where")) {
				myPatientFhirPath = myPatientFhirPath.substring(0, myPatientFhirPath.indexOf(".where"));
			}
		}
		return myPatientFhirPath;
	}
}
