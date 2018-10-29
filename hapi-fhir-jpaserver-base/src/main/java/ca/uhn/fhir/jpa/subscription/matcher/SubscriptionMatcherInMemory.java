package ca.uhn.fhir.jpa.subscription.matcher;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {

	@Autowired
	private FhirContext myCtx;
	
	@Autowired
	private DaoConfig myConfig;
	
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	
	// FIXME remove entity manager
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	
	@Override
	public boolean match(String criteria, ResourceModifiedMessage msg) {
		IBaseResource resource = msg.getNewPayload(myCtx);
		ResourceTable entity = new ResourceTable();
		populateResourceIntoEntity(null, resource, entity, false);
		ResourceIndexedSearchParams searchParams = beanFactory.getBean(ResourceIndexedSearchParams.class, entity);
		// FIXME KHS implement
		return true;
	}
	
	// FIXME KHS this is copy/pasted from 
	private void populateResourceIntoEntity(RequestDetails theRequest, IBaseResource theResource, ResourceTable theEntity, boolean theUpdateHash) {
		FhirContext myContext = myCtx;

		if (theEntity.getResourceType() == null) {
			theEntity.setResourceType(myContext.getResourceDefinition(theResource).getName());
		}

		
		if (theResource != null) {
			List<BaseResourceReferenceDt> refs = myContext.newTerser().getAllPopulatedChildElementsOfType(theResource, BaseResourceReferenceDt.class);
			for (BaseResourceReferenceDt nextRef : refs) {
				if (nextRef.getReference().isEmpty() == false) {
					if (nextRef.getReference().hasVersionIdPart()) {
						nextRef.setReference(nextRef.getReference().toUnqualifiedVersionless());
					}
				}
			}
		}

		byte[] bytes;
		ResourceEncodingEnum encoding;
		boolean changed = false;

		if (theEntity.getDeleted() == null) {

			encoding = myConfig.getResourceEncoding();
			Set<String> excludeElements = BaseHapiFhirDao.EXCLUDE_ELEMENTS_IN_ENCODED;
			theEntity.setFhirVersion(myContext.getVersion().getVersion());

			bytes = BaseHapiFhirDao.encodeResource(theResource, encoding, excludeElements, myContext);

			if (theUpdateHash) {
				HashFunction sha256 = Hashing.sha256();
				String hashSha256 = sha256.hashBytes(bytes).toString();
				if (hashSha256.equals(theEntity.getHashSha256()) == false) {
					changed = true;
				}
				theEntity.setHashSha256(hashSha256);
			}

			Set<ResourceTag> allDefs = new HashSet<>();
			Set<ResourceTag> allTagsOld = getAllTagDefinitions(theEntity);

			if (theResource instanceof IResource) {
				extractTagsHapi((IResource) theResource, theEntity, allDefs);
			} else {
				extractTagsRi((IAnyResource) theResource, theEntity, allDefs);
			}

			RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
			if (def.isStandardType() == false) {
				String profile = def.getResourceProfile("");
				if (isNotBlank(profile)) {
					TagDefinition profileDef = getTagOrNull(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, profile, null);
					if (def != null) {
						ResourceTag tag = theEntity.addTag(profileDef);
						allDefs.add(tag);
						theEntity.setHasTags(true);
					}
				}
			}

			Set<ResourceTag> allTagsNew = getAllTagDefinitions(theEntity);
			Set<TagDefinition> allDefsPresent = new HashSet<>();
			allTagsNew.forEach(tag -> {

				// Don't keep duplicate tags
				if (!allDefsPresent.add(tag.getTag())) {
					theEntity.getTags().remove(tag);
				}

				// Drop any tags that have been removed
				if (!allDefs.contains(tag)) {
					if (shouldDroppedTagBeRemovedOnUpdate(theRequest, tag)) {
						theEntity.getTags().remove(tag);
					}
				}

			});

			if (!allTagsOld.equals(allTagsNew)) {
				changed = true;
			}
			theEntity.setHasTags(!allTagsNew.isEmpty());

		} else {
			theEntity.setHashSha256(null);
			bytes = null;
			encoding = ResourceEncodingEnum.DEL;
		}

		if (changed == false) {
			if (theEntity.getId() == null) {
				changed = true;
			} else {
				ResourceHistoryTable currentHistoryVersion = myResourceHistoryTableDao.findForIdAndVersion(theEntity.getId(), theEntity.getVersion());
				if (currentHistoryVersion == null || currentHistoryVersion.getResource() == null) {
					changed = true;
				} else {
					changed = !Arrays.equals(currentHistoryVersion.getResource(), bytes);
				}
			}
		}
	}

	// FIXME copy/paste
	private Set<ResourceTag> getAllTagDefinitions(ResourceTable theEntity) {
		HashSet<ResourceTag> retVal = Sets.newHashSet();
		if (theEntity.isHasTags()) {
			for (ResourceTag next : theEntity.getTags()) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	// FIXME copy/paste
	private void extractTagsHapi(IResource theResource, ResourceTable theEntity, Set<ResourceTag> allDefs) {
		TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tagList != null) {
			for (Tag next : tagList) {
				TagDefinition def = getTagOrNull(TagTypeEnum.TAG, next.getScheme(), next.getTerm(), next.getLabel());
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<BaseCodingDt> securityLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(theResource);
		if (securityLabels != null) {
			for (BaseCodingDt next : securityLabels) {
				TagDefinition def = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystemElement().getValue(), next.getCodeElement().getValue(), next.getDisplayElement().getValue());
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<IdDt> profiles = ResourceMetadataKeyEnum.PROFILES.get(theResource);
		if (profiles != null) {
			for (IIdType next : profiles) {
				TagDefinition def = getTagOrNull(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					allDefs.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	//FIXME copy/paste
	private void extractTagsRi(IAnyResource theResource, ResourceTable theEntity, Set<ResourceTag> theAllTags) {
		List<? extends IBaseCoding> tagList = theResource.getMeta().getTag();
		if (tagList != null) {
			for (IBaseCoding next : tagList) {
				TagDefinition def = getTagOrNull(TagTypeEnum.TAG, next.getSystem(), next.getCode(), next.getDisplay());
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IBaseCoding> securityLabels = theResource.getMeta().getSecurity();
		if (securityLabels != null) {
			for (IBaseCoding next : securityLabels) {
				TagDefinition def = getTagOrNull(TagTypeEnum.SECURITY_LABEL, next.getSystem(), next.getCode(), next.getDisplay());
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}

		List<? extends IPrimitiveType<String>> profiles = theResource.getMeta().getProfile();
		if (profiles != null) {
			for (IPrimitiveType<String> next : profiles) {
				TagDefinition def = getTagOrNull(TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, next.getValue(), null);
				if (def != null) {
					ResourceTag tag = theEntity.addTag(def);
					theAllTags.add(tag);
					theEntity.setHasTags(true);
				}
			}
		}
	}

	// FIXME copy/paste
	protected TagDefinition getTagOrNull(TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel) {
		if (isBlank(theScheme) && isBlank(theTerm) && isBlank(theLabel)) {
			return null;
		}

		// FIXME these should be cached
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TagDefinition> cq = builder.createQuery(TagDefinition.class);
		Root<TagDefinition> from = cq.from(TagDefinition.class);

		if (isNotBlank(theScheme)) {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType),
					builder.equal(from.get("mySystem"), theScheme),
					builder.equal(from.get("myCode"), theTerm)));
		} else {
			cq.where(
				builder.and(
					builder.equal(from.get("myTagType"), theTagType),
					builder.isNull(from.get("mySystem")),
					builder.equal(from.get("myCode"), theTerm)));
		}

		TypedQuery<TagDefinition> q = myEntityManager.createQuery(cq);
		try {
			return q.getSingleResult();
		} catch (NoResultException e) {
			TagDefinition retVal = new TagDefinition(theTagType, theScheme, theTerm, theLabel);
			myEntityManager.persist(retVal);
			return retVal;
		}
	}

	// FIXME copy/paste
	
	protected boolean shouldDroppedTagBeRemovedOnUpdate(RequestDetails theRequest, ResourceTag theTag) {

		Set<TagTypeEnum> metaSnapshotModeTokens = null;

		if (theRequest != null) {
			List<String> metaSnapshotMode = theRequest.getHeaders(JpaConstants.HEADER_META_SNAPSHOT_MODE);
			if (metaSnapshotMode != null && !metaSnapshotMode.isEmpty()) {
				metaSnapshotModeTokens = new HashSet<>();
				for (String nextHeaderValue : metaSnapshotMode) {
					StringTokenizer tok = new StringTokenizer(nextHeaderValue, ",");
					while (tok.hasMoreTokens()) {
						switch (trim(tok.nextToken())) {
							case "TAG":
								metaSnapshotModeTokens.add(TagTypeEnum.TAG);
								break;
							case "PROFILE":
								metaSnapshotModeTokens.add(TagTypeEnum.PROFILE);
								break;
							case "SECURITY_LABEL":
								metaSnapshotModeTokens.add(TagTypeEnum.SECURITY_LABEL);
								break;
						}
					}
				}
			}
		}

		if (metaSnapshotModeTokens == null) {
			metaSnapshotModeTokens = Collections.singleton(TagTypeEnum.PROFILE);
		}

		if (metaSnapshotModeTokens.contains(theTag.getTag().getTagType())) {
			return true;
		}

		return false;
	}

}
