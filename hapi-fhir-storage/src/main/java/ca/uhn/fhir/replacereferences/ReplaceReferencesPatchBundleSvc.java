/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;

import java.util.List;
import java.util.UUID;

import static ca.uhn.fhir.jpa.patch.FhirPatch.OPERATION_REPLACE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_OPERATION;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_PATH;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_TYPE;
import static ca.uhn.fhir.jpa.patch.FhirPatch.PARAMETER_VALUE;

public class ReplaceReferencesPatchBundleSvc {

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	public ReplaceReferencesPatchBundleSvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Build a bundle of PATCH entries that make the requested reference updates
	 * @param theReplaceReferencesRequest source and target for reference switch
	 * @param theResourceIds the ids of the resource to create the patch entries for (they will all have references to the source resource)
	 * @param theRequestDetails
	 * @return
	 */
	public Bundle patchReferencingResources(
			ReplaceReferencesRequest theReplaceReferencesRequest,
			List<IdDt> theResourceIds,
			RequestDetails theRequestDetails) {
		Bundle patchBundle = buildPatchBundle(theReplaceReferencesRequest, theResourceIds, theRequestDetails);
		IFhirSystemDao<Bundle, Meta> systemDao = myDaoRegistry.getSystemDao();
		Bundle result = systemDao.transaction(theRequestDetails, patchBundle);

		result.setId(UUID.randomUUID().toString());
		return result;
	}

	private Bundle buildPatchBundle(
			ReplaceReferencesRequest theReplaceReferencesRequest,
			List<IdDt> theResourceIds,
			RequestDetails theRequestDetails) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		theResourceIds.forEach(referencingResourceId -> {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(referencingResourceId.getResourceType());
			IBaseResource resource = dao.read(referencingResourceId, theRequestDetails);
			Parameters patchParams = buildPatchParams(theReplaceReferencesRequest, resource);
			IIdType resourceId = resource.getIdElement();
			bundleBuilder.addTransactionFhirPatchEntry(resourceId, patchParams);
		});
		return bundleBuilder.getBundleTyped();
	}

	private @Nonnull Parameters buildPatchParams(
			ReplaceReferencesRequest theReplaceReferencesRequest, IBaseResource referencingResource) {
		Parameters params = new Parameters();

		myFhirContext.newTerser().getAllResourceReferences(referencingResource).stream()
				.filter(refInfo -> matches(
						refInfo,
						theReplaceReferencesRequest.sourceId)) // We only care about references to our source resource
				.map(refInfo -> createReplaceReferencePatchOperation(
						getFhirPathForPatch(referencingResource, refInfo),
						new Reference(theReplaceReferencesRequest.targetId.getValueAsString())))
				.forEach(params::addParameter); // Add each operation to parameters
		return params;
	}

	private static boolean matches(ResourceReferenceInfo refInfo, IIdType theSourceId) {
		return refInfo.getResourceReference()
				.getReferenceElement()
				.toUnqualified()
				.getValueAsString()
				.equals(theSourceId.getValueAsString());
	}

	private String getFhirPathForPatch(IBaseResource theReferencingResource, ResourceReferenceInfo theRefInfo) {
		// construct the path to the element containing the reference in the resource, e.g. "Observation.subject"
		String path = theReferencingResource.fhirType() + "." + theRefInfo.getName();
		// check the allowed cardinality of the element containing the reference
		int maxCardinality = myFhirContext
				.newTerser()
				.getDefinition(theReferencingResource.getClass(), path)
				.getMax();
		if (maxCardinality != 1) {
			// if the element allows high cardinality, specify the exact reference to replace by appending a where
			// filter to the path. If we don't do this, all the existing references in the element would be lost as a
			// result of getting replaced with the new reference, and that is not the behaviour we want.
			// e.g. "Observation.performer.where(reference='Practitioner/123')"
			return String.format(
					"%s.where(reference='%s')",
					path,
					theRefInfo.getResourceReference().getReferenceElement().getValueAsString());
		}
		// the element allows max cardinality of 1, so the whole element can be safely replaced
		return path;
	}

	@Nonnull
	private Parameters.ParametersParameterComponent createReplaceReferencePatchOperation(
			String thePath, Type theValue) {

		Parameters.ParametersParameterComponent operation = new Parameters.ParametersParameterComponent();
		operation.setName(PARAMETER_OPERATION);
		operation.addPart().setName(PARAMETER_TYPE).setValue(new CodeType(OPERATION_REPLACE));
		operation.addPart().setName(PARAMETER_PATH).setValue(new StringType(thePath));
		operation.addPart().setName(PARAMETER_VALUE).setValue(theValue);
		return operation;
	}
}
