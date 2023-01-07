package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.entity.ResourceSearchView;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.MetaUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.cleanProvenanceSourceUri;
import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.decodeResource;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaStorageResourceParser implements IJpaStorageResourceParser {
	public static final LenientErrorHandler LENIENT_ERROR_HANDLER = new LenientErrorHandler(false).setErrorOnInvalidValue(false);
	private static final Logger ourLog = LoggerFactory.getLogger(JpaStorageResourceParser.class);
	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Override
	public IBaseResource toResource(IBasePersistedResource theEntity, boolean theForHistoryOperation) {
		RuntimeResourceDefinition type = myContext.getResourceDefinition(theEntity.getResourceType());
		Class<? extends IBaseResource> resourceType = type.getImplementingClass();
		return toResource(resourceType, (IBaseResourceEntity) theEntity, null, theForHistoryOperation);
	}

	@Override
	public <R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation) {

		// 1. get resource, it's encoding and the tags if any
		byte[] resourceBytes;
		String resourceText;
		ResourceEncodingEnum resourceEncoding;
		@Nullable
		Collection<? extends BaseTag> tagList = Collections.emptyList();
		long version;
		String provenanceSourceUri = null;
		String provenanceRequestId = null;

		if (theEntity instanceof ResourceHistoryTable) {
			ResourceHistoryTable history = (ResourceHistoryTable) theEntity;
			resourceBytes = history.getResource();
			resourceText = history.getResourceTextVc();
			resourceEncoding = history.getEncoding();
			switch (myDaoConfig.getTagStorageMode()) {
				case VERSIONED:
				default:
					if (history.isHasTags()) {
						tagList = history.getTags();
					}
					break;
				case NON_VERSIONED:
					if (history.getResourceTable().isHasTags()) {
						tagList = history.getResourceTable().getTags();
					}
					break;
				case INLINE:
					tagList = null;
			}
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceTable) {
			ResourceTable resource = (ResourceTable) theEntity;
			ResourceHistoryTable history;
			if (resource.getCurrentVersionEntity() != null) {
				history = resource.getCurrentVersionEntity();
			} else {
				version = theEntity.getVersion();
				history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
				((ResourceTable) theEntity).setCurrentVersionEntity(history);

				while (history == null) {
					if (version > 1L) {
						version--;
						history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theEntity.getId(), version);
					} else {
						return null;
					}
				}
			}

			resourceBytes = history.getResource();
			resourceEncoding = history.getEncoding();
			resourceText = history.getResourceTextVc();
			switch (myDaoConfig.getTagStorageMode()) {
				case VERSIONED:
				case NON_VERSIONED:
					if (resource.isHasTags()) {
						tagList = resource.getTags();
					}
					break;
				case INLINE:
					tagList = null;
					break;
			}
			version = history.getVersion();
			if (history.getProvenance() != null) {
				provenanceRequestId = history.getProvenance().getRequestId();
				provenanceSourceUri = history.getProvenance().getSourceUri();
			}
		} else if (theEntity instanceof ResourceSearchView) {
			// This is the search View
			ResourceSearchView view = (ResourceSearchView) theEntity;
			resourceBytes = view.getResource();
			resourceText = view.getResourceTextVc();
			resourceEncoding = view.getEncoding();
			version = view.getVersion();
			provenanceRequestId = view.getProvenanceRequestId();
			provenanceSourceUri = view.getProvenanceSourceUri();
			switch (myDaoConfig.getTagStorageMode()) {
				case VERSIONED:
				case NON_VERSIONED:
					if (theTagList != null) {
						tagList = theTagList;
					}
					break;
				case INLINE:
					tagList = null;
					break;
			}
		} else {
			// something wrong
			return null;
		}

		// 2. get The text
		String decodedResourceText = decodedResourceText(resourceBytes, resourceText, resourceEncoding);

		// 3. Use the appropriate custom type if one is specified in the context
		Class<R> resourceType = determineTypeToParse(theResourceType, tagList);

		// 4. parse the text to FHIR
		R retVal = parseResource(theEntity, resourceEncoding, decodedResourceText, resourceType);

		// 5. fill MetaData
		retVal = populateResourceMetadata(theEntity, theForHistoryOperation, tagList, version, retVal);

		// 6. Handle source (provenance)
		populateResourceSource(provenanceSourceUri, provenanceRequestId, retVal);

		// 7. Add partition information
		populateResourcePartitionInformation(theEntity, retVal);

		return retVal;
	}

	private <R extends IBaseResource> void populateResourcePartitionInformation(IBaseResourceEntity theEntity, R retVal) {
		if (myPartitionSettings.isPartitioningEnabled()) {
			PartitionablePartitionId partitionId = theEntity.getPartitionId();
			if (partitionId != null && partitionId.getPartitionId() != null) {
				PartitionEntity persistedPartition = myPartitionLookupSvc.getPartitionById(partitionId.getPartitionId());
				retVal.setUserData(Constants.RESOURCE_PARTITION_ID, persistedPartition.toRequestPartitionId());
			} else {
				retVal.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			}
		}
	}

	private <R extends IBaseResource> void populateResourceSource(String provenanceSourceUri, String provenanceRequestId, R retVal) {
		if (isNotBlank(provenanceRequestId) || isNotBlank(provenanceSourceUri)) {
			String sourceString = cleanProvenanceSourceUri(provenanceSourceUri)
				+ (isNotBlank(provenanceRequestId) ? "#" : "")
				+ defaultString(provenanceRequestId);

			MetaUtil.setSource(myContext, retVal, sourceString);
		}
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R parseResource(IBaseResourceEntity theEntity, ResourceEncodingEnum resourceEncoding, String decodedResourceText, Class<R> resourceType) {
		R retVal;
		if (resourceEncoding != ResourceEncodingEnum.DEL) {

			IParser parser = new TolerantJsonParser(getContext(theEntity.getFhirVersion()), LENIENT_ERROR_HANDLER, theEntity.getId());

			try {
				retVal = parser.parseResource(resourceType, decodedResourceText);
			} catch (Exception e) {
				StringBuilder b = new StringBuilder();
				b.append("Failed to parse database resource[");
				b.append(myContext.getResourceType(resourceType));
				b.append("/");
				b.append(theEntity.getIdDt().getIdPart());
				b.append(" (pid ");
				b.append(theEntity.getId());
				b.append(", version ");
				b.append(theEntity.getFhirVersion().name());
				b.append("): ");
				b.append(e.getMessage());
				String msg = b.toString();
				ourLog.error(msg, e);
				throw new DataFormatException(Msg.code(928) + msg, e);
			}

		} else {

			retVal = (R) myContext.getResourceDefinition(theEntity.getResourceType()).newInstance();

		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> Class<R> determineTypeToParse(Class<R> theResourceType, @Nullable Collection<? extends BaseTag> tagList) {
		Class<R> resourceType = theResourceType;
		if (tagList != null) {
			if (myContext.hasDefaultTypeForProfile()) {
				for (BaseTag nextTag : tagList) {
					if (nextTag.getTag().getTagType() == TagTypeEnum.PROFILE) {
						String profile = nextTag.getTag().getCode();
						if (isNotBlank(profile)) {
							Class<? extends IBaseResource> newType = myContext.getDefaultTypeForProfile(profile);
							if (newType != null && theResourceType.isAssignableFrom(newType)) {
								ourLog.debug("Using custom type {} for profile: {}", newType.getName(), profile);
								resourceType = (Class<R>) newType;
								break;
							}
						}
					}
				}
			}
		}
		return resourceType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R extends IBaseResource> R populateResourceMetadata(IBaseResourceEntity theEntitySource, boolean theForHistoryOperation, @Nullable Collection<? extends BaseTag> tagList, long theVersion, R theResourceTarget) {
		if (theResourceTarget instanceof IResource) {
			IResource res = (IResource) theResourceTarget;
			theResourceTarget = (R) populateResourceMetadataHapi(theEntitySource, tagList, theForHistoryOperation, res, theVersion);
		} else {
			IAnyResource res = (IAnyResource) theResourceTarget;
			theResourceTarget = populateResourceMetadataRi(theEntitySource, tagList, theForHistoryOperation, res, theVersion);
		}
		return theResourceTarget;
	}

	@SuppressWarnings("unchecked")
	private <R extends IResource> R populateResourceMetadataHapi(IBaseResourceEntity theEntity, @Nullable Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, R res, Long theVersion) {
		R retVal = res;
		if (theEntity.getDeleted() != null) {
			res = (R) myContext.getResourceDefinition(res).newInstance();
			retVal = res;
			ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt(theEntity.getDeleted()));
			if (theForHistoryOperation) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.DELETE);
			}
		} else if (theForHistoryOperation) {
			/*
			 * If the create and update times match, this was when the resource was created so we should mark it as a POST. Otherwise, it's a PUT.
			 */
			Date published = theEntity.getPublished().getValue();
			Date updated = theEntity.getUpdated().getValue();
			if (published.equals(updated)) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.POST);
			} else {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, BundleEntryTransactionMethodEnum.PUT);
			}
		}

		res.setId(theEntity.getIdDt().withVersion(theVersion.toString()));

		ResourceMetadataKeyEnum.VERSION.put(res, Long.toString(theEntity.getVersion()));
		ResourceMetadataKeyEnum.PUBLISHED.put(res, theEntity.getPublished());
		ResourceMetadataKeyEnum.UPDATED.put(res, theEntity.getUpdated());
		IDao.RESOURCE_PID.put(res, theEntity.getResourceId());

		if (theTagList != null) {
			if (theEntity.isHasTags()) {
				TagList tagList = new TagList();
				List<IBaseCoding> securityLabels = new ArrayList<>();
				List<IdDt> profiles = new ArrayList<>();
				for (BaseTag next : theTagList) {
					switch (next.getTag().getTagType()) {
						case PROFILE:
							profiles.add(new IdDt(next.getTag().getCode()));
							break;
						case SECURITY_LABEL:
							IBaseCoding secLabel = (IBaseCoding) myContext.getVersion().newCodingDt();
							secLabel.setSystem(next.getTag().getSystem());
							secLabel.setCode(next.getTag().getCode());
							secLabel.setDisplay(next.getTag().getDisplay());
							securityLabels.add(secLabel);
							break;
						case TAG:
							tagList.add(new Tag(next.getTag().getSystem(), next.getTag().getCode(), next.getTag().getDisplay()));
							break;
					}
				}
				if (tagList.size() > 0) {
					ResourceMetadataKeyEnum.TAG_LIST.put(res, tagList);
				}
				if (securityLabels.size() > 0) {
					ResourceMetadataKeyEnum.SECURITY_LABELS.put(res, toBaseCodingList(securityLabels));
				}
				if (profiles.size() > 0) {
					ResourceMetadataKeyEnum.PROFILES.put(res, profiles);
				}
			}
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private <R extends IBaseResource> R populateResourceMetadataRi(IBaseResourceEntity theEntity, @Nullable Collection<? extends BaseTag> theTagList, boolean theForHistoryOperation, IAnyResource res, Long theVersion) {
		R retVal = (R) res;
		if (theEntity.getDeleted() != null) {
			res = (IAnyResource) myContext.getResourceDefinition(res).newInstance();
			retVal = (R) res;
			ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt(theEntity.getDeleted()));
			if (theForHistoryOperation) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, Bundle.HTTPVerb.DELETE.toCode());
			}
		} else if (theForHistoryOperation) {
			/*
			 * If the create and update times match, this was when the resource was created so we should mark it as a POST. Otherwise, it's a PUT.
			 */
			Date published = theEntity.getPublished().getValue();
			Date updated = theEntity.getUpdated().getValue();
			if (published.equals(updated)) {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, Bundle.HTTPVerb.POST.toCode());
			} else {
				ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.put(res, Bundle.HTTPVerb.PUT.toCode());
			}
		}

		res.getMeta().setLastUpdated(null);
		res.getMeta().setVersionId(null);

		updateResourceMetadata(theEntity, res);
		res.setId(res.getIdElement().withVersion(theVersion.toString()));

		res.getMeta().setLastUpdated(theEntity.getUpdatedDate());
		IDao.RESOURCE_PID.put(res, theEntity.getResourceId());

		if (theTagList != null) {
			res.getMeta().getTag().clear();
			res.getMeta().getProfile().clear();
			res.getMeta().getSecurity().clear();
			for (BaseTag next : theTagList) {
				switch (next.getTag().getTagType()) {
					case PROFILE:
						res.getMeta().addProfile(next.getTag().getCode());
						break;
					case SECURITY_LABEL:
						IBaseCoding sec = res.getMeta().addSecurity();
						sec.setSystem(next.getTag().getSystem());
						sec.setCode(next.getTag().getCode());
						sec.setDisplay(next.getTag().getDisplay());
						break;
					case TAG:
						IBaseCoding tag = res.getMeta().addTag();
						tag.setSystem(next.getTag().getSystem());
						tag.setCode(next.getTag().getCode());
						tag.setDisplay(next.getTag().getDisplay());
						break;
				}
			}
		}

		return retVal;
	}

	@Override
	public void updateResourceMetadata(IBaseResourceEntity theEntitySource, IBaseResource theResourceTarget) {
		IIdType id = theEntitySource.getIdDt();
		if (myContext.getVersion().getVersion().isRi()) {
			id = myContext.getVersion().newIdType().setValue(id.getValue());
		}

		if (id.hasResourceType() == false) {
			id = id.withResourceType(theEntitySource.getResourceType());
		}

		theResourceTarget.setId(id);
		if (theResourceTarget instanceof IResource) {
			ResourceMetadataKeyEnum.VERSION.put((IResource) theResourceTarget, id.getVersionIdPart());
			ResourceMetadataKeyEnum.UPDATED.put((IResource) theResourceTarget, theEntitySource.getUpdated());
		} else {
			IBaseMetaType meta = theResourceTarget.getMeta();
			meta.setVersionId(id.getVersionIdPart());
			meta.setLastUpdated(theEntitySource.getUpdatedDate());
		}
	}

	private FhirContext getContext(FhirVersionEnum theVersion) {
		Validate.notNull(theVersion, "theVersion must not be null");
		if (theVersion == myContext.getVersion().getVersion()) {
			return myContext;
		}
		return FhirContext.forCached(theVersion);
	}

	private static String decodedResourceText(byte[] resourceBytes, String resourceText, ResourceEncodingEnum resourceEncoding) {
		String decodedResourceText;
		if (resourceText != null) {
			decodedResourceText = resourceText;
		} else {
			decodedResourceText = decodeResource(resourceBytes, resourceEncoding);
		}
		return decodedResourceText;
	}

	private static List<BaseCodingDt> toBaseCodingList(List<IBaseCoding> theSecurityLabels) {
		ArrayList<BaseCodingDt> retVal = new ArrayList<>(theSecurityLabels.size());
		for (IBaseCoding next : theSecurityLabels) {
			retVal.add((BaseCodingDt) next);
		}
		return retVal;
	}

}
