package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MdmControllerHelper {

	private final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;
	private final IMdmSettings myMdmSettings;
	private final MessageHelper myMessageHelper;
	private final IMdmMatchFinderSvc myMdmMatchFinderSvc;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	public MdmControllerHelper(FhirContext theFhirContext,
										IResourceLoader theResourceLoader,
										IMdmMatchFinderSvc theMdmMatchFinderSvc,
										IMdmSettings theMdmSettings,
										MessageHelper theMessageHelper,
										IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
		myMdmSettings = theMdmSettings;
		myMdmMatchFinderSvc = theMdmMatchFinderSvc;
		myMessageHelper = theMessageHelper;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	public void validateSameVersion(IAnyResource theResource, String theResourceId) {
		String storedId = theResource.getIdElement().getValue();
		if (hasVersionIdPart(theResourceId) && !storedId.equals(theResourceId)) {
			throw new ResourceVersionConflictException(Msg.code(1501) + "Requested resource " + theResourceId + " is not the latest version.  Latest version is " + storedId);
		}
	}

	private boolean hasVersionIdPart(String theId) {
		return new IdDt(theId).hasVersionIdPart();
	}

	public IAnyResource getLatestGoldenResourceFromIdOrThrowException(String theParamName, String theGoldenResourceId) {
		IdDt resourceId = MdmControllerUtil.getGoldenIdDtOrThrowException(theParamName, theGoldenResourceId);
		IAnyResource iAnyResource = loadResource(resourceId.toUnqualifiedVersionless());
		if (MdmResourceUtil.isGoldenRecord(iAnyResource)) {
			return iAnyResource;
		} else {
			throw new InvalidRequestException(Msg.code(1502) + myMessageHelper.getMessageForFailedGoldenResourceLoad(theParamName, theGoldenResourceId));
		}
	}


	public IAnyResource getLatestSourceFromIdOrThrowException(String theParamName, String theSourceId) {
		IIdType sourceId = MdmControllerUtil.getSourceIdDtOrThrowException(theParamName, theSourceId);
		return loadResource(sourceId.toUnqualifiedVersionless());
	}

	protected IAnyResource loadResource(IIdType theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

	public void validateMergeResources(IAnyResource theFromGoldenResource, IAnyResource theToGoldenResource) {
		validateIsMdmManaged(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResource);
		validateIsMdmManaged(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResource);
	}

	public String toJson(IAnyResource theAnyResource) {
		return myFhirContext.newJsonParser().encodeResourceToString(theAnyResource);
	}

	public void validateIsMdmManaged(String theName, IAnyResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		if (!myMdmSettings.isSupportedMdmType(resourceType)) {
			throw new InvalidRequestException(Msg.code(1503) + myMessageHelper.getMessageForUnsupportedResource(theName, resourceType)
			);
		}

		if (!MdmResourceUtil.isMdmManaged(theResource)) {
			throw new InvalidRequestException(Msg.code(1504) + myMessageHelper.getMessageForUnmanagedResource());
		}
	}

	/**
	 * Helper method which will return a bundle of all Matches and Possible Matches.
	 */
	public IBaseBundle getMatchesAndPossibleMatchesForResource(IAnyResource theResource, String theResourceType, RequestDetails theRequestDetails) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, theResourceType, null);
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(theResourceType, theResource, requestPartitionId);
		matches.sort(Comparator.comparing((MatchedTarget m) -> m.getMatchResult().getNormalizedScore()).reversed());

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.setBundleField("type", "searchset");
		builder.setBundleField("id", UUID.randomUUID().toString());
		builder.setMetaField("lastUpdated", builder.newPrimitive("instant", new Date()));

		IBaseBundle retVal = builder.getBundle();
		for (MatchedTarget next : matches) {
			boolean shouldKeepThisEntry = next.isMatch() || next.isPossibleMatch();
			if (!shouldKeepThisEntry) {
				continue;
			}

			IBase entry = builder.addEntry();
			builder.addToEntry(entry, "resource", next.getTarget());

			IBaseBackboneElement search = builder.addSearch(entry);
			toBundleEntrySearchComponent(builder, search, next);
		}
		return retVal;
	}

	public IBaseBackboneElement toBundleEntrySearchComponent(BundleBuilder theBuilder, IBaseBackboneElement theSearch, MatchedTarget theMatchedTarget) {
		theBuilder.setSearchField(theSearch, "mode", "match");
		double score = theMatchedTarget.getMatchResult().getNormalizedScore();
		theBuilder.setSearchField(theSearch, "score",
			theBuilder.newPrimitive("decimal", BigDecimal.valueOf(score)));

		String matchGrade = getMatchGrade(theMatchedTarget);
		IBaseDatatype codeType = (IBaseDatatype) myFhirContext.getElementDefinition("code").newInstance(matchGrade);
		IBaseExtension searchExtension = theSearch.addExtension();
		searchExtension.setUrl(MdmConstants.FIHR_STRUCTURE_DEF_MATCH_GRADE_URL_NAMESPACE);
		searchExtension.setValue(codeType);

		return theSearch;
	}

	@Nonnull
	protected String getMatchGrade(MatchedTarget theTheMatchedTarget) {
		String retVal = "probable";
		if (theTheMatchedTarget.isMatch()) {
			retVal = "certain";
		} else if (theTheMatchedTarget.isPossibleMatch()) {
			retVal = "possible";
		}
		return retVal;
	}
}
