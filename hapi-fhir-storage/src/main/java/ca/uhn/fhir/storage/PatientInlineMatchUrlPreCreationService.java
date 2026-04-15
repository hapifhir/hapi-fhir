/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.auth.CompartmentSearchParameterModifications;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This service processes a FHIR transaction bundle and looks for non-Patient resources
 * that have patient-compartment references in the form of inline match URLs
 * (e.g. {@code Patient?identifier=http://system|value}). For each unique inline match URL found,
 * it adds a conditional-create Patient entry to the bundle with a matching identifier.
 * <p>
 * The new Patient entries are inserted at the beginning of the bundle so that they
 * are processed before the resources that reference them.
 * <p>
 * This ensures that when the {@link ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor}
 * tries to determine the partition for compartment resources, a Patient with the referenced
 * identifier will exist (or be created) so that partition resolution succeeds.
 */
// Created by claude-opus-4-6
public class PatientInlineMatchUrlPreCreationService {

	private static final String PATIENT_STR = "Patient";
	private static final String SP_IDENTIFIER = "identifier";

	private final FhirContext myFhirContext;
	private final MatchUrlService myMatchUrlService;

	public PatientInlineMatchUrlPreCreationService(FhirContext theFhirContext, MatchUrlService theMatchUrlService) {
		myFhirContext = theFhirContext;
		myMatchUrlService = theMatchUrlService;
	}

	/**
	 * Scans a transaction bundle for non-Patient resources that have patient-compartment
	 * references in the form of inline match URLs, and inserts conditional-create Patient
	 * entries at the beginning of the bundle for each unique match URL found.
	 *
	 * @param theBundle the transaction bundle to process
	 */
	public void addConditionalCreateEntriesForInlineMatchUrls(IBaseBundle theBundle) {
		FhirTerser terser = myFhirContext.newTerser();

		Set<String> inlineMatchUrls = new LinkedHashSet<>();

		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myFhirContext, theBundle);
		for (BundleEntryParts entry : entries) {
			IBaseResource resource = entry.getResource();
			if (resource == null) {
				continue;
			}

			String resourceType = myFhirContext.getResourceType(resource);
			if (PATIENT_STR.equals(resourceType)) {
				continue;
			}

			List<IBaseReference> compartmentRefs = terser.getCompartmentReferencesForResource(
							PATIENT_STR, resource, new CompartmentSearchParameterModifications())
					.toList();

			for (IBaseReference ref : compartmentRefs) {
				String refValue = ref.getReferenceElement().getValue();
				if (refValue != null && refValue.startsWith(PATIENT_STR + "?")) {
					inlineMatchUrls.add(refValue);
				}
			}
		}

		if (inlineMatchUrls.isEmpty()) {
			return;
		}

		RuntimeResourceDefinition patientDefinition = myFhirContext.getResourceDefinition(PATIENT_STR);

		// Build new Patient entries using a temporary bundle
		BundleBuilder tempBundleBuilder = new BundleBuilder(myFhirContext);
		for (String matchUrl : inlineMatchUrls) {
			TokenParam identifierParam = parseAndValidateIdentifier(matchUrl, patientDefinition);
			IBaseResource patient = buildPatientWithIdentifier(identifierParam);
			tempBundleBuilder.addTransactionCreateEntry(patient).conditional(matchUrl);
		}

		// Use the child definition accessor to get the live backing list, then prepend new entries
		BaseRuntimeChildDefinition entryChild =
				myFhirContext.getResourceDefinition("Bundle").getChildByName("entry");
		List<IBase> bundleEntryList = entryChild.getAccessor().getValues(theBundle);
		List<IBase> newEntries = entryChild.getAccessor().getValues(tempBundleBuilder.getBundle());
		bundleEntryList.addAll(0, newEntries);
	}

	private TokenParam parseAndValidateIdentifier(String theMatchUrl, RuntimeResourceDefinition thePatientDefinition) {
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theMatchUrl, thePatientDefinition);

		List<List<IQueryParameterType>> identifierParamsAnd = searchParameterMap.get(SP_IDENTIFIER);

		if (!isSingleAndOrValue(identifierParamsAnd)) {
			throw new PreconditionFailedException(Msg.code(2583)
					+ "Inline Patient match URL must contain exactly one identifier parameter: "
					+ UrlUtil.sanitizeUrlPart(theMatchUrl));
		}

		if (searchParameterMap.size() > 1) {
			throw new PreconditionFailedException(Msg.code(2584)
					+ "Can not include multiple parameters in inline Patient match URL: "
					+ UrlUtil.sanitizeUrlPart(theMatchUrl));
		}

		TokenParam identifierParam = (TokenParam) identifierParamsAnd.get(0).get(0);

		if (identifierParam.getModifier() != null
				|| identifierParam.getMissing() != null
				|| identifierParam.isMdmExpand()) {
			throw new PreconditionFailedException(Msg.code(2585)
					+ "Inline Patient match URL identifier must not use modifiers: "
					+ UrlUtil.sanitizeUrlPart(theMatchUrl));
		}

		if (isBlank(identifierParam.getSystem()) || isBlank(identifierParam.getValue())) {
			throw new PreconditionFailedException(Msg.code(2586)
					+ "Inline Patient match URL identifier must have both a system and a value: "
					+ UrlUtil.sanitizeUrlPart(theMatchUrl));
		}

		return identifierParam;
	}

	private IBaseResource buildPatientWithIdentifier(TokenParam theIdentifierParam) {
		FhirTerser terser = myFhirContext.newTerser();

		IBaseResource patient = myFhirContext.getResourceDefinition(PATIENT_STR).newInstance();

		IBase identifier = terser.addElement(patient, "identifier");
		terser.setElement(identifier, "system", theIdentifierParam.getSystem());
		terser.setElement(identifier, "value", theIdentifierParam.getValue());

		return patient;
	}

	private static boolean isSingleAndOrValue(List<List<IQueryParameterType>> theParamsAnd) {
		return theParamsAnd != null
				&& theParamsAnd.size() == 1
				&& theParamsAnd.get(0) != null
				&& theParamsAnd.get(0).size() == 1;
	}
}
